package com.api.annotation;

import org.springframework.cloud.openfeign.EnableFeignClients;

import java.lang.annotation.*;

/**
 * <pre>EnableIBFeignClients</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-09
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableFeignClients
public @interface EnableIBFeignClients {
    String[] value() default {};

    String[] basePackages() default {"com.api"};

    Class<?>[] basePackageClasses() default {};

    Class<?>[] defaultConfiguration() default {};

    Class<?>[] clients() default {};
}
