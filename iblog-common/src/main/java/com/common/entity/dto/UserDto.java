package com.common.entity.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <pre>UserDto</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 这里指的是用名称不是登录名
     */
    private String nickname;

    private String avatar;
}
