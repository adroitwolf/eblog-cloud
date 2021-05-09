package com.user.controller.protal;

import com.common.entity.vo.AutoToken;
import com.common.entity.vo.BaseResponse;
import com.user.entity.vo.LoginParams;
import com.user.entity.vo.PasswordParams;
import com.user.entity.vo.RegisterParams;
import com.user.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * <pre>UserController</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-09
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    AccountService accountService;


//    @ApiOperation("用户登陆")
    @PostMapping("/login")
    public BaseResponse login(@RequestBody @Valid LoginParams loginParams) {

        return accountService.loginService(loginParams);
    }


//    @MethodLog
//    @ApiOperation("更改用户密码")
    @PutMapping("/changePassword")
    public BaseResponse updatePassword(@Valid @RequestBody PasswordParams passwordParams, HttpServletRequest request) {
//        String token = userService.getUsernameByToken(request.getHeader("Authentication"));

        return accountService.updatePassword(passwordParams.getOldPassword(), passwordParams.getNewPassword(), request.getHeader("Authentication"));
    }

//    @ApiOperation("发送邮箱验证码")
    @GetMapping("/getMailCode/{mail}")
    public void getMailCode(@PathVariable("mail") String mail) {
        accountService.getMailCode(mail);
    }

//    @ApiOperation("注册新用户")
    @PostMapping("/register")
    public BaseResponse register(@Valid @RequestBody RegisterParams registerParams) {
        return accountService.registerUser(registerParams);
    }


//    @ApiOperation("获取refresh令牌")
    @GetMapping("/refresh/{refreshToken}")
    public BaseResponse refresh(@PathVariable("refreshToken") String refreshToken) {
        return accountService.refresh(refreshToken);
    }


//    @MethodLog
    @PostMapping("/logout")
//    @ApiOperation("用户登出")
    public BaseResponse logout(AutoToken autoToken) {
        return accountService.logout(autoToken);
    }

}
