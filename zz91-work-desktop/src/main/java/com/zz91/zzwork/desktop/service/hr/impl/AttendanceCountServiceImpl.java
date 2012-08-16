package com.zz91.zzwork.desktop.service.hr.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.zz91.util.datetime.DateUtil;
import com.zz91.zzwork.desktop.dao.hr.AttendanceCountDao;
import com.zz91.zzwork.desktop.dao.hr.AttendanceDao;

import com.zz91.zzwork.desktop.domain.hr.Attendance;
import com.zz91.zzwork.desktop.domain.hr.AttendanceCount;
import com.zz91.zzwork.desktop.dto.PageDto;
import com.zz91.zzwork.desktop.service.hr.AttendanceCountService;

@Component("attendanceCountService")
public class AttendanceCountServiceImpl implements AttendanceCountService {

	@Resource
	private AttendanceCountDao attendanceCountDao;
	@Resource
	private AttendanceDao attendanceDao;

	@Override
	public void analysisAttendance(Date gmtTargetMonth) {
		// step 1：读取指定月数据 8800
		// step 2：循环（）
		// step 2.1：问题？
		// step 2.2：结果
		// step 3：保存结果

		// 获取指定月份的第一天
		Date dateStart = null;
		Date dateEnd = null;
		try {
			dateStart = DateUtil.getDate(gmtTargetMonth, "yyyy-MM-01");
			dateEnd = DateUtil.getDateAfterMonths(dateStart, 1);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		List<Attendance> atts = attendanceDao
				.queryByGmtWork(dateStart, dateEnd);

		// 统计
		Map<String, AttendanceCount> map = getattCount(atts);
		// 记录
		for (int i = 0; i < map.size(); i++) {
			attendanceCountDao.insert(map.get(i));
		}

	}

	@Override
	public PageDto<AttendanceCount> pageResult(String name,
			Date gmtTargetMonth, PageDto<AttendanceCount> page) {

		page.setRecords(attendanceCountDao.queryResult(name, gmtTargetMonth,
				page));
		page.setTotalRecords(attendanceCountDao.queryResultCount(name,
				gmtTargetMonth));

		return page;

	}

	// 统计
	private static Map<String, AttendanceCount> getattCount(
			List<Attendance> atts) {

		AttendanceCount attCount = null;
		Map<String, AttendanceCount> map = new HashMap<String, AttendanceCount>();
		for (int i = 0; i < atts.size(); i++) {

			String code = atts.get(i).getCode();
			String name = atts.get(i).getName();
			if (map.get(name) == null) {
				attCount = new AttendanceCount();
				attCount.setCode(code);
				attCount.setPunch0(0);
				attCount.setPunch20(0);
				attCount.setPunch60(0);
				attCount.setPunch80(0);
				attCount.setPunchCount(0);
				map.put(name, attCount);
			}
			Date gmtWork = atts.get(i).getGmtWork();
			int days = 0;
			Date date = new Date();
			try {
				days = DateUtil.getIntervalDays(gmtWork, date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long d1=DateUtil.getTheDayZero(date, days);
			if (gmtWork.getTime()>(d1+30600000) && gmtWork.getTime() <= (d1+31800000) || (d1+63600000) <= gmtWork.getTime()
					&& gmtWork.getTime() <=(d1+64800000) ) {
				// 打卡时间超过八点半 不超过八点五十的 或者打卡时间在五点四十 到六点之间的
				attCount.setPunch20(attCount.getPunch20() + 1);
				attCount.setPunchCount(attCount.getPunchCount() + 1);
				attCount.setGmtWork(gmtWork);
				break;

			}
			if ((d1+31800000) <= gmtWork.getTime() && gmtWork.getTime() <= (d1+33600000) || (d1+61200000) < gmtWork.getTime()
					&& gmtWork.getTime() <= (d1+63600000)) {
				// 打卡时间超过八点五十 不超过九点半的
				attCount.setPunch60(attCount.getPunch60() + 1);
				attCount.setPunchCount(attCount.getPunchCount() + 1);
				attCount.setGmtWork(gmtWork);
				break;

			}
			if (830 <= gmtWork.getTime() && gmtWork.getTime() <= 850) {
				// 无打卡记录
				attCount.setPunch0(attCount.getPunch0() + 1);
				attCount.setPunchCount(attCount.getPunchCount() + 1);
				attCount.setGmtWork(gmtWork);
				break;
			}
			if (gmtWork.getTime() <= (d1+72000000)) {
				// 八点以后的打卡的次数
				attCount.setPunch80(attCount.getPunch80() + 1);
				attCount.setPunchCount(attCount.getPunchCount() + 1);
				attCount.setGmtWork(gmtWork);
				break;
			}
		}
		return map;
	}

}
