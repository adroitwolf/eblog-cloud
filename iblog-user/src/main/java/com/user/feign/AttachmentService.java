package com.user.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * <pre>AttachmentService</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@FeignClient(name = "iblog-attach")
public interface AttachmentService {
    @GetMapping("/feign/path/id/{id}")
    String getPathById(@PathVariable("id")Long id);
}
