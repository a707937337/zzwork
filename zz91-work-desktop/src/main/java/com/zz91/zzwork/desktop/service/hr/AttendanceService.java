package com.zz91.zzwork.desktop.service.hr;

import java.util.Date;

import com.zz91.zzwork.desktop.domain.hr.Attendance;
import com.zz91.zzwork.desktop.dto.PageDto;

public interface AttendanceService {
 
	public Boolean impt(Date from, Date to);
	public PageDto<Attendance> pageAttendance(String name, String code, Date gmtWork, PageDto<Attendance> page);
}
 
