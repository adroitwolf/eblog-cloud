package com.blog.entity.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * <pre>PopularBlog</pre>
 *  人气博客
 * @author <p>ADROITWOLF</p> 2021-05-07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Slf4j
public class PopularBlog implements Serializable, Comparable{
    private String blogName;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private Integer clickcount;

    @Override
    public boolean equals(Object obj) {
        log.info(this + "....equals...." + obj);

        if (this == obj){
            return true;
        }
        if (!(obj instanceof PopularBlog)){
            return false;
        }
        PopularBlog p = (PopularBlog) obj;
        return this.blogName.equals(p.blogName) && this.id.equals(p.id);
    }

    @Override
    public int compareTo(Object o) {
        log.info(this + "....compareTo...." + o);
        PopularBlog p = (PopularBlog) o;
        return this.clickcount.compareTo(p.clickcount);
    }
}
