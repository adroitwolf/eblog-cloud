package com.auth.service.impl;

import com.auth.service.RedisService;
import com.common.exception.BadRequestException;
import com.common.exception.UnAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * <pre>RedisServiceImpl</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@Service
@Slf4j
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    // 用户登陆--redis锁
    private static final String LOCAL_TOKEN_PRE = "LOCAL_TOKEN_PRE";


    //redis通用锁value
    private static final String LOCAL_VALUE = "LOCAL_VALUE";

    @Override
    public void putAutoToken(String refreshToken, Long userId, int timeout, TimeUnit timeUnit) {

        StringBuilder lockKey = new StringBuilder();

        lockKey.append(LOCAL_TOKEN_PRE);

        lockKey.append(userId);


        Boolean lock = getLock(lockKey.toString(), LOCAL_VALUE, 5, TimeUnit.SECONDS);
        if (!lock) {
            throw new BadRequestException("请不要频繁登陆");
        }
        try {
            redisTemplate.opsForValue().setIfAbsent(refreshToken, userId, timeout, timeUnit);
        } finally {
            delete(lockKey.toString());
        }

    }

    //    获取redis锁
    @Override
    public Boolean getLock(String key, String value, int timeout, TimeUnit timeUnit) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeUnit);
    }

    //删除redis键值对
    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public Long getUserIdByRefreshToken(String key) {

        Long id = (Long) redisTemplate.opsForValue().get(key);

        if (null == id || id.equals(0L)) {
            return 0L;
        }
        return id;
    }
}
