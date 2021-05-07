package com.common.enums;

/**
 * Created with IntelliJ IDEA.
 * User: WHOAMI
 * Time: 2019 2019/10/18 18:56
 * Description: 博客状态
 */
public enum ArticleStatusEnum implements BaseEnum<String> {
    /**
     * 审核成功
     */
    PUBLISHED,
    /**
     * 审核中
     */
    CHECK,
    /**
     * 审核失败
     */
    NO,
    /**
     * 回收站
     */
    RECYCLE;

    public String getName() {
        return name();
    }
}
