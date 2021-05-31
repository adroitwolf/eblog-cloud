package com.blog.controller.portal;

import com.blog.service.BlogService;
import com.common.entity.vo.BaseResponse;
import com.common.entity.vo.PageInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <pre>BlogController</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-09
 */
@RestController
@Slf4j
@RequestMapping("/api/blog")
public class BlogController {
    @Autowired
    BlogService blogService;

    private static final String TOKEN = "Authentication";

    @GetMapping("/detail/{blogId:\\d+}")
    @ApiOperation("博客详细信息")
    public BaseResponse getDetail(@PathVariable("blogId") Long blogId) {
        return blogService.getArticleDetail(blogId);
    }

    @GetMapping("/query")
    @ApiOperation("搜索文章")
    public BaseResponse getListByExample(@Valid PageInfo pageInfo,
                                         @RequestParam String keyword) {
        return blogService.getArticleListByExample(pageInfo, keyword);
    }

    @GetMapping("/list")
    @ApiOperation("获取当前所有文章，并且默认按照创建时间排序")
    public BaseResponse getList(PageInfo pageInfo) {
        return blogService.getArticleList(pageInfo);
    }


    @ApiOperation("获取点击量前五的文章")
    @GetMapping("/top")
    public BaseResponse getTopPosts() {
        return blogService.getTopPosts();
    }

}
