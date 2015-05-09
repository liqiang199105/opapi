package com.litb.opapi.test;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
		locations = { "classpath*:/spring/applicationContext.xml" }
)
@Transactional
public class TestCaseBase extends TestCase {

	@Test
	public void test() {
		Assert.assertTrue(1>0);
	}
}
