package com.blog.dao;

import com.common.config.BaseMapper;
import com.common.entity.vo.QueryParams;
import com.common.entity.pojo.Blog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <pre>BlogDao</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-06
 */
public interface BlogDao extends BaseMapper<Blog> {
    List<Blog> selectByUserExample(@Param("query") QueryParams postQueryParams, @Param("blogger_id") Long bloggerId);

    void deletePicByPicId(Long picId);
}
