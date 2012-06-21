package com.zz91.zzwork.desktop.service.hr.impl;



import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import javax.annotation.Resource;

import org.springframework.stereotype.Component;



import com.zz91.zzwork.desktop.dao.hr.AttendanceCountDao;
import com.zz91.zzwork.desktop.dao.hr.AttendanceDao;

import com.zz91.zzwork.desktop.domain.hr.Attendance;
import com.zz91.zzwork.desktop.domain.hr.AttendanceCount;
import com.zz91.zzwork.desktop.dto.PageDto;
import com.zz91.zzwork.desktop.service.hr.AttendanceCountService;

@Component("attendanceCountService")
public class AttendanceCountServiceImpl implements AttendanceCountService {

	@Resource
	private AttendanceCountDao attendanceCountDao;
	@Resource
	private AttendanceDao      attendanceDao;

	@Override
	public void analysisAttendance(Date gmtTargetMonth) {
	    /***  
		List<Attendance>  atts  = attendanceDao.queryByGmtWork(gmtTargetMonth, gmtTargetMonth);
	       Attendance  att = null;
	       Map<String, Attendance> map  =  new  HashMap<String, Attendance>();
	       
	       int count0 = 0;无打卡纪律
	       int count20 = 0；迟到二十分钟
	       int count60 = 0 ; 迟到20以上 60以下
	       int sumcount =0; 总迟到次数
	       for(int i =0;i<atts.size();i++){
	    	    String code = atts.get(i).getCode();
	    	   if(!map.isEmtory()&&map.get(code)==null){
	    	     att.setPunch0(count0);
	    	     att.setPunch20(count20);
	    	     att.setPunch60(count60);
	    	     att.setPunchCount(sumcount);
	    		   count0 =0;
	    		   count20=0;
	    		   count60=0;
	    		   sumcount=0;
	    		  att =  new Attendance();
	    		  att.setName(name);
	    		  att.setCode(code);
	    		  map.put(code,att);
	    	   }else if(map.isEmtory()){
	    	      att =  new Attendance();
	    		  att.setName(name);
	    		  att.setCode(code);
	    		  map.put(code,att);
	    	   }else {
	    	       if(){    如果是上午 且打卡时间超过八点半 不超过八点五十的
                         count20 ++;	
                         sumcount ++;
                            		   
	    	   }else if(){    如果是下午  且打卡时间在五点四十 到六点之间的  
	    	     count20 ++;  
	    	     sumcount ++;
	    	    }else if(){如果是上午 且打卡时间超过八点五十 不超过九点半的
	    	       count60++;
	    	       sumcount ++;
	    	    }else if(){如果是下午 且打卡时间在五点 到六点之间的 
	    	       count60++;
	    	       sumcount ++;
	    	    }else if{如果某天无某人的打卡记录 则
	    	      count0 ++;
	    	      sumcount ++;
	    	    }
	    	   
	       }
	    	   }
	    	    
	    for(int i =0;i<map.size();i++){
	       attendanceCountDao.insert(map.get(i));
	    }	  
	       ****/
	}

	@Override
	public PageDto<AttendanceCount> pageResult(String name,
			Date gmtTargetMonth, PageDto<AttendanceCount> page) {

		page.setRecords(attendanceCountDao.queryResult(name, gmtTargetMonth,
				page));
		page.setTotalRecords(attendanceCountDao.queryResultCount(name,
				gmtTargetMonth));

		return page;

	}
	
	
}
