package com.blog.feign.controller;

import com.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>ArticleController</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-06-01
 */
@RequestMapping("/feign/article")
@RestController
public class ArticleController {

    @Autowired
    ArticleService articleService;

    /**
     * 功能描述: 根据id获取到博客文章名称 -此功能目前用来缓存redis精选文章
     *
     * @Param: [blogId]
     * @Return: java.lang.String
     * @Author: WHOAMI
     * @Date: 2020/1/30 18:13
     */
    @RequestMapping("/blogName/{id}")
    String getArticleNameByBlogId(@PathVariable("id") Long blogId){
        return articleService.getArticleNameByBlogId(blogId);
    }
}
