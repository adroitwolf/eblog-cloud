package com.blog.feign.service;

import com.common.enums.CiteNumEnum;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * <pre>AttachmentService</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@FeignClient(name = "iblog-attach")
@Component
public interface AttachmentService {
    @GetMapping("/feign/attach/path/id/{id}")
    String getPathById(@PathVariable("id")Long id);

    @PostMapping("/feign/attach/changeStatus")
    int changePictureStatus(Long id, CiteNumEnum citeNumEnum);

    @GetMapping("/getPath/{id}")
    String getPicPathById(@PathVariable("id") Long id);
}
