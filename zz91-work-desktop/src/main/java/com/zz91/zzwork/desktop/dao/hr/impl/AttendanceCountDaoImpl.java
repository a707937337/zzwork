package com.zz91.zzwork.desktop.dao.hr.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.zz91.zzwork.desktop.dao.BaseDao;
import com.zz91.zzwork.desktop.dao.hr.AttendanceCountDao;
import com.zz91.zzwork.desktop.domain.hr.AttendanceCount;
import com.zz91.zzwork.desktop.dto.PageDto;

@Component("attenddanceCountDao")
public class AttendanceCountDaoImpl extends BaseDao implements
		AttendanceCountDao {
     
	@Override
	public Integer insert(AttendanceCount attendanceCount) {
	     
		return (Integer)this.getSqlMapClientTemplate().insert("addAttendanceCount", attendanceCount);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AttendanceCount> queryResult(String name, Date gmtTargetMonth,
		PageDto<AttendanceCount> page) {
		Map<String ,Object>  attendanceCount = new HashMap<String, Object>();
		attendanceCount.put("name", name);
		attendanceCount.put("gmtTargetMonth", gmtTargetMonth);
		attendanceCount.put("page", page);
		return this.getSqlMapClientTemplate().queryForList("selectAttendanceCount", attendanceCount);
	}

	@Override
	public Integer queryResultCount(String name, Date gmtTargetMonth) {
		Map<String, Object> resultCount = new HashMap<String, Object>();
		resultCount.put("name", name);
		resultCount.put("gmtTargetMonth", gmtTargetMonth);
		return (Integer)this.getSqlMapClientTemplate().queryForObject("countByNameAndDate", resultCount);
	}

}
