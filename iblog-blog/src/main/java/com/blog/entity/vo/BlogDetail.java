package com.blog.entity.vo;

import com.blog.entity.model.Blog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <pre>BlogContent</pre>
 *  博客内容的详细内容
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class BlogDetail extends Blog {
    private String content;

    private String contentMd;
}
