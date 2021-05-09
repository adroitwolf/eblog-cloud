package com.auth.feign.controller;

import com.auth.service.TokenService;
import com.common.entity.model.User;
import com.common.entity.vo.AutoToken;
import com.common.enums.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <pre>FeignController</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@RestController
@RequestMapping("/feign/auth")
public class TokenFeignController {
    @Autowired
    TokenService tokenService;

    @PostMapping("/getUserId/{token}")
    public Long getUserIdByToken(@PathVariable("token")String token){
        return tokenService.getUserIdWithToken(token);
    }


    @PostMapping("/authentication")
    public boolean authentication(@RequestParam("id") Long id,@RequestParam("token") String token){
        return tokenService.authentication(id,token);
    }

    @PostMapping("/getRoles/{token}")
    public List<RoleEnum> getRoles(@PathVariable("token") String token){
        return tokenService.getRoles(token);
    }

    @PostMapping("/buildToken")
    public AutoToken buildAutoToken(@RequestBody  User user){
        return tokenService.buildAutoToken(user);
    }



    @PostMapping("/refreshToken/getUserId/{token}")
    public Long getUserIdByRefreshToken(@PathVariable("token") String token){
        return tokenService.getUserIdByRefreshToken(token);
    }


    @PostMapping("/refreshToken/del/{key}")
    public boolean deleteRefreshToken(@PathVariable("key") String key){
        tokenService.deleteRefreshToken(key);
        return true;
    }
}
