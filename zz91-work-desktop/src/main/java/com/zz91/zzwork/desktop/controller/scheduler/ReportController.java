/**
 * Copyright 2011 ASTO.
 * All right reserved.
 * Created on 2011-7-5
 */
package com.zz91.zzwork.desktop.controller.scheduler;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.zz91.zzwork.desktop.controller.BaseController;

/**
 * @author mays (mays@zz91.com)
 *
 * created on 2011-7-5
 */
@Controller
public class ReportController extends BaseController{

	@RequestMapping
	public ModelAndView index(HttpServletRequest request, Map<String, Object> out){
		
		return null;
	}
	
	@RequestMapping
	public ModelAndView compose(HttpServletRequest request, Map<String, Object> out){
		//当前年份前后五年
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setTime(new Date());
		out.put("year", calendar.get(Calendar.YEAR));
		out.put("week", calendar.get(Calendar.WEEK_OF_YEAR));
		return null;
	}
	
}
