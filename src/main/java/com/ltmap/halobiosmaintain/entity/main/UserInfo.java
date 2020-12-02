package com.ltmap.halobiosmaintain.entity.main;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author Niko
 * @since 2020-09-14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@TableName("user_info")
public class UserInfo extends Model<UserInfo> {

    private static final long serialVersionUID=1L;

    /**
     * 用户ID
     */
    @TableId(value = "user_id", type = IdType.ID_WORKER_STR)
    private String userId;

    /**
     * 角色ID
     */
    private String roleId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 登陆密码
     */
    private String password;

    /**
     * 用户部门
     */
    private String depart;

    /**
     * 联系方式
     */
    private String tel;

    /**
     * 角色名称
     */
    @TableField(exist = false)
    private String roleName;

    /**
     * 角色集合
     */
    @TableField(exist = false)
    private List<RoleInfo> roles=new ArrayList<>();
}
