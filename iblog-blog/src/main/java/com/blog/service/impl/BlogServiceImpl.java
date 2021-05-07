package com.blog.service.impl;

import com.blog.dao.BlogContentDao;
import com.blog.dao.BlogDao;
import com.blog.entity.vo.BlogDetailWithAuthor;
import com.blog.entity.vo.QueryParams;
import com.blog.feign.service.AttachmentService;
import com.blog.service.BlogService;
import com.common.entity.dto.UserDto;
import com.common.exception.NotFoundException;
import com.common.entity.pojo.Blog;
import com.common.entity.pojo.BlogContent;
import com.common.entity.vo.BaseResponse;
import com.common.entity.vo.DataGrid;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <pre>BlogServiceImpl</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-06
 */
@Service
@Slf4j
public class BlogServiceImpl implements BlogService {
    /**
     * 功能描述: 新增两个标签服务的mapper层
     *
     * @Author: WHOAMI
     * @Date: 2019/8/23 17:37
     */

    @Autowired
    TagServiceImpl tagService;
    /*代码修改结束*/


    @Autowired
    RedisService redisService;

    @Autowired
    BlogStatusService blogStatusService;

    /*
     * 功能描述: 新增博客图片功能
     * @Author: WHOAMI
     * @Date: 2019/9/3 18:08
     */
    @Autowired
    AttachmentService attachmentService;

    /*代码修改结束*/

    @Autowired
    BlogDao blogDao;

    @Autowired
    BlogContentDao blogContentDao;


    @Autowired
    BloggerProfileMapper bloggerProfileMapper;

    @Autowired
    UserService userService;

    @Override
    public BaseResponse getArticleList(com.common.entity.vo.PageInfo pageInfo) {


        Example example = Example.builder(Blog.class).andWhere(WeekendSqls.<Blog>custom().andEqualTo(Blog::getStatus, "PUBLISHED")).orderByDesc("release_date").build();

        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());

        List<Blog> blogs = blogDao.selectByExample(example);

        PageInfo<Blog> list = new PageInfo<>(blogs);

        List<com.blog.entity.model.Blog> resultX = transDtoFrmModel(list);
        DataGrid dataGrid = new DataGrid();

        dataGrid.setRows(resultX);
        dataGrid.setTotal(list.getTotal());

        return new BaseResponse(HttpStatus.OK.value(), "", dataGrid);
    }

    @Override
    public BaseResponse getArticleListByExample(com.common.entity.vo.PageInfo pageInfo, String keyword) {

        QueryParams postQueryParams = new QueryParams();

        postQueryParams.setKeyword(keyword);
        postQueryParams.setStatus("PUBLISHED");

        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());

        List<Blog> blogList = blogDao.selectByUserExample(postQueryParams, null);

        PageInfo<Blog> list = new PageInfo<>(blogList);

        DataGrid dataGrid = new DataGrid();

        List<com.blog.entity.model.Blog> blogs = transDtoFrmModel(list);

        dataGrid.setRows(blogs);

        dataGrid.setTotal(list.getTotal());

        return new BaseResponse(HttpStatus.OK.value(), "", dataGrid);
    }

    @Override
    public BaseResponse getArticleDetail(Long blogId) {

        BaseResponse baseResponse = new BaseResponse();


        Blog blog = blogDao.selectByPrimaryKey(blogId);

        //当博客id不存在时
        if (Objects.isNull(blog)) {
            throw new NotFoundException("没有相关博客信息");
        }

        BlogDetailWithAuthor blogDetailWithAuthor = new BlogDetailWithAuthor();

        BlogContent blogContent = blogContentDao.selectByPrimaryKey(blogId);

        BeanUtils.copyProperties(blog, blogDetailWithAuthor);

        BeanUtils.copyProperties(blogContent, blogDetailWithAuthor);

        //todo: tags
        List<String> nowTags = new ArrayList<>();
        if (!StringUtils.isBlank(blog.getTagTitle())) {

            nowTags = tagService.selectTagTitleByIdString(blog.getTagTitle());
        }

        blogDetailWithAuthor.setTagsTitle(nowTags);

        String pic = null != blog.getPictureId() ? attachmentService.getPathById(blog.getPictureId()) : null;

        blogDetailWithAuthor.setPicture(pic);

        UserDto author = userService.getUserDTOById(blog.getBloggerId());

        blogDetailWithAuthor.setAuthor(author);


//        BlogDetailWithAuthor blogDetailWithAuthor = new BlogDetailWithAuthor(blogId,blog.getTitle(),blog.getSummary(),blog.getReleaseDate(),nowTags,blogContent.getContent(),pic,bloggerProfile.getNickname(),avatarPath.toString());

        baseResponse.setStatus(HttpStatus.OK.value());
        baseResponse.setData(blogDetailWithAuthor);

        return baseResponse;
    }

    @Override
    public BaseResponse getArticleListByTag(run.app.entity.VO.PageInfo pageInfo, String tag) {

        Long id = tagService.selectIdWithName(tag);


        DataGrid dataGrid = new DataGrid();


        if (null != id) {
            List<Long> list = tagService.selectBlogIdByTagId(pageInfo, id);
            log.debug("tag集合" + list.size());
            PageInfo<Long> longPageInfo = new PageInfo<>(list);
//          采取分页的方式 10-9 -19
//            List<Long> list1 = list.subList((pageNum - 1) * pageSize, list.size()>pageNum * pageSize?pageNum*pageSize:list.size());
// 此方法逻辑上有错误
            List<run.app.entity.DTO.Blog> blogs = new ArrayList<>();

            list.stream().forEach(x -> {
                run.app.entity.DTO.Blog blogx = new run.app.entity.DTO.Blog();
                Blog blog = blogMapper.selectByPrimaryKey(x);
                BeanUtils.copyProperties(blog, blogx);

                if (!StringUtils.isBlank(blog.getTagTitle())) {
                    blogx.setTagsTitle(tagService.selectTagTitleByIdString(blog.getTagTitle()));
                }

                if (null != blog.getPictureId()) {
                    blogx.setPicture(attachmentService.covertAttachmentPath(attachmentService.selectPicById(blog.getPictureId())));
                }

                blogs.add(blogx);

            });


            dataGrid.setRows(blogs);
            dataGrid.setTotal(longPageInfo.getTotal());
            return new BaseResponse(HttpStatus.OK.value(), "", dataGrid);
        }

        dataGrid.setTotal(0);

        return new BaseResponse(HttpStatus.OK.value(), "", dataGrid);
    }

    @Override
    public BaseResponse getTopPosts() {
        Set<PopularBlog> popularPosts = redisService.listTop5FrmRedis();
        if (null == popularPosts || popularPosts.size() < 5) { //说明redis不准确,需要查询数据库
//            todo  这里有可能会出现线程击穿
            popularPosts = new HashSet<>(blogStatusService.listTop5Posts());

        }
        log.info(popularPosts.toString());
        return new BaseResponse(HttpStatus.OK.value(), null, popularPosts);
    }


    private List<com.blog.entity.model.Blog> transDtoFrmModel(PageInfo<Blog> list) {
        return list.getList().stream().map(item -> {
            List<String> tagsTitle = new ArrayList<>();
            if (!StringUtils.isBlank(item.getTagTitle())) {

                tagsTitle = tagService.selectTagTitleByIdString(item.getTagTitle());
            }

            String pic = "";

            // 获取图片url

            if (null != item.getPictureId()) {
                pic = attachmentService.getPathById(item.getPictureId());
            }

            run.app.entity.DTO.Blog blog = new run.app.entity.DTO.Blog();

            BeanUtils.copyProperties(item, blog);
            blog.setTagsTitle(tagsTitle);
            blog.setPicture(pic);
            return blog;
        }).collect(Collectors.toList());
    }
}
