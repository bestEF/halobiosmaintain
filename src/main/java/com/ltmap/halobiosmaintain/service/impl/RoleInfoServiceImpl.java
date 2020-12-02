package com.ltmap.halobiosmaintain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.ltmap.halobiosmaintain.entity.main.RoleInfo;
import com.ltmap.halobiosmaintain.mapper.main.RoleInfoMapper;
import com.ltmap.halobiosmaintain.service.IRoleInfoService;
import com.ltmap.halobiosmaintain.service.IRolePermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author Niko
 * @since 2020-09-14
 */
@Service
public class RoleInfoServiceImpl extends ServiceImpl<RoleInfoMapper, RoleInfo> implements IRoleInfoService {
    @Resource
    private RoleInfoMapper roleInfoMapper;
    @Resource
    private IRolePermissionService rolePermissionService;

    /**
     * 根据userId获取用户角色
     * @param userId
     * @return
     */
    public List<RoleInfo> queryRolesByUserId(String userId){
        List<RoleInfo> roleInfos = roleInfoMapper.queryRolesByUserId(userId);
        return roleInfos;
    }

    /*
     * @Description:用查角色信息
     * @Param current:
     * @Param size:
     * @Param userName:
     * @Param realName:
     * @Return: com.baomidou.mybatisplus.core.metadata.IPage<com.ltmap.entity.main.UserInfo>
     * @Author: Niko
     * @Date: 2020/9/14 19:36
     */
    @Override
    public IPage<RoleInfo> info(Integer current, Integer size, String id, String roleName) {
        LambdaQueryWrapper<RoleInfo> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(!Strings.isNullOrEmpty(id), RoleInfo::getRoleId, id).eq(!Strings.isNullOrEmpty(roleName), RoleInfo::getRoleName, roleName);
        Page<RoleInfo> page = new Page<>(current, size);
        return roleInfoMapper.selectPage(page, lambdaQueryWrapper);
    }

    /*
     * @Description:删除角色信息
     * @Param id:
     * @Return: boolean
     * @Author: Niko
     * @Date: 2020/9/14 18:36
     */
    @Override
    public boolean delete(String id) {

        //分配前先将原先的权限清除
        Boolean deleteFlag = rolePermissionService.deleteByRoleId(id);
        if(!deleteFlag){
            return deleteFlag;
        }

        return removeById(id);
    }

    /*
     * @Description:保存角色信息
     * @Param dataQualityControl:
     * @Return: boolean
     * @Author: Niko
     * @Date: 2020/9/14 18:06
     */
    @Override
    public boolean insert(RoleInfo roleInfo) {
        return save(roleInfo);
    }

    /*
     * @Description:修改角色信息
     * @Param dataQualityControl:
     * @Return: boolean
     * @Author: Niko
     * @Date: 2020/9/14 18:06
     */
    @Override
    public boolean edit(RoleInfo roleInfo) {
        return updateById(roleInfo);
    }
}
