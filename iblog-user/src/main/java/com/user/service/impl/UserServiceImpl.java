package com.user.service.impl;

import com.common.entity.pojo.BloggerAccount;
import com.common.entity.pojo.BloggerProfile;
import com.common.entity.vo.BaseResponse;
import com.user.dao.BloggerAccountDao;
import com.user.dao.BloggerProfileDao;
import com.user.entity.vo.UserDetail;
import com.user.entity.vo.UserParams;
import com.user.feign.AttachmentService;
import com.user.service.UserService;
import lombok.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

/**
 * <pre>UserServiceImpl</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-07
 */

public class UserServiceImpl implements UserService {
    @Autowired
    AttachmentService attachmentService;

    @Autowired
    BloggerAccountDao bloggerAccountDao;

    @Autowired
    BloggerProfileDao bloggerProfileDao;

//    @Autowired
//    RoleService roleService;
//
//    @Autowired
//    TokenService tokenService;


    private final String TITLE = "用户头像";

    @Override
    public @NonNull
    UserDetail findUserDetailByBloggerId(@NonNull Long bloggerId) {

        BloggerAccount bloggerAccount = bloggerAccountDao.selectByPrimaryKey(bloggerId);

        BloggerProfile bloggerProfile = bloggerProfileDao.selectByPrimaryKey(bloggerId);

        UserDetail userDetail = new UserDetail();
        BeanUtils.copyProperties(bloggerAccount, userDetail);
        BeanUtils.copyProperties(bloggerProfile, userDetail);
        userDetail.setNickname(bloggerProfile.getNickname());
//        需要判断是否为空
        if (Objects.nonNull(bloggerProfile.getAvatarId())) {

            userDetail.setAvatar(attachmentService.getPathById(bloggerProfile.getAvatarId()));
        }

        //找到用户权限
//        userDetail.setRoles(roleService.getRolesByUserId(bloggerId).stream().map(n -> n.getAuthority()).collect(Collectors.toList()));

        return userDetail;
    }

    @Override
    @Transactional
    public @NonNull
    BaseResponse updateProfileById(@NonNull UserParams userParams, @NonNull String token) {

        BaseResponse baseResponse = new BaseResponse();

        Long userId = tokenService.getUserIdWithToken(token);

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

        bloggerProfileMapper.updateByPrimaryKeySelective(bloggerProfile);

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
        Long id = tokenService.getUserIdWithToken(token);

        return new BaseResponse(HttpStatus.OK.value(), "", findUserDetailByBloggerId(id));

    }

    @Transactional
    @Override
    public BaseResponse updateAvatar(@NonNull MultipartFile avatar, @NonNull String token) {
        Long id = tokenService.getUserIdWithToken(token);

//        删除原有附件

        BloggerProfile bloggerProfile = bloggerProfileMapper.selectByPrimaryKey(id);

        if (null != bloggerProfile.getAvatarId()) {
            attachmentService.deletePic(bloggerProfile.getAvatarId());
        }

        //        添加现有附件
        Long picId = attachmentService.uploadFile(avatar, id, TITLE);
        BloggerProfile profile = new BloggerProfile();
        profile.setBloggerId(id);
        profile.setAvatarId(picId);
        bloggerProfileMapper.updateByPrimaryKeySelective(profile);
        return new BaseResponse(HttpStatus.OK.value(), "更新头像成功", attachmentService.getPathById(picId));
    }


    @Override
    public UserDTO getUserDTOById(Long id) {
        BloggerProfile bloggerProfile = bloggerProfileMapper.selectByPrimaryKey(id);

        UserDTO user = new UserDTO();

        user.setId(bloggerProfile.getBloggerId());

        user.setNickname(bloggerProfile.getNickname());


        String avatar = null == bloggerProfile.getAvatarId() ? null : attachmentService.getPathById(bloggerProfile.getAvatarId());

        user.setAvatar(avatar);

        return user;
    }

    @Override
    public String getNicknameById(Long id) {
        return bloggerProfileMapper.selectByPrimaryKey(id).getNickname();
    }
}
