package com.blog.controller.manager;

import com.auth.aop.annotation.Role;
import com.blog.service.CommentService;
import com.common.entity.vo.BaseResponse;
import com.common.entity.vo.PageInfo;
import com.common.enums.RoleEnum;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <pre>CommentController</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-09
 */
@RestController
@Slf4j
@RequestMapping("/manage/comment")
public class CommentManageController {

    @Autowired
    CommentService commentService;

    private static final String TOKEN = "Authentication";

    @ApiOperation("管理评论列表")
    @GetMapping("/list")
    @Role(require = {RoleEnum.USER})
    public BaseResponse getList(PageInfo pageInfo, HttpServletRequest request) {
        return commentService.getListByToken(pageInfo, request.getHeader(TOKEN));
    }



    @DeleteMapping("{commentId:\\d+}/del")
    @ApiOperation("删除评论")
    @Role(require = {RoleEnum.USER})
    public BaseResponse deleteComment(@PathVariable("commentId") Long commentId, HttpServletRequest request) {
        return commentService.deleteComment(commentId, request.getHeader(TOKEN));
    }
}
