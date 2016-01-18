package com.yangyang.service.impl;

import com.juit.YhdResource;
import com.yangyang.dao.PersonDao;
import com.yangyang.model.Person;
import com.yangyang.service.PersonService;

public class PersonServiceImpl implements PersonService{
	@YhdResource
	private PersonDao personDao;
	private String name;
	private Integer age;
	
	public PersonDao getPersonDao() {
		return personDao;
	}
	
	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Override
	public void savePerson() {
		System.out.println("name:"+name+",age:"+age);
		System.out.println("service中的save方法调用成功");
		personDao.savePerson();
	}

}
