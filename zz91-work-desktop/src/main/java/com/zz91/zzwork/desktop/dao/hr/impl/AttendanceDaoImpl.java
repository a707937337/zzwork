package com.zz91.zzwork.desktop.dao.hr.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.zz91.zzwork.desktop.dao.BaseDao;
import com.zz91.zzwork.desktop.dao.hr.AttendanceDao;
import com.zz91.zzwork.desktop.domain.hr.Attendance;
import com.zz91.zzwork.desktop.dto.PageDto;


@Component("attendanceDao")
public class AttendanceDaoImpl extends BaseDao implements AttendanceDao {

	@Override
	public Integer deleteAttendance(Date from, Date to) {
		Map<String, Object> datetodate = new HashMap<String, Object>();
		datetodate.put("fromdate", from);
		datetodate.put("todate", to);
		return this.getSqlMapClientTemplate().delete("deleteAttendance", datetodate);
	}

	@Override
	public Integer insert(Attendance attendance) {
		return (Integer)this.getSqlMapClientTemplate().insert("addAttendance",attendance);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Attendance> queryAttendance(String name, String code,
			Date gmtWork, PageDto<Attendance> page) {
		Map<String, Object>  attendance  =  new  HashMap<String, Object>();
		attendance.put("name", name);
		attendance.put("code", code);
		attendance.put("gmtWork", gmtWork);
		attendance.put("page", page);
		
		return this.getSqlMapClientTemplate().queryForList("getAllattendance",attendance);
	}

	@Override
	public Integer queryAttendanceCount(String name, String code, Date gmtWork) {
		
		return (Integer)this.getSqlMapClientTemplate().queryForObject("getAttendanceCount", gmtWork);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Attendance> queryByGmtWork(Date from, Date to) {
		 Map<String, Date>  fromdateto = new HashMap<String, Date>();
		 fromdateto.put("from", from);
		 fromdateto.put("to", to);
		return this.getSqlMapClientTemplate().queryForList("getAttByDate", fromdateto);
	}

	

}
