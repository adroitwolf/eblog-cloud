package com.user.service.impl;

import cn.hutool.http.HttpStatus;
import com.common.entity.pojo.BloggerRole;
import com.common.entity.pojo.BloggerRoleMapKey;
import com.common.entity.vo.BaseResponse;
import com.common.enums.RoleEnum;
import com.common.exception.ServiceException;
import com.common.utils.CommonUtils;
import com.common.utils.DataUtils;
import com.user.dao.BloggerRoleDao;
import com.user.dao.BloggerRoleMapDao;
import com.user.entity.vo.RoleParams;
import com.user.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    private Map<String,Long> roles;


    public RoleServiceImpl(@Autowired DataUtils dataUtils) {
        roles = dataUtils.loadRoleJson();
    }

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

    @Override
    public BaseResponse getRoles() {
        Example example = Example.builder(BloggerRole.class).andWhere(WeekendSqls.<BloggerRole>custom().andNotEqualTo(BloggerRole::getRoleName, RoleEnum.LORD.toString())).build();
        return BaseResponse.success(bloggerRoleDao.selectByExample(example));
    }

    @Override
    public BaseResponse updateUserRoles(RoleParams roleParams) {
        Example example = Example.builder(BloggerRoleMapKey.class).andWhere(WeekendSqls.<BloggerRoleMapKey>custom().andEqualTo(BloggerRoleMapKey::getUserId, roleParams.getUserId())).build();

        bloggerRoleMapDao.deleteByExample(example);


        List<BloggerRoleMapKey> list = roleParams.getRoles().stream().map(roleEnum -> {
            BloggerRoleMapKey mapKey = new BloggerRoleMapKey();
            mapKey.setUserId(roleParams.getUserId());
            mapKey.setRoleId(roles.get(roleEnum.toString()));
            return mapKey;
        }).collect(Collectors.toList());
        bloggerRoleMapDao.insertList(list);
        BaseResponse response = new BaseResponse();
        response.setStatus(HttpStatus.HTTP_OK);
        return response;
    }

    @Override
    public List<Long> getUserIds() {
        // 获取用户和超级管理员的用户id
        Example example = Example.builder(BloggerRole.class).andWhere(WeekendSqls.<BloggerRole>custom().andNotEqualTo(BloggerRole::getRoleName, RoleEnum.LORD.toString())).build();
        List<Long> ids = bloggerRoleDao.selectByExample(example).stream().map(BloggerRole::getId).collect(Collectors.toList());
        List<Long> result = new ArrayList<>();
        ids.stream().forEach(id->{
            Example subExample = Example.builder(BloggerRoleMapKey.class).andWhere(WeekendSqls.<BloggerRoleMapKey>custom().andEqualTo(BloggerRoleMapKey::getRoleId, id)).build();
            List<Long> list = bloggerRoleMapDao.selectByExample(subExample).stream().map(BloggerRoleMapKey::getUserId).collect(Collectors.toList());
            result.addAll(list);
        });

        return result;
    }


}
