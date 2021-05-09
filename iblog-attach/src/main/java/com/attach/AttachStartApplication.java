package com.attach;

import com.api.annotation.EnableIBFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * <pre>AttachStartApplication</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@EnableIBFeignClients
@MapperScan(basePackages = {"com.attach.dao"})
@SpringBootApplication
@EnableDiscoveryClient
public class AttachStartApplication {
    public static void main(String[] args) {
        SpringApplication.run(AttachStartApplication.class);
    }

}


