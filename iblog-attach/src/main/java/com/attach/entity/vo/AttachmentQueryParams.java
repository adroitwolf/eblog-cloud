package com.attach.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <pre>AttachmentQueryParams</pre>
 *  附件查询
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentQueryParams {
    private String keywords;

    private String mediaType;
}
