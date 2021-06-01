package com.common.entity.pojo;

import lombok.*;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * <pre>BlogLabel</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-06
 */
@Table(name = "e_blog_label")
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogLabel {
    @Id
    private Long id;

    private String title;

    private Date createDate;

    /**
     * 引用人数
     */
    private Integer citeNum;
}
