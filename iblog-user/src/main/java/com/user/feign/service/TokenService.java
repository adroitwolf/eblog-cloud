package com.user.feign.service;

import com.common.entity.model.User;
import com.common.entity.vo.AutoToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/feign/auth/buildToken")
    AutoToken buildAutoToken(User user);

    @GetMapping("/feign/auth/refreshToken/getUserId/{token}")
    Long getUserIdByRefreshToken(@PathVariable("token") String token);

    @PostMapping("/feign/auth/refreshToken/del/{key}")
    boolean deleteRefreshToken(@PathVariable("key") String key);
}
