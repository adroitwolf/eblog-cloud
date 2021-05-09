package com.attach.service;

import com.attach.entity.vo.AttachmentParams;
import com.attach.entity.vo.AttachmentQueryParams;
import com.common.enums.CiteNumEnum;
import com.common.entity.vo.BaseResponse;
import com.common.entity.vo.PageInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * <pre>AttachmentService</pre>
 *  附件库的服务层
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
public interface AttachmentService {

    /**
     * 根据图片id获取可以访问的url
     * @param id
     * @return
     */
    String getPicPathById(Long id);

    /**
     * 功能描述: 根据图片ID获取到服务器物理层盘上的图片名称
     *
     * @Param: [id]
     * @Return: java.lang.String
     * @Author: WHOAMI
     * @Date: 2020/1/30 18:20
     */
    String selectPicById(Long id);

    /**
     * 模糊查询该用户下面的所有附件
     * @param pageInfo
     * @param attachmentQueryParams
     * @param userId
     * @return
     */
    BaseResponse getAttachmentList(PageInfo pageInfo, AttachmentQueryParams attachmentQueryParams, String token);

    /**
     * 功能描述: 用户上传附件
     *
     */
    BaseResponse uploadAttachment(MultipartFile file, String token);

    /**
     * 功能描述:用户上传照片
     *
     * @Param: [file, userId, title]
     * @Return: java.lang.Long
     * @Author: WHOAMI
     * @Date: 2020/1/30 18:23
     */
    Long uploadFile(MultipartFile file, Long userId, String title);

    /**
     * 功能描述: 根据附件物理磁盘名称获取到附件ID
     *
     * @Param: [title]
     * @Return: java.lang.Long
     * @Author: WHOAMI
     * @Date: 2020/1/30 18:25
     */
    Long getIdByTitle(String filekey);

    /**
     * 功能描述: 根据附件id获取附件图片逻辑名(用户定义的名称)
     *
     * @Param: [id]
     * @Return: java.lang.String
     * @Author: WHOAMI
     * @Date: 2020/1/30 19:27
     */
    String getTitleById(Long id);

    /**
     * 功能描述: 根据图片id获取图片物理路径
     *
     * @Param: [id]
     * @Return: java.lang.String
     * @Author: WHOAMI
     * @Date: 2020/1/30 19:28
     */
    String getPathById(Long id);


    /**
     * 功能描述: 更新附件的信息
     *
     * @Param: [id, attachmentParams, token]
     * @Return: run.app.entity.DTO.BaseResponse
     * @Author: WHOAMI
     * @Date: 2020/1/30 19:30
     */
    BaseResponse updateInfo(Long id, AttachmentParams attachmentParams,String token);

    /**
     * 功能描述: 获取到附件的基本信息
     *
     * @Param: [id, token]
     * @Return: run.app.entity.DTO.BaseResponse
     * @Author: WHOAMI
     * @Date: 2020/1/30 19:33
     */
    BaseResponse getInfo(Long id, String token);

    /**
     * 功能描述: 删除附件信息
     *
     * @Param: [id, token]
     * @Return: run.app.entity.DTO.BaseResponse
     * @Author: WHOAMI
     * @Date: 2020/1/30 19:33
     */
    BaseResponse deleteAttachment(Long id, String token);

    /**
     * 功能描述: 删除图片
     *
     * @Param: [id]
     * @Return: void
     * @Author: WHOAMI
     * @Date: 2020/1/30 19:39
     */
    void deletePic(Long id);

    /**
     * 功能描述: 找到当前用户所有的附件类型
     *
     * @Param: [token]
     * @Return: run.app.entity.DTO.BaseResponse
     * @Author: WHOAMI
     * @Date: 2020/1/30 19:39
     */
    BaseResponse findAllMediaType(String token);

    /**
     * 功能描述: 改变附件当前引用人数
     *
     * @Param: [id, citeNumEnum]
     * @Return: void
     * @Author: WHOAMI
     * @Date: 2020/1/30 19:40
     */
    void changePictureStatus(Long id, CiteNumEnum citeNumEnum);


    /**
     * 功能描述: 将附件的地址转换成可以访问的url
     *
     * @Param: [path]
     * @Return: java.lang.String
     * @Author: WHOAMI
     * @Date: 2020/3/5 22:14
     */
    String covertAttachmentPath(String path);
}
