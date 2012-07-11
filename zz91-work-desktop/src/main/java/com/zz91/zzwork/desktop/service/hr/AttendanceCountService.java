package com.zz91.zzwork.desktop.service.hr;


import java.util.Date;

import com.zz91.zzwork.desktop.domain.hr.AttendanceCount;
import com.zz91.zzwork.desktop.dto.PageDto;

public interface AttendanceCountService {
 
	public PageDto<AttendanceCount> pageResult(String name, Date gmtTargetMonth, PageDto<AttendanceCount> page);
	public void analysisAttendance(Date gmtTargetMonth);
}
 
