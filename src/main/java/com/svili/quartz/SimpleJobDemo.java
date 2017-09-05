package com.svili.quartz;

import javax.annotation.Resource;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.svili.util.DateUtil;

/***
 * JobDemo</br>
 * 1.extends QuartzJobBean</br>
 * 2.依赖注入由{@link com.ybg.job.SimpleJobFactory}完成.</br>
 * 3.Job类不要使用@Component注解,因为它是由Quartz容器管理,而不是Spring容器.
 * 
 * @author svili
 * @data 2017年7月21日
 *
 */
public class SimpleJobDemo extends QuartzJobBean {

	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		String sql = "select 1 from dual ";
		int num = jdbcTemplate.queryForObject(sql, Number.class).intValue();
		System.out.println(DateUtil.format() + " : " + num);
	}

}
