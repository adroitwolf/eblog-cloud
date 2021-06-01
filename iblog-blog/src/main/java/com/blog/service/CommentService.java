package com.blog.service;

import com.blog.entity.vo.CommentParams;
import com.common.entity.vo.BaseResponse;
import com.common.entity.vo.PageInfo;

import javax.validation.constraints.NotNull;

/**
 * <pre>CommentService</pre>
 *  评论服务
 * @author <p>ADROITWOLF</p> 2021-05-09
 */

public interface CommentService {

    /**
     * 功能描述: 用户发布评论
     *
     * @Author: WHOAMI
     * @Date: 2020/1/28 16:24
     */
    BaseResponse comment(CommentParams commentParams, String token);


    /**
     * 功能描述: 获取id对象下的所有评论
     *
     * @Param: [pageInfo, token]
     * @Return: run.app.entity.DTO.BaseResponse
     * @Author: WHOAMI
     * @Date: 2020/1/30 21:01
     */
    BaseResponse getList(Long id, String type, PageInfo pageInfo);


    /**
     * 功能描述: 获得自己所有的文章评论 用来评论管理
     *
     * @Return: run.app.entity.DTO.BaseResponse
     * @Author: WHOAMI
     * @Date: 2020/2/8 11:26
     */
    BaseResponse getListByToken(PageInfo pageInfo, @NotNull String token);


    /**
     * 功能描述: 根据评论删除id
     *
     * @Param: [commentId, token]
     * @Return: run.app.entity.DTO.BaseResponse
     * @Author: WHOAMI
     * @Date: 2020/2/15 12:52
     */
    BaseResponse deleteComment(Long commentId, String token);
}
