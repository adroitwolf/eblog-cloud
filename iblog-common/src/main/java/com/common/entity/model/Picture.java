package com.common.entity.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;

/**
 * <pre>Picture</pre>
 *  传输给用户的图片bean
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class Picture {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String path;

    private String title;
}
