package com.auth.service;

import java.util.concurrent.TimeUnit;

/**
 * <pre>RedisService</pre>
 *  redis服务层
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
public interface RedisService {
    /**
     * 功能描述: 存放token与access_token机制
     *
     * @Param: [userId, accessToken, refreshToken, timeout, timeUnit]
     * @Return: void
     * @Author: WHOAMI
     * @Date: 2020/2/27 22:03
     */
    void putAutoToken(String accessToken, Long userId, int timeout, TimeUnit timeUnit);


    /**
     * 功能描述: 获取到redis锁
     *
     * @Param: [key, value, timeout, timeUnit]
     * @Return: java.lang.Boolean
     * @Author: WHOAMI
     * @Date: 2020/1/30 19:45
     */
    Boolean getLock(String key, String value, int timeout, TimeUnit timeUnit);


    /**
     * 功能描述: 删除redis键值对
     *
     * @Param: [key]
     * @Return: void
     * @Author: WHOAMI
     * @Date: 2020/1/30 19:45
     */
    void delete(String key);


    /**
     * 功能描述: 通过refreshToken获取用户id，当token无效时会抛出异常
     *
     * @Param: [key]
     * @Return: java.lang.Long
     * @Author: WHOAMI
     * @Date: 2020/4/6 16:38
     */
    Long getUserIdByRefreshToken(String key);
}
