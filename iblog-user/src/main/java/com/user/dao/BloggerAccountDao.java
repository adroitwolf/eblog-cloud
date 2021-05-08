package com.user.dao;

import com.common.config.BaseMapper;
import com.common.entity.model.UserInfo;
import com.common.entity.pojo.BloggerAccount;
import com.common.entity.vo.QueryParams;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <pre>BloggerAccountDao</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
public interface BloggerAccountDao extends BaseMapper<BloggerAccount> {
    List<UserInfo> selectByQueryParams(@Param("query") QueryParams postQueryParams);
}
