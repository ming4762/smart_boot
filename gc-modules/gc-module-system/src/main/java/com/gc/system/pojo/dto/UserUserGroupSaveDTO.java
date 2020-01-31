package com.gc.system.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author jackson
 * 2020/1/30 2:03 下午
 */
@ToString
@Getter
@Setter
@ApiModel("用户所在用户组参数实体")
public class UserUserGroupSaveDTO implements Serializable {
    private static final long serialVersionUID = -6508219832444270076L;

    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @ApiModelProperty(value = "用户组ID", required = true)
    @NotNull(message = "用户组ID不能为空")
    @Length(min = 1, message = "用户组ID不能为空")
    private List<Long> groupIdList;
}
