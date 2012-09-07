package com.zz91.zzwork.desktop.controller.hr;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.ModelAndView;

import com.zz91.util.datetime.DateUtil;
import com.zz91.util.lang.StringUtils;
import com.zz91.zzwork.desktop.controller.BaseController;
import com.zz91.zzwork.desktop.domain.hr.Attendance;
import com.zz91.zzwork.desktop.dto.PageDto;
import com.zz91.zzwork.desktop.dto.hr.WorkDay;
import com.zz91.zzwork.desktop.service.hr.AttendanceService;

@Controller
public class AttendanceController extends BaseController {

	@Resource
	private AttendanceService attendanceService;

	@RequestMapping
	public void index(HttpServletRequest request,Map<String, Object> out) {

	}

	@RequestMapping
	public ModelAndView query(String name, String code, Date from, Date to,
			PageDto<Attendance> page ,Map<String, Object> out) {
		page = attendanceService.pageAttendance(name, code, from, to, page);
		return printJson(page, out);
	}

	@RequestMapping
	public ModelAndView impt(HttpServletRequest request, Map<String, Object> out, Integer error) {
		out.put("error", error);
		return null;
	}

	final static String DATE_FORMAT="yyyy-MM-dd";
	@RequestMapping
	public ModelAndView doImpt(HttpServletRequest request, Map<String, Object> out,String from, String to, String dateFormat) {
		
		int error=0;
		do {
			if(StringUtils.isEmpty(from)){
				error=1;
				break;
			}
			if(StringUtils.isEmpty(to)){
				error=1;
				break;
			}
			Date fromDate=null;
			Date toDate=null;
			
			try {
				fromDate=DateUtil.getDate(from, DATE_FORMAT);
				toDate=DateUtil.getDate(to, DATE_FORMAT);
			} catch (ParseException e) {
				error=1;
				break;
			}
			
			MultipartRequest multipartRequest = (MultipartRequest)request;
			MultipartFile file = multipartRequest.getFile("uploadfile");
			if(file.getOriginalFilename()!=null &&
					!file.getOriginalFilename().equals("")){
				try {
					attendanceService.impt(fromDate, toDate, file.getInputStream(), dateFormat);
				} catch (IOException e) {
				}
			}
			
		} while (false);
		
		out.put("error", error);
		
		return new ModelAndView("redirect:impt.htm");
	}
	
	/**
	 * @param request
	 * @param out
	 * @param targetMonth
	 * @return
	 */
	@RequestMapping
	public ModelAndView analysis(HttpServletRequest request, Map<String, Object> out, String targetMonth){
		
		Date targetMonthDate=null;
		try {
			if(StringUtils.isNotEmpty(targetMonth)){
				targetMonthDate =  DateUtil.getDate(targetMonth, "yyyy-MM");
			}else {
				targetMonthDate = DateUtil.getDate(new Date(), "yyyy-MM");
				targetMonth=DateUtil.toString(targetMonthDate, "yyyy-MM");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		out.put("targetMonth", targetMonth);
		
		List<WorkDay> list=attendanceService.buildworkDays(targetMonthDate);
		out.put("monthDay", list);
		
		out.put("workf", "8:30");
		
		Calendar d=Calendar.getInstance();
		d.setTime(targetMonthDate);
		int m=d.get(Calendar.MONTH);
		if(m<9 && m>4){
			out.put("workt", "18:00");
		}else {
			out.put("workt", "17:30");
		}

		out.put("weekName", new String[]{ "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" });
		
		return null;
	}
	
//	public static void main(String[] args) throws ParseException {
//		String targetMonth="2012-08";
//		Calendar d=Calendar.getInstance();
//		d.setTime(DateUtil.getDate(targetMonth, "yyyy-MM"));
//		System.out.println(d.get(Calendar.MONTH));
//		System.out.println(d.get(Calendar.DAY_OF_WEEK));
//	}
//	
	@RequestMapping
	public ModelAndView doAnalysis(HttpServletRequest request, Map<String, Object> out,
			String targetMonth, String dayArr, String workfArr, String worktArr){
		
		
		
		Date targetMonthDate=null;
		try {
			targetMonthDate = DateUtil.getDate(targetMonth, "yyyy-MM");
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		List<WorkDay> list = attendanceService.buildWorkday(targetMonthDate, 
				StringUtils.StringToIntegerArray(dayArr), workfArr.split(","), worktArr.split(","));
		
		attendanceService.analysis(targetMonthDate, list);
		
		return new ModelAndView("redirect:analysis.htm");
	}
}
