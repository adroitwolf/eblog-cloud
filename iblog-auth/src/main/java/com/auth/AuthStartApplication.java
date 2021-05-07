package com.auth;

import com.auth.props.JWTProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * <pre>AuthStartApplication</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties(JWTProperties.class)
@MapperScan(basePackages={"com.auth.dao"})
public class AuthStartApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthStartApplication.class);
    }
}
