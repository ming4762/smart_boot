package com.gc.auth.security.pojo.bo;

import com.gc.common.auth.model.RestUserDetails;
import com.google.common.collect.Maps;
import lombok.Getter;

import java.io.Serializable;
import java.util.Map;

/**
 * @author jackson
 * 2020/2/15 12:08 下午
 */
public class SessionCache implements Serializable {
    private static final long serialVersionUID = -5579023650416568094L;

    @Getter
    private RestUserDetails user;

    private Map<Object, Object> attribute = Maps.newConcurrentMap();

    public SessionCache(RestUserDetails user) {
        this.user = user;
    }
}
