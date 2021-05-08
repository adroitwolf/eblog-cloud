package com.blog.service;

import com.blog.entity.model.PopularBlog;
import com.common.entity.pojo.BlogStatus;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <pre>RedisService</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
public interface RedisService {
    /**
     * 功能描述: 在数据库中获取到今日的精选文章
     *
     * @Param: []
     * @Return: java.util.Set<run.app.entity.DTO.PopularBlog>
     * @Author: WHOAMI
     * @Date: 2020/1/30 19:52
     */
    Set<PopularBlog> listTop5FrmRedis();

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
     * 功能描述: 列出缓存中所有博客的点击量 用于更新到数据库中
     *
     * @Param: []
     * @Return: java.util.List<run.app.entity.model.BlogStatus>
     * @Author: WHOAMI
     * @Date: 2020/1/30 19:48
     */
    List<BlogStatus> listBlogClickedCounts();

    /**
     * 功能描述: 获取到redis锁
     *
     * @Param: [key, value, timeout, timeUnit]
     * @Return: java.lang.Boolean
     * @Author: WHOAMI
     * @Date: 2020/1/30 19:45
     */
    Boolean getLock(String key, String value, int timeout, TimeUnit timeUnit);
}
