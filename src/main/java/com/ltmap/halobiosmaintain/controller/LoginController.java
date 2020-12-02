package com.ltmap.halobiosmaintain.controller;

import com.ltmap.halobiosmaintain.common.result.Response;
import com.ltmap.halobiosmaintain.common.utils.ShiroUtils;
import com.ltmap.halobiosmaintain.config.Constant;
import com.ltmap.halobiosmaintain.entity.main.UserInfo;
import com.ltmap.halobiosmaintain.service.ISysLogService;
import com.ltmap.halobiosmaintain.service.IUserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.logging.log4j.util.Strings;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录相关Controller
 */

@RestController
@Api(tags = "登录相关Controller")
@RequestMapping("/base")
public class LoginController {
    @Resource
    private ISysLogService sysLogService;
    @Resource
    private IUserInfoService userInfoService;

    /**
     * 退出登录
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation(value = "退出登录")
    public Response logout(){
        if(ShiroUtils.isLogin()){
            sysLogService.saveLog(ShiroUtils.getUserId(), Constant.loginAndLogout,"退出");
            ShiroUtils.logout();
            return Response.ok();
        }else{
            return Response.fail("未登录，无法退出！");
        }
    }

    /**
     * 登录验证
     */
    @ApiOperation("登录验证")
    @PostMapping(value = "/login")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username",value = "用户名",required = true),
            @ApiImplicitParam(name = "password",value = "密码",required = true)
    })
    public Response<Map<String, Serializable>> loginCheck(String username, String password) {
        if (Strings.isBlank(username)) {
            return Response.fail("请输入用户名！");
        }
        if (Strings.isBlank(password)) {
            return Response.fail("请输入密码！");
        }
/*        String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
        if (!captcha.equalsIgnoreCase(kaptcha)) {
            return Res.error("验证码不正确");
        }
         对密码进行加密解密
        try {
            password = AESUtil.decrypt(password, config.getAesKey());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        try {
            Subject subject = ShiroUtils.getSubject();
            // sha256加密
            // password = MD5Util.encode(password);
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            subject.login(token);
            sysLogService.saveLog(ShiroUtils.getUserId(), Constant.loginAndLogout,"登录");
        } catch (UnknownAccountException e) {
            return Response.fail("用户名错误");
        } catch (IncorrectCredentialsException e) {
            return Response.fail("密码错误!!");
        } catch (LockedAccountException e) {
            return Response.fail("用户被锁定");
        } catch (AuthenticationException e) {
            return Response.fail("账户验证失败");
        }

        //token信息
        Subject subject = SecurityUtils.getSubject();
        Serializable tokenId = subject.getSession().getId();
        //用户角色权限信息
        UserInfo userInfo = (UserInfo) SecurityUtils.getSubject().getPrincipal();
        UserInfo userRoles = userInfoService.findRolesByUserName(userInfo.getUserName());

        //用于当该用户无角色权限信息时回显登录名
        if(ObjectUtils.isEmpty(userRoles)){
            userRoles = new UserInfo();
            userRoles.setUserName(username);
        }

        Map<String, Serializable> authTokenMap = new HashMap<>();
        authTokenMap.put("authToken",tokenId);
        authTokenMap.put("userInfo",userRoles);
        return Response.ok(authTokenMap);
    }
}
