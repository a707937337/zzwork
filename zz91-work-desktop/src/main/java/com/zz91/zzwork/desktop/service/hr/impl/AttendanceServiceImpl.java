package com.zz91.zzwork.desktop.service.hr.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Component;

import com.zz91.util.datetime.DateUtil;
import com.zz91.util.lang.StringUtils;
import com.zz91.zzwork.desktop.dao.hr.AttendanceDao;
import com.zz91.zzwork.desktop.dao.staff.StaffDao;
import com.zz91.zzwork.desktop.domain.hr.Attendance;
import com.zz91.zzwork.desktop.dto.PageDto;
import com.zz91.zzwork.desktop.dto.hr.WorkDay;
import com.zz91.zzwork.desktop.service.hr.AttendanceService;

@Component("attendanceService")
public class AttendanceServiceImpl implements AttendanceService {

	@Resource
	private AttendanceDao attendanceDao;
	@Resource
	private StaffDao staffDao;

	@Override
	public Boolean impt(Date from, Date to, InputStream inputStream) {

		//清理老数据
		attendanceDao.deleteAttendance(from, to);
		
		//导入新数据
		HSSFWorkbook work=null;
		try {
			work = new HSSFWorkbook(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(work==null){
			return false;
		}
		
		HSSFSheet sheet = work.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
		
		Map<String, String> accountMap=new HashMap<String, String>();
		
		for (int i = 1; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if(row!=null){
				Attendance att=new Attendance();
				att.setCode(row.getCell(0).getRichStringCellValue().toString());
				att.setName(row.getCell(1).getRichStringCellValue().toString());
				
				String gmtwork=row.getCell(2).getRichStringCellValue().toString();
				if(!StringUtils.isEmpty(gmtwork)){
					try {
						att.setGmtWork(DateUtil.getDate(gmtwork, "yyyy-M-d HH:mm"));
					} catch (ParseException e) {
					}
				}
				
				if(StringUtils.isNotEmpty(att.getName())){
					if(accountMap.get(att.getName())==null){
						String ac= staffDao.queryAccountByName(att.getName());
						if(ac==null){
							ac="";
						}
						accountMap.put(att.getName(), ac);
					}
					att.setAccount(accountMap.get(att.getName()));
				}
				
				attendanceDao.insert(att);
			}
		}
		
		return true;
	}

	@Override
	public PageDto<Attendance> pageAttendance(String name, String code,
			Date from, Date to, PageDto<Attendance> page) {
		//计算 from to
		page.setRecords(attendanceDao.queryAttendance(name, code, from, to, page));
		page.setTotalRecords(attendanceDao.queryAttendanceCount(name, code, from, to));
		return page;
	}

	@Override
	public void analysis(Date month, List<WorkDay> workDay) {
		//循环工作日，获取每天打卡数据
		//计算相关数据值
		//保存统计结果
	}

	@Override
	public List<WorkDay> buildWorkday(Date month, Integer[] day, String[] workf,
			String[] workt) {
		List<WorkDay> list=new ArrayList<WorkDay>();
		int i=0;
		for(Integer dayofmonth:day){
			WorkDay wd=new WorkDay();
			Date d=DateUtil.getDateAfterDays(month, dayofmonth-1);
			wd.setDay(d.getTime());
			wd.setWorkf(parseWorkTime(workf[i])*1000l);
			wd.setWorkt(parseWorkTime(workt[i])*1000l);
			list.add(wd);
			i++;
		}
		return list;
	}
	
	private Long parseWorkTime(String wt){
		String[] wtArr=wt.split(":");
		return Long.valueOf(wtArr[0])*3600 + Long.valueOf(wtArr[1])*60;
	}

	@Override
	public List<WorkDay> buildworkDays(Date month) {
		Date nextMonth=DateUtil.getDateAfterMonths(month, 1);
		Date monthLastDay=DateUtil.getDateAfterDays(nextMonth, -1);
		
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(month);
		int firstDay=calendar.get(Calendar.DAY_OF_MONTH);
		calendar.setTime(monthLastDay);
		int lastDay=calendar.get(Calendar.DAY_OF_MONTH);
		
		List<WorkDay> list=new ArrayList<WorkDay>();
		
		for(int i=firstDay;i<=lastDay;i++){
			calendar.setTime(DateUtil.getDateAfterDays(month, i-1));
			WorkDay wd=new WorkDay();
			wd.setDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
			wd.setDayOfMonth(i);
			list.add(wd);
		}
		
		return list;
	}
	
}
