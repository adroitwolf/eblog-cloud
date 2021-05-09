package com.common.entity.vo;

import lombok.*;

import javax.validation.constraints.Min;

/**
 * <pre>PageInfo</pre>
 *  用来表示页面的实体类
 * @author <p>ADROITWOLF</p> 2021-05-06
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PageInfo {
    @NonNull
    @Min(value = 0, message = "请不要进行非法操作")
    int pageSize;

    @NonNull
    @Min(value = 0, message = "请不要进行非法操作")
    int pageNum;

    @NonNull
    String sortName;

    @NonNull
    String sortOrder  = "desc" ;


}
