package com.user.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <pre>UserDetail</pre>
 *  用户详细资料
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDetail {
    //这里指的是用户用户名称
    private String nickname;

//    private String phone;
//
//    private String email;

    private String avatar;

    private String aboutMe;

    private List<String> roles;
}
