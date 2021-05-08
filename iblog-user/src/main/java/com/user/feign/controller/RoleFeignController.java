package com.user.feign.controller;

import com.common.enums.RoleEnum;
import com.user.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <pre>RoleFeignController</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-08
 */
@RestController
@RequestMapping("/feign/role")
public class RoleFeignController {
    @Autowired
    RoleService roleService;

    @GetMapping("/getRoles/{id}")
    public List<RoleEnum> getRolesByUserId(@PathVariable("id") Long userId){
        return roleService.getRolesByUserId(userId);
    }
}
