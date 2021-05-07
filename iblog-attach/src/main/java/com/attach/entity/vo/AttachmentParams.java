package com.attach.entity.vo;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * <pre>AttachmentParams</pre>
 *  附件请求的参数
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@Data
@ToString
public class AttachmentParams {
    @NotBlank(message = "附件名称不能为空")
    @Size(max = 255, message = "附件名称的字符长度不能超过 {max}")
    private String title;
}
