package com.auth.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * <pre>JWTProperties</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@Data
@Component
@Validated
@ConfigurationProperties(prefix = "jwt")
public class JWTProperties {
    /**
     * 发行者名
     */
    private String name;

    /**
     * base64加密密钥
     */
    private String base64Secret;

    /**
     * access_token中过期时间设置(s)
     */
    private int jwtExpires;

    /**
     * refresh_token过期时间（day）
     */
    private int refreshExpires;
}
