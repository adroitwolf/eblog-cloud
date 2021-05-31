package com.blog;

import com.api.annotation.EnableIBFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * <pre>BlogStartApplication</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-06
 */
@MapperScan(basePackages = {"com.blog.dao","com.auth"})
@SpringBootApplication
@EnableDiscoveryClient
@EnableIBFeignClients
public class BlogStartApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogStartApplication.class);
    }
}
