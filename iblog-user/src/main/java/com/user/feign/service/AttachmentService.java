package com.user.feign.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/feign/attach/del/{id}")
    int delAttachment(@PathVariable("id")Long id);

    @PostMapping("/feign/attach/upload")
    Long uploadFile(MultipartFile avatar, Long userId, String title);
}
