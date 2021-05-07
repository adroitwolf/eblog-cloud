package com.blog.dao;

import com.common.config.BaseMapper;
import com.common.entity.pojo.BlogLabel;

/**
 * <pre>BlogLabelDao</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-06
 */
public interface BlogLabelDao extends BaseMapper<BlogLabel> {
    int updateByPrimaryKeyForReduceNum(Long id);

    int updateByPrimaryKeyForAddNum(Long id);

}
