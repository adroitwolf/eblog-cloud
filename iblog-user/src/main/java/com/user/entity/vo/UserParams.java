package com.user.entity.vo;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * <pre>UserParams</pre>
 *  用户详细信息
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@Data
@ToString
public class UserParams {
    @NotBlank(message = "昵称不能为空！")
    @Size(max = 20, message = "昵称不能超过{max}")
    private String nickname;

    @Size(max = 50, message = "自我介绍不能超过{max}")
    private String aboutMe;

//    @Email(message = "电子邮件格式不正确")
//    private String email;
}
