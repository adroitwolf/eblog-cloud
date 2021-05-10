package com.common.utils;

import com.common.entity.model.ImageFile;
import com.common.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * <pre>UploadUtil</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@Slf4j
public class UploadUtil {

    private UploadUtil(){}

    public static ImageFile  uploadFile(MultipartFile file,String imgPath) {



        ImageFile imageFile = new ImageFile();

        BufferedImage image;
        String originFilename = file.getOriginalFilename();
        /**
         * adroitwolf 2021-5-7
         *  添加解决可能导致NPE的代码
         */
        if(Objects.isNull(originFilename)){
            throw new NotFoundException("上传文件名称为空！");
        }
        String type = !originFilename.contains(".") ? null : originFilename.substring(originFilename.lastIndexOf(".") + 1);

        String filename = UUID.randomUUID().toString().replace("-", "");

        String finalFilename = imgPath + File.separator + filename + (null == type ? "" : ("." + type));

        File file1 = new File(finalFilename);
        /**
         * 如果路径不存在需要先创建路径
         */

        try {
            Path path = Paths.get(imgPath);
            if(!Files.exists(path)){
                log.info("创建文件夹:{}",imgPath);
                Files.createDirectory(path);
            }
            String thumbFile = file1.getParent() + File.separator + filename + "-thumb.jpg";
            image = ImageIO.read(file.getInputStream());
            file.transferTo(file1);
            Thumbnails.of(file1)
                    .scale(0.25f)
                    .toFile(thumbFile);
            imageFile.setThumbPath(filename + "-thumb.jpg");

        } catch (IOException e) {
            log.error("上传文件出错:{}",e.getMessage());

            return null;
        }

        //开始创建pojo对象
        String fileKey = filename + (null == type ? "" : ("." + type));
        imageFile.setPath(fileKey);
        imageFile.setTitle(!originFilename.contains(".") ? originFilename :
                originFilename.substring(0, originFilename.lastIndexOf(".")));

        imageFile.setSuffx(type);
        //todo  有漏洞  容易被人修改请求头
        imageFile.setMediaType(MediaType.valueOf(Objects.requireNonNull(file.getContentType())));

        imageFile.setFileKey(fileKey);

        // 读取文件属性
        imageFile.setSize(file.getSize());
        imageFile.setHeight(image.getHeight());
        imageFile.setWidth(image.getWidth());

        return imageFile;
    }


    public static void delFile(String filename,String imgPath) {
        try{
            Path path = Paths.get(imgPath + File.separator + filename);
            Files.delete(path);
            log.info("文件删除成功");
        }catch (Exception e){
            log.error("文件删除失败" + filename);
        }

    }
}
