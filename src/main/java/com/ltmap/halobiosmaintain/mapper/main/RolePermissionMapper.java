package com.ltmap.halobiosmaintain.mapper.main;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ltmap.halobiosmaintain.entity.main.RolePermission;

/**
 * <p>
 * 角色权限关联表 Mapper 接口
 * </p>
 *
 * @author Niko
 * @since 2020-09-14
 */
public interface RolePermissionMapper extends BaseMapper<RolePermission> {
    boolean deleteRolePermission(String roleId);
}
