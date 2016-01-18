package com.juit;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean对象
 * @author Administer
 *
 */
public class BeanDefinition {

	private String id;//bean的id
	private String className;//bean的类
	private List<PropertyDefinition> propertyDefinitions=new ArrayList<PropertyDefinition>();//bean对象的属性
	
	public BeanDefinition(String id, String className) {
		this.id = id;
		this.className = className;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public List<PropertyDefinition> getPropertyDefinitions() {
		return propertyDefinitions;
	}
	public void setPropertyDefinitions(List<PropertyDefinition> propertyDefinitions) {
		this.propertyDefinitions = propertyDefinitions;
	}
}
