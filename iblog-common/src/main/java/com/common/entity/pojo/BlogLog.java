package com.common.entity.pojo;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * <pre>BlogLog</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-06
 */
@Table(name = "e_blog_log")
@ToString
@Data
public class BlogLog {
    @Id
    private Long id;

    private String romoteip;

    private String username;

    private Date romotetime;

    private String operation;
}
