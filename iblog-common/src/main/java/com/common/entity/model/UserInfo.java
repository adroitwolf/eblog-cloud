package com.common.entity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <pre>UserInfo</pre>
 *  用户详细信息
 * @author <p>ADROITWOLF</p> 2021-05-08
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo extends User{
    private String email;

    private String aboutMe;

    private String nickname;

    private Long avatarId;

    private String avatar;

    private String registerDate;

    private String isEnabled;
    /**
     * 用户账号
     */
    private String username;
}
