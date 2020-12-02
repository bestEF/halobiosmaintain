package com.ltmap.halobiosmaintain.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltmap.halobiosmaintain.common.result.Response;
import com.ltmap.halobiosmaintain.common.result.Responses;
import com.ltmap.halobiosmaintain.entity.main.UserInfo;
import com.ltmap.halobiosmaintain.service.impl.UserInfoServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author Niko
 * @since 2020-09-14
 */
@Api(tags = "用户信息")
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
private final UserInfoServiceImpl userInfoServiceIpl ;
    @Autowired
    public UserInfoController(UserInfoServiceImpl userInfoServiceIpl) {
        this.userInfoServiceIpl = userInfoServiceIpl;
    }

    @ApiOperation("用户信息查询")
    @PostMapping("/info")
    public Response<IPage<UserInfo>> info(@RequestParam(value="current",defaultValue = "1") Integer current,
                                          @RequestParam(value="size",defaultValue = "10")Integer size,
                                          String userId, String userName) {
        return Responses.or(userInfoServiceIpl.info_all(current, size,userId,userName));
    }
//    @ApiOperation("用户信息详情")
//    @PostMapping("/info_detail")
//    public Response<List<UserInfo>> info(String userId) {
//        return Responses.or(userInfoServiceIpl.info_detail(userId));
//    }

    @ApiOperation("用户信息入库")
    @PostMapping("/save")
    public Response save(UserInfo userInfo) {
        return Responses.or(userInfoServiceIpl.save(userInfo));
    }

    @ApiOperation("用户信息修改")
    @PostMapping("/edit")
    public Response edit(UserInfo userInfo) {
        return Responses.or(userInfoServiceIpl.updateById(userInfo));
    }

    @ApiOperation("用户信息删除")
    @PostMapping("/delete")
    public Response delete(String id){
        return Responses.or(userInfoServiceIpl.delete(id));
    }
}

