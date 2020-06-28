package com.gc.system.pojo.dto.user;

import com.gc.system.constants.UserTypeConstants;
import com.gc.validate.common.constraints.Contain;
import com.gc.validate.common.constraints.Mobile;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author shizhongming
 * 2020/6/2 4:48 下午
 */
@Getter
@Setter
@ToString
public class UserSaveUpdateDTO implements Serializable {

    private static final long serialVersionUID = 8790839450326558559L;
    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空")
    private String username;

    /**
     * 姓名
     */
    @NotNull(message = "姓名不能为空")
    private String realname;


    /**
     * 邮箱
     */
    @Email(message = "email格式错误")
    private String email;

    /**
     * 手机
     */
    @Mobile(message = "手机号码格式错误")
    private String mobile;

    /**
     * 电话
     */
    private String telephone;

    /**
     * 性别
     */
    @NotNull(message = "性别不能为空")
    private Integer sex;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 部门ID
     */
    private Integer deptId;

    /**
     * 状态（10：启用，20：禁用）
     */
    private String status;

    /**
     * 用户类型（10：系统用户，20：业务用户）
     */
    @Contain(message = "用户类型错误", allowClass = UserTypeConstants.class)
    @NotNull(message = "用户类型不能为空")
    private String userType;


    /**
     * 职务ID
     */
    private Integer postId;

    /**
     * 工号
     */
    private String workNo;

    /**
     * 序号
     */
    private Integer seq;
}
