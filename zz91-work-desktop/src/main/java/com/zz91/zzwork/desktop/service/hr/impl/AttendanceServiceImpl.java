package com.zz91.zzwork.desktop.service.hr.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Component;

import com.zz91.zzwork.desktop.dao.hr.AttendanceDao;
import com.zz91.zzwork.desktop.domain.hr.Attendance;
import com.zz91.zzwork.desktop.dto.PageDto;
import com.zz91.zzwork.desktop.service.hr.AttendanceService;

@Component("attendanceService")
public class AttendanceServiceImpl implements AttendanceService {
	  
	@Resource
    private AttendanceDao  attendanceDao;
	
	@Override
	public Boolean impt(Date from, Date to,InputStream inputStream) {
		Boolean b = false;
		try {
			List<Attendance> atts =  inputExcel(from, to, inputStream);
			for(int i=0;i<atts.size();i++){
				  attendanceDao.insert(atts.get(i));
			  }
			 b =true;
		} catch (Exception e) {
			// TODO: handle exception
		   e.printStackTrace();
		}
		
       return b;
		
	}

	@Override
	public PageDto<Attendance> pageAttendance(String name, String code,
		      Date gmtWork, PageDto<Attendance> page) {
		page.setRecords(attendanceDao.queryAttendance(name, code, gmtWork, page));
		page.setTotalRecords(attendanceDao.queryAttendanceCount(name, code, gmtWork));
		return page;
	}
    
	@SuppressWarnings("deprecation")
	public List<Attendance> inputExcel(Date from, Date to,InputStream inputStream)
	{    StringBuffer  sb = null;
	 List<Attendance> atts  = new ArrayList<Attendance>();
	 Attendance  attendance = null;
	 try {
		HSSFWorkbook work = new HSSFWorkbook(inputStream);
		HSSFSheet  sheet  = work.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
	   for(int i =0;i<rows;i++){
		   HSSFRow row = sheet.getRow(i);
		   if(row != null){
			   int cells = row.getPhysicalNumberOfCells();
			    sb = new StringBuffer();
			    
			   for(int j=0;j<cells;j++){
				   HSSFCell cell = row.getCell(i);
				   if(cell!=null){
					 
					   switch (cell.getCellType()) {
					case HSSFCell.CELL_TYPE_STRING:
						  sb.append(cell.getRichStringCellValue().toString());
						  sb.append(",");
						  
						 break;
					case HSSFCell.CELL_TYPE_NUMERIC:
						if (HSSFDateUtil.isCellDateFormatted(cell)) {
							sb.append(cell.getDateCellValue().toString());
							sb.append(",");
						} else {
                             sb.append(cell.getNumericCellValue()+",");
						}
						
						break;
					
					}
				   }
			   }
			   String [] att = sb.toString().split(",");
			   attendance  = new Attendance();
			   attendance.setCode(att[0]);
			   attendance.setName(att[1]);
			   attendance.setGmtWork(new Date(att[2]));
			   if(attendance.getGmtWork().after(from)&&attendance.getGmtWork().after(to))
			   {
				   atts.add(attendance);
			   }
			  
		   }
	   }
	} catch (IOException e) {
		
		e.printStackTrace();
	}
		return  atts;
	}
}
