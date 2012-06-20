package com.zz91.zzwork.desktop.service.hr.impl;

import java.io.FileInputStream;
import java.util.Date;

import javax.annotation.Resource;

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
	public Boolean impt(Date from, Date to,FileInputStream inputStream) {
		 

		return null;
	}

	@Override
	public PageDto<Attendance> pageAttendance(String name, String code,
			Date gmtWork, PageDto<Attendance> page) {
		  page.setRecords(attendanceDao.queryAttendance(name, code, gmtWork, page));
		  page.setTotalRecords(attendanceDao.queryAttendanceCount(name, code, gmtWork));
		return page;
	}

}
