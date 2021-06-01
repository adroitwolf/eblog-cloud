package com.auth.service.impl;

import com.auth.props.JWTProperties;
import com.auth.service.JwtService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.common.entity.model.User;
import com.common.enums.RoleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <pre>JwtServiceImpl</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@Service("authJwtService")
@Slf4j
public class JwtServiceImpl implements JwtService {
    @Autowired
    JWTProperties jwtProperties;
    @Override
    public String generateToken(User user) {
        long currentTimeMillis = System.currentTimeMillis();
        return JWT.create()
                .withIssuer(jwtProperties.getName())
                .withExpiresAt(new Date(currentTimeMillis + jwtProperties.getJwtExpires() * 1000))
                .withClaim("userId", user.getId())
                .withArrayClaim("roles", user.getRoles().toArray(new String[0]))
                .withAudience(user.getId().toString(), user.getEmail())
                .sign(Algorithm.HMAC256(jwtProperties.getBase64Secret()));
    }

    @Override
    public  JWTVerifier generateVerifier() {
        return JWT.require(Algorithm.HMAC256(jwtProperties.getBase64Secret()))
                .withIssuer(jwtProperties.getName())
                .build();
    }

    @Override
    public Date generateExpirationDate(String token) {
        return JWT.decode(token).getExpiresAt();
    }

    @Override
    public List<RoleEnum> generateRole(String token) {
        return JWT.decode(token).getClaim("roles").asList(RoleEnum.class);
    }

}
