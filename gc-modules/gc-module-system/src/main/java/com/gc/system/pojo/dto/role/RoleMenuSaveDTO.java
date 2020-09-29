package com.gc.system.pojo.dto.role;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author shizhongming
 * 2020/9/29 10:31 下午
 */
@Getter
@Setter
@ToString
public class RoleMenuSaveDTO implements Serializable {
    private static final long serialVersionUID = -4698594343302930017L;

    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    @NotNull(message = "菜单ID不能为空")
    private List<Long> functionIdList;
}
