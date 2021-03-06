package com.common.enums.converter;

import com.common.enums.BaseEnum;
import org.springframework.core.convert.converter.Converter;

/**
 * Created with IntelliJ IDEA.
 * User: WHOAMI
 * Time: 2019 2019/10/18 17:14
 * Description: :基础的转换器
 */
public abstract class BaseConverter<E extends BaseEnum<V>, V> implements Converter<V, E> {

    private final Class<E> classz;

    public BaseConverter(Class<E> classz) {
        this.classz = classz;

    }

    @Override
    public E convert(V v) {
        return BaseEnum.ValueConvertToEnum(classz, v);
    }
}
