package com.litb.opapi.test.service;

import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.litb.opapi.test.TestCaseBase;

public class DeveloperServiceTest  extends TestCaseBase{
	private static Logger logger = LoggerFactory.getLogger(DeveloperServiceTest.class);

//    @Autowired IDeveloperService developerService;
//    @Autowired IOpDeveloperDao developerDao;

	@Before
	public void setUp() throws Exception {
		logger.info("setUp before test function running.");
	} 
	
	@After
	public void tearDown() throws Exception { 
		logger.info("clean up after test function finished.");
	} 
	
//	@Test
//	@Transactional
//	public void testDeveloperService0() {
//		logger.info("Goto test developerService.");
//		OpDeveloper developer;
//		developer = developerDao.find(2);
//		Assert.assertTrue(developer.getDeveloperId()>0);
//		
//		developer = developerDao.findDeveloperByAppKey("ec7cd2b0c4837fe2ff276a66ff16d3ed");
//		Assert.assertTrue(developer.getDeveloperId()>0);
//
//		OpOtherPlatformUser otherPlatformUser;
//		otherPlatformUser = developerDao.findOtherPlatformUserByAppKey("ec7cd2b0c4837fe2ff276a66ff16d3ed");
//		Assert.assertTrue(otherPlatformUser.getUserId()>0);
//		
//		otherPlatformUser = developerDao.findOtherPlatformUserByExample(otherPlatformUser);
//		Assert.assertTrue(otherPlatformUser.getUserId()>0);
//		
//		OpApplication application;
//		application = developerDao.findApplicationByAppKey("ec7cd2b0c4837fe2ff276a66ff16d3ed");
//		Assert.assertTrue(application.getAppId()>0);
//	}
	
    
}
