package com.gc.auth.extensions.jwt.context;

import lombok.*;

/**
 * @author ShiZhongMing
 * 2020/12/31 16:16
 * @since 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtContext {

    private String loginUrl;

    private String logoutUrl;
}
