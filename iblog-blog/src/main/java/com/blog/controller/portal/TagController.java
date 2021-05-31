package com.blog.controller.portal;

import com.blog.service.BlogService;
import com.common.entity.vo.BaseResponse;
import com.common.entity.vo.PageInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <pre>TagController</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-09
 */
@RestController
@Slf4j
@RequestMapping("/api/tag")
public class TagController {

    @Autowired
    BlogService blogService;

    @GetMapping("tag")
    @ApiOperation("查询相应tag标签")
    public BaseResponse searchTagBlogList(@Valid PageInfo pageInfo,
                                          @RequestParam String tag) {
        return blogService.getArticleListByTag(pageInfo, tag);
    }


}
