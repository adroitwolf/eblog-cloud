package com.common.entity.pojo;

import lombok.*;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * <pre>Comments</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-06
 */
@Table(name = "e_comments")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Comments {
    @Id
    private Long id;

    private Long authorId;

    private Long objectId;

    private Long root;

    private Long pid;

    private String content;

    private Byte type;

    private Long fromId;

    private Long toId;

    private Date createTime;

    private Date updateTime;

    private String ipAddress;

    private Byte isDel;

    private Byte isAuthor;
}
