package com.juit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解
a． java的注解实际上是自动继承了java.lang.annotation.Annotation接口，因此在自定义注解时不能继承其他的注解或者接口。
b． Retention：告诉编译器如何处理注解，即注解运行在什么时刻。
RetentionPolicy是个枚举类型，有以下三种状态值：
1).SOURCE：该注解存储在源文件中，编译过后即废弃。
2).CLASS：该注解存储在class文件中，是其缺省的值。
3).RUNTIME：该注解存储在class文件中，并且有Java虚拟机读入解析，该类型非常重要，可以使用反射机制获取注解相关信息，可以进行程序分析处理。
c. @Target：指定注解的目标使用对象。
ElementType也是个枚举类型，有以下几种状态值：
1).TYPE：该注解适用于class,interface,enum等类级别的目标对象。
2).FIELD：该注解适用于类中字段级别的目标对象。
3).METHOD：该注解适用于类中方法级别的目标对象。
4).PARAMETER：该注解适用于方法参数级别的目标对象。
5).CONSTRUCTOR：该注解适用于类构造方法。
6).LOCAL_VARIABLE：该注解适用于局部变量。
7).ANNOTATION_TYPE：该注解适用于annotation对象。
8).PACKAGE：该注解适用于package对象。
d.注解里面只能声明属性，不能声明方法，声明属性的方式比较特殊：
语法格式为：数据类型 属性() default 默认值(默认值是可选的); 如：Stringvalue();
使用时，“注解对象(属性=属性值)”为注解指定属性值，通过“注解对象.属性;”就可以得到注解属性的值。
e.注解的解析：使用java反射机制获得注解的目标对象就可以得到注解对象，如：
通过反射得到了注解目标的Field对象field，则通过“field.getAnnotation(注解类.class);”就可以得到注解对象。*/

@Retention(RetentionPolicy.RUNTIME)//注解处理在运行时刻
@Target({ElementType.FIELD,ElementType.METHOD})//对字段和方法使用注解
public @interface YhdResource {
	public String name() default "";//注解里面只能声明属性，不能声明方法，声明属性的方式比较特殊：
							//语法格式为：数据类型 属性() default 默认值(默认值是可选的); 如：Stringvalue();
}
