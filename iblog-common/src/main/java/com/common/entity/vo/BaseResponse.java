package com.common.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <pre>BaseResponse</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-06
 */
@Data
@NoArgsConstructor
public class BaseResponse {
    private Integer status;
    private String message;
    private Object data;

    public BaseResponse(Integer status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static BaseResponse success(Object data){
        return new BaseResponse(200,"请求成功",data);
    }
}
