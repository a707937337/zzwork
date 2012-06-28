package com.zz91.zzwork.desktop.controller.hr;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.zz91.zzwork.desktop.controller.BaseController;
import com.zz91.zzwork.desktop.domain.hr.Attendance;
import com.zz91.zzwork.desktop.domain.hr.FileUploadBean;
import com.zz91.zzwork.desktop.dto.PageDto;
import com.zz91.zzwork.desktop.service.hr.AttendanceService;

@Controller
public class AttendanceController extends BaseController {

	@Resource
	private AttendanceService attendanceService;

	@RequestMapping
	public void index(HttpServletRequest request,Map<String, Object> out) {
        
	}

	@RequestMapping
	public ModelAndView query(String name, String code, Date gmtWork,
			PageDto<Attendance> page ,Map<String, Object> out) {
		PageDto<Attendance>  attendances = attendanceService.pageAttendance(name, code,gmtWork, page);
		out.put("attendances",attendances);
		return null;
	}

	@RequestMapping
	public void impt(HttpServletRequest request, Map<String, Object> out) {
		System.out.println("sssss");
	}

	@RequestMapping
	public ModelAndView doImpt(HttpServletRequest request, Date from, Date to,Map<String, Object> out,Object command) {
		/****FileUploadBean bean = (FileUploadBean)command;
		MultipartFile file = bean.getFile();
		try {
			attendanceService.impt(from, to, file.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}****/
	       System.out.println("sssss");
		return null;
	}

}
