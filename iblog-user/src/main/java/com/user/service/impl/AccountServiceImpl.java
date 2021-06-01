package com.user.service.impl;

import cn.hutool.core.lang.Validator;
import com.api.feign.service.AttachmentFeignService;
import com.auth.service.TokenService;
import com.common.entity.model.User;
import com.common.entity.model.UserInfo;
import com.common.entity.pojo.BloggerAccount;
import com.common.entity.pojo.BloggerProfile;
import com.common.entity.vo.AutoToken;
import com.common.entity.vo.BaseResponse;
import com.common.entity.vo.DataGrid;
import com.common.entity.vo.QueryParams;
import com.common.enums.RoleEnum;
import com.common.enums.UserStatusEnum;
import com.common.exception.*;
import com.common.utils.CommonUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.user.dao.BloggerAccountDao;
import com.user.dao.BloggerProfileDao;
import com.user.entity.vo.LoginParams;
import com.user.entity.vo.RegisterParams;
import com.user.service.AccountService;
import com.user.service.QMailService;
import com.user.service.RedisService;
import com.user.service.RoleService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <pre>AccountServiceImpl</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-08
 */
@Service
@Slf4j
public class AccountServiceImpl implements AccountService {
    @Autowired
    BloggerAccountDao bloggerAccountDao;

    @Autowired
    BloggerProfileDao bloggerProfileDao;


    @Autowired
    AttachmentFeignService attachmentFeignService;

    @Autowired
    RoleService roleService;

    @Resource(name = "authTokenService")
    TokenService tokenService;


    @Autowired
    RedisService redisService;


    @Autowired
    QMailService qMailService;


    private static final String PASSERROR = "用户名或密码不正确";

    private static final String LOGINSUCCESS = "用户登陆成功";

    private static final String BLOCKED = "账号被封禁";

    private static final String NOTFOUND = "账号未找到";

    @Override
    public @NonNull
    BaseResponse loginService(@NonNull LoginParams loginParams) {

//        if(tokenService.islogined(loginParams.getUsername())){
////            throw new BadRequestException("用户已经在别处登陆！");
////        }
        //判断用户是用邮箱还是账号登陆的
        final BloggerAccount user;

        User userRs;
        try {
            if (Validator.isEmail(loginParams.getP())) {
                user = loginWithEmail(loginParams.getP());
            }else{
                user  = loginWithUsername(loginParams.getP());
            }

        } catch (NotFoundException e) {
            throw new BadRequestException(PASSERROR);
        }
        //判断密码是否正确
        if (user.getPassword().equals(loginParams.getPassword())) {

//            这里应该判断账户是否被封禁
            log.info("用户{},登陆ing",user.toString());
            //说明被封禁
            if (!UserStatusEnum.YES.getName().equals(user.getIsEnabled())) {
                throw new BadRequestException(BLOCKED);
            }
            userRs = convertBloggerAccount2User(user);

        } else {
            throw new BadRequestException(PASSERROR);
        }

        BaseResponse baseResponse = new BaseResponse();
        AutoToken autoToken = tokenService.buildAutoToken(userRs);
        if(Objects.isNull(autoToken)){
            log.error("认证出现异常");
            throw new BadRequestException("认证服务出现异常");
        }
        baseResponse.setData(autoToken);

//        HashMap<String, Object> map = new HashMap<>();
//        map.put("token",token.get());
//        map.put("user",userRs);
//        baseResponse.setData(map);
//        baseResponse.setMessage(LOGINSUCCESS);
        baseResponse.setStatus(HttpStatus.OK.value());
        return baseResponse;
    }

    @Override
    public BaseResponse refresh(String refresh) {
        Long userId = tokenService.getUserIdByRefreshToken(refresh);
        if(userId.equals(0L)){
            throw new UnAuthenticationException("ReFreshToken凭证已失效，请重新登录");
        }
        BloggerAccount bloggerAccount = bloggerAccountDao.selectByPrimaryKey(userId);

        User user = convertBloggerAccount2User(bloggerAccount);

        AutoToken autoToken = tokenService.buildAutoToken(user);

        if(Objects.isNull(autoToken)){
            log.error("认证出现异常");
            throw new BadRequestException("认证服务出现异常");
        }

        //删除原来的refreshToken
        tokenService.deleteRefreshToken(refresh);


        BaseResponse baseResponse = new BaseResponse();

        baseResponse.setData(autoToken);

        baseResponse.setStatus(HttpStatus.OK.value());

        return baseResponse;
    }


    @Override
    public BaseResponse updatePassword(@NonNull String oldPassword, @NonNull String newPassword, String token) {

        BaseResponse baseResponse = new BaseResponse();
        Long id = tokenService.getUserIdByToken(token);

        Example example = Example.builder(BloggerAccount.class).andWhere(WeekendSqls.<BloggerAccount>custom().andEqualTo(BloggerAccount::getId, id).andEqualTo(BloggerAccount::getPassword, oldPassword)).build();
        BloggerAccount bloggerAccount = bloggerAccountDao.selectOneByExample(example);

        if (Objects.isNull(bloggerAccount)) {
            throw new BadRequestException("密码错误！");
        }
        bloggerAccount.setPassword(newPassword);
        bloggerAccountDao.updateByPrimaryKey(bloggerAccount);
//        baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
//        return baseResponse;

        baseResponse.setStatus(HttpStatus.OK.value());
        return baseResponse;
    }


    @Override
    public String getEmailByToken(@NonNull String token) {

        Long id = tokenService.getUserIdByToken(token);

        return getEmailById(id);
    }

    @Override
    public String getEmailById(Long userId) {
        BloggerAccount bloggerAccount = bloggerAccountDao.selectByPrimaryKey(userId);
        /**
         * 问题描述: 开发环境与生产环境的jwt生成算法必须不一致
         * @Author: WHOAMI
         * @Date: 2019/12/2 23:11
         */
        return bloggerAccount.getEmail();
    }

    @Override
    public @NonNull Long findBloggerIdByUsername(@NonNull String username) {

        Example example = Example.builder(BloggerAccount.class).andWhere(WeekendSqls.<BloggerAccount>custom().andEqualTo(BloggerAccount::getUsername, username)).build();

        BloggerAccount bloggerAccount = bloggerAccountDao.selectOneByExample(example);

        if(Objects.isNull(bloggerAccount)){
            return -1L;
        }
        return bloggerAccount.getId();
    }

    @Override
    public void getMailCode(String mail) {

        /**
         * 不是邮箱直接舍弃
         * 邮箱是否合法
         */
        if (!Validator.isEmail(mail)) {
            log.error("该账号非邮箱");
            return;
        }

        StringBuilder mailContent = new StringBuilder();

        //判断邮箱是否被注册过
        Example example = Example.builder(BloggerAccount.class).andWhere(WeekendSqls.<BloggerAccount>custom().andEqualTo(BloggerAccount::getEmail, mail)).build();

        BloggerAccount bloggerAccounts = bloggerAccountDao.selectOneByExample(example);

        if (!Objects.isNull(bloggerAccounts)) {
            mailContent.append("该邮箱已被注册");
            log.error("{}该邮箱已被注册",mail);
        } else {
            //生成验证码
            String code = CommonUtils.getCode();
            //存储到缓存中 有效期一天
            redisService.putEmailCode(mail, code, 2, TimeUnit.HOURS);
            mailContent.append("验证码为：");
            mailContent.append(code);
            mailContent.append("            ");
            mailContent.append("验证码有效期为2小时,请尽快完成注册");
        }

        qMailService.sendSimpleMail(mail, "用户注册", mailContent.toString());

    }

    @Override
    public BaseResponse registerUser(@NonNull RegisterParams registerParams) {
        BaseResponse baseResponse = new BaseResponse();

        //判断邮箱是否被注册过

        Example example = Example.builder(BloggerAccount.class).andWhere(WeekendSqls.<BloggerAccount>custom().andEqualTo(BloggerAccount::getEmail, registerParams.getEmail())).build();
        BloggerAccount bloggerAccounts = bloggerAccountDao.selectOneByExample(example);

        if (!Objects.isNull(bloggerAccounts)) {
            throw new BadRequestException("此邮箱已被注册");
        }

        //验证验证码是否正确
        if (!registerParams.getEmail().equals(redisService.getEmailByCode(registerParams.getCode()))) {
            throw new BadRequestException("验证码不正确！");
        }
//        先查询是否账号是否可用

        if(findBloggerIdByUsername(registerParams.getAccount()) != -1){
            baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponse.setMessage("此账号已被人抢先注册了！");
            return baseResponse;
        }

        BloggerAccount bloggerAccount = new BloggerAccount();

        BeanUtils.copyProperties(registerParams, bloggerAccount);

        bloggerAccount.setId(CommonUtils.nextId());
        bloggerAccount.setUsername(registerParams.getAccount());
        bloggerAccount.setRegisterDate(new Date());
        bloggerAccount.setIsEnabled(UserStatusEnum.YES.getName());

        log.debug(bloggerAccount.toString());


        if (bloggerAccountDao.insertSelective(bloggerAccount) == 0) {
            baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponse.setData("服务异常，请稍后重试");
            return baseResponse;
        }

        BloggerProfile bloggerProfile = new BloggerProfile();

        bloggerProfile.setNickname(registerParams.getUsername());
        bloggerProfile.setBloggerId(bloggerAccount.getId());
        bloggerProfileDao.insertSelective(bloggerProfile);
        baseResponse.setStatus(HttpStatus.OK.value());
        //设置用户角色
        //默认都是User用户

        List<RoleEnum> roles = new ArrayList<>();
        roles.add(RoleEnum.USER);

        roleService.setRolesWithUserId(roles, bloggerAccount.getId());
        return baseResponse;
    }

    public BloggerAccount loginWithEmail(String email) {

        Example example = Example.builder(BloggerAccount.class).andWhere(WeekendSqls.<BloggerAccount>custom().andEqualTo(BloggerAccount::getEmail, email)).build();

        BloggerAccount bloggerAccounts = bloggerAccountDao.selectOneByExample(example);
        if(Objects.isNull(bloggerAccounts)){
            throw new NotFoundException("用户邮箱不存在");
        }

        return bloggerAccounts;
    }

    private BloggerAccount loginWithUsername(String username) {


        Example example = Example.builder(BloggerAccount.class).andWhere(WeekendSqls.<BloggerAccount>custom().andEqualTo(BloggerAccount::getUsername, username)).build();

        BloggerAccount user = bloggerAccountDao.selectOneByExample(example);

        if(Objects.isNull(user)){
            throw new NotFoundException("用户账号不存在");
        }

        return user;
    }


    @Override
    public BaseResponse updateUserStatus(Long bloggerId, String status, String token) {
//      不允许封禁自己的账户
        if (bloggerId.equals(tokenService.getUserIdByToken(token))) {
            throw new UnAccessException("不允许对自身账号进行任何操作");
        }

//        应该考虑用户不存在的情况
        BloggerAccount bloggerAccount = new BloggerAccount();
        bloggerAccount.setId(bloggerId);
        //这里防止了非法字符注入
        bloggerAccount.setIsEnabled(UserStatusEnum.valueOf(status).getName());

        if (bloggerAccountDao.updateByPrimaryKeySelective(bloggerAccount) == 0) {
            throw new BadRequestException(NOTFOUND);
        }
        return new BaseResponse(HttpStatus.OK.value(), "账号状态更改成功", null);
    }

    /**
     * 管理员使用的接口，只能删除和管理用户角色
     * @param bloggerId
     * @param token
     * @return
     */
    @Override
    public BaseResponse deleteUser(Long bloggerId, String token) { //删除用户需要权限
        //todo 需要在用户验证 验证码之后的操作

        log.info(String.valueOf(bloggerId));
        Long id = tokenService.getUserIdByToken(token);
        log.info("经过token解释过的id:{}", id);

        if (bloggerId.equals(id)) {
            throw new UnAccessException("不允许删除自己");
        }
        BloggerAccount bloggerAccount = bloggerAccountDao.selectByPrimaryKey(bloggerId);

        //说明没有此账号
        if(Objects.isNull(bloggerAccount)){
            throw new BadRequestException(NOTFOUND);
        }
        bloggerAccount.setIsEnabled(UserStatusEnum.NO.toString());
        //删除个人配置文件
        bloggerAccountDao.updateByPrimaryKeySelective(bloggerAccount);

        roleService.deleteUserById(bloggerId);
        //todo:一些繁琐文件应该放到另一个不浪费时间的时候运行

        return new BaseResponse(HttpStatus.OK.value(), "账删除成功", null);
    }

    @Override
    public BaseResponse selectUserByExample(com.common.entity.vo.PageInfo pageInfo, QueryParams queryParams) {
        if (!StringUtils.isEmpty(queryParams.getStatus())) {
            UserStatusEnum.valueOf(queryParams.getStatus());
        }

        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
        List<UserInfo> accounts = bloggerAccountDao.selectByQueryParams(queryParams);

        PageInfo<UserInfo> pageInfoObject = new PageInfo<>(accounts);

        DataGrid dataGrid = new DataGrid();

        List<UserInfo> lists = pageInfoObject.getList().stream().map(item -> {

            UserInfo userInfo = new UserInfo();

            BeanUtils.copyProperties(item, userInfo);

            if (Objects.nonNull(item.getAvatarId())) { //查询是否头像为空
                userInfo.setAvatar(attachmentFeignService.getPathById(item.getAvatarId()));
            }

            //查询当前用户角色
            userInfo.setRoles(roleService.getRolesByUserId(userInfo.getId()).stream().map(RoleEnum::getAuthority).collect(Collectors.toList()));

            return userInfo;
        }).collect(Collectors.toList());


        dataGrid.setRows(lists);

        dataGrid.setTotal(pageInfoObject.getTotal());


        return new BaseResponse(HttpStatus.OK.value(), null, dataGrid);
    }

    @Override
    public BaseResponse logout(AutoToken autoToken) {

        //消除redis缓存
        redisService.delete(autoToken.getRefreshToken());
        return new BaseResponse();
    }

    @Override
    public User convertBloggerAccount2User(BloggerAccount user) {
        User userRs = new User();

        BeanUtils.copyProperties(user, userRs);


        userRs.setRoles(roleService.getRolesByUserId(userRs.getId())
                .stream().map(RoleEnum::getAuthority).collect(Collectors.toList()));

        return userRs;
    }

}
