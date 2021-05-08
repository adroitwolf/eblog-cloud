package com.user.service;

import java.util.concurrent.TimeUnit;

/**
 * <pre>RedisService</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-08
 */
public interface RedisService {

    /**
     * 功能描述: 存放email和code的匹配序列
     *
     * @Param: [email, code, timeout, timeUnit]
     * @Return: void
     * @Author: WHOAMI
     * @Date: 2020/3/10 12:47
     */
    void putEmailCode(String email, String code, int timeout, TimeUnit timeUnit);


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
     * 功能描述: 获取到redis锁
     *
     * @Param: [key, value, timeout, timeUnit]
     * @Return: java.lang.Boolean
     * @Author: WHOAMI
     * @Date: 2020/1/30 19:45
     */
    Boolean getLock(String key, String value, int timeout, TimeUnit timeUnit);

    /**
     * 功能描述: 通过验证码获取注册邮箱
     *
     * @Param: [code]
     * @Return: java.lang.String
     * @Author: WHOAMI
     * @Date: 2020/4/6 16:35
     */
    String getEmailByCode(String code);
}
