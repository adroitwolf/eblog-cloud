package com.blog.controller.portal;

import com.blog.entity.vo.CommentParams;
import com.blog.service.CommentService;
import com.common.entity.vo.BaseResponse;
import com.common.entity.vo.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * <pre>CommentController</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-09
 */
@RestController
@Slf4j
@RequestMapping("/api/comment")
public class CommentController {

    private static final String TOKEN = "Authentication";

    @Autowired
    CommentService commentService;

    @PostMapping("/comments")
//    @ApiOperation("发布评论")
    public BaseResponse comment(@RequestBody @Valid CommentParams commentParams, HttpServletRequest httpServletRequest) {
        log.info(commentParams.toString());
        return commentService.comment(commentParams, httpServletRequest.getHeader(TOKEN));
    }


    @GetMapping("/{id:\\d+}/comments")
//    @ApiOperation("获取评论")
    public BaseResponse getList(@PathVariable("id") Long id,
                                @RequestParam String type,
                                PageInfo pageInfo) {
        log.info(pageInfo.toString());
        return commentService.getList(id, type, pageInfo);

    }
}
