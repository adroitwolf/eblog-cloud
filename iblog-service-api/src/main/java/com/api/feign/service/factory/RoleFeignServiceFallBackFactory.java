package com.api.feign.service.factory;

import com.api.feign.service.RoleFeignService;
import com.common.enums.RoleEnum;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <pre>RoleFeignServiceFallBackFactory</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-09
 */
@Component
@Slf4j
public class RoleFeignServiceFallBackFactory implements FallbackFactory<RoleFeignService> {
    @Override
    public RoleFeignService create(Throwable throwable) {
        log.error("远程接口异常:{}",throwable.getMessage());
        return new RoleFeignService() {
            @Override
            public List<RoleEnum> getRolesByUserId(Long userId) {
                return null;
            }
        };
    }
}
