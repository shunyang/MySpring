package com.juit;

/**
 * 属性模型
 * @author Administer
 *
 */
public class PropertyDefinition {

	/**
	 * 属性名称
	 */
	private String name;
	/**
	 * 属性引用值
	 */
	private String ref;
	
	/**
	 * 属性value值
	 */
	private String value;
	public PropertyDefinition(String name, String ref,String value) {
		this.name = name;
		this.ref = ref;
		this.value=value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	};
}
