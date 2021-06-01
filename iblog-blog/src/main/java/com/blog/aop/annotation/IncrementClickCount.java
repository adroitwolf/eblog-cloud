package com.blog.aop.annotation;

import java.lang.annotation.*;

/**
 * <pre>IncrementClickCount</pre>
 *  增加点击量注解
 * @author <p>ADROITWOLF</p> 2021-06-01
 */
@Target(ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface IncrementClickCount {

}
