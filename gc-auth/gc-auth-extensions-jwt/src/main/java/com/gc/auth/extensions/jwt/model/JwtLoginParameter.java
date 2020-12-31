package com.gc.auth.extensions.jwt.model;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author ShiZhongMing
 * 2020/12/31 16:48
 * @since 1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtLoginParameter implements Serializable {
    private static final long serialVersionUID = -45881808856568247L;

    private String username;

    private String password;

    private Boolean remember;

    public boolean getRemember() {
        if (Objects.isNull(remember)) {
            remember = false;
        }
        return remember;
    }
}
