package com.blog.service.impl;

import com.blog.dao.BlogStatusDao;
import com.common.entity.model.PopularBlog;
import com.blog.service.ArticleService;
import com.blog.service.BlogStatusService;
import com.blog.service.RedisService;
import com.common.entity.pojo.BlogStatus;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <pre>BlogStatusServiceImpl</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@Service
@Slf4j
public class BlogStatusServiceImpl implements BlogStatusService {
    @Autowired
    RedisService redisService;


    @Autowired
    ArticleService articleService;

    @Autowired
    BlogStatusDao blogStatusDao;

    private static final String LOCAL_TRANS_CLICK_UPDATE_KEY = "LOCAL_TRANS_CLICK_UPDATE_KEY";

    private static final String LOCAL_TRANS_CLICK_UPDATE_VALUE = "LOCAL_TRANS_CLICK_UPDATE_KEY";


    @Override
    public List<PopularBlog> listTop5Posts() {

        Example example = Example.builder(BlogStatus.class).orderByDesc("clickcount").build();
        PageHelper.startPage(0, 5);
        return blogStatusDao.selectByExample(example).stream().filter(Objects::nonNull).map(status -> {
            PopularBlog popularBlog = new PopularBlog();
            BeanUtils.copyProperties(status, popularBlog);
            popularBlog.setBlogName(articleService.getArticleNameByBlogId(popularBlog.getId()));
            return popularBlog;
        }).collect(Collectors.toList());
    }
}
