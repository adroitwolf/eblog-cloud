package com.auth.service;

import com.auth0.jwt.interfaces.JWTVerifier;
import com.common.entity.model.User;
import com.common.entity.vo.AutoToken;
import com.common.enums.RoleEnum;

import java.util.List;

/**
 * <pre>TokenService</pre>
 *  令牌的服务接口
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
public interface TokenService {
    /**
     * 功能描述: 根据用户账号信息构建令牌
     *
     * @Param: [user]
     * @Return: run.app.entity.DTO.AutoToken
     * @Author: WHOAMI
     * @Date: 2020/2/27 21:30
     */
    AutoToken buildAutoToken(User user);

    void deleteRefreshToken(String key);


    /**
     * 功能描述: 通过refresh_token获取用户id   若token不存在会抛出401异常
     *
     * @Param: [token]
     * @Return: java.lang.Long
     * @Author: WHOAMI
     * @Date: 2020/2/28 9:13
     */
    Long getUserIdByRefreshToken(String token);


    /**
     * 功能描述: 用来认证用户id和当前操作对象的作者id的权限一致性
     *
     * @Param: [id, token]
     * @Return: void
     * @Author: WHOAMI
     * @Date: 2020/1/30 17:55
     */
    void authentication(Long id, String token);

    /**
     * 功能描述: 生成token
     *
     * @Param: [user]
     * @Return: java.lang.String
     * @Author: WHOAMI
     * @Date: 2020/1/30 17:55
     */
    String generateToken(User user);

    /**
     * 功能描述: 验证令牌
     *
     * @Param: [token]
     * @Return: boolean
     * @Author: WHOAMI
     * @Date: 2020/1/30 17:57
     */
    boolean verifierToken(String token);

    /**
     * 功能描述: 判断令牌是否过期
     *
     * @Param: [token]
     * @Return: boolean
     * @Author: WHOAMI
     * @Date: 2020/1/30 17:57
     */
    boolean isExpire(String token);

    /**
     * 功能描述: 获取当前用户的角色
     *
     * @Param: [token]
     * @Return: java.util.List<run.app.entity.enums.RoleEnum>
     * @Author: WHOAMI
     * @Date: 2020/1/30 17:57
     */
    List<RoleEnum> getRoles(String token);

    /**
     * 功能描述: 获取token认证器
     *
     * @Param: [token]
     * @Return: com.auth0.jwt.JWTVerifier
     * @Author: WHOAMI
     * @Date: 2020/1/30 17:57
     */
    JWTVerifier getVerifierWithToken(String token);


    /**
     * 功能描述: 根据access_token令牌获取角色id
     *
     * @Param: [token]
     * @Return: java.lang.Long
     * @Author: WHOAMI
     * @Date: 2020/1/30 17:58
     */
    Long getUserIdWithToken(String token);
}
