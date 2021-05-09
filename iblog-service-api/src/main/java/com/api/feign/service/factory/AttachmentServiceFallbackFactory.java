package com.api.feign.service.factory;

import com.api.feign.service.AttachmentFeignService;
import com.common.enums.CiteNumEnum;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * <pre>AttachmentServiceFallbackFactory</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-09
 */
@Component
@Slf4j
public class AttachmentServiceFallbackFactory implements FallbackFactory<AttachmentFeignService> {
    @Override
    public AttachmentFeignService create(Throwable throwable) {
        log.error("远程接口异常:{}",throwable.getMessage());

        return new AttachmentFeignService() {
            @Override
            public String getPathById(Long id) {
                return null;
            }

            @Override
            public int delAttachment(Long id) {
                return 0;
            }

            @Override
            public Long uploadFile(MultipartFile avatar, Long userId, String title) {
                return null;
            }

            @Override
            public int changePictureStatus(Long id, CiteNumEnum citeNumEnum) {
                return 0;
            }

            @Override
            public String getPicPathById(Long id) {
                return null;
            }
        };
    }
}
