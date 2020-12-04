package com.ltmap.halobiosmaintain.config.shiro;

import com.ltmap.halobiosmaintain.entity.main.UserInfo;
import com.ltmap.halobiosmaintain.service.IUserInfoService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;

/**
 * 自定义Realm
 * @author fjh
 * @date 2020/11/14 9:26
 */
public class UserRealm extends AuthorizingRealm {
    @Resource
    private IUserInfoService userInfoService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(username);
        // 查询用户信息
        //Res res = null;
        UserInfo user = userInfoService.findByUserName(username);
        //SysUser user = (SysUser)res.get("data");
        // 账号不存在
        if (user == null) {
            throw new UnknownAccountException("账号或密码不正确");
        }

        // 密码错误
        if (!password.equals(user.getPassword())) {
            throw new IncorrectCredentialsException("账号或密码不正确");
        }

        // 账号锁定
       /* if (user.getStatus() == 0) {
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }*/

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, getName());
        return info;
    }
}
