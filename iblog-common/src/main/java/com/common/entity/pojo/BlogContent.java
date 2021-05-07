package com.common.entity.pojo;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <pre>BlogContent</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-06
 */
@Table(name = "e_blog_content")
@Data
@ToString
public class BlogContent {
    @Id
    private Long id;

    private String content;

    private String contentMd;
}
