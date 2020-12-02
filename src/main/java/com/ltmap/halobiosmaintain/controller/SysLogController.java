package com.ltmap.halobiosmaintain.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.ltmap.halobiosmaintain.common.result.Response;
import com.ltmap.halobiosmaintain.common.result.Responses;
import com.ltmap.halobiosmaintain.service.ISysLogService;
import com.ltmap.halobiosmaintain.vo.resp.SysLogResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 系统日志表 前端控制器
 * </p>
 *
 * @author Niko
 * @since 2020-09-14
 */
@RestController
@RequestMapping("/sysLog")
@Api(tags = "系统日志")
public class SysLogController {
    @Autowired
    private ISysLogService sysLogService;

    /**
     * 批量删除日志
     * @param ids
     * @return
     */
    @PostMapping("/deleteSysLog")
    @ApiOperation("批量删除日志")
    @ApiImplicitParam(name = "ids",value = "一组id字符串用逗号分隔",required = true)
    public Response deleteSysLog(String ids){
        String[] idArray = ids.split(",");
        List<String> listId = Arrays.asList(idArray);
        return Responses.or(sysLogService.removeByIds(listId));
    }

    /**
     * 清空所有日志 不再使用 可以删除
     * @return
     */
    /*@PostMapping("/clearSysLog")
    @ApiOperation(value = "清空所有日志")
    public Response clearSysLog(){
        return Response.ok();
    }*/

    /**
     * 查询日志
     * @return
     */
    @PostMapping("/listSysLog")
    @ApiOperation(value = "查询所有日志")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current",value = "当前页",required = false),
            @ApiImplicitParam(name = "size",value = "每页显示数量",required = false),
            @ApiImplicitParam(name = "userName",value = "调用人",required = false),
            @ApiImplicitParam(name = "startDate",value = "查询开始时间",required = false),
            @ApiImplicitParam(name = "endDate",value = "查询结束时间",required = false)
    })
    public Response<IPage<SysLogResp>> listSysLog(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            String userName,
            String startDate,
            String endDate){
        return Responses.or(sysLogService.listSysLog(
                current,size,
                userName,
                startDate,
                endDate));
    }
}

