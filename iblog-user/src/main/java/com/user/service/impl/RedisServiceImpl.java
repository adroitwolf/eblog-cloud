package com.user.service.impl;

import com.common.exception.BadRequestException;
import com.user.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * <pre>RedisServiceImpl</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-08
 */
@Service
@Slf4j
public class RedisServiceImpl implements RedisService {
    //  邮箱验证码--redis锁
    private static final String LOCAL_EMAIL_CODE_RE = "LOCAL_EMAIL_CODE_PRE";

    //redis通用锁value
    private final String LOCAL_VALUE = "LOCAL_VALUE";

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public void putEmailCode(String email, String code, int timeout, TimeUnit timeUnit) {
        StringBuilder lockKey = new StringBuilder();

        lockKey.append(LOCAL_EMAIL_CODE_RE);

        lockKey.append(email);

        Boolean lock = getLock(lockKey.toString(), LOCAL_VALUE, 5, TimeUnit.SECONDS);

        if (!lock) {
            throw new BadRequestException("请不要频繁操作");
        }
        try {

            redisTemplate.opsForValue().setIfAbsent(code, email, timeout, timeUnit);

        } finally {
            delete(lockKey.toString());
        }
    }

    //删除redis键值对
    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 获取redis锁
     */
    @Override
    public Boolean getLock(String key, String value, int timeout, TimeUnit timeUnit) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeUnit);
    }

    @Override
    public String getEmailByCode(String code) {
        String string = redisTemplate.opsForValue().get(code).toString();
        //删除验证码
        delete(code);

        return string;
    }
}
