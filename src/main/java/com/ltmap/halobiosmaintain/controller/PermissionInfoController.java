package com.ltmap.halobiosmaintain.controller;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ltmap.halobiosmaintain.common.result.Response;
import com.ltmap.halobiosmaintain.common.result.Responses;
import com.ltmap.halobiosmaintain.entity.main.PermissionInfo;
import com.ltmap.halobiosmaintain.entity.main.RolePermission;
import com.ltmap.halobiosmaintain.service.IPermissionInfoService;
import com.ltmap.halobiosmaintain.service.IRolePermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Niko
 * @since 2020-11-21
 */
@Api(tags = "权限信息表")
@RestController
@RequestMapping("/permissionInfo")
public class PermissionInfoController {
    @Resource
    private IPermissionInfoService permissionInfoService;
    @Resource
    private IRolePermissionService rolePermissionService;


//    @ApiOperation("权限信息查询")
//    @PostMapping("/info")
//    public Response<List<PermissionInfo>> info(String permissionCode, String permissionCodeName) {
//        return Responses.or(permissionInfoService.info(permissionCode,permissionCodeName));
//    }
//
//    @ApiOperation("权限信息入库")
//    @PostMapping("/save")
//    public Response save(PermissionInfo permission) {
//        return Responses.or(permissionInfoService.save(permission));
//    }
//
//    @ApiOperation("权限信息修改")
//    @PostMapping("/edit")
//    public Response edit(PermissionInfo permission) {
//        return Responses.or(permissionInfoService.updateById(permission));
//    }
//
//    @ApiOperation("权限信息删除")
//    @PostMapping("/delete")
//    public Response delete(String id){
//        return Responses.or(permissionInfoService.delete(id));
//    }

    /**
     * 为角色分配权限
     * @param rolePermissionList
     * @return
     */
    @PostMapping("addPermForRole")
    @ApiOperation(value = "为角色分配权限")
    public Response addPermForRole(@RequestBody List<RolePermission> rolePermissionList){
        Boolean allocateFlag=false;
        //分配权限
        if(CollectionUtils.isNotEmpty(rolePermissionList)){

            //分配前先将原先的权限清除
            String roleId = rolePermissionList.get(0).getRoleId();
            Boolean deleteFlag = rolePermissionService.deleteByRoleId(roleId);
            if(!deleteFlag){
                return Response.fail("权限分配失败，请重试");
            }

            allocateFlag = rolePermissionService.allocatePermForRole(rolePermissionList);
        }
        return Responses.or(allocateFlag);
    }

    /**
     * 查询某一角色对应权限信息
     * @param roleId
     * @return
     */
    @ApiOperation(value = "查询某一角色对应权限信息")
    @PostMapping("queryPermByRoleId")
    public Response<List<Map<String,Object>>> queryPermByRoleId(String roleId){
        List<Map<String, Object>> list = permissionInfoService.queryPermByRoleId(roleId);
        return Responses.or(list);
    }

    /**
     * 查询所有权限信息
     * @return
     */
    @ApiOperation(value = "查询所有权限信息")
    @PostMapping("queryAllPerm")
    public Response<List<Map<String,Object>>> queryAllPerm(){
        List<Map<String, Object>> list = permissionInfoService.queryAllPerm();
        return Responses.or(list);
    }
}

