package com.ltmap.halobiosmaintain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ltmap.halobiosmaintain.entity.main.UserInfo;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author Niko
 * @since 2020-09-14
 */
public interface IUserInfoService extends IService<UserInfo> {

    /**
     * 根据用户名查询用户角色权限信息
     * @param userName
     */
    UserInfo findRolesByUserName(String userName);

    /**
     * 根据用户名获取用户信息
     * @param userName
     * @return
     */
    UserInfo findByUserName(String userName);

    IPage<UserInfo> info_all(Integer current, Integer size, String userId, String userName);

    boolean delete(String id);

    boolean insert(UserInfo userInfo);

    boolean edit(UserInfo userInfo);

}
