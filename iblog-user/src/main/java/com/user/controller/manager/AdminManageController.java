package com.user.controller.manager;

import com.common.entity.vo.BaseResponse;
import com.common.entity.vo.PageInfo;
import com.common.entity.vo.QueryParams;
import com.user.entity.vo.RoleParams;
import com.user.service.RoleService;
import com.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <pre>AdminManageController</pre>
 * 管理角色的控制器
 * @author <p>ADROITWOLF</p> 2021-05-26
 */
@RestController
@RequestMapping("/manage/role")
public class AdminManageController {
    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService;

    /**
     * 获取所有超级管理员可以管理的用户
     * @param pageInfo
     * @param queryParams
     * @return
     */
    @GetMapping("/getListUsers")
    public BaseResponse listUsers(@Valid PageInfo pageInfo, QueryParams queryParams){
        return userService.getAllUserInfo(queryParams,pageInfo);
    }

    @GetMapping("/getRoles")
    public BaseResponse listRoles(){
        return roleService.getRoles();
    }

    /**
     * 更新用户角色状态
     * @param roleParams
     * @return
     */
    @PutMapping("/updateRoles")
    public BaseResponse updateRoles(@RequestBody  RoleParams roleParams){
        return roleService.updateUserRoles(roleParams);
    }
}
