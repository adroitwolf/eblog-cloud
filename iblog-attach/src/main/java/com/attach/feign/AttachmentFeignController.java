package com.attach.feign;

import com.attach.service.AttachmentService;
import com.common.enums.CiteNumEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * <pre>FeignController</pre>
 * 暴露给其他服务的接口
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@RestController
@RequestMapping("/feign/attach")
public class AttachmentFeignController {
    @Autowired
    AttachmentService attachmentService;

    @GetMapping("/path/id/{id}")
    public String getPathById(@PathVariable("id")Long id){
        return attachmentService.getPathById(id);
    }

    @PostMapping("/del/{id}")
    public int delAttachment(@PathVariable("id")Long id){
        attachmentService.deletePic(id);
        return 1;
    }

    @PostMapping("/upload")
    public Long uploadFile(MultipartFile avatar, Long userId, String title){
        return attachmentService.uploadFile(avatar,userId,title);
    }


    @PostMapping("/changeStatus")
    public int changePictureStatus(Long id, CiteNumEnum citeNumEnum) {
        attachmentService.changePictureStatus(id,citeNumEnum);
        return 1;
    }

    @GetMapping("/getPath/{id}")
    public String getPicPathById(@PathVariable("id") Long id){
        return attachmentService.getPathById(id);
    }
}
