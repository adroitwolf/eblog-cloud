package com.auth.service;

import com.common.enums.RoleEnum;

import java.util.List;

/**
 * <pre>RoleService</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
public interface RoleService {
    /**
     * 功能描述: 根据用户id获取到当前用户的角色s
     *
     * @Param: [userId]
     * @Return: java.util.List<run.app.entity.enums.RoleEnum>
     * @Author: WHOAMI
     * @Date: 2020/1/30 19:54
     */
    List<RoleEnum> getRolesByUserId(Long userId);


    /**
     * 功能描述: 通过id获取到当前的角色
     *
     * @Param: [id]
     * @Return: run.app.entity.enums.RoleEnum
     * @Author: WHOAMI
     * @Date: 2020/1/30 19:55
     */
    RoleEnum getRoleById(Long id);
}
