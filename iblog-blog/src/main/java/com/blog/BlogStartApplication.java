package com.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * <pre>BlogStartApplication</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-06
 */
@MapperScan(basePackages = {"com.blog.dao"})
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class BlogStartApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogStartApplication.class);
    }
}
