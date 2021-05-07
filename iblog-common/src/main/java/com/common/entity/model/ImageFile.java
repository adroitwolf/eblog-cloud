package com.common.entity.model;

import lombok.Data;
import org.springframework.http.MediaType;

/**
 * <pre>UPLOAD_UTIL</pre>
 *  图像文件详细信息
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@Data
public class ImageFile {
    private String path;

    private String fileKey;

    private String title;

    private String suffx;

    private Long size;

    private Integer width;

    private Integer height;

    private String thumbPath;

    private MediaType mediaType;
}
