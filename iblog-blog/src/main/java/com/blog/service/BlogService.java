package com.blog.service;

import com.common.entity.vo.BaseResponse;
import com.common.entity.vo.PageInfo;

/**
 * <pre>BlogService</pre>
 * 针对前台的博客服务
 * @author <p>ADROITWOLF</p> 2021-05-06
 */
public interface BlogService {
    /**
     * 功能描述: 获取文章列表
     */
    BaseResponse getArticleList(PageInfo pageInfo);

    /**
     * 功能描述: 根据条件搜索文章
     */
    BaseResponse getArticleListByExample(PageInfo pageInfo, String keyword);

    /**
     * 功能描述: 获取文章详细内容
     */
    BaseResponse getArticleDetail(Long blogId);

    /**
     * 功能描述: 根据tag标签获取到文章列表
     */
    BaseResponse getArticleListByTag(PageInfo pageInfo, String tag);

    /**
     * 功能描述: 获取到每日精选文章
     */
    BaseResponse getTopPosts();
}
