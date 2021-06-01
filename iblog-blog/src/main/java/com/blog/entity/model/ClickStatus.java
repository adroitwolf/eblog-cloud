package com.blog.entity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <pre>ClickStatus</pre>
 *  点赞状态
 * @author <p>ADROITWOLF</p> 2021-06-01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ClickStatus {

    private Long blogId;
    private String remoteIp;
    private String sessionId;
    private Integer count;
}
