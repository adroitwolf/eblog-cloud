package com.user.feign.controller;

import com.common.entity.dto.UserDto;
import com.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>FeignController</pre>
 *  暴露给其他微服务的接口
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@RestController
@RequestMapping("feign/user")
public class UserFeignController {

    @Autowired
    UserService userService;

    @GetMapping("/getUserDTO/{id}")
    public  UserDto getUserDTOById(@PathVariable("id") Long id){
        return userService.getUserDTOById(id);
    }
}
