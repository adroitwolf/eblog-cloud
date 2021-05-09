package com.common.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <pre>BlogTagMapKey</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-06
 */
@Table(name = "e_blog_tag_map")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BlogTagMapKey {
    @Id
    private Long id;

    private Long tagId;

    private Long blogId;
}
