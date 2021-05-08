package com.blog.feign.service;

import com.common.enums.RoleEnum;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * <pre>TokenService</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@FeignClient(name = "iblog-auth")
@Component
public interface TokenService {
    @GetMapping("/feign/auth/getUserId/{token}")
    Long getUserIdByToken(@PathVariable("token")String token);

    @GetMapping("/feign/auth/authentication")
    boolean authentication(Long id, String token);

    @GetMapping("/feign/auth/getRoles/{token}")
    List<RoleEnum> getRoles(@PathVariable("token") String token);
}
