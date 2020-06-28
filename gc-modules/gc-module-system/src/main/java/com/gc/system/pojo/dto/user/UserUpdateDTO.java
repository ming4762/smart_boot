package com.gc.system.pojo.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @author shizhongming
 * 2020/6/2 4:50 下午
 */
@Getter
@Setter
@ToString
public class UserUpdateDTO extends UserSaveUpdateDTO {
    private static final long serialVersionUID = -8473696763380911595L;

    @NotNull(message = "用户ID不能为空")
    private String userId;
}
