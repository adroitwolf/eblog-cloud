package com.blog.service;

import com.common.entity.model.PopularBlog;

import java.util.List;

/**
 * <pre>BlogStatusService</pre>
 *  博客状态服务
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
public interface BlogStatusService {

    /**
     * 功能描述: 在数据库中获取到精选文章
     *
     * @Return: java.util.List<run.app.entity.DTO.PopularBlog>
     * @Author: WHOAMI
     * @Date: 2020/1/30 19:43
     */
    List<PopularBlog> listTop5Posts();
}
