package com.user.service.impl;

import com.common.entity.pojo.BloggerRole;
import com.common.entity.pojo.BloggerRoleMapKey;
import com.common.enums.RoleEnum;
import com.common.exception.ServiceException;
import com.common.utils.CommonUtils;
import com.user.dao.BloggerRoleDao;
import com.user.dao.BloggerRoleMapDao;
import com.user.service.RoleService;
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
 * @author <p>ADROITWOLF</p> 2021-05-08
 */
@Slf4j
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private BloggerRoleDao bloggerRoleDao;

    @Autowired
    private BloggerRoleMapDao bloggerRoleMapDao;


    @Override
    public void setRolesWithUserId(List<RoleEnum> roles, Long userId) {
        List<BloggerRoleMapKey> result = roles.stream().map(roleEnum -> {
            Long roleId = getRoleIdByType(roleEnum);
            return BloggerRoleMapKey.builder().id(CommonUtils.nextId()).userId(userId).roleId(roleId).build();
        }).collect(Collectors.toList());

        bloggerRoleMapDao.insertList(result);
    }


    @Override
    public void addRole(BloggerRole bloggerRole) {
        bloggerRoleDao.insert(bloggerRole);
    }

    @Override
    public Long getRoleIdByType(RoleEnum role) {
        Example example = Example.builder(BloggerRole.class).andWhere(WeekendSqls.<BloggerRole>custom().andEqualTo(BloggerRole::getRoleName, role.getAuthority())).build();
        BloggerRole bloggerRole = bloggerRoleDao.selectOneByExample(example);
        if(Objects.isNull(bloggerRole)){
            throw new ServiceException("服务异常");
        }
        return bloggerRole.getId();
    }

    @Override
    public List<RoleEnum> getRolesByUserId(Long userId) {

        Example example = Example.builder(BloggerRoleMapKey.class).andWhere(WeekendSqls.<BloggerRoleMapKey>custom().andEqualTo(BloggerRoleMapKey::getUserId, userId)).build();

        return bloggerRoleMapDao.selectByExample(example).stream()
                .filter(Objects::nonNull)
                .map(n -> getRoleById(n.getRoleId()))
                .collect(Collectors.toList());
    }

    @Override
    public RoleEnum getRoleById(Long id) {
        return RoleEnum.valueOf(bloggerRoleDao.selectByPrimaryKey(id).getRoleName());
    }

    @Override
    public void deleteUserById(Long id) {
        Example example = Example.builder(BloggerRoleMapKey.class).andWhere(WeekendSqls.<BloggerRoleMapKey>custom().andEqualTo(BloggerRoleMapKey::getUserId, id)).build();
        bloggerRoleMapDao.deleteByExample(example);
    }
}
