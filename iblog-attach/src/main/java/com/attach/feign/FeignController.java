package com.attach.feign;

import com.attach.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>FeignController</pre>
 * 暴露给其他服务的接口
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@RestController
@RequestMapping("/feign")
public class FeignController {
    @Autowired
    AttachmentService attachmentService;

    @GetMapping("/path/id/{id}")
    String getPathById(@PathVariable("id")Long id){
        return attachmentService.getPathById(id);
    }
}
