package com.attach.dao;

import com.attach.entity.vo.AttachmentQueryParams;
import com.common.config.BaseMapper;
import com.common.entity.pojo.BloggerPicture;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <pre>BloggerPictureDao</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
public interface BloggerPictureDao extends BaseMapper<BloggerPicture> {
    List<BloggerPicture> selectPictureByExample(@Param("query") AttachmentQueryParams attachmentQueryParams, @Param("userId") Long blogger_id);

    @Select("select DISTINCT media_type from e_blogger_picture WHERE BLOGGER_ID = #{userId}")
    List<String> findAllMediaType(@Param("userId") Long userId);

    @Update("update e_blogger_picture set CITE_NUM = CITE_NUM + 1 where ID = #{Id}")
    void updatePictureByAddCiteNum(@Param("Id") Long id);

    @Update("update e_blogger_picture set CITE_NUM = CITE_NUM - 1 where ID = #{Id}")
    void updatePictureByReduceCiteNum(@Param("Id") Long id);
}
