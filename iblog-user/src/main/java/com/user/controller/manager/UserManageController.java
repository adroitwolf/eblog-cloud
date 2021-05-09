package com.user.controller.manager;

import com.common.entity.vo.BaseResponse;
import com.common.entity.vo.PageInfo;
import com.common.entity.vo.QueryParams;
import com.user.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * <pre>UserController</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-09
 */
@Slf4j
@RestController
@RequestMapping("/manage/user")
public class UserManageController {

    @Autowired
    AccountService accountService;

    private static final String TOKEN = "Authentication";

    @PutMapping("/userUpdate/{bloggerId:\\d+}/{status}")
//    @ApiOperation("管理帐户状态")
    public BaseResponse updateUserStatus(@PathVariable("bloggerId") Long bloggerId,
                                         @PathVariable("status") String status, HttpServletRequest request) {
        return accountService.updateUserStatus(bloggerId, status, request.getHeader(TOKEN));
    }


    @DeleteMapping("/delete/{bloggerId:\\d+}")
//    @ApiOperation("删除用户")
    public BaseResponse deleteUser(@PathVariable("bloggerId") Long bloggerId, HttpServletRequest request) {
        return accountService.deleteUser(bloggerId, request.getHeader(TOKEN));
    }


    @GetMapping("/query")
//    @ApiOperation("查询用户状态")
    public BaseResponse queryUserByExample(
            @Valid PageInfo pageInfo,
            QueryParams queryParams) {
        return accountService.selectUserByExample(pageInfo, queryParams);
    }
}
