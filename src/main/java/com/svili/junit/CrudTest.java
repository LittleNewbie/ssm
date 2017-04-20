package com.svili.junit;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import com.svili.crud.UserDao;
import com.svili.portal.pojo.User;
import com.svili.portal.type.DataState;

//@RunWith(SpringJUnit4ClassRunner.class)
@RunWith(JUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:configs/spring.xml", "classpath:configs/mybatis-config.xml" })
//@Transactional
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class CrudTest {

	private static Logger logger = Logger.getLogger(CrudTest.class);

	@Resource(name = "userDao")
	private UserDao dao;
	

	@Test
	public void selectTest() throws Exception {
		User user = dao.select(8);
		//System.out.println(user.toString());
		logger.info(user);
		/*TableTest2 obj =crudService.selectByPrimaryKey(TableTest2.class, 1);
		logger.info(obj.getId());
		logger.info(obj.getName());
		logger.info(obj.getState());
		logger.info(obj.getTime());*/
	}
	
	//@Test
	public void insert(){
		User user = new User();
		user.setUserId(100);
		user.setUserName("svili");
		user.setState(DataState.EFFECT);
		user.setUpdateTime(new Date());
		dao.insert(user);
	}

}
