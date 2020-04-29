package com.gc.common.auth.model;

import lombok.*;

/**
 * 权限信息
 * @author shizhongming
 * 2020/4/29 10:55 上午
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Permission {

    /**
     * 请求方法
     */
    private String method;

    private String url;

    private String permission;
}
