package com.common.entity.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * <pre>User</pre>
 *  用户账号
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    //这里指的是邮箱
    private String email;

    private List<String> roles;
}
