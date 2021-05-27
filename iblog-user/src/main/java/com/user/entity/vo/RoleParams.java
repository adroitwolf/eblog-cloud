package com.user.entity.vo;

import com.common.enums.RoleEnum;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <pre>RoleParams</pre>
 *  更新用户穿过来的参数
 * @author <p>ADROITWOLF</p> 2021-05-27
 */
@Data
@ToString
public class RoleParams {

    @NotEmpty(message = "至少选择一名角色")
    private List<RoleEnum> roles;

    @NotBlank(message = "用户id不能为空")
    private Long userId;
}
