package com.zz91.zzwork.desktop.dao.hr;

import java.util.List;
import java.util.Date;


import com.zz91.zzwork.desktop.domain.hr.AttendanceCount;
import com.zz91.zzwork.desktop.dto.PageDto;


public interface AttendanceCountDao {
 
	public List<AttendanceCount> queryResult(String name, Date gmtTargetMonth, PageDto<AttendanceCount> page);
	public Integer queryResultCount(String name, Date gmtTargetMonth);
	public Integer insert(AttendanceCount attendanceCount);
}
 
