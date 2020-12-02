package com.ltmap.halobiosmaintain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ltmap.halobiosmaintain.entity.main.RoleInfo;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务类
 * </p>
 *
 * @author Niko
 * @since 2020-09-14
 */
public interface IRoleInfoService extends IService<RoleInfo> {
    /**
     * 根据userId获取用户角色
     * @param userId
     * @return
     */
    List<RoleInfo> queryRolesByUserId(String userId);

    IPage<RoleInfo> info(Integer current, Integer size, String id, String roleName);

    boolean delete(String id);

    boolean insert(RoleInfo roleInfo);

    boolean edit(RoleInfo roleInfo);
}
