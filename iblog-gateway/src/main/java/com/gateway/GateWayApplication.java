package com.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * <pre>GateWayApplication</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-06
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class },scanBasePackages = {"com.gateway","com.auth"})
@EnableDiscoveryClient
public class GateWayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GateWayApplication.class);
    }
}
