package com.common.entity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * <pre>PictureInfo</pre>
 *  用户上传图片的详细信息
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PictureInfo extends Picture {
    private Date uploadDate;

    private String suffx;

    private Long size;

    private Integer width;

    private Integer height;

    private Integer citeNum;

    private String mediaType;
}
