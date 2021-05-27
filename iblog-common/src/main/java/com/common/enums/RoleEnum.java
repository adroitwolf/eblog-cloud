package com.common.enums;

/**
 * Created with IntelliJ IDEA.
 * User: WHOAMI
 * Time: 2019 2019/10/23 17:39
 * Description: 用户角色模型
 */
public enum RoleEnum implements BaseEnum<String> {
    /**
     *  管理员模型
     */
    ADMIN,
    /**
     * 普通用户角色
     */
    USER,
    /**
     * 超级管理员权限
     */
    LORD;

    public String getAuthority() {
        return name();
    }


}
