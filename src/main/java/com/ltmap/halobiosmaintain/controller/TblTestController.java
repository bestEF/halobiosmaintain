package com.ltmap.halobiosmaintain.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  测试前端控制器
 * </p>
 *
 * @author fjh
 * @since 2020-11-13
 */
@Controller
@RequestMapping("/tblTest")
@Api(tags = "测试前端控制器")
public class TblTestController {

    @PostMapping("tblTestController")
    @ResponseBody
    @ApiOperation(value = "测试处理器")
    @ApiImplicitParam(name = "count",value = "数量",dataType = "int",required = true)
    public String testController(Integer count){
        return "adfsfa";
    }
}

