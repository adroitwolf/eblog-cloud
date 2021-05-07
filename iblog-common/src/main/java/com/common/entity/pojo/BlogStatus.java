package com.common.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Table;

/**
 * <pre>BlogStatus</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-06
 */
@Table(name = "e_blog_status")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BlogStatus {
    private Long id;

    private Integer clickcount;
}