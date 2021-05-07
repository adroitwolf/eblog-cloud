package com.blog.entity.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <pre>QueryParams</pre>
 * 用户可以搜索的条件
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@Data
@ToString
@NoArgsConstructor
public class QueryParams {
    private String keyword;

    private String status;
}
