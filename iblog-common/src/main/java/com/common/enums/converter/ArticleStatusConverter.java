package com.common.enums.converter;

import com.common.enums.ArticleStatusEnum;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: WHOAMI
 * Time: 2019 2019/10/18 19:00
 * Description: 博客状态转换器
 */
@Component
public class ArticleStatusConverter extends BaseConverter<ArticleStatusEnum, String> {
    public ArticleStatusConverter() {
        super(ArticleStatusEnum.class);
    }


}
