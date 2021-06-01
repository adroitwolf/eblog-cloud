package com.api.feign.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * <pre>ArticleFeignService</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-06-01
 */
@FeignClient(name = "iblog-blog")
public interface ArticleFeignService {
    /**
     * 功能描述: 根据id获取到博客文章名称 -此功能目前用来缓存redis精选文章
     *
     * @Param: [blogId]
     * @Return: java.lang.String
     * @Author: WHOAMI
     * @Date: 2020/1/30 18:13
     */
    @GetMapping("/feign/article/blogName/{id}")
    String getArticleNameByBlogId(@PathVariable("id") Long blogId);
}
