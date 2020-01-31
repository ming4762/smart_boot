package com.gc.common.auth.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gc.starter.crud.model.BaseModelUserTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 用户实体类
 * @author jackson
 * 2020/1/22 7:04 下午
 */
@TableName("sys_user")
@Getter
@Setter
@ToString
public class SysUserPO extends BaseModelUserTime {

    private static final long serialVersionUID = -671533082313767123L;

    /**
     * 用户ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 姓名
     */
    private String realname;

    /**
     * 密码
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 电话
     */
    private String telephone;

    /**
     * 性别
     */
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
