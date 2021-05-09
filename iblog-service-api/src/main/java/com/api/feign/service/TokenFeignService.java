package com.api.feign.service;

import com.api.feign.service.factory.TokenServiceFallBackFactory;
import com.common.entity.model.User;
import com.common.entity.vo.AutoToken;
import com.common.enums.RoleEnum;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <pre>TokenService</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@FeignClient(name = "iblog-auth")
public interface TokenFeignService {
    @PostMapping("/feign/auth/getUserId/{token}")
    Long getUserIdByToken(@PathVariable("token")String token);

    @PostMapping("/feign/auth/buildToken")
    AutoToken buildAutoToken(@RequestBody User user);

    @GetMapping("/feign/auth/refreshToken/getUserId/{token}")
    Long getUserIdByRefreshToken(@PathVariable("token") String token);

    @PostMapping("/feign/auth/refreshToken/del/{key}")
    boolean deleteRefreshToken(@RequestParam("key") String key);

    @PostMapping("/feign/auth/authentication")
    boolean authentication(@RequestParam("id") Long id,@RequestParam("token") String token);

    @PostMapping("/feign/auth/getRoles/{token}")
    List<RoleEnum> getRoles(@PathVariable("token") String token);
}
