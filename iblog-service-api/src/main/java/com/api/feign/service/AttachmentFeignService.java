package com.api.feign.service;

import com.common.enums.CiteNumEnum;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * <pre>AttachmentService</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@FeignClient(name = "iblog-attach")
public interface AttachmentFeignService {
    @PostMapping("/feign/attach/path/id/{id}")
    String getPathById(@PathVariable("id")Long id);

    @PostMapping("/feign/attach/del/{id}")
    int delAttachment(@PathVariable("id")Long id);

    @PostMapping(value = "/feign/attach/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Long uploadFile(@RequestBody MultipartFile avatar, @RequestParam("userId") Long userId,@RequestParam("title") String title);

    @PostMapping("/feign/attach/changeStatus")
    int changePictureStatus(@RequestParam("id") Long id, @RequestBody CiteNumEnum citeNumEnum);

    @PostMapping("/getPath/{id}")
    String getPicPathById(@PathVariable("id") Long id);
}
