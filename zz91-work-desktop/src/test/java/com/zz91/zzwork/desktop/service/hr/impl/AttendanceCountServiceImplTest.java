package com.zz91.zzwork.desktop.service.hr.impl;

import java.sql.SQLException;
import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;

import com.zz91.service.BaseServiceTestCase;
import com.zz91.zzwork.desktop.domain.hr.AttendanceCount;
import com.zz91.zzwork.desktop.dto.PageDto;
import com.zz91.zzwork.desktop.service.hr.AttendanceCountService;

public class AttendanceCountServiceImplTest extends BaseServiceTestCase {

	@Resource
	private AttendanceCountService attendanceCountService;

	@Test
	public void test_pageRuest() {
		clean();
		
		for (int i = 1; i <= 37; i++) {
			insert(null, null, null, 0, 2, 3, 5, null, null, null);
		}
		
		PageDto<AttendanceCount> page = new PageDto<AttendanceCount>();
		page = attendanceCountService.pageResult(null, null, page);
		assertNotNull(page);
		assertNotNull(page.getTotalRecords());
		assertEquals(20, page.getRecords().size());
		

		// 最后一页的测试
		page = new PageDto<AttendanceCount>();
		page.setStart(20);
		page = attendanceCountService.pageResult(null, null, page);
		assertNotNull(page);
		assertNotNull(page.getTotalRecords());
		assertEquals(17, page.getTotalRecords().intValue());

	}

	

	/***********************************/

	private void clean() {
		try {
			connection.prepareStatement("delete from attendance").execute();
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public int insert(String name, String code, String account, Integer punch0,
			Integer punch20, Integer punch60, Integer punchCount,
			Date gmtMonth, Date gmtCreated, Date modeified) {

	
		StringBuffer sb = new StringBuffer();
		sb.append("insert into attendance");
		sb
				.append("(name,code,account,punch0,punch20,punch60,punchCount,gmtMonth,gmtCreated,modeified)");
		sb.append("values");
		sb.append("(" + name + "," + code + "," + account + "," + punch0 + ","
				+ punch20 + "," + punch60 + "," + punchCount + "," + gmtMonth
				+ "," + gmtCreated + "," + modeified + ")");

		try {
			connection.prepareStatement(sb.toString()).execute();
			return insertResult();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}

	}

}
