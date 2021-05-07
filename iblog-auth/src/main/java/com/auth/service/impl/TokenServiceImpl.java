package com.auth.service.impl;

import com.auth.props.JWTProperties;
import com.auth.service.RedisService;
import com.auth.service.RoleService;
import com.auth.service.TokenService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.common.entity.model.User;
import com.common.entity.vo.AutoToken;
import com.common.enums.RoleEnum;
import com.common.exception.ServiceException;
import com.common.exception.UnAccessException;
import com.common.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * <pre>TokenServiceImpl</pre>
 *  令牌实现类
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    AccountService accountService;

    @Autowired
    RedisService redisService;


    @Autowired
    JWTProperties jwtProperties;


    @Override
    public AutoToken buildAutoToken(User user) {
        AutoToken autoToken = new AutoToken();
        String accessToken = Optional.ofNullable(generateToken(user)).orElseThrow(() -> new ServiceException("服务异常"));

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

    @Override
    public void authentication(Long id, String token) {
        if (roleService.getRolesByUserId(getUserIdWithToken(token)).contains(RoleEnum.ADMIN)) {
            return;
        } else if (null == id || !id.equals(getUserIdWithToken(token))) {

            throw new UnAccessException("您没有权限进行该操作");
        }
    }

    //    利用Jwt生成token
    @Override
    public String generateToken(User user) {
        return JwtUtil.generateToken(user);
    }

    @Override
    public boolean verifierToken(String token) {
        try {
            JwtUtil.generateVerifier().verify(token);
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
            Date date = JwtUtil.generateExpirationDate(token);
            return date.compareTo(new Date()) <= 0 ? true : false;
        } catch (JWTDecodeException e) {
            return false;
        } catch (Exception e) {

            return false;
        }

    }

    @Override
    public List<RoleEnum> getRoles(String token) {
        return JwtUtil.generateRole(token);
    }


    @Override
    public JWTVerifier getVerifierWithToken(String token) {
        return JwtUtil.generateVerifier();
    }

    @Override
    public Long getUserIdWithToken(String token) {

        return JWT.decode(token).getClaim("userId").asLong();

    }
}
