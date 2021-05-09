package com.api.feign.service.factory;

import com.api.feign.service.TokenFeignService;
import com.common.entity.model.User;
import com.common.entity.vo.AutoToken;
import com.common.enums.RoleEnum;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <pre>TokenServiceFallBackFactory</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-09
 */
@Component
@Slf4j
public class TokenServiceFallBackFactory implements FallbackFactory<TokenFeignService> {
    @Override
    public TokenFeignService create(Throwable throwable) {
        log.error("远程接口异常:{}",throwable.getMessage());
        return new TokenFeignService() {
            @Override
            public Long getUserIdByToken(String token) {
                return null;
            }

            @Override
            public AutoToken buildAutoToken(User user) {
                return null;
            }

            @Override
            public Long getUserIdByRefreshToken(String token) {
                return null;
            }

            @Override
            public boolean deleteRefreshToken(String key) {
                return false;
            }

            @Override
            public boolean authentication(Long id, String token) {
                return false;
            }

            @Override
            public List<RoleEnum> getRoles(String token) {
                return null;
            }
        };
    }
}
