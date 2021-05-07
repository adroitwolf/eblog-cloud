package com.auth.service.impl;

import com.auth.dao.BloggerRoleDao;
import com.auth.dao.BloggerRoleMap;
import com.auth.service.RoleService;
import com.common.entity.pojo.BloggerRoleMapKey;
import com.common.enums.RoleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <pre>RoleServiceImpl</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {
    @Autowired
    BloggerRoleMap bloggerRoleMap;

    @Autowired
    BloggerRoleDao bloggerRoleDao;

    @Override
    public List<RoleEnum> getRolesByUserId(Long userId) {
        Example example = Example.builder(BloggerRoleMapKey.class).andWhere(WeekendSqls.<BloggerRoleMapKey>custom().andEqualTo(BloggerRoleMapKey::getUserId, userId)).build();
        return bloggerRoleMap.selectByExample(example).stream()
                .filter(Objects::nonNull)
                .map(n -> getRoleById(n.getRoleId()))
                .collect(Collectors.toList());
    }


    @Override
    public RoleEnum getRoleById(Long id) {
        return RoleEnum.valueOf(bloggerRoleDao.selectByPrimaryKey(id).getRoleName());
    }
}
