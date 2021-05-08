package com.auth.service;

import com.auth0.jwt.JWTVerifier;
import com.common.entity.model.User;
import com.common.enums.RoleEnum;

import java.util.Date;
import java.util.List;

/**
 * <pre>JwtService</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
public interface JwtService {
    /**
     * 生成token令牌
     * @param user
     * @return
     */
    String generateToken(User user);

    JWTVerifier generateVerifier();

    Date generateExpirationDate(String token);

    List<RoleEnum> generateRole(String token);
}
