package com.user.entity.vo;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * <pre>PasswordParams</pre>
 *  用户修改密码
 * @author <p>ADROITWOLF</p> 2021-05-09
 */
@Data
@ToString
public class PasswordParams {
    @NotBlank(message = "密码不能为空")
    @Size(max = 100, message = "超出特定密码长度")
    private String oldPassword;


    @NotBlank(message = "密码不能为空")
    @Size(max = 100, message = "超出特定密码长度")
    private String newPassword;
}
