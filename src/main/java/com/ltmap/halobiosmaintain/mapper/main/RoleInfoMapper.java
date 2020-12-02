package com.ltmap.halobiosmaintain.mapper.main;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ltmap.halobiosmaintain.entity.main.PermissionInfo;
import com.ltmap.halobiosmaintain.entity.main.RoleInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色信息表 Mapper 接口
 * </p>
 *
 * @author Niko
 * @since 2020-09-14
 */
public interface RoleInfoMapper extends BaseMapper<RoleInfo> {

    /**
     * 根据角色Id获取权限信息
     * @param roleId
     * @return
     */
    List<PermissionInfo> findPermsByRoleId(@Param("roleId") String roleId);

    /**
     * 根据userId获取用户角色
     * @param userId
     * @return
     */
    List<RoleInfo> queryRolesByUserId(@Param("userId") String userId);
}
