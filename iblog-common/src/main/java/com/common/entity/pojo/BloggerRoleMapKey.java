package com.common.entity.pojo;

import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <pre>BloggerRoleMapKey</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-06
 */
@Table(name = "e_role_map")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class BloggerRoleMapKey {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    private Long userId;

    private Long roleId;
}
