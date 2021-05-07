package com.common.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;

/**
 * <pre>BlogTagMapKey</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-06
 */
@Table(name = "e_blog_tag_map")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BlogTagMapKey {
    private Long tagId;

    private Long blogId;
}
