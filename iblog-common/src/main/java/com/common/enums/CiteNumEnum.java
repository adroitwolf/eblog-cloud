package com.common.enums;

/**
 * Created with IntelliJ IDEA.
 * User: WHOAMI
 * Time: 2019 2019/11/10 13:10
 * Description: 根据引用人数状态所设集合
 */
public enum CiteNumEnum implements BaseEnum<Boolean> {
    /**
     * 增加
     */
    ADD(true),
    /**
     * 减少
     */
    REDUCE(false);

    private final Boolean value;

    CiteNumEnum(Boolean value) {
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }
}
