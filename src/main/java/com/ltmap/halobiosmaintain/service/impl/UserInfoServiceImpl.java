package com.ltmap.halobiosmaintain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ltmap.halobiosmaintain.entity.main.UserInfo;
import com.ltmap.halobiosmaintain.mapper.main.RoleInfoMapper;
import com.ltmap.halobiosmaintain.mapper.main.UserInfoMapper;
import com.ltmap.halobiosmaintain.service.IUserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author Niko
 * @since 2020-09-14
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private RoleInfoMapper roleInfoMapper;

    //根据用户名查询用户角色权限信息
    public UserInfo findRolesByUserName(String userName){
        //根据用户名获取用户和角色信息
        UserInfo roles = userInfoMapper.findRolesByUserName(userName);
        return roles;
    }

    //根据用户名获取用户信息
    public UserInfo findByUserName(String userName){
        LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserInfo::getUserName,userName);
        UserInfo userInfo = userInfoMapper.selectOne(lambdaQueryWrapper);
        return userInfo;
    }

    /*
     * @Description:用查询户信息
     * @Param current:
     * @Param size:
     * @Param userName:
     * @Param realName:
     * @Return: com.baomidou.mybatisplus.core.metadata.IPage<com.ltmap.entity.main.UserInfo>
     * @Author: Niko
     * @Date: 2020/9/14 19:36
     */
    @Override
    public IPage<UserInfo> info_all(Integer current, Integer size, String userId, String userName) {
        Page<UserInfo> page = new Page<>(current, size);
        return userInfoMapper.info_all(page, userId, userName);
    }

//    @Override
//    public List<UserInfo> info_detail(String userId) {
//        LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = Wrappers.lambdaQuery();
//        lambdaQueryWrapper.eq(!Strings.isNullOrEmpty(userId), UserInfo::getUserId, userId);
//        return userInfoMapper.info_detail(userId);
//    }
    /*
     * @Description:删除用户信息
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
     * @Description:保存用户信息
     * @Param dataQualityControl:
     * @Return: boolean
     * @Author: Niko
     * @Date: 2020/9/14 18:06
     */
    @Override
    public boolean insert(UserInfo userInfo) {
        return save(userInfo);
    }

    /*
     * @Description:修改用户信息
     * @Param dataQualityControl:
     * @Return: boolean
     * @Author: Niko
     * @Date: 2020/9/14 18:06
     */
    @Override
    public boolean edit(UserInfo userInfo) {
        return updateById(userInfo);
    }
}
