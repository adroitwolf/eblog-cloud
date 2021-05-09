package com.api.feign.service;

import com.api.feign.service.factory.RoleFeignServiceFallBackFactory;
import com.common.enums.RoleEnum;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * <pre>RoleService</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-08
 */
@FeignClient(name = "iblog-user")
public interface RoleFeignService {
    @GetMapping("/feign/role/getRoles/{id}")
    List<RoleEnum> getRolesByUserId(@PathVariable("id") Long userId);
}
