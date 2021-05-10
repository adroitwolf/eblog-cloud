package com.attach.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <pre>ResourceConfig</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-10
 */

@Configuration
@Slf4j
public class ResourceConfig  implements WebMvcConfigurer {
    @Value("${resourcePath}")
    private String resourcePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("加载静态资源路径{}",resourcePath);
        registry.addResourceHandler("/images/**").addResourceLocations(resourcePath);
    }
}
