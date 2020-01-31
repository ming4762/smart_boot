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
 * 保存用户组的用户DTO
 * @author jackson
 * 2020/1/30 12:54 下午
 */
@ToString
@Getter
@Setter
@ApiModel(value = "保存用户组的用户实体")
public class UserGroupUserSaveDTO implements Serializable {
    private static final long serialVersionUID = 3856463051638844582L;

    @NotNull(message = "用户组ID不能为空")
    @ApiModelProperty(value = "用户组ID", required = true)
    private Long groupId;

    @NotNull(message = "用户ID不能为空")
    @Length(min = 1, message = "用户ID不能为空")
    @ApiModelProperty(value = "用户ID集合", required = true)
    private List<Long> userIdList;
}
