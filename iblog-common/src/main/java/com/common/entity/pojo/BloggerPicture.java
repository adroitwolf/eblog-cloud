package com.common.entity.pojo;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * <pre>BloggerPicture</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-06
 */
@Table(name = "e_blogger_picture")
@Data
@ToString
public class BloggerPicture {
    @Id
    private Long id;

    private Long bloggerId;

    private String path;

    private String title;

    private Date uploadDate;

    private String suffx;

    private Long size;

    private Integer width;

    private Integer height;

    private Date updateDate;

    private Integer citeNum;

    private String thumbPath;

    private String mediaType;

    private String fileKey;
}
