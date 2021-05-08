package com.blog.entity.vo;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * <pre>ArticleParams</pre>
 *  文章参数
 * @author <p>ADROITWOLF</p> 2021-05-08
 */
@Data
@ToString
public class ArticleParams {
    @NotBlank(message = "文章标题不能为空")
    @Size(max = 30, message = "标题字数不能超过{max}")
    private String title;


    @NotBlank(message = "文章内容不能为空")
    private String contentMd;

    @NotBlank(message = "文章内容不能为空")
    private String content;

    private Long pictureId;

    private List<String> tagList;

//    private Integer tag;

    @NotBlank(message = "文章总结不能为空")
    @Size(max = 100, message = "文章总结不能超过{maxz}")
    private String summary;
}
