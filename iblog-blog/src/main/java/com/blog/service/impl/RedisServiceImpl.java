package com.blog.service.impl;

import com.blog.entity.model.ClickStatus;
import com.blog.utils.RedisUtils;
import com.common.entity.model.PopularBlog;
import com.blog.service.RedisService;
import com.common.entity.pojo.BlogStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <pre>RedisServiceImpl</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@Service
@Slf4j
public class RedisServiceImpl implements RedisService {

    private static final String LIST_TOP5_POSTS = "TOP5_POSTS";

    private static final String BLOG_CLICKED_KEY = "BLOG_CLICKED";

    /**
     * 用户点击--redis锁
      */
    private final String LOCAL_BLOG_CLICK_PRE = "LOCAL_BLOG_CLICK_KEY";

    /**
     * redis通用锁value
     */
    private final String LOCAL_VALUE = "LOCAL_VALUE";

    @Autowired
    RedisTemplate redisTemplate;
    @Override
    public Set<PopularBlog> listTop5FrmRedis() {
        return redisTemplate.opsForZSet().reverseRange(LIST_TOP5_POSTS, 0, 5);
    }

    @Override
    public void incrementBlogClickedCount(ClickStatus clickStatus) {
        StringBuilder builder = new StringBuilder();
        builder.append(LOCAL_BLOG_CLICK_PRE);
        builder.append(clickStatus.getBlogId());
        Boolean lock = getLock(builder.toString(), LOCAL_VALUE, 2, TimeUnit.SECONDS);

        if (!lock) {
            //此时锁有人占用
            log.info("redis正在添加缓存...请稍等");
            return;
        }

        try {
            String key = RedisUtils.getClickSetKey(clickStatus);
            if (redisTemplate.opsForSet().isMember(key, clickStatus.getBlogId())) {
                return;
            }
            redisTemplate.opsForHash().increment(BLOG_CLICKED_KEY, clickStatus.getBlogId(), clickStatus.getCount());

            redisTemplate.opsForSet().add(key, clickStatus.getBlogId());
            //过期时间为1天
            redisTemplate.expire(key, 1, TimeUnit.DAYS);

        } finally {
            delete(builder.toString());
        }
    }

    /**
     * 删除redis键值对
     * @param key
     */
    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public List<BlogStatus> listBlogClickedCounts() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(BLOG_CLICKED_KEY, ScanOptions.NONE);

        List<BlogStatus> list = new ArrayList<>();

        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            String key = (String) entry.getKey();
            Long blogId = Long.valueOf(key);
            Integer count = (Integer) entry.getValue();

            BlogStatus status = new BlogStatus(blogId, count);

            list.add(status);
            redisTemplate.opsForHash().delete(BLOG_CLICKED_KEY, blogId);
        }
        return list;
    }

    /**
     * 获取redis锁
     */
    @Override
    public Boolean getLock(String key, String value, int timeout, TimeUnit timeUnit) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeUnit);
    }
}
