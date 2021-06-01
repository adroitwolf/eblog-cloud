package com.attach;

import com.api.annotation.EnableIBFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * <pre>AttachStartApplication</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@EnableIBFeignClients
@MapperScan(basePackages = {"com.attach.dao","com.auth"})
@SpringBootApplication(scanBasePackages ={"com.attach","com.auth"} )
@EnableDiscoveryClient
public class AttachStartApplication {
    public static void main(String[] args) {
        SpringApplication.run(AttachStartApplication.class);
    }

}


