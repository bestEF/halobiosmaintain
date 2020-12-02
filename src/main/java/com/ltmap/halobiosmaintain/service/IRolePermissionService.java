package com.ltmap.halobiosmaintain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ltmap.halobiosmaintain.entity.main.RolePermission;

import java.util.List;

/**
 * <p>
 * 角色权限关联表 服务类
 * </p>
 *
 * @author Niko
 * @since 2020-09-14
 */
public interface IRolePermissionService extends IService<RolePermission> {
    IPage<RolePermission> info(Integer current, Integer size, String roleId, String roleName);

    boolean delete(String roleId);

    boolean insert(RolePermission rolePermission);

    boolean edit(RolePermission rolePermission);

    boolean saveUpdate(List<RolePermission> rolePermissionList);

    /**
     * 根据角色id查询该角色权限中间表
     * @param roleId
     * @return
     */
    List<RolePermission> queyRolePerm(String roleId);

    /**
     * 根据角色id清除该角色对应的权限信息
     * @param roleId
     * @return
     */
    Boolean deleteByRoleId(String roleId);

    /**
     * 分配权限
     * @param rolePermissionList
     * @return
     */
    Boolean allocatePermForRole(List<RolePermission> rolePermissionList);
}
