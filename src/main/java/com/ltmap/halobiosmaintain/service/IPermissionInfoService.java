package com.ltmap.halobiosmaintain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ltmap.halobiosmaintain.entity.main.PermissionInfo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Niko
 * @since 2020-11-21
 */
public interface IPermissionInfoService extends IService<PermissionInfo> {

    List<PermissionInfo> info(String permissionCode, String permissionName);

    boolean delete(String id);

    boolean insert(PermissionInfo permission);

    boolean edit(PermissionInfo permission);

    /**
     * 查询所有权限信息
     * @return
     */
    List<Map<String,Object>> queryAllPerm();

    /**
     * 查询某一角色对应权限信息
     * @param roleId
     * @return
     */
    List<Map<String, Object>> queryPermByRoleId(String roleId);
}
