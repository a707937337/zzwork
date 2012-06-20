package com.zz91.zzwork.desktop.service.hr.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.zz91.zzwork.desktop.dao.hr.AttendanceCountDao;
import com.zz91.zzwork.desktop.domain.hr.AttendanceCount;
import com.zz91.zzwork.desktop.dto.PageDto;
import com.zz91.zzwork.desktop.service.hr.AttendanceCountService;

@Component("attendanceCountService")
public class AttendanceCountServiceImpl implements AttendanceCountService {

	@Resource
	private AttendanceCountDao attendanceCountDao;

	@Override
	public void analysisAttendance(Date gmtTargetMonth) {
		

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

}
