package com.blog.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * <pre>PComment</pre>
 *  父类评论
 * @author <p>ADROITWOLF</p> 2021-05-09
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PComment extends Comment{
    private long childrenCount;


    private List<Comment> children;
}
