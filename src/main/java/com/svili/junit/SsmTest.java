package com.svili.junit;

import org.apache.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

//@RunWith(SpringJUnit4ClassRunner.class)
@RunWith(JUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:configs/spring.xml", "classpath:configs/mybatis-config.xml" })
//@Transactional
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class SsmTest {

	private static Logger logger = Logger.getLogger(SsmTest.class);


}
