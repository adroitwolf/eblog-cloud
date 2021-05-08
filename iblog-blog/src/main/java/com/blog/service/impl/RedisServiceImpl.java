package com.blog.service.impl;

import com.blog.entity.model.PopularBlog;
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

    @Autowired
    RedisTemplate redisTemplate;
    @Override
    public Set<PopularBlog> listTop5FrmRedis() {
        return redisTemplate.opsForZSet().reverseRange(LIST_TOP5_POSTS, 0, 5);
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
