package com.blog.entity.model;

import com.blog.entity.vo.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <pre>MComment</pre>
 *  后台管理的评论模型
 * @author <p>ADROITWOLF</p> 2021-05-09
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MComment {
    Comment self;
    Comment parent;

    BaseBlog blog;
}
