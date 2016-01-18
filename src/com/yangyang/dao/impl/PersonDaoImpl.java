package com.yangyang.dao.impl;

import com.yangyang.dao.PersonDao;

public class PersonDaoImpl implements PersonDao{

	@Override
	public void savePerson() {
		System.out.println("dao中的save方法调用成功");
	}

}
