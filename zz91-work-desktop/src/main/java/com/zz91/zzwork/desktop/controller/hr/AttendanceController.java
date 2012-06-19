package com.zz91.zzwork.desktop.controller.hr;


import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.zz91.zzwork.desktop.controller.BaseController;
import com.zz91.zzwork.desktop.domain.hr.Attendance;
import com.zz91.zzwork.desktop.dto.PageDto;


@Controller
public class AttendanceController extends BaseController {
    
	@RequestMapping
	public void index() {
	 
	}
	
	@RequestMapping
	public ModelAndView query(String name, String code, Date gmtWork, PageDto<Attendance> page) {
		return null;
	}
	
	
	@RequestMapping
	public void impt() {
	 
	}
	
	@RequestMapping
	public void doImpt(Date from, Date to) {
	 
	}
	 
}
 
