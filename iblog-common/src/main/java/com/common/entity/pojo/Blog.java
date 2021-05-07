package com.common.entity.pojo;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * <pre>Blog</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-06
 */
@Table(name="e_blog")
@Data
@ToString
public class Blog {
    @Id
    private Long id;

    private Long bloggerId;

    private String status;

    private String title;

    private Long pictureId;

    private String summary;

    private Date releaseDate;

    private Date nearestModifyDate;

    private String tagTitle;
}
