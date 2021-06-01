package com.attach.service.impl;

import com.attach.service.RedisService;
import com.common.entity.model.PopularBlog;
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
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <pre>RedisServiceImpl</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-31
 */
@Slf4j
@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    RedisTemplate redisTemplate;

    private static final String BLOG_CLICKED_KEY = "BLOG_CLICKED";

    /**
     * 更新top5文章--redis锁
     */
    private static final String LOCAL_TOP5_POST_UPDATE_KEY = "LOCAL_TOP5_POST_UPDATE_KEY";

    /**
     * redis通用锁value
     */
    private static final String LOCAL_VALUE = "LOCAL_VALUE";


    private static final String LIST_TOP5_POSTS = "TOP5_POSTS";

    /**
     * 获取redis锁
     */
    @Override
    public Boolean getLock(String key, String value, int timeout, TimeUnit timeUnit) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeUnit);
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
     * 删除redis键值对
     * @param key
     */
    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }


    @Override
    public void transTop5Posts2Redis(List<PopularBlog> list) {

        Boolean lock = getLock(LOCAL_TOP5_POST_UPDATE_KEY, LOCAL_VALUE, 2 * 60, TimeUnit.SECONDS);
        if (!lock) {
            //此时锁有人占用
            log.info("redis正在添加缓存...请稍等");
            return;
        }
        try {
            /**
             * 当文章少于5的时候，为了填满5个会出现一些重复序列
             * @Author: WHOAMI
             * @Date: 2019/12/13 14:10
             */
            //-先增，然后删除，避免在更新的时候有人访问-[不对]
            redisTemplate.delete(LIST_TOP5_POSTS);
            list.stream().filter(Objects::nonNull).forEach(entity -> {
//                PopularBlogRedis blogRedis = new PopularBlogRedis();
//                BeanUtils.copyProperties(entity,blogRedis);
                redisTemplate.opsForZSet().add(LIST_TOP5_POSTS, entity, entity.getClickcount());
            });


//            Long size = redisTemplate.opsForZSet().size(LIST_TOP5_POSTS);
//            if(size>5){
//                Long end = size - 6;
//                redisTemplate.opsForZSet().removeRange(LIST_TOP5_POSTS, 0, end);
//            }

        } finally {
            delete(LOCAL_TOP5_POST_UPDATE_KEY);
        }
    }

}
