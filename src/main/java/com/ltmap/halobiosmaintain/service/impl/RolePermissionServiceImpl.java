package com.ltmap.halobiosmaintain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ltmap.halobiosmaintain.entity.main.RolePermission;
import com.ltmap.halobiosmaintain.mapper.main.RolePermissionMapper;
import com.ltmap.halobiosmaintain.service.IRolePermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 角色权限关联表 服务实现类
 * </p>
 *
 * @author Niko
 * @since 2020-09-14
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements IRolePermissionService {
    @Resource
    private RolePermissionMapper rolePermissionMapper;

    /**
     * 分配权限
     * @param rolePermissionList
     * @return
     */
    public Boolean allocatePermForRole(List<RolePermission> rolePermissionList){
        //添加角色权限关联信息
        boolean bool=false;
        for (int i = 0; i <rolePermissionList.size() ; i++) {
            bool=save(rolePermissionList.get(i));
        }
        return bool;
    }

    /**
     * 根据角色id清除该角色对应的权限信息
     * @param roleId
     * @return
     */
    public Boolean deleteByRoleId(String roleId){
        LambdaQueryWrapper<RolePermission> lqw = Wrappers.lambdaQuery();
        lqw.eq(RolePermission::getRoleId,roleId);

        int count = count(lqw);
        if(count==0){
            return true;
        }

        int deleteCount = rolePermissionMapper.delete(lqw);
        if(deleteCount==0){
            return false;
        }else {
            return true;
        }
    }

    //根据角色id查询该角色权限中间表
    public List<RolePermission> queyRolePerm(String roleId){
        LambdaQueryWrapper<RolePermission> lqw = Wrappers.lambdaQuery();
        lqw.eq(RolePermission::getRoleId,roleId);
        List<RolePermission> permissionList = rolePermissionMapper.selectList(lqw);
        return permissionList;
    }

    /*
     * @Description:用查角色权限信息
     * @Param current:
     * @Param size:
     * @Param userName:
     * @Param realName:
     * @Return: com.baomidou.mybatisplus.core.metadata.IPage<com.ltmap.entity.main.UserInfo>
     * @Author: Niko
     * @Date: 2020/9/14 19:36
     */
    @Override
    public IPage<RolePermission> info(Integer current, Integer size, String roleId, String permissionCode) {
        LambdaQueryWrapper<RolePermission> lambdaQueryWrapper = Wrappers.lambdaQuery();
        //lambdaQueryWrapper.eq(!Strings.isNullOrEmpty(roleId), RolePermission::getRoleId, roleId).eq(!Strings.isNullOrEmpty(permissionCode), RolePermission::getPermissionCode, permissionCode);
        Page<RolePermission> page = new Page<>(current, size);
        return rolePermissionMapper.selectPage(page, lambdaQueryWrapper);
    }

    /*
     * @Description:删除角色权限信息
     * @Param id:
     * @Return: boolean
     * @Author: Niko
     * @Date: 2020/9/14 18:36
     */
    @Override
    public boolean delete(String roleId) {
        return removeById(roleId);
    }

    /*
     * @Description:保存角色权限信息
     * @Param dataQualityControl:
     * @Return: boolean
     * @Author: Niko
     * @Date: 2020/9/14 18:06
     */
    @Override
    public boolean insert(RolePermission rolePermission) {
        return save(rolePermission);
    }

    /*
     * @Description:修改角色权限信息
     * @Param dataQualityControl:
     * @Return: boolean
     * @Author: Niko
     * @Date: 2020/9/14 18:06
     */
    @Override
    public boolean edit(RolePermission rolePermission) {
        return updateById(rolePermission);
    }

    /*
     * @Description:批量插入或更新角色权限关联表
     * @Param rolePermissionList:
     * @Return: boolean
     * @Author: Niko
     * @Date: 2020/9/15 9:25
     */
    @Override
    public boolean saveUpdate(List<RolePermission> rolePermissionList) {
        //删除角色权限关联信息
        for (int i = 0; i <rolePermissionList.size() ; i++) {
            rolePermissionMapper.deleteRolePermission(rolePermissionList.get(i).getRoleId());
        }
        //添加角色权限关联信息
        boolean bool=false;
        for (int i = 0; i <rolePermissionList.size() ; i++) {
            bool=save(rolePermissionList.get(i));
        }
        return bool;
    }
}
