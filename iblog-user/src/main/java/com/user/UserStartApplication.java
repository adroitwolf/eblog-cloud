package com.user;

import com.api.annotation.EnableIBFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * <pre>UserStartApplication</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableIBFeignClients
@MapperScan(basePackages={"com.user.dao","com.auth"})
public class UserStartApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserStartApplication.class);
    }
}
