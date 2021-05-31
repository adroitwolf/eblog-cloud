package com.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <pre>SwaggerConfig</pre>
 *  API配置文件
 * @author <p>ADROITWOLF</p> 2021-05-31
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket CreateRestfulApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("run.app.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("ADROITWOLF博客API")
                .description("多用户博客门户网站")
                .termsOfServiceUrl("https://adroitwolf.github.io")
                .version("1.0")
                .build();
    }
}
