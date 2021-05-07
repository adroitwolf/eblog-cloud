package com.common.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Table;

/**
 * <pre>BloggerRoleMapKey</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-06
 */
@Table(name = "e_role_map")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class BloggerRoleMapKey {
    private Long userId;

    private Long roleId;
}
