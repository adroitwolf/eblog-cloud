package com.user.controller.manager;

import com.auth.aop.annotation.Role;
import com.common.entity.vo.BaseResponse;
import com.common.entity.vo.PageInfo;
import com.common.entity.vo.QueryParams;
import com.common.enums.RoleEnum;
import com.user.entity.vo.RoleParams;
import com.user.service.RoleService;
import com.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <pre>AdminManageController</pre>
 * 超级管理员管理用户接口
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
    @Role(require = {RoleEnum.LORD})
    @GetMapping("/getListUsers")
    public BaseResponse listUsers(@Valid PageInfo pageInfo, QueryParams queryParams){
        return userService.getAllUserInfo(queryParams,pageInfo);
    }

    @Role(require = {RoleEnum.LORD})
    @GetMapping("/getRoles")
    public BaseResponse listRoles(){
        return roleService.getRoles();
    }

    /**
     * 更新用户角色状态
     * @param roleParams
     * @return
     */
    @Role(require = {RoleEnum.LORD})
    @PutMapping("/updateRoles")
    public BaseResponse updateRoles(@RequestBody  RoleParams roleParams){
        return roleService.updateUserRoles(roleParams);
    }

//    @PutMapping("/updateUserSatus")
//    public BaseResponse updateStatus(){
//
//    }
}
