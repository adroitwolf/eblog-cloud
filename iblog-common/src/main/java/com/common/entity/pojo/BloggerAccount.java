package com.common.entity.pojo;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * <pre>BloggerAccount</pre>
 *
 * @author <p>ADROITWOLF</p> 2021-05-06
 */
@Table(name = "e_account")
@Data
@ToString
public class BloggerAccount {
    @Id
    private Long id;

    private String password;

    private Date registerDate;

    private String email;

    private String isEnabled;
}
