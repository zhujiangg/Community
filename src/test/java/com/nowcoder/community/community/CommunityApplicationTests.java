package com.nowcoder.community.community;

import com.nowcoder.community.community.config.AlphaConfig;
import com.nowcoder.community.community.controller.AlphaController;
import com.nowcoder.community.community.dao.AlphaDao;
import com.nowcoder.community.community.dao.AlphaDaoHibernateImpl;
import com.nowcoder.community.community.service.AlphaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest

/**
1、使 Test 底下同样使用配置类 CommunityApplication.class 环境测试
 */
@ContextConfiguration(classes = CommunityApplication.class)
class CommunityApplicationTests implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Test
	public void testApplicationContext(){
		System.out.println(applicationContext);
		AlphaDao alphaDao = applicationContext.getBean(AlphaDaoHibernateImpl.class);
		System.out.println(alphaDao.select());
	}

	@Test
	public void testBeanManagement(){
		AlphaService alphaService = applicationContext.getBean(AlphaService.class);
		System.out.println(alphaService);
		alphaService = applicationContext.getBean(AlphaService.class);
		System.out.println(alphaService);
	}

	@Test
	public void testBeanConfig(){
		AlphaConfig alphaConfig = applicationContext.getBean(AlphaConfig.class);
		SimpleDateFormat simpleDateFormat = alphaConfig.simpleDateFormat();
		System.out.println(simpleDateFormat.format(new Date()));
	}

	@Autowired
	@Qualifier("alphaHi")
	private AlphaDao alphaDao;

	@Autowired
	private AlphaService alphaService;

	@Autowired
	private SimpleDateFormat simpleDateFormat;

	@Test
	public void testDI() {
		System.out.println(alphaDao);
		System.out.println(alphaService);
		System.out.println(simpleDateFormat);
	}

}
