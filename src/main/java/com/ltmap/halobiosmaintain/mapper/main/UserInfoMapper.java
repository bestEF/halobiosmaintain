package com.ltmap.halobiosmaintain.mapper.main;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltmap.halobiosmaintain.entity.main.UserInfo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author Niko
 * @since 2020-09-14
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    /**
     * 根据用户名查询用户角色权限信息
     * @param userName
     */
    UserInfo findRolesByUserName(@Param("userName") String userName);

    /*
     * @Description:
     * @Param id:
     * @Return: java.util.List<com.ltmap.entity.main.UserInfo>
     * @Author: Niko
     * @Date: 2020/9/17 18:02
     */
    IPage<UserInfo> info_all(Page page, @Param("userId") String userId, @Param("userName") String userName);

}
