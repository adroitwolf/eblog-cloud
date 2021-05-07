package com.common.entity.pojo;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <pre>BloggerProfile</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-06
 */
@Table(name = "e_user_profile")
@Data
@ToString
public class BloggerProfile {
    @Id
    private Long bloggerId;

    private String aboutMe;

    private String nickname;

    private Long avatarId;
}
