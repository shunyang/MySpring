package com.core.xml;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

import com.juit.BeanDefinition;
import com.juit.PropertyDefinition;

public class XmlParse {

	/**
	 * 根据文件名读取xml的配置文件
	 * @param fileName
	 * Administer
	 * 2013-8-18 上午12:56:31
	 */
	public static List<BeanDefinition>  readXml(String fileName) {
		List<BeanDefinition> beanDefines = null;
		//创建一个读取器
		SAXReader saxReader=new SAXReader();
		Document document=null;
		try {
			//获取要读取的配置文件的路径
			URL xmlPath=XmlParse.class.getClassLoader().getResource(fileName);
			//读取文件内容
			document=saxReader.read(xmlPath);
			Map<String, String> nsMap=new HashMap<String, String>();
			nsMap.put("ns", "http://www.springframework.org/schema/beans");//加入命名空间
			XPath xsub=document.createXPath("//ns:beans/ns:bean");//定义beans/bean的查询路径
			xsub.setNamespaceURIs(nsMap);//设置命名空间
			List<Element> beans=xsub.selectNodes(document);//获取文档下所有bean节点
			if (beans != null && beans.size() >0) {
				for (Element element : beans) {
					String id=element.attributeValue("id");//获取bean的id属性值
					String clazz=element.attributeValue("class");//获取bean的class属性值
					BeanDefinition beanDefinition=new BeanDefinition(id,clazz);
					beanDefines.add(beanDefinition);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return beanDefines;
	}

	@SuppressWarnings("rawtypes")
	public static List<BeanDefinition> readXml2(String fileName) {
		List<BeanDefinition> beanDefines = new ArrayList<BeanDefinition>();
		//创建一个读取器
		SAXReader saxReader=new SAXReader();
		Document document=null;
		try {
			//获取要读取的配置文件的路径
			URL xmlPath=XmlParse.class.getClassLoader().getResource(fileName);
			//读取文件内容
			document=saxReader.read(xmlPath);
			//获取xml中的根元素
			Element rootElement=document.getRootElement();
			for (Iterator iterator = rootElement.elementIterator(); iterator.hasNext();) {
				Element element = (Element) iterator.next();
				String id=element.attributeValue("id");//获取bean的id属性值
				String clazz=element.attributeValue("class");//获取bean的class属性值
				BeanDefinition beanDefinition=new BeanDefinition(id,clazz);
				//获取bean的Property属性
				for (Iterator subElementIterator = element.elementIterator(); subElementIterator.hasNext();) {
					Element subElement = (Element) subElementIterator.next();
					String propertyName=subElement.attributeValue("name");
					String propertyRef= subElement.attributeValue("ref");
					String propertyValue=subElement.attributeValue("value");
					PropertyDefinition propertyDefinition=new PropertyDefinition(propertyName, propertyRef,propertyValue);
					beanDefinition.getPropertyDefinitions().add(propertyDefinition);
				}
				beanDefines.add(beanDefinition);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return beanDefines;
	}
}
