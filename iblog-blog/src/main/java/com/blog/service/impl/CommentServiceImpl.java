package com.blog.service.impl;

import com.api.feign.service.UserFeignService;
import com.auth.service.TokenService;
import com.blog.dao.CommentDao;
import com.blog.entity.model.MComment;
import com.blog.entity.vo.Comment;
import com.blog.entity.vo.CommentParams;
import com.blog.entity.vo.PComment;
import com.blog.service.ArticleService;
import com.blog.service.CommentService;
import com.common.entity.dto.UserDto;
import com.common.entity.pojo.Blog;
import com.common.entity.pojo.Comments;
import com.common.entity.vo.BaseResponse;
import com.common.entity.vo.DataGrid;
import com.common.entity.vo.PageInfo;
import com.common.enums.CommentTypeEnum;
import com.common.exception.BadRequestException;
import com.common.exception.NotFoundException;
import com.common.utils.CommonUtils;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <pre>CommentServiceImpl</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-09
 */
@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    @Autowired
    ArticleService articleService;

    @Autowired
    TokenService tokenService;

    @Autowired
    UserFeignService userFeignService;

    @Autowired
    CommentDao commentDao;


    @Override
    public BaseResponse comment(CommentParams commentParams, String token) {

        Comments comments = new Comments();

        BeanUtils.copyProperties(commentParams, comments);

        comments.setType(CommentTypeEnum.valueOf(commentParams.getType()).getValue());
        Long userId = tokenService.getUserIdByToken(token);

        // 这里来判断一下是否为作者
        // 这里应该确定文章是否存在

        Blog blog = articleService.getBlogByBlogId(commentParams.getObjectId());

        //这里应该确定父评论id是否存在
        //如果这是一个子评论
        if (!commentParams.getPid().equals(0L)) {
            Comments parent = commentDao.selectByPrimaryKey(commentParams.getPid());

            if (null == parent) {
                throw new NotFoundException("评论失败");
            } else {
                comments.setRoot(parent.getRoot().equals(0L) ? parent.getId() : parent.getRoot());
            }
        } else {
            comments.setRoot(0L);
        }

        if (null == blog) {
            throw new NotFoundException("该文章已飞到火星,评论失败");
        }

        Byte isAuthor = userId.equals(blog.getBloggerId()) ? (byte) 1 : (byte) 0;

        comments.setIsAuthor(isAuthor);

        Long commentId = CommonUtils.nextId();

        comments.setId(commentId);

        comments.setFromId(userId);

        comments.setAuthorId(blog.getBloggerId());

        comments.setCreateTime(new Date());

        comments.setUpdateTime(new Date());

        String content = wrapContent(commentParams.getToId(), commentParams.getContent());

        comments.setContent(content);

        //设定评论未被删除
        comments.setIsDel((byte) 0);

        commentDao.insertSelective(comments);

        return new BaseResponse(HttpStatus.OK.value(), "评论成功", null);
    }

    @Override
    public BaseResponse getList(Long id, String type, PageInfo pageInfo) {

        DataGrid dataGrid = CommentTypeEnum.BLOG_COMMENT.equals(CommentTypeEnum.valueOf(type)) ?
                getPCommentsList(id, pageInfo) : getChildCommentsList(id, pageInfo);
        BaseResponse baseResponse = new BaseResponse();

        baseResponse.setStatus(HttpStatus.OK.value());

        baseResponse.setData(dataGrid);

        return baseResponse;
    }

    @Override
    public BaseResponse getListByToken(PageInfo pageInfo, String token) {

        /**
         * 功能描述: 注意这里其实是可以看到那些删除评论相关的子评论的  具体结构是本体评论并且带上父评论 和哔哩哔哩的评论机制差不多
         * @Return: run.app.entity.DTO.BaseResponse
         * @Author: WHOAMI
         * @Date: 2020/2/17 12:16
         */
        BaseResponse baseResponse = new BaseResponse();

        Long userId = tokenService.getUserIdByToken(token);

        //排序规则
        Example example ;

        if(StringUtils.isEmpty(pageInfo.getSortName())){
            example = Example.builder(Comments.class).andWhere(WeekendSqls.<Comments>custom().andEqualTo(Comments::getAuthorId,userId).andEqualTo(Comments::getIsDel,(byte) 0)).build();
        }else{
            if("desc".equals(pageInfo.getSortOrder())){
                example = Example.builder(Comments.class).andWhere(WeekendSqls.<Comments>custom().andEqualTo(Comments::getAuthorId,userId).andEqualTo(Comments::getIsDel,(byte) 0)).orderByDesc(pageInfo.getSortName()).build();

            }else {
                example = Example.builder(Comments.class).andWhere(WeekendSqls.<Comments>custom().andEqualTo(Comments::getAuthorId,userId).andEqualTo(Comments::getIsDel,(byte) 0)).orderByAsc(pageInfo.getSortName()).build();
            }
        }





        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());


        List<Comments> commentsList = commentDao.selectByExample(example);


        com.github.pagehelper.PageInfo<Comments> list = new com.github.pagehelper.PageInfo<>(commentsList);

        //这里应该是一个子评论加一个父评论的效果

        List<MComment> collect = list.getList().stream().map(item -> {

            MComment mComment = new MComment();


            Comment comment = commentsConvertToDto(item);

            mComment.setSelf(comment);


            if (0L != comment.getPid()) {  //这时候的父评论不管是否删除过都会显示
                mComment.setParent(commentsConvertToDto(commentDao.selectByPrimaryKey(comment.getPid())));
            }

            mComment.setBlog(articleService.getBaseBlogById(item.getObjectId()));

            return mComment;

        }).collect(Collectors.toList());


        DataGrid dataGrid = new DataGrid();

        dataGrid.setTotal(list.getTotal());


        dataGrid.setRows(collect);


        baseResponse.setData(dataGrid);


        baseResponse.setStatus(HttpStatus.OK.value());
        return baseResponse;
    }

    @Override
    @Transactional
    public BaseResponse deleteComment(Long commentId, String token) {
        /**
         * 功能描述:这里的逻辑是 逻辑删除,物理删除需要通过专门的服务进行清理
         * @Author: WHOAMI
         * @Date: 2020/2/15 19:56
         */
        Comments comments = commentDao.selectByPrimaryKey(commentId);


        if (null == comments) {
            throw new BadRequestException("请不要进行注入操作");
        }

        comments.setIsDel((byte) 1);


        commentDao.updateByPrimaryKeySelective(comments);

        return new BaseResponse(HttpStatus.OK.value(), "删除评论成功", null);
    }


    private DataGrid getPCommentsList(Long id, PageInfo pageInfo) {

        DataGrid dataGrid = new DataGrid();


        Example example;
        if(StringUtils.isEmpty(pageInfo.getSortName())){
            example = Example.builder(Comments.class).andWhere(WeekendSqls.<Comments>custom()
                    .andEqualTo(Comments::getPid,0L).andEqualTo(Comments::getObjectId,id)
                    .andEqualTo(Comments::getIsDel,(byte) 0)).build();
        }else{
            if("desc".equals(pageInfo.getSortOrder())){
                example = Example.builder(Comments.class).andWhere(WeekendSqls.<Comments>custom()
                        .andEqualTo(Comments::getPid,0L).andEqualTo(Comments::getObjectId,id)
                        .andEqualTo(Comments::getIsDel,(byte) 0)).orderByDesc(pageInfo.getSortName()).build();
            }else {
                example = Example.builder(Comments.class).andWhere(WeekendSqls.<Comments>custom()
                        .andEqualTo(Comments::getPid,0L).andEqualTo(Comments::getObjectId,id)
                        .andEqualTo(Comments::getIsDel,(byte) 0)).orderByAsc(pageInfo.getSortName()).build();
            }
        }


        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());

        List<Comments> comments = commentDao.selectByExample(example);

        com.github.pagehelper.PageInfo<Comments> list = new com.github.pagehelper.PageInfo<>(comments);

        dataGrid.setTotal(list.getTotal());

        List<PComment> data = list.getList().stream().map(parent -> {
            PComment comment = new PComment();

            BeanUtils.copyProperties(parent, comment);

            //获取到评论人相关信息
            UserDto user = userFeignService.getUserDTOById(parent.getFromId());

            comment.setUser(user);

            //这里只会获取前三条子评论


            Example cExample;
            if(StringUtils.isEmpty(pageInfo.getSortName())){
                cExample = Example.builder(Comments.class).andWhere(WeekendSqls.<Comments>custom()
                        .andEqualTo(Comments::getRoot,parent.getId())).build();
            }else{
                if("desc".equals(pageInfo.getSortOrder())){
                    cExample = Example.builder(Comments.class).andWhere(WeekendSqls.<Comments>custom()
                            .andEqualTo(Comments::getRoot,parent.getId())).orderByDesc(pageInfo.getSortName()).build();
                }else {
                    cExample = Example.builder(Comments.class).andWhere(WeekendSqls.<Comments>custom()
                            .andEqualTo(Comments::getRoot,parent.getId())).orderByAsc(pageInfo.getSortName()).build();
                }
            }


            PageHelper.startPage(1, 3);

            List<Comments> commentsList = commentDao.selectByExample(cExample);

            com.github.pagehelper.PageInfo<Comments> commentsPageInfo = new com.github.pagehelper.PageInfo<>(commentsList);

            List<Comment> childs = commentsPageInfo.getList().stream().map(this::commentsConvertToDto).collect(Collectors.toList());

            comment.setChildren(childs);
            comment.setChildrenCount(commentsPageInfo.getTotal());

            return comment;

        }).collect(Collectors.toList());

        dataGrid.setRows(data);

        return dataGrid;

    }

    private DataGrid getChildCommentsList(Long id, PageInfo pageInfo) { //获取子评论，这里的id是父评论id

        DataGrid dataGrid = new DataGrid();


        Example example;
        if(StringUtils.isEmpty(pageInfo.getSortName())){
            example = Example.builder(Comments.class).andWhere(WeekendSqls.<Comments>custom()
                    .andEqualTo(Comments::getRoot,id).andEqualTo(Comments::getIsDel,(byte) 0)).build();
        }else{
            if("desc".equals(pageInfo.getSortOrder())){
                example = Example.builder(Comments.class).andWhere(WeekendSqls.<Comments>custom()
                        .andEqualTo(Comments::getRoot,id).andEqualTo(Comments::getIsDel,(byte) 0)).orderByDesc(pageInfo.getSortName()).build();
            }else {
                example = Example.builder(Comments.class).andWhere(WeekendSqls.<Comments>custom()
                        .andEqualTo(Comments::getRoot,id).andEqualTo(Comments::getIsDel,(byte) 0)).orderByAsc(pageInfo.getSortName()).build();
            }
        }


        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());

        List<Comments> commentsList = commentDao.selectByExample(example);


        com.github.pagehelper.PageInfo<Comments> commentsPageInfo = new com.github.pagehelper.PageInfo<>(commentsList);

        List<Comment> comments = commentsPageInfo.getList().stream().map(this::commentsConvertToDto).collect(Collectors.toList());


        dataGrid.setRows(comments);

        dataGrid.setTotal(commentsPageInfo.getTotal());
        return dataGrid;

    }


    private String wrapContent(Long id, String content) {


        StringBuilder builder = new StringBuilder();

        //这说明是一个父评论
        if (null == id || id.equals(0L)) {

            builder.append(content);

        } else {
            builder.append("回复");
            builder.append(userFeignService.getNicknameById(id));
            builder.append(": ");
            builder.append(content);
        }

        return builder.toString();
    }


    private Comment commentsConvertToDto(Comments comments) {

        Comment comment1 = new Comment();
        BeanUtils.copyProperties(comments, comment1);

        UserDto user1 = userFeignService.getUserDTOById(comments.getFromId());

        comment1.setUser(user1);
        return comment1;
    }

}
