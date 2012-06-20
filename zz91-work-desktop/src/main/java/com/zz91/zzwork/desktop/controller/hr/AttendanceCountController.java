package com.zz91.zzwork.desktop.controller.hr;


import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import com.zz91.zzwork.desktop.domain.hr.AttendanceCount;
import com.zz91.zzwork.desktop.dto.PageDto;
import com.zz91.zzwork.desktop.service.hr.AttendanceCountService;

@Controller
public class AttendanceCountController {
	 @Resource
     private AttendanceCountService attendanceCountService;
	
	public void index() {
	 
	}
	 
	public ModelAndView query(Date gmtTargetMonth, String name, PageDto<AttendanceCount> page) {
		return null;
	}
	 
	public ModelAndView analysis(Date gmtTargetMonth) {
		return null;
	}
	 
}
 
