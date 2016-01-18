package com.juit;

import org.junit.BeforeClass;
import org.junit.Test;

import com.juit.YhdClassPathXmlApplicationContext;
import com.yangyang.service.PersonService;

public class SpringTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testInstanceSping() {
		YhdClassPathXmlApplicationContext ctx=new YhdClassPathXmlApplicationContext("resources/beans.xml");
		PersonService personService=(PersonService)ctx.getBean("personService3");
		personService.savePerson();
	}

}
