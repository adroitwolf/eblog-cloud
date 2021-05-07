package com.common.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

/**
 * Created with IntelliJ IDEA.
 * User: WHOAMI
 * Time: 2019 2019/7/22 11:21
 * Description: 自定义异常文件
 */
public abstract class IBlogException extends RuntimeException {


    private Object errData;

    public IBlogException(String message) {
        super(message);
    }

    public IBlogException(String message, Throwable cause) {
        super(message, cause);
    }


    public abstract HttpStatus getStatus();

    public Object getErrorData() {
        return errData;
    }


    public IBlogException setErrorData(@Nullable Object errorData) {
        this.errData = errorData;
        return this;
    }

}
