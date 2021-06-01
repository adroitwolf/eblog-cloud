package com.auth.aop.annotation;

import com.common.enums.RoleEnum;

import java.lang.annotation.*;

/**
 * <pre>Role</pre>
 * 判断进入接口所需要的角色
 * @author <p>ADROITWOLF</p> 2021-05-31
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Role {
    // 所需要角色
    RoleEnum [] require() default {};
}
