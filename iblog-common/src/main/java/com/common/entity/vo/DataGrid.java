package com.common.entity.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * <pre>DataGrid</pre>
 * 列表类型的bean
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@Data
@ToString
public class DataGrid{
    private long total;

    private List<?> rows;
}
