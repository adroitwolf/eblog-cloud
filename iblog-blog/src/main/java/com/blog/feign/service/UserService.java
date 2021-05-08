package com.blog.feign.service;

import com.common.entity.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * <pre>UserService</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@FeignClient(name = "iblog-user")
@Component
public interface UserService {
    @GetMapping("/feign/user/getUserDTO/{id}")
    UserDto getUserDTOById(@PathVariable("id") Long id);
}
