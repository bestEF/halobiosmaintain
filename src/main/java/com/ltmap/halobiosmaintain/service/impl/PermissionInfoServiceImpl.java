package com.ltmap.halobiosmaintain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.ltmap.halobiosmaintain.entity.main.RolePermission;
import com.ltmap.halobiosmaintain.entity.main.PermissionInfo;
import com.ltmap.halobiosmaintain.mapper.main.PermissionInfoMapper;
import com.ltmap.halobiosmaintain.service.IPermissionInfoService;
import com.ltmap.halobiosmaintain.service.IRolePermissionService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Niko
 * @since 2020-11-21
 */
@Service
public class PermissionInfoServiceImpl extends ServiceImpl<PermissionInfoMapper, PermissionInfo> implements IPermissionInfoService {
    @Resource
    private PermissionInfoMapper permissionInfoMapper;
    @Resource
    private IRolePermissionService rolePermissionService;


    /*
     * @Description:查权限信息
     * @Param current:
     * @Param size:
     * @Param userName:
     * @Param realName:
     * @Return: com.baomidou.mybatisplus.core.metadata.IPage<com.ltmap.entity.main.UserInfo>
     * @Author: Niko
     * @Date: 2020/9/14 19:36
     */
    @Override
    public List<PermissionInfo> info(String permissionCode, String permissionName) {
        LambdaQueryWrapper<PermissionInfo> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(!Strings.isNullOrEmpty(permissionCode), PermissionInfo::getPermissionCode, permissionCode).eq(!Strings.isNullOrEmpty(permissionName), PermissionInfo::getPermissionName, permissionName);
        return permissionInfoMapper.selectList(lambdaQueryWrapper);
    }

    /*
     * @Description:删除角权限信息
     * @Param id:
     * @Return: boolean
     * @Author: Niko
     * @Date: 2020/9/14 18:36
     */
    @Override
    public boolean delete(String id) {
        return removeById(id);
    }

    /*
     * @Description:保存权限信息
     * @Param dataQualityControl:
     * @Return: boolean
     * @Author: Niko
     * @Date: 2020/9/14 18:06
     */
    @Override
    public boolean insert(PermissionInfo permission) {
        return save(permission);
    }

    /*
     * @Description:修改权限信息
     * @Param dataQualityControl:
     * @Return: boolean
     * @Author: Niko
     * @Date: 2020/9/14 18:06
     */
    @Override
    public boolean edit(PermissionInfo permission) {
        return updateById(permission);
    }


    //查询某一角色对应权限信息
    @Override
    public List<Map<String, Object>> queryPermByRoleId(String roleId){
        //查询所有权限信息
        LambdaQueryWrapper<PermissionInfo> lambdaQueryWrapper = Wrappers.lambdaQuery();
        List<PermissionInfo> permissionList = permissionInfoMapper.selectList(lambdaQueryWrapper);

        //根据角色id查询该角色权限中间表
        List<RolePermission> rolePermissionList = rolePermissionService.queyRolePerm(roleId);

        //用于存放权限树
        List<Map<String,Object>> permList = new ArrayList<>();

        //判断该角色有无权限信息 没有直接返回
        if(CollectionUtils.isEmpty(rolePermissionList)){
            return permList;
        }

        //存放某一父节点及其对应子节点
        for (PermissionInfo permissionF : permissionList) {

            //查询第一级节点 判断当前节点是否为顶级父节点 0代表顶级父节点
            if(permissionF.getParent().equals("0")){
                //用于存放某一父节点及其子节点
                Map<String, Object> map = new HashMap<>();
                map.put("permissionId",permissionF.getPermissionId());
                map.put("label",permissionF.getPermissionName());
                map.put("name",permissionF.getPermissionCode());
                map.put("order",permissionF.getPermissionOrder());
                map.put("icon","default");

                //查询第二级节点
                List<Object> childrenList = new ArrayList<>();
                for (PermissionInfo permissionS : permissionList) {
                    if(permissionS.getParent().equals(permissionF.getPermissionId())){
                        //存放子节点
                        Map<Object, Object> mapSecod = new HashMap<>();
                        mapSecod.put("permissionId",permissionS.getPermissionId());
                        mapSecod.put("label",permissionS.getPermissionName());
                        mapSecod.put("name",permissionS.getPermissionCode());
                        mapSecod.put("order",permissionS.getPermissionOrder());
                        mapSecod.put("path","/"+permissionF.getPermissionCode()+"/"+permissionS.getPermissionCode());

                        //查询第三级节点
                        //存放具体操作权限
                        List<Map<String, Object>> operateList = new ArrayList<>();
                        for (PermissionInfo permissionT : permissionList) {
                            if(permissionT.getParent().equals(permissionS.getPermissionId())){
                                for (RolePermission rolePermission:rolePermissionList){
                                    if(permissionT.getPermissionId().equals(rolePermission.getPermissionId())){
                                        Map<String, Object> operateMap = new HashMap<>();
                                        operateMap.put("permissionId",permissionT.getPermissionId());
                                        operateMap.put("name",permissionT.getPermissionCode());
                                        operateMap.put("order",permissionT.getPermissionOrder());
                                        operateMap.put("label",permissionT.getPermissionName());
                                        operateList.add(operateMap);
                                    }
                                }
                            }
                        }

                        //判断三级节点是否为空 如果为空 那么再判断二级节点是否为该角色权限
                        if(CollectionUtils.isNotEmpty(operateList)){
                            mapSecod.put("operate",operateList);
                            childrenList.add(mapSecod);
                        }else {
                            for (RolePermission rolePermission:rolePermissionList){
                                if(mapSecod.get("permissionId").equals(rolePermission.getPermissionId())){
                                    mapSecod.put("operate",operateList);
                                    childrenList.add(mapSecod);
                                }
                            }
                        }

                    }
                }

                //判断二级节点是否为空 如果为空 那么再判断一级节点是否为该角色权限
                if(CollectionUtils.isNotEmpty(childrenList)){
                    map.put("children",childrenList);
                    permList.add(map);
                }else {
                    for (RolePermission rolePermission:rolePermissionList){
                        if(map.get("permissionId").equals(rolePermission.getPermissionId())){
                            map.put("children",childrenList);
                            permList.add(map);
                        }
                    }
                }

            }
        }

        return permList;

    }

    //查询所有权限信息
    @Override
    public List<Map<String,Object>> queryAllPerm() {
        LambdaQueryWrapper<PermissionInfo> lambdaQueryWrapper = Wrappers.lambdaQuery();
        List<PermissionInfo> permissionList = permissionInfoMapper.selectList(lambdaQueryWrapper);

        //用于存放权限树
        List<Map<String,Object>> permList = new ArrayList<>();

        //存放某一父节点及其对应字节点
        for (PermissionInfo permissionF : permissionList) {

            //查询第一级节点 判断当前节点是否为顶级父节点 0代表顶级父节点
            if(permissionF.getParent().equals("0")){
                //用于存放某一父节点及其子节点
                Map<String, Object> map = new HashMap<>();
                map.put("permissionId",permissionF.getPermissionId());
                map.put("label",permissionF.getPermissionName());
                map.put("name",permissionF.getPermissionCode());
                map.put("order",permissionF.getPermissionOrder());
                map.put("icon","default");

                //查询第二级节点
                List<Object> childrenList = new ArrayList<>();
                for (PermissionInfo permissionS : permissionList) {
                    if(permissionS.getParent().equals(permissionF.getPermissionId())){
                        //存放子节点
                        Map<Object, Object> mapSecod = new HashMap<>();
                        mapSecod.put("permissionId",permissionS.getPermissionId());
                        mapSecod.put("label",permissionS.getPermissionName());
                        mapSecod.put("name",permissionS.getPermissionCode());
                        mapSecod.put("order",permissionS.getPermissionOrder());
                        mapSecod.put("path","/"+permissionF.getPermissionCode()+"/"+permissionS.getPermissionCode());

                        //查询第三级节点
                        //存放具体操作权限
                        List<Map<String, Object>> operateList = new ArrayList<>();
                        for (PermissionInfo permissionT : permissionList) {
                            if(permissionT.getParent().equals(permissionS.getPermissionId())){
                                Map<String, Object> operateMap = new HashMap<>();
                                operateMap.put("permissionId",permissionT.getPermissionId());
                                operateMap.put("name",permissionT.getPermissionCode());
                                operateMap.put("label",permissionT.getPermissionName());
                                operateMap.put("order",permissionT.getPermissionOrder());
                                operateList.add(operateMap);
                            }
                        }
                        mapSecod.put("operate",operateList);
                        childrenList.add(mapSecod);
                    }
                    map.put("children",childrenList);
                }
                permList.add(map);
            }
        }
        return permList;
    }
}
