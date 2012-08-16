package com.zz91.zzwork.desktop.controller.hr;


import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.zz91.zzwork.desktop.domain.hr.AttendanceCount;
import com.zz91.zzwork.desktop.dto.PageDto;
import com.zz91.zzwork.desktop.service.hr.AttendanceCountService;

@Controller
public class AttendanceCountController {
	 @Resource
     private AttendanceCountService attendanceCountService;
	
	@RequestMapping
	public void index(HttpServletRequest request ,Map<String, Object> out) {
	  
	}
	@RequestMapping
	public ModelAndView query(Date gmtTargetMonth, String name, PageDto<AttendanceCount> page,Map<String, Object> out) {
	    PageDto<AttendanceCount> attCounts =	attendanceCountService.pageResult(name, gmtTargetMonth, page);
		out.put("attCounts",attCounts );
		return null;
	}
	@RequestMapping
	public ModelAndView analysis(Date gmtTargetMonth ,HttpServletRequest request,Map<String, Object> out) {
		attendanceCountService.analysisAttendance(gmtTargetMonth);
		
		return null;
	}
	 
}
 
