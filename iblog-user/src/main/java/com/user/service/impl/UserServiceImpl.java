package com.user.service.impl;

import com.api.feign.service.AttachmentFeignService;
import com.api.feign.service.TokenFeignService;
import com.common.entity.dto.UserDto;
import com.common.entity.model.UserInfo;
import com.common.entity.pojo.BloggerAccount;
import com.common.entity.pojo.BloggerProfile;
import com.common.entity.vo.BaseResponse;
import com.common.entity.vo.DataGrid;
import com.common.entity.vo.PageInfo;
import com.common.entity.vo.QueryParams;
import com.common.enums.RoleEnum;
import com.github.pagehelper.PageHelper;
import com.user.dao.BloggerAccountDao;
import com.user.dao.BloggerProfileDao;
import com.user.entity.vo.UserDetail;
import com.user.entity.vo.UserParams;
import com.user.service.RoleService;
import com.user.service.UserService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <pre>UserServiceImpl</pre>
 * 用户服务
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    AttachmentFeignService attachmentFeignService;

    @Autowired
    BloggerAccountDao bloggerAccountDao;

    @Autowired
    BloggerProfileDao bloggerProfileDao;

    @Autowired
    RoleService roleService;

    @Autowired
    TokenFeignService tokenFeignService;


    private final String TITLE = "用户头像";

    @Override
    public  UserDetail findUserDetailByBloggerId(@NonNull Long bloggerId) {

        BloggerAccount bloggerAccount = bloggerAccountDao.selectByPrimaryKey(bloggerId);

        BloggerProfile bloggerProfile = bloggerProfileDao.selectByPrimaryKey(bloggerId);

        UserDetail userDetail = new UserDetail();
        BeanUtils.copyProperties(bloggerAccount, userDetail);
        BeanUtils.copyProperties(bloggerProfile, userDetail);
        userDetail.setNickname(bloggerProfile.getNickname());
//        需要判断是否为空
        if (Objects.nonNull(bloggerProfile.getAvatarId())) {

            userDetail.setAvatar(attachmentFeignService.getPathById(bloggerProfile.getAvatarId()));
        }

        //找到用户权限
        userDetail.setRoles(roleService.getRolesByUserId(bloggerId).stream().map(RoleEnum::getAuthority).collect(Collectors.toList()));

        return userDetail;
    }

    @Override
    @Transactional
    public @NonNull
    BaseResponse updateProfileById(@NonNull UserParams userParams, @NonNull String token) {

        BaseResponse baseResponse = new BaseResponse();

        Long userId = tokenFeignService.getUserIdByToken(token);

        BloggerProfile bloggerProfile = new BloggerProfile();

//        设置phone 和email  (不能更改)

//        BloggerAccount bloggerAccount = new BloggerAccount();

//        bloggerAccount.setId(userId);
//        BeanUtils.copyProperties(userParams,bloggerAccount);

//        log.info(bloggerAccount.toString());
//
//        bloggerAccountMapper.updateByPrimaryKeySelective(bloggerAccount);


        //    这个才是昵称
        bloggerProfile.setBloggerId(userId);

        BeanUtils.copyProperties(userParams, bloggerProfile);
//        bloggerProfile.setNickname(userParams.getUsername());
//        bloggerProfile.setAboutMe(userParams.getAboutMe());
//        bloggerProfileMapper.updateByExampleSelective(bloggerProfileWithBLOBs,bloggerProfileExample);

        bloggerProfileDao.updateByPrimaryKeySelective(bloggerProfile);

        UserDetail userDetail = new UserDetail();

        userDetail.setAboutMe(bloggerProfile.getAboutMe());
        userDetail.setNickname(bloggerProfile.getNickname());
//        userDetail.setEmail(bloggerAccount.getEmail());
        baseResponse.setData(userDetail);
        baseResponse.setStatus(HttpStatus.OK.value());

        return baseResponse;
    }

    @Override
    public BaseResponse getUserDetailByToken(@NonNull String token) {
        Long id = tokenFeignService.getUserIdByToken(token);

        return new BaseResponse(HttpStatus.OK.value(), "", findUserDetailByBloggerId(id));

    }

    @Transactional
    @Override
    public BaseResponse updateAvatar(@NonNull MultipartFile avatar, @NonNull String token) {
        Long id = tokenFeignService.getUserIdByToken(token);

//        删除原有附件

        BloggerProfile bloggerProfile = bloggerProfileDao.selectByPrimaryKey(id);

        if (null != bloggerProfile.getAvatarId()) {
            attachmentFeignService.delAttachment(bloggerProfile.getAvatarId());
        }

        //        添加现有附件
        Long picId = attachmentFeignService.uploadFile(avatar, id, TITLE);
        BloggerProfile profile = new BloggerProfile();
        profile.setBloggerId(id);
        profile.setAvatarId(picId);
        bloggerProfileDao.updateByPrimaryKeySelective(profile);
        return new BaseResponse(HttpStatus.OK.value(), "更新头像成功", attachmentFeignService.getPathById(picId));
    }


    @Override
    public UserDto getUserDTOById(Long id) {
        BloggerProfile bloggerProfile = bloggerProfileDao.selectByPrimaryKey(id);

        UserDto user = new UserDto();

        user.setId(bloggerProfile.getBloggerId());

        user.setNickname(bloggerProfile.getNickname());


        String avatar = null == bloggerProfile.getAvatarId() ? null : attachmentFeignService.getPathById(bloggerProfile.getAvatarId());

        user.setAvatar(avatar);

        return user;
    }

    @Override
    public String getNicknameById(Long id) {
        BloggerProfile profile = bloggerProfileDao.selectByPrimaryKey(id);
        if(Objects.isNull(profile)){
            return "";
        }
        return profile.getNickname();
    }

    @Override
    public BaseResponse getAllUserInfo(QueryParams queryParams, PageInfo pageInfo) {
        PageHelper.startPage(pageInfo.getPageNum(),pageInfo.getPageSize());
        List<UserInfo> infos = bloggerAccountDao.selectAllUsersByQueryParams(queryParams);
        List<UserInfo> result = infos.stream().map(item->{
            UserInfo userInfo = new UserInfo();

            BeanUtils.copyProperties(item, userInfo);

            if (Objects.nonNull(item.getAvatarId())) { //查询是否头像为空
                userInfo.setAvatar(attachmentFeignService.getPathById(item.getAvatarId()));
            }

            //查询当前用户角色
            userInfo.setRoles(roleService.getRolesByUserId(userInfo.getId()).stream().map(RoleEnum::getAuthority).collect(Collectors.toList()));
            return userInfo;
        }).collect(Collectors.toList());

        DataGrid dataGrid = new DataGrid();

        com.github.pagehelper.PageInfo<UserInfo> pageInfoObject = new com.github.pagehelper.PageInfo<>(infos);

        dataGrid.setRows(result);

        dataGrid.setTotal(pageInfoObject.getTotal());

        return BaseResponse.success(dataGrid);
    }
}
