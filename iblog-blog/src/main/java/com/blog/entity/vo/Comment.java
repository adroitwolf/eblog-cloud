package com.blog.entity.vo;

import com.common.entity.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * <pre>Comment</pre>
 *  返回给前台的评论结构
 * @author <p>ADROITWOLF</p> 2021-05-09
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String content;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long root;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long pid;

    @DateTimeFormat(pattern = "yyyy-mm-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private UserDto user;
}
