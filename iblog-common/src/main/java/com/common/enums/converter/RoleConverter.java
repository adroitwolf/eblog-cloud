package com.common.enums.converter;

import com.common.enums.RoleEnum;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: WHOAMI
 * Time: 2019 2019/10/30 21:23
 * Description: 角色转换器
 */
@Component
public class RoleConverter extends BaseConverter<RoleEnum, String> {
    public RoleConverter() {
        super(RoleEnum.class);
    }
}
