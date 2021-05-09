package com.blog.service.impl;

import com.blog.dao.BlogLabelDao;
import com.blog.dao.BlogLabelMapDao;
import com.blog.service.TagService;
import com.common.entity.pojo.BlogLabel;
import com.common.entity.pojo.BlogTagMapKey;
import com.common.entity.pojo.BloggerRoleMapKey;
import com.common.utils.CommonUtils;
import com.common.entity.vo.PageInfo;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <pre>TagServiceImpl</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-06
 */
@Service
@Slf4j
public class TagServiceImpl implements TagService {
    @Autowired
    BlogLabelDao blogLabelDao;

    @Autowired
    BlogLabelMapDao blogLabelMapDao;


    @Override
    public String submitArticleWithTagString(List<String> tags, Long blogId) {


        // 去重
        tags = tags.stream().distinct().collect(Collectors.toList());

        List<Long> nowTags = new ArrayList<>();

        List<BlogTagMapKey> maps = new ArrayList<>();

        tags.stream().filter(Objects::nonNull).forEach(item -> {
            Example example = Example.builder(BlogLabel.class).andWhere(WeekendSqls.<BlogLabel>custom().andEqualTo(BlogLabel::getTitle, item)).build();

            BlogLabel blogLabel = blogLabelDao.selectOneByExample(example);
            // 如果存在，则更新操作
            if (Objects.nonNull(blogLabel)) {
                nowTags.add(blogLabel.getId());
                blogLabel.setCiteNum(blogLabel.getCiteNum() + 1);
                blogLabelDao.updateByPrimaryKey(blogLabel);
                maps.add(BlogTagMapKey.builder().id(CommonUtils.nextId()).tagId(blogLabel.getId()).blogId(blogId).build());

            } else {
                Long id = CommonUtils.nextId();
                blogLabelDao.insertSelective(BlogLabel.builder().citeNum(1).createDate(new Date()).id(id).build());
                maps.add(BlogTagMapKey.builder().id(CommonUtils.nextId()).tagId(blogLabel.getId()).blogId(blogId).build());
                nowTags.add(id);
            }
        });
        // 批量更新关联表
        blogLabelMapDao.insertList(maps);

        // 更新关联表
        return StringUtils.join(nowTags, ",");

    }

    @Override
    @Transactional
    public String updateTagsWithId(Long blogId, List<String> tagsParams) {
        /**
         * 功能描述: 增加更新文章代码
         * 内容描述：主要是在标签生成方面 本次修改伴随着数据库底层代码修改
         * @Author: WHOAMI
         * @Date: 2019/8/23 17:35
         */

//        查询更新之前到相应的标签id 并且每一个都要在tag标签使用人数中自减1 并且删除掉tag_map标签中的 集合

        Example example = Example.builder(BlogTagMapKey.class).andWhere(WeekendSqls.<BlogTagMapKey>custom().andEqualTo(BlogTagMapKey::getBlogId, blogId)).build();

        List<Long> updateTags = blogLabelMapDao.selectByExample(example).stream().map(BlogTagMapKey::getTagId).collect(Collectors.toList());

        updateTags.stream().filter(Objects::nonNull)
                .forEach(tags -> {
                    blogLabelDao.updateByPrimaryKeyForReduceNum(tags);

                    Example delExample = Example.builder(BlogTagMapKey.class).andWhere(WeekendSqls.<BlogTagMapKey>custom().andEqualTo(BlogTagMapKey::getBlogId, blogId).andEqualTo(BlogTagMapKey::getTagId, tags)).build();

                    blogLabelMapDao.deleteByExample(delExample);
                });

//        查询到现在的标签id没有的话新增

        List<Long> nowTags = new ArrayList<>();


        tagsParams.stream().filter(Objects::nonNull)
                .forEach(value -> {
                    log.info("需要更新的标签名称:{}", value);


                    Example.Builder blogLabelExample = Example.builder(BlogLabel.class).andWhere(WeekendSqls.<BlogLabel>custom().andEqualTo(BlogLabel::getTitle, value));


                    BlogLabel label = blogLabelDao.selectOneByExample(blogLabelExample);

                    if (Objects.isNull(label)) {
//                        需要新建标签
                        Long id = CommonUtils.nextId();
                        BlogLabel blogLabel = BlogLabel.builder().citeNum(1).title(value).createDate(new Date()).id(id).build();
                        blogLabelDao.insertSelective(blogLabel);

                        blogLabelMapDao.insertSelective(new BlogTagMapKey(CommonUtils.nextId(),id, blogId));

                        nowTags.add(id);

                    } else {
                        log.info("id:" + label);
                        blogLabelDao.updateByPrimaryKeyForAddNum(label.getId());
                        blogLabelMapDao.insert(new BlogTagMapKey(CommonUtils.nextId(),label.getId(), blogId));
                        nowTags.add(label.getId());
                    }

                });


        return StringUtils.join(nowTags, ",");


    }

    @Override
    public List<String> selectTagTitleByIdString(String ids) {
        List<String> list = Arrays.asList(ids.split(","));


        List<String> tags = new ArrayList<>();

        list.stream().filter(Objects::nonNull)
                .forEach(x -> tags.add(blogLabelDao.selectByPrimaryKey(tags).getTitle()));


        return tags;
    }

    @Override
    public void dealWithNumByIdString(String ids, boolean status) {
        Arrays.stream(ids.split(",")).filter(Objects::nonNull)
                .forEach(x -> {
                    if (status) {
                        blogLabelDao.updateByPrimaryKeyForAddNum(Long.valueOf(x));
                    } else {
                        blogLabelDao.updateByPrimaryKeyForReduceNum(Long.valueOf(x));
                    }
                });
    }

    @Override
    public void deleteTagsKeyByBlogId(Long blogId) {
        Example example = Example.builder(BlogTagMapKey.class).andWhere(WeekendSqls.<BlogTagMapKey>custom().andEqualTo(BlogTagMapKey::getBlogId, blogId)).build();
        blogLabelMapDao.deleteByExample(example);
    }

    @Override
    public Long selectIdWithName(String tag) {

        Example example = Example.builder(BlogLabel.class).andWhere(WeekendSqls.<BlogLabel>custom().andEqualTo(BlogLabel::getTitle, tag)).build();
        return blogLabelDao.selectOneByExample(example).getId();

    }

    @Override
    public List<Long> selectBlogIdByTagId(PageInfo pageInfo, Long id) {

        Example example = Example.builder(BlogTagMapKey.class).andWhere(WeekendSqls.<BlogTagMapKey>custom().andEqualTo(BlogTagMapKey::getTagId, id)).build();
        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());


        return blogLabelMapDao.selectByExample(example).stream().map(BlogTagMapKey::getBlogId).collect(Collectors.toList());


    }
}
