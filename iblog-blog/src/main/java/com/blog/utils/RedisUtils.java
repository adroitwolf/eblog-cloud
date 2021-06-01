package com.blog.utils;

import com.blog.entity.model.ClickStatus;

/**
 * <pre>RedisUtils</pre>
 *  redis相关工具类
 * @author <p>ADROITWOLF</p> 2021-06-01
 */
public class RedisUtils {

    /**
     * 功能描述: 通过客户端生成文章点击量的key值
     *
     * @Author: WHOAMI
     * @Date: 2019/11/29 17:00
     */
    public static String getClickSetKey(ClickStatus clickStatus) {
        StringBuilder builder = new StringBuilder();
        builder.append(clickStatus.getSessionId());
        builder.append("-");
        builder.append(clickStatus.getRemoteIp());

        return builder.toString();
    }
}
