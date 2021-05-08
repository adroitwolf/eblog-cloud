package com.blog.entity.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <pre>BaseBlog</pre>
 *  博客的基础信息
 * @author <p>ADROITWOLF</p> 2021-05-08
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BaseBlog {
    private String title;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String picture;
}
