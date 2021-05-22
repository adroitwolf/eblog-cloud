package com.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Random;
import java.util.UUID;

/**
 * <pre>CommonUtils</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-06
 */
public class CommonUtils {
    private static final SnowFlake SNOW_FLAKE = new SnowFlake(0, 0);

    /**
     * 功能描述: 流水生成自增id
     *
     * @Author: WHOAMI
     * @Date: 2019/11/29 16:18
     */
    public static long nextId() {
        return SNOW_FLAKE.nextId();
    }

    /**
     * 获取uuid
     */
    public static String randomUUIDWithoutDash() {
        return StringUtils.remove(UUID.randomUUID().toString(), "-");
    }

    /**
     * 功能描述: 生成6位验证码
     *
     * @Param: []
     * @Return: java.lang.String
     * @Author: WHOAMI
     * @Date: 2020/3/10 12:43
     */
    public static String getCode() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            int randomInt = random.nextInt(10);
            builder.append(randomInt);
        }

        return builder.toString();
    }
}
