package com.ltmap.halobiosmaintain.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;

import com.ltmap.halobiosmaintain.common.result.Response;
import com.ltmap.halobiosmaintain.common.result.Responses;
import com.ltmap.halobiosmaintain.entity.main.RoleInfo;
import com.ltmap.halobiosmaintain.service.impl.RoleInfoServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 角色信息表 前端控制器
 * </p>
 *
 * @author Niko
 * @since 2020-09-14
 */
@Api(tags = "角色信息")
@RestController
@RequestMapping("/roleInfo")
public class RoleInfoController {
    private final RoleInfoServiceImpl roleInfoServiceImpl ;
    @Autowired
    public RoleInfoController(RoleInfoServiceImpl roleInfoServiceImpl) {
        this.roleInfoServiceImpl = roleInfoServiceImpl;
    }

    @ApiOperation("角色信息查询")
    @PostMapping("/info")
    public Response<IPage<RoleInfo>> info(@RequestParam(value="current",defaultValue = "1") Integer current,
                                          @RequestParam(value="size",defaultValue = "10")Integer size,
                                          String id, String roleName) {
        return Responses.or(roleInfoServiceImpl.info(current, size,id,roleName));
    }

    @ApiOperation("角色信息入库")
    @PostMapping("/save")
    public Response save(RoleInfo roleInfo) {
        return Responses.or(roleInfoServiceImpl.save(roleInfo));
    }

    @ApiOperation("角色信息修改")
    @PostMapping("/edit")
    public Response edit(RoleInfo roleInfo) {
        return Responses.or(roleInfoServiceImpl.updateById(roleInfo));
    }

    @ApiOperation("角色信息删除")
    @PostMapping("/delete")
    public Response delete(String id){
        return Responses.or(roleInfoServiceImpl.delete(id));
    }
}

