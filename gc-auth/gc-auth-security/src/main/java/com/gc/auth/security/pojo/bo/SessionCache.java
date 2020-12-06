package com.gc.auth.security.pojo.bo;

import com.gc.common.auth.model.RestUserDetailsImpl;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author shizhongming
 * 2020/2/15 12:08 下午
 */
public class SessionCache implements Serializable {
    private static final long serialVersionUID = -5579023650416568094L;

    @Getter
    private RestUserDetailsImpl user;

    public SessionCache(RestUserDetailsImpl user) {
        this.user = user;
    }
}
