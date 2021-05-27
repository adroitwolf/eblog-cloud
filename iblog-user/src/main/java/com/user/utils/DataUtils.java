package com.user.utils;

import com.alibaba.fastjson.JSONObject;
import com.common.entity.pojo.BloggerRole;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>DataUtils</pre>
 *  读取静态资源
 * @author <p>ADROITWOLF</p> 2021-05-27
 */
@Component
public class DataUtils {
    @Value("classpath:data/role.json")
    private Resource roles;

    @PostConstruct
    public Map<String,Long> loadRoleJson(){
        try {
            String json = IOUtils.toString(roles.getInputStream());
            Map<String,Long> result = new HashMap<>();
            JSONObject temp = JSONObject.parseObject(json);
            String roles = temp.getJSONArray("roles").toString();
            List<BloggerRole> list = JSONObject.parseArray(roles,BloggerRole.class);

            list.forEach(role->{
                result.put(role.getRoleName(), role.getId());
            });

            return result;

        } catch (IOException e) {
            return Collections.emptyMap();
        }
    }

}
