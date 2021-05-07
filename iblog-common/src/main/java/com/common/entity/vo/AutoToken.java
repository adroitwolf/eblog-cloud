package com.common.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <pre>AutoToken</pre>
 *  用户认证token
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AutoToken {
    /**
     * Access token.
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * Refresh token.
     */
    @JsonProperty("refresh_token")
    private String refreshToken;


    /**
     * Expired in. (seconds)
     */
    @JsonProperty("expired_in")
    private long expiredIn;
}
