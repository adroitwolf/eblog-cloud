package com.blog.entity.vo;

import com.common.entity.dto.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <pre>BlogDetailWithAuthor</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@Data
@ToString(callSuper = true)
@NoArgsConstructor
public class BlogDetailWithAuthor extends BlogDetail{
    private UserDto author;
}
