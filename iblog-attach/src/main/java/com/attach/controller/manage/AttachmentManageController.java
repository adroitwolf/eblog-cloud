package com.attach.controller.manage;

import com.attach.entity.vo.AttachmentParams;
import com.attach.entity.vo.AttachmentQueryParams;
import com.attach.service.AttachmentService;
import com.common.entity.vo.BaseResponse;
import com.common.entity.vo.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * <pre>AttachmentController</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-09
 */
@Slf4j
@RestController
@RequestMapping("/manage/attachment")
public class AttachmentManageController {
    @Autowired
    AttachmentService attachmentService;

    private static final String TOKEN = "Authentication";

    @GetMapping("list")
//    @ApiOperation("获取所有附件")
    public BaseResponse getAttachmentList(PageInfo pageInfo,
                                          AttachmentQueryParams attachmentQueryParams,
                                          HttpServletRequest request) {
        return attachmentService.getAttachmentList(pageInfo, attachmentQueryParams, request.getHeader(TOKEN));
    }


    @PostMapping(value = "upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @ApiOperation("上传图片")
    public BaseResponse uploadFile(MultipartFile file, HttpServletRequest request) {
        return attachmentService.uploadAttachment(file, request.getHeader(TOKEN));
    }


    @GetMapping("{picId:\\d+}/info")
//    @ApiOperation("获取图片的详细信息")
    public BaseResponse getInfo(@PathVariable("picId") Long picId, HttpServletRequest request) {
        return attachmentService.getInfo(picId, request.getHeader(TOKEN));
    }


    @PutMapping("{picId:\\d+}/info")
//    @ApiOperation("更新图片信息")
    public BaseResponse updateInfo(@PathVariable("picId") Long picId, @Valid @RequestBody AttachmentParams attachmentParams, HttpServletRequest request) {
        return attachmentService.updateInfo(picId, attachmentParams, request.getHeader(TOKEN));
    }


    @DeleteMapping("{picId:\\d+}")
//    @ApiOperation("删除图片")
    public BaseResponse deleteAttachment(@PathVariable("picId") Long picId, HttpServletRequest request) {
        return attachmentService.deleteAttachment(picId, request.getHeader(TOKEN));
    }


    @GetMapping("/list_media")
//    @ApiOperation("列出所有媒体类型")
    public BaseResponse findAllMediaType(HttpServletRequest request) {
        return attachmentService.findAllMediaType(request.getHeader(TOKEN));
    }
}
