package com.user.entity.vo;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * <pre>LoginParams</pre>
 *  用户登陆密码
 * @author <p>ADROITWOLF</p> 2021-05-08
 */
@Data
@ToString
public class LoginParams {
    @NotBlank(message = "用户名不能为空！")
    @Size(max = 255, message = "用户名称超出长度{max}")
    private String p;

    @NotBlank(message = "密码不能为空")
    @Size(max = 100, message = "密码长度不能超过{max}")
    private String password;
}
