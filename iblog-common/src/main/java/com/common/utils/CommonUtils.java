package com.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * <pre>CommonUtils</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-06
 */
public class CommonUtils {
    private static final SnowFlake snowFlake = new SnowFlake(0, 0);

    /**
     * 功能描述: 流水生成自增id
     *
     * @Author: WHOAMI
     * @Date: 2019/11/29 16:18
     */
    public static long nextId() {
        return snowFlake.nextId();
    }

    /**
     * 获取uuid
     */
    public static String randomUUIDWithoutDash() {
        return StringUtils.remove(UUID.randomUUID().toString(), "-");
    }

}
