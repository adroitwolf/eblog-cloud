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
@SpringBootApplication(scanBasePackages ={"com.blog","com.auth","com.api","com.common"} )
@EnableDiscoveryClient
@EnableIBFeignClients
@MapperScan(basePackages = {"com.blog.dao","com.auth"})
public class BlogStartApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogStartApplication.class);
    }
}
