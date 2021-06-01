package com.blog.controller.manager;

import com.auth.aop.annotation.Role;
import com.blog.entity.vo.ArticleParams;
import com.blog.service.ArticleService;
import com.blog.service.BlogService;
import com.common.entity.vo.BaseResponse;
import com.common.entity.vo.PageInfo;
import com.common.entity.vo.QueryParams;
import com.common.enums.RoleEnum;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * <pre>BlogController</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-09
 */
@RestController
@Slf4j
@RequestMapping("/manager/blog")
public class BlogManageController {
    @Autowired
    ArticleService articleService;

    @Autowired
    BlogService blogService;

    private static final String AUTHENICATION = "Authentication";

    @GetMapping("/query")
    @ApiOperation("管理员审核文章列表")
    @Role(require = {RoleEnum.ADMIN})
    public BaseResponse getListByExample(@Valid PageInfo pageInfo,
                                         QueryParams postQueryParams,
                                         HttpServletRequest request) {

        log.info(postQueryParams.toString());
        return articleService.getArticleListToAdminByExample(pageInfo, postQueryParams, request.getHeader(AUTHENICATION));
    }


    //    @MethodLog
    @ApiOperation("普通用户文章查询")
    @GetMapping("querys")
    @Role(require = {RoleEnum.USER})
    public BaseResponse getSelfListByExample(PageInfo pageInfo,
                                             QueryParams postQueryParams,
                                             HttpServletRequest request) {

        log.info(postQueryParams.toString());
        return articleService.getArticleListByExample(pageInfo, postQueryParams, request.getHeader(AUTHENICATION));
    }


    @PutMapping("/check/{blogId:\\d+}/result/{result}")
    @ApiOperation("文章审核状态")
    @Role(require = {RoleEnum.ADMIN})
    public BaseResponse checkUserArticle(@PathVariable("result") String result, @PathVariable("blogId") Long blogId, HttpServletRequest request) {

        return articleService.updateArticleStatusByAdmin(blogId, result, request.getHeader(AUTHENICATION));
    }


//    @MethodLog
    @ApiOperation("提交新的博客文档")
    @PostMapping("/submit")
    @Role(require = {RoleEnum.USER})
    public BaseResponse submitArticle(@Valid @RequestBody ArticleParams articleParams, HttpServletRequest request) {
        log.info(articleParams.toString());
        return articleService.submitArticle(articleParams, request.getHeader(AUTHENICATION));
    }

//    @MethodLog
    @Role(require = {RoleEnum.ADMIN,RoleEnum.USER})
    @ApiOperation("更新博客状态的操作")
    @PutMapping("/{BlogId:\\d+}/status/{status}")
    public BaseResponse updateArticleStatus(@PathVariable("BlogId") Long blogId,
                                            @PathVariable("status") String status, HttpServletRequest request) {
        return articleService.updateArticleStatus(blogId, status, request.getHeader(AUTHENICATION));
    }

    @Role(require = {RoleEnum.ADMIN})
    @ApiOperation("查看博客详细内容")
    @GetMapping("detail/{BlogId:\\d+}")
    public BaseResponse getBlogDetail(@PathVariable("BlogId") Long blogId, HttpServletRequest request) {
        return articleService.getArticleDetail(blogId, request.getHeader(AUTHENICATION));
    }


//    @MethodLog
    @Role(require = {RoleEnum.USER})
    @ApiOperation("更新博客文档")
    @PutMapping("{BlogId:\\d+}")
    public BaseResponse updateArticle(@PathVariable("BlogId") Long blogId, @Valid @RequestBody ArticleParams articleParams,
                                      HttpServletRequest request) {

        log.debug(articleParams.toString());

        return articleService.updateArticle(articleParams, blogId, request.getHeader(AUTHENICATION));
    }


//    @MethodLog
    @Role(require = {RoleEnum.ADMIN,RoleEnum.USER})
    @ApiOperation("删除博客")
    @DeleteMapping("{blogId:\\d+}")
    public BaseResponse deleteBlog(@PathVariable("blogId") Long blogId, HttpServletRequest request) {

        return articleService.deleteBlog(blogId, request.getHeader(AUTHENICATION));
    }

    @Role(require = {RoleEnum.USER})
    @ApiOperation("获取博客数量")
    @GetMapping("count")
    public BaseResponse countList(HttpServletRequest request) {
        return articleService.getArticleCount(request.getHeader(AUTHENICATION));
    }


}
