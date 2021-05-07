package com.attach.service.impl;

import com.attach.dao.BloggerPictureDao;
import com.attach.entity.vo.AttachmentParams;
import com.attach.entity.vo.AttachmentQueryParams;
import com.attach.service.AttachmentService;
import com.common.entity.model.ImageFile;
import com.common.entity.model.Picture;
import com.common.entity.model.PictureInfo;
import com.common.enums.CiteNumEnum;
import com.common.exception.BadRequestException;
import com.common.exception.NotFoundException;
import com.common.exception.UnAccessException;
import com.common.entity.pojo.BloggerPicture;
import com.common.utils.CommonUtils;
import com.common.utils.UploadUtil;
import com.common.entity.vo.BaseResponse;
import com.common.entity.vo.DataGrid;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <pre>AttachmentServiceImpl</pre>
 *  附件服务层实现类
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@Service
@Slf4j
public class AttachmentServiceImpl implements AttachmentService {
    @Autowired
    BloggerPictureDao bloggerPictureDao;


    @Value("${fileServer}")
    String fileServer;

    @Value(value = "${web.upload-path}")
    private String imgPath;

    private static final Integer DEFAULT_NUM = 0;



    @Override
    public String selectPicById(Long id) {
        return bloggerPictureDao.selectByPrimaryKey(id).getFileKey();
    }


    @Override
    public BaseResponse getAttachmentList(com.common.entity.vo.PageInfo pageInfo, AttachmentQueryParams attachmentQueryParams, Long userId) {

        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());

        /**
         * 功能描述: 修改成模糊查询
         * @Author: WHOAMI
         * @Date: 2019/11/13 14:00
         */
        List<BloggerPicture> bloggerPictures = bloggerPictureDao.selectPictureByExample(attachmentQueryParams, userId);

        PageInfo<BloggerPicture> bloggerPicturePageInfo = new PageInfo<>(bloggerPictures);

        //todo:代码优化
        List<Picture> pictures = bloggerPictures.stream().filter(Objects::nonNull).map(item ->
            Picture.builder().id(item.getId()).path(covertAttachmentPath(item.getPath())).title(item.getTitle()).build()
        ).collect(Collectors.toList());

        DataGrid dataGrid = new DataGrid();

        dataGrid.setTotal(bloggerPicturePageInfo.getTotal());
        dataGrid.setRows(pictures);

        BaseResponse baseResponse = new BaseResponse();

        baseResponse.setStatus(HttpStatus.OK.value());
        baseResponse.setData(dataGrid);

        return baseResponse;
    }

    @Override
    @Transactional
    public BaseResponse uploadAttachment(MultipartFile file, Long userId) {
        uploadFile(file, userId, null);
        return new BaseResponse(HttpStatus.OK.value(), null, "上传附件成功");
    }

    @Override
    public Long uploadFile(MultipartFile file, Long userId, String title) {
        ImageFile imageFile = UploadUtil.uploadFile(file,imgPath).orElseThrow(() -> new BadRequestException("用户上传图片失败"));
        BloggerPicture bloggerPicture = new BloggerPicture();

        bloggerPicture.setId(CommonUtils.nextId());

        bloggerPicture.setBloggerId(userId);


        bloggerPicture.setUploadDate(new Date());

        bloggerPicture.setUpdateDate(new Date());

//        修复上传逻辑错误，开始上传的图片引用人数应该是0
        bloggerPicture.setCiteNum(DEFAULT_NUM);

        bloggerPicture.setMediaType(imageFile.getMediaType().getType());

        BeanUtils.copyProperties(imageFile, bloggerPicture);

//        判断用户是否自主赋值title
        if (!StringUtils.isBlank(title)) {
            bloggerPicture.setTitle(title);
        }

        bloggerPictureDao.insertSelective(bloggerPicture);

        return bloggerPicture.getId();
    }


    @Override
    public Long getIdByTitle(String fileKey) {
        Example example = Example.builder(BloggerPicture.class).andWhere(WeekendSqls.<BloggerPicture>custom()
                .andEqualTo(BloggerPicture::getFileKey, fileKey)).build();
        BloggerPicture bloggerPicture = bloggerPictureDao.selectOneByExample(example);
        if(Objects.isNull(bloggerPicture)){
            return null;
        }
        return bloggerPicture.getId();
    }

    @Override
    public String getTitleById(Long id) {
        BloggerPicture bloggerPicture = bloggerPictureDao.selectByPrimaryKey(id);
        if(Objects.isNull(bloggerPicture)){
            return "";
        }
        return bloggerPicture.getTitle();
    }

    @Override
    public String getPathById(Long id) {
        BloggerPicture bloggerPicture = bloggerPictureDao.selectByPrimaryKey(id);
        if(Objects.isNull(bloggerPicture)){
            return "";
        }
        return covertAttachmentPath(bloggerPicture.getPath());
    }


    @Override
    public String covertAttachmentPath(String path) {
        return fileServer +
                File.separator +
                path;
    }

    @Override
    @Transactional
    public BaseResponse updateInfo(Long id, AttachmentParams attachmentParams, Long userId) {

        BloggerPicture bloggerPicture = bloggerPictureDao.selectByPrimaryKey(id);

        if(Objects.isNull(bloggerPicture)){
            throw new NotFoundException("未找到附件信息");
        }else if(bloggerPicture.getBloggerId().equals(userId)){
            throw  new UnAccessException("您没有权限进行该操作");
        }


        BloggerPicture bloggerPicture1 = new BloggerPicture();

        bloggerPicture1.setTitle(attachmentParams.getTitle());
        bloggerPicture1.setId(id);
        bloggerPictureDao.updateByPrimaryKeySelective(bloggerPicture1);

        BaseResponse baseResponse = new BaseResponse();

        baseResponse.setStatus(HttpStatus.OK.value());

        return baseResponse;
    }

    @Override
    public BaseResponse getInfo(Long id, String token) {
        BloggerPicture bloggerPicture = bloggerPictureDao.selectByPrimaryKey(id);

        //需要处理空字符问题
//        tokenService.authentication(bloggerPicture.getBloggerId(), token);

        PictureInfo pictureInfo = new PictureInfo();

        BeanUtils.copyProperties(bloggerPicture, pictureInfo);

        return new BaseResponse(HttpStatus.OK.value(), null, pictureInfo);
    }

    @Override
    @Transactional
    public BaseResponse deleteAttachment(Long id, String token) {
        BloggerPicture bloggerPicture = bloggerPictureDao.selectByPrimaryKey(id);

        //需要处理空字符问题
//        tokenService.authentication(bloggerPicture.getBloggerId(), token);
        // todo 删除文章引用的文章
       // articleService.deleteQuotePic(id);

//          todo 这里多做了一次查询
        deletePic(bloggerPicture.getId());

        return new BaseResponse(HttpStatus.OK.value(), "图片删除成功", null);
    }

    @Override
    public void deletePic(Long id) {
        BloggerPicture bloggerPicture = bloggerPictureDao.selectByPrimaryKey(id);
        bloggerPictureDao.deleteByPrimaryKey(id);
//        带着缩略图一起删除
        UploadUtil.delFile(bloggerPicture.getPath(),imgPath);

        UploadUtil.delFile(bloggerPicture.getThumbPath(),imgPath);
    }

    @Override
    public BaseResponse findAllMediaType(Long userId) {

        return new BaseResponse(HttpStatus.OK.value(), null, bloggerPictureDao.findAllMediaType(userId));
    }

    @Override
    public void changePictureStatus(Long id, CiteNumEnum citeNumEnum) {
        if (Boolean.TRUE.equals(citeNumEnum.getValue())) {
            bloggerPictureDao.updatePictureByAddCiteNum(id);
        } else {
            bloggerPictureDao.updatePictureByReduceCiteNum(id);
        }
    }
}
