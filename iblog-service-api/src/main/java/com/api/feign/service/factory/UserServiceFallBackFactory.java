package com.api.feign.service.factory;

import com.api.feign.service.UserFeignService;
import com.common.entity.dto.UserDto;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <pre>UserServiceFallBackFactory</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-09
 */
@Component
@Slf4j
public class UserServiceFallBackFactory implements FallbackFactory<UserFeignService> {
    @Override
    public UserFeignService create(Throwable throwable) {
        log.error("远程接口异常:{}",throwable.getMessage());
        return new UserFeignService() {
            @Override
            public UserDto getUserDTOById(Long id) {
                return null;
            }

            @Override
            public String getNicknameById(Long id) {
                return null;
            }
        };
    }
}
