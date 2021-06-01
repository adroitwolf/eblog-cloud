package com.auth.service.impl;

import com.auth.props.JWTProperties;
import com.auth.service.JwtService;
import com.auth.service.RedisService;
import com.auth.service.TokenService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.common.entity.model.User;
import com.common.entity.vo.AutoToken;
import com.common.enums.RoleEnum;
import com.common.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <pre>TokenServiceImpl</pre>
 *  令牌实现类
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@Slf4j
@Service("authTokenService")
public class TokenServiceImpl implements TokenService {

    @Autowired
    RedisService redisService;


    @Autowired
    JWTProperties jwtProperties;

    @Autowired
    JwtService jwtService;


    @Override
    public AutoToken buildAutoToken(User user) {
        AutoToken autoToken = new AutoToken();
        String accessToken = generateToken(user);

        if(StringUtils.isEmpty(accessToken)){
            return null;
        }
        String refreshToken = CommonUtils.randomUUIDWithoutDash();

        redisService.putAutoToken(refreshToken, user.getId(), jwtProperties.getRefreshExpires(), TimeUnit.DAYS);

        autoToken.setAccessToken(accessToken);

        autoToken.setRefreshToken(refreshToken);

        autoToken.setExpiredIn(jwtProperties.getJwtExpires());

        return autoToken;
    }

    @Override
    public void deleteRefreshToken(String key) {
        redisService.delete(key);
    }

    @Override
    public Long getUserIdByRefreshToken(String token) {
        return redisService.getUserIdByRefreshToken(token);
    }

    /**
     * 利用Jwt生成token
     * @param user
     * @return
     */
    @Override
    public String generateToken(User user) {
        return jwtService.generateToken(user);
    }

    @Override
    public boolean verifierToken(String token) {
        try {
            jwtService.generateVerifier().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            log.error(e.getMessage());
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isExpire(String token) {
        try {
            Date date = jwtService.generateExpirationDate(token);
            return date.compareTo(new Date()) <= 0;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public List<RoleEnum> getRoles(String token) {
        return jwtService.generateRole(token);
    }


    @Override
    public JWTVerifier getVerifierWithToken(String token) {
        return jwtService.generateVerifier();
    }

    @Override
    public Long getUserIdByToken(String token) {

        return JWT.decode(token).getClaim("userId").asLong();

    }
}
