package com.gc.system.pojo.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author shizhongming
 * 2020/9/24 9:50 下午
 */
@Getter
@Setter
@ToString
public class UserSetRoleDTO implements Serializable {
    private static final long serialVersionUID = -3857476896362551942L;

    @NotNull(message = "用户Id不能为空")
    private Long userId;

    @NotNull(message = "角色ID不能为空")
    private List<Long> roleIdList;
}
