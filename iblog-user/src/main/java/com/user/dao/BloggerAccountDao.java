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

    /**
     * 这里获得所有普通用户
     * @param postQueryParams
     * @return
     */
    List<UserInfo> selectByQueryParams(@Param("query") QueryParams postQueryParams);

    /**
     * 获取所有可以管理的用户，不包括超级管理员，这里不获得该用户所获得的角色
     * @param postQueryParams
     * @return
     */
    List<UserInfo> selectAllUsersByQueryParams(@Param("query") QueryParams postQueryParams);
}
