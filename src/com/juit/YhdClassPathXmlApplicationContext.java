package com.juit;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;

import com.core.xml.XmlParse;

public class YhdClassPathXmlApplicationContext {
	
	private List<BeanDefinition> beanDefines=new ArrayList<BeanDefinition>();//用来存储实例化前的bean,即配置文件中配置的bean
	private Map<String, Object> sigletons =new HashMap<String, Object>();//用来存储实例化后的bean
	
	public YhdClassPathXmlApplicationContext(String fileName){
		//1.读取spring的配置文件
	//	this.readXml(fileName);
		beanDefines = XmlParse.readXml2(fileName);
		//2.实例化bean
		this.instanceBeans(beanDefines);
		//3.注解方式注入依赖对象
		this.annotationInject();
		//4.实现对依赖对象的注入功能
		this.injectObject();
	}
	/**
	 * 注解方式注入
	 * 
	 * Administer
	 * 2013-8-25 下午8:08:29
	 */
	private void annotationInject() {
		//遍历所有的bean
		for (String beanName : sigletons.keySet()) {
			Object bean=sigletons.get(beanName);//获取需要注入的bean
			if (bean != null) {
				try {
					//先对属性进行处理，即setter方法上标识有注解的
					BeanInfo info = Introspector.getBeanInfo(bean.getClass());//通过类Introspector的getBeanInfo方法获取对象的BeanInfo 信息  
					PropertyDescriptor[] pds = info.getPropertyDescriptors();//获得 bean所有的属性描述
					for (PropertyDescriptor pd : pds) {
						Method setter=pd.getWriteMethod();//获取属性的setter方法
						//属性存在setter方法，并且setter方法存在YhdResource注解
						if (setter != null && setter.isAnnotationPresent(YhdResource.class)) {
							YhdResource resource=setter.getAnnotation(YhdResource.class);//取得setter方法的注解
							Object value=null;
							//注解的name属性不为空
							if (resource != null && resource.name() != null && !"".equals(resource.name())) {
								value=sigletons.get(resource.name());//根据注解的name属性从容器中取出来
							}else {//注解上没有标注name属性
								value=sigletons.get(pd.getName());//根据属性的名称取集合中寻找此名称的bean
								if (value == null) {
									//没找到，遍历所有所有的bean，找类型相匹配的bean
									for (String key : sigletons.keySet()) {
										//判断类型是否匹配
										if (pd.getPropertyType().isAssignableFrom(sigletons.get(key).getClass())) {
											value=sigletons.get(key);//类型匹配的话就把此相同类型的
											break;//找到了类型相同的bean，退出循环
										}
									}
								}
							}
							setter.setAccessible(true);//保证setter方法可以访问私有
							try {
								setter.invoke(bean,value);//把引用对象注入到属性中
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					
					//再对字段进行处理，即对字段上标识有注解
					Field[] fields=bean.getClass().getDeclaredFields();//取得声明的所有字段
					for (Field field : fields) {
						//判断字段上是否存在注解,若存在
						if (field.isAnnotationPresent(YhdResource.class)) {
							YhdResource resource=field.getAnnotation(YhdResource.class);//取得字段上的注解
							Object value=null;
							//字段上存在注解，并且字段上注解的name属性不为空
							if (resource != null && resource.name() != null && !resource.name().equals("")) {
								value=sigletons.get(resource.name());//依赖对象为根据此注解的name属性指定的对象
							}else {
								value=sigletons.get(field.getName());//根据字段的名称到容器中寻找bean
								if (value == null) {
									//没找到，根据字段的类型去寻找
									for (String key : sigletons.keySet()) {
										//判断类型是否匹配
										if (field.getType().isAssignableFrom(sigletons.get(key).getClass())) {
											value=sigletons.get(key);//类型匹配的话就把此相同类型的
											break;//找到了类型相同的bean，退出循环
										}
									}
								}
							}
							field.setAccessible(true);//设置允许访问私有字段
							try {
								field.set(bean, value);//将值为value的注入到bean对象上
							} catch (Exception e) {
								e.printStackTrace();
							}
							
						}
					}
				} catch (IntrospectionException e) {
					e.printStackTrace();
				}
                
			}
		}
	}

	/**
	 * 为bean对象的属性注入值
	 * 
	 * Administer
	 * 2013-8-18 下午7:59:03
	 */
	private void injectObject() {
		//遍历配置文件中定义的所有的bean
		for (BeanDefinition beanDefinition : beanDefines) {
			//找到要注入的bean
			Object bean=sigletons.get(beanDefinition.getId());
			if (bean != null) {
				try {
					BeanInfo info = Introspector.getBeanInfo(bean.getClass());//通过类Introspector的getBeanInfo方法获取对象的BeanInfo 信息
					//通过BeanInfo来获取属性的描述器(PropertyDescriptor),通过这个属性描述器就可以获取某个属性对应的getter/setter方法,然后我们就可以通过反射机制来调用这些方法。
	                PropertyDescriptor[] pds = info.getPropertyDescriptors();//获得 bean所有的属性描述
	                //遍历要注入的bean的所有属性
	                for (PropertyDefinition propertyDefinition : beanDefinition.getPropertyDefinitions()) {
	                	//遍历要注入bean通过属性描述器得到的所有属性以及行为
	                	for (PropertyDescriptor propertyDescriptor : pds) {
	                		//用户定义的bean属性与java内省后的bean属性名称相同时
							if (propertyDefinition.getName().equals(propertyDescriptor.getName())) {
								Method setter=propertyDescriptor.getWriteMethod();//获取属性的setter方法
								//取到了setter方法
								if (setter != null) {
									Object value=null;//用来存储引用的值
									if (propertyDefinition.getRef() != null && !propertyDefinition.getRef().equals("")) {
										value=sigletons.get(propertyDefinition.getRef());//获取引用的对象的值
									}else {
										//ConvertUtil依赖两个jar包，一个是common-beanutils,而common-beanutils又依赖common-logging
										//ConvertUtil将任意类型转化为需要的类型
										value=ConvertUtils.convert(propertyDefinition.getValue(), propertyDescriptor.getPropertyType());
									}
									setter.setAccessible(true);//保证setter方法可以访问私有
									try {
										setter.invoke(bean, value);//把引用对象注入到属性
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								break;//找到了注入的属性后，跳出循环
							}
						}
					}
				} catch (IntrospectionException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 完成实例化beans
	 * 
	 * Administer
	 * 2013-8-18 上午1:07:51
	 */
	private void instanceBeans(List<BeanDefinition> beanDefines) {
		if (beanDefines != null && beanDefines.size() >0) {
			//对每个bean进行实例化
			for (BeanDefinition beanDefinition : beanDefines) {
				try {
					//bean的class属性存在的时候才进行实例化，否则不进行实例化
					if (beanDefinition.getClassName() != null && !beanDefinition.getClassName().equals("")) {
						//实例化的关键操作
						sigletons.put(beanDefinition.getId(),Class.forName(beanDefinition.getClassName()).newInstance());
						System.out.println("id为："+beanDefinition.getId()+"的bean实例化成功");
					}
				} catch (Exception e) {
					System.out.println("bean实例化失败");
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 通过bean名称来获取bean对象
	 * @param beanName
	 * @return
	 * Administer
	 * 2013-8-18 上午1:17:02
	 */
	public Object getBean(String beanName){
		return sigletons.get(beanName);
	}
}
