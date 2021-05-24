package com.blog.service.impl;

import com.api.feign.service.AttachmentFeignService;
import com.api.feign.service.RoleFeignService;
import com.api.feign.service.TokenFeignService;
import com.api.feign.service.UserFeignService;
import com.blog.dao.BlogContentDao;
import com.blog.dao.BlogDao;
import com.blog.dao.BlogLabelDao;
import com.blog.dao.BlogTagMapDao;
import com.blog.entity.model.BaseBlog;
import com.blog.entity.vo.ArticleParams;
import com.blog.entity.vo.BlogDetail;
import com.blog.entity.vo.BlogDetailWithAuthor;
import com.common.entity.vo.QueryParams;
import com.blog.service.ArticleService;
import com.blog.service.TagService;
import com.common.entity.dto.UserDto;
import com.common.entity.pojo.Blog;
import com.common.entity.pojo.BlogContent;
import com.common.entity.vo.BaseResponse;
import com.common.entity.vo.DataGrid;
import com.common.enums.ArticleStatusEnum;
import com.common.enums.CiteNumEnum;
import com.common.enums.RoleEnum;
import com.common.exception.BadRequestException;
import com.common.exception.NotFoundException;
import com.common.exception.UnAccessException;
import com.common.utils.CommonUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <pre>ArticleServiceImpl</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-08
 */
@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {
    /**
     * 功能描述: 新增两个标签服务的mapper层
     *
     * @Author: WHOAMI
     * @Date: 2019/8/23 17:37
     */
    @Autowired
    BlogTagMapDao blogTagMapDao;

    @Autowired
    BlogLabelDao blogLabelDao;
    /*代码修改结束*/

    /*
     * 功能描述: 添加博客图片功能
     * @Author: WHOAMI
     * @Date: 2019/9/3 18:05
     */

    @Autowired
    AttachmentFeignService attachmentFeignService;

    /*代码修改结束*/
    @Autowired
    BlogDao blogDao;

    //    @Autowired
//    UserService userService;
    @Autowired
    TokenFeignService tokenFeignService;

    @Autowired
    RoleFeignService roleFeignService;

    @Autowired
    TagService tagService;

    @Autowired
    BlogContentDao blogContentDao;

    @Autowired
    UserFeignService userFeignService;



    @Override
    @Transactional
    public @NonNull
    BaseResponse submitArticle(@NonNull ArticleParams articleParams, @NonNull String token) {

        Blog blog = new Blog();

        Long bloggerId;
//        if((bloggerId =userService.getUserIdByToken(token)) == -1){
        if ((bloggerId = tokenFeignService.getUserIdByToken(token)) == -1) {
            throw new BadRequestException("用户信息错误！");
        }
        blog.setBloggerId(bloggerId);
        blog.setReleaseDate(new Date());
        blog.setNearestModifyDate(new Date());
        BeanUtils.copyProperties(articleParams, blog);

        /*生成文章id 10-9 - 2019 WHOAMI*/
        Long blogId = CommonUtils.nextId();
        blog.setId(blogId);

        /*增加代码结束*/

        /**
         * 功能描述: 增加文章缩略图
         * @Author: WHOAMI
         * @Date: 2019/9/4 20:17
         */

        if (null != articleParams.getPictureId()) {
            blog.setPictureId(articleParams.getPictureId());
//            相对应的应该让改图片引用人数+1
            attachmentFeignService.changePictureStatus(blog.getPictureId(), CiteNumEnum.ADD);
        }

        /*增加代码结束*/

//        blog.setTagTitle(articleParams.getTag());
        /**
         * 功能描述: 这里增加文章逻辑，需要管理员审核通过后文章才会使用户可见，但是管理员的文章会直接发布
         * @Author: WHOAMI
         * @Date: 2020/1/10 20:29
         */
        List<RoleEnum> roles = roleFeignService.getRolesByUserId(bloggerId);
        String status = roles.contains(RoleEnum.ADMIN) ? ArticleStatusEnum.PUBLISHED.getName() : ArticleStatusEnum.CHECK.getName();

        blog.setStatus(status);

        /**
         * 功能描述: 增加文章标签功能
         * @Author: WHOAMI
         */

        String tag = tagService.submitArticleWithTagString(articleParams.getTagList(), blogId);

        blog.setTagTitle(tag);
        try {
            blogDao.insertSelective(blog);
        } catch (Exception e) {
            log.info(e.getMessage());
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                throw new BadRequestException("您已经发布过类似文章，请仔细查询");
            }
        }
        /*增加代码结束*/

        BlogContent blogContent = new BlogContent();

        BeanUtils.copyProperties(articleParams, blogContent);
        blogContent.setId(blog.getId());

        blogContentDao.insertSelective(blogContent);


        return new BaseResponse(HttpStatus.OK.value(), "上传成功", null);
    }

    @Override
    @Transactional
    public BaseResponse updateArticle(@NonNull ArticleParams articleParams, @NonNull Long blogId, @NonNull String token) {

        Blog blog1 = blogDao.selectByPrimaryKey(blogId);

        if(!tokenFeignService.authentication(blog1.getBloggerId(), token)){
            throw new UnAccessException("不能执行此操作");
        }


        /**
         * 功能描述: 增加文章标签功能
         * @Author: WHOAMI
         */
        String nowTagsString = tagService.updateTagsWithId(blogId, articleParams.getTagList());

        /*增加代码结束*/

        /**
         * 功能描述: 增加文章缩略图
         * @Author: WHOAMI
         * @Date: 2019/9/4 20:17
         */

        Long picture_id = -1L;

        if (null != articleParams.getPictureId()) {
//            picture_id = attachmentService.getIdByTitle(articleParams.getPictureId());
            picture_id = articleParams.getPictureId();
            /**
             * 功能描述: 相应当前所引用的博客图片人数进行更新
             * @Author: WHOAMI
             * @Date: 2019/11/10 13:22
             */
            if (null == blog1.getPictureId()) {
                attachmentFeignService.changePictureStatus(picture_id, CiteNumEnum.ADD);
            } else if (!picture_id.equals( blog1.getPictureId())) {
                attachmentFeignService.changePictureStatus(picture_id, CiteNumEnum.ADD);
                attachmentFeignService.changePictureStatus(blog1.getPictureId(), CiteNumEnum.REDUCE);
            }


        }

        /*增加代码结束*/

        Blog blog = new Blog();
        blog.setId(blogId);
        blog.setNearestModifyDate(new Date());
        BeanUtils.copyProperties(articleParams, blog);
        String status = tokenFeignService.getRoles(token).contains(RoleEnum.ADMIN) ? ArticleStatusEnum.PUBLISHED.getName() : ArticleStatusEnum.CHECK.getName();
        blog.setStatus(status);
        //todo tag问题
        blog.setTagTitle(nowTagsString);
//        blog.setTagTitle(articleParams.getTag());

        if (picture_id != -1) {
            blog.setPictureId(picture_id);
        }
        try {
            blogDao.updateByPrimaryKeySelective(blog);

        } catch (Exception e) {
            log.info(e.getMessage());
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                throw new BadRequestException("您已经发布过类似文章，请仔细查询");
            }
        }

        BlogContent blogContent = new BlogContent();

        blogContent.setId(blogId);

        BeanUtils.copyProperties(articleParams, blogContent);

        blogContentDao.updateByPrimaryKey(blogContent);

        return new BaseResponse(HttpStatus.OK.value(), "文章更新成功", null);
    }

    @Override
    public BaseResponse updateArticleStatus(@NonNull Long blogId, @NonNull String status, String token) {


        Blog blog1 = blogDao.selectByPrimaryKey(blogId); //这里应该会有空指针异常 应该制止

        if (ArticleStatusEnum.NO.getName().equals(blog1.getStatus())) { //审核失败的文章只允许删除操作
            throw new UnAccessException("请不要尝试非法操作");
        }

        if(!tokenFeignService.authentication(blog1.getBloggerId(), token)){
            throw new UnAccessException("不能执行此操作");
        }
        Blog blog = new Blog();
        //检测是否有非法字符注入
//        String articleStatus =ArticleStatusEnum.valueOf(status).equals(ArticleStatusEnum.PUBLISHED) ?
//                (tokenService.getRoles(token).contains(RoleEnum.ADMIN)?ArticleStatusEnum.PUBLISHED.getName():ArticleStatusEnum.CHECK.getName()):ArticleStatusEnum.RECYCLE.getName();
        StringBuilder articleStatus = new StringBuilder();
        if (tokenFeignService.getRoles(token).contains(RoleEnum.ADMIN)) {
            articleStatus.append(ArticleStatusEnum.valueOf(status));
        }

        blog.setStatus(articleStatus.toString());
        blog.setId(blogId);

        blogDao.updateByPrimaryKeySelective(blog);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.OK.value());
        Map<String, String> updateStatus = new HashMap<>();
        updateStatus.put("status", articleStatus.toString());
        baseResponse.setData(updateStatus);
        return baseResponse;
    }

    @Override
    public BaseResponse updateArticleStatusByAdmin(@NonNull Long blogId, @NonNull String status, String token) {
//        if(ArticleStatusEnum.PUBLISHED.getName().equals(status)){ //说明审核通过
//
//        }else if(ArticleStatusEnum.NO.getName().equals(status)){
//
//        }
        //这里有一个问题就是审核失败的文章怎么办？ 删还是不删
        // 2020-1-30补充
        return updateArticleStatus(blogId, status, token);
    }

    @Override
    public BaseResponse getArticleDetail(@NonNull Long blogId, String token) {

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.OK.value());

        Blog blog = blogDao.selectByPrimaryKey(blogId);
        if (Objects.isNull(blog)) { //查询没有相关博客
            throw new NotFoundException("没有相关博客信息");
        }
        if(!tokenFeignService.authentication(blog.getBloggerId(), token)){
            throw new UnAccessException("不能执行此操作");
        }

        BlogContent blogContent = blogContentDao.selectByPrimaryKey(blogId);

        BlogDetail blogDetail = new BlogDetail();

        BeanUtils.copyProperties(blog, blogDetail);

        BeanUtils.copyProperties(blogContent, blogDetail);

//        todo tag标签问题

        if (!StringUtils.isBlank(blog.getTagTitle())) {
            blogDetail.setTagsTitle(tagService.selectTagTitleByIdString(blog.getTagTitle()));
        }

//        博客缩略图问题
        if (null != blog.getPictureId()) {
            blogDetail.setPicture(attachmentFeignService.getPathById(blog.getPictureId()));
        }

        baseResponse.setData(blogDetail);
        baseResponse.setStatus(HttpStatus.OK.value());
        return baseResponse;


    }


    @Override
    public BaseResponse getArticleListByExample(com.common.entity.vo.PageInfo pageInfo, QueryParams postQueryParams, @NonNull String token) {

        if (!StringUtils.isEmpty(postQueryParams.getStatus())) {

            ArticleStatusEnum.valueOf(postQueryParams.getStatus());
        }
        log.info("查询目标" + postQueryParams.toString());

        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
        List<Blog> blogList = blogDao.selectByUserExample(postQueryParams, tokenFeignService.getUserIdByToken(token));

        PageInfo<Blog> list = new PageInfo<>(blogList);

        DataGrid dataGrid = new DataGrid();

        List<com.blog.entity.model.Blog> blogs = list.getList().stream().map(item -> {


            List<String> tagsTitle = new ArrayList<>();
            if (!StringUtils.isBlank(item.getTagTitle())) {

                tagsTitle = tagService.selectTagTitleByIdString(item.getTagTitle());
            }
            String pic = "";
//            获取博客图片逻辑路径
            if (null != item.getPictureId()) {
                pic = attachmentFeignService.getPathById(item.getPictureId());
            }
            com.blog.entity.model.Blog blog = new com.blog.entity.model.Blog();
            BeanUtils.copyProperties(item, blog);
            blog.setPicture(pic);
            blog.setTagsTitle(tagsTitle);
            return blog;
        }).collect(Collectors.toList());


        dataGrid.setRows(blogs);

        dataGrid.setTotal(list.getTotal());

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.OK.value());
        baseResponse.setData(dataGrid);


        return baseResponse;
    }

    /**
     * 功能描述: 面向管理员的文章查询功能，其中默认是查询审核中的
     * @Return: run.app.entity.DTO.BaseResponse
     * @Author: WHOAMI
     * @Date: 2020/1/11 10:58
     */
    @Override
    public BaseResponse getArticleListToAdminByExample(com.common.entity.vo.PageInfo pageInfo, QueryParams postQueryParams, @NonNull String token) {
        if (!StringUtils.isEmpty(postQueryParams.getStatus())) {

            ArticleStatusEnum.valueOf(postQueryParams.getStatus());

        } else {
            postQueryParams.setStatus(ArticleStatusEnum.CHECK.getName());
        }

        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());

        List<Blog> blogList = blogDao.selectByUserExample(postQueryParams, null);

        PageInfo<Blog> list = new PageInfo<>(blogList);

        DataGrid dataGrid = new DataGrid();

        List<BlogDetailWithAuthor> blogs = list.getList().stream().map(item -> {


            List<String> tagsTitle = new ArrayList<>();

            if (!StringUtils.isBlank(item.getTagTitle())) {

                tagsTitle = tagService.selectTagTitleByIdString(item.getTagTitle());
            }

            String pic = "";
//            获取博客图片逻辑路径
            if (null != item.getPictureId()) {
                pic = attachmentFeignService.getPathById(item.getPictureId());
            }

            BlogDetailWithAuthor blog = new BlogDetailWithAuthor();

            BeanUtils.copyProperties(item, blog);

            blog.setPicture(pic);

            blog.setTagsTitle(tagsTitle);

//            获取博客作者相关信息
            UserDto author = userFeignService.getUserDTOById(item.getBloggerId());

            blog.setAuthor(author);

            return blog;
        }).collect(Collectors.toList());


        dataGrid.setRows(blogs);

        dataGrid.setTotal(list.getTotal());

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.OK.value());
        baseResponse.setData(dataGrid);


        return baseResponse;
    }

    @Override
    public String getArticleNameByBlogId(Long blogId) {
        Blog blog = blogDao.selectByPrimaryKey(blogId);
        if(Objects.isNull(blog)){
            log.info("博客id为:{}未找到",blogId);
            throw  new NotFoundException("博客未找到");
        }
        return blog.getTitle();
    }


    @Override
    @Transactional
    public BaseResponse deleteBlog(@NonNull Long blogId, String token) {

        Blog blog1 = blogDao.selectByPrimaryKey(blogId);

        if(!tokenFeignService.authentication(blog1.getBloggerId(), token)){
            throw new UnAccessException("你没有权限操作");
        }

        /**
         * 功能描述:这里应该先查询此文章的所有标签，然后删除，顺序不可以变
         * @Author: WHOAMI
         * @Date: 2019/8/28 7:35
         */

//        todo:这里应该也减少tag标签
        Blog blog = blogDao.selectByPrimaryKey(blogId);

        if (!StringUtils.isBlank(blog.getTagTitle())) {
            tagService.deleteTagsKeyByBlogId(blogId);
            tagService.dealWithNumByIdString(blog.getTagTitle(), false);
        }
        blogDao.deleteByPrimaryKey(blogId);
        blogContentDao.deleteByPrimaryKey(blogId);

        return new BaseResponse(HttpStatus.OK.value(), "删除成功", null);
    }

    @Override
    public BaseResponse getArticleCount(@NonNull String token) {
//        Integer bloggerId = userService.getUserIdByToken(token);
        BaseResponse baseResponse = new BaseResponse();

        Long bloggerId = tokenFeignService.getUserIdByToken(token);

        Example example = Example.builder(Blog.class).andWhere(WeekendSqls.<Blog>custom().andEqualTo(Blog::getBloggerId, bloggerId)).build();
        // 自动装箱
        long count = blogDao.selectCountByExample(example);
        baseResponse.setStatus(HttpStatus.OK.value());
        baseResponse.setData(count);
        return baseResponse;

    }

    @Override
    @Transactional
    public BaseResponse deletePostsByUserId(Long userId) { //逻辑太多 需要放到一个单独的服务里面运行

        return new BaseResponse();

    }

    @Override
    public void deleteQuotePic(Long picId) {
        blogDao.deletePicByPicId(picId);
    }

    @Override
    public Blog getBlogByBlogId(Long id) {
        return blogDao.selectByPrimaryKey(id);
    }

    @Override
    public BaseBlog getBaseBlogById(Long id) {
        Blog blog = blogDao.selectByPrimaryKey(id);
        BaseBlog baseBlog = new BaseBlog();

        BeanUtils.copyProperties(blog, baseBlog);

        if (null != blog.getPictureId()) {

            baseBlog.setPicture(attachmentFeignService.getPathById(blog.getPictureId()));
        }

        return baseBlog;
    }
}
