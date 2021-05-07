package com.common.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <pre>BloggerRole</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-06
 */
@Table(name = "e_role")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class BloggerRole {
    @Id
    private Long id;

    private String roleName;

    private String roleZh;
}
