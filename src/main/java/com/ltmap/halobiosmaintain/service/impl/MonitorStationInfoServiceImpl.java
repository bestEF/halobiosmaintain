package com.ltmap.halobiosmaintain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.base.Strings;
import com.ltmap.halobiosmaintain.entity.work.MonitorStationInfo;
import com.ltmap.halobiosmaintain.mapper.work.MonitorStationInfoMapper;
import com.ltmap.halobiosmaintain.service.IMonitorStationInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fjh
 * @since 2020-11-27
 */
@Service
public class MonitorStationInfoServiceImpl extends ServiceImpl<MonitorStationInfoMapper, MonitorStationInfo> implements IMonitorStationInfoService {
    @Resource
    private MonitorStationInfoMapper monitorStationInfoMapper;


    /*
     * @Description:查询所有监测站位
     * @Param
     * @Return:
     * @Author: Niko
     * @Date: 2020/11/28 9:51
     */
    @Override
    public List<MonitorStationInfo> queryStationInfo(String year,String voyage){
        LambdaQueryWrapper<MonitorStationInfo> lambdaQueryWrapper = Wrappers.lambdaQuery();
        return monitorStationInfoMapper.queryStationInfo(year,voyage);
    }

    @Override
    public List<MonitorStationInfo> queryStationInfoById(Long stationId,Long reportId,String stationName){
        LambdaQueryWrapper<MonitorStationInfo> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(stationId!=null, MonitorStationInfo::getStationId, stationId).eq(reportId!=null,MonitorStationInfo::getReportId,reportId)
        .eq(!Strings.isNullOrEmpty(stationName),MonitorStationInfo::getStationName,stationName);
        return monitorStationInfoMapper.selectList(lambdaQueryWrapper);
    }
}
