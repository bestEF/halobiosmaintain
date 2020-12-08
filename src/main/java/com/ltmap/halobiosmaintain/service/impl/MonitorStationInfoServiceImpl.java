package com.ltmap.halobiosmaintain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.base.Strings;
import com.ltmap.halobiosmaintain.entity.work.MonitorStationInfo;
import com.ltmap.halobiosmaintain.mapper.work.MonitorStationInfoMapper;
import com.ltmap.halobiosmaintain.service.IMonitorStationInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
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

    /**
     * 保存站位信息
     * @param monitorStationInfo
     * @return
     */
    public Long addMonitorStation(MonitorStationInfo monitorStationInfo){
        LambdaQueryWrapper<MonitorStationInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(MonitorStationInfo::getStationName,monitorStationInfo.getStationName());
        lqw.eq(MonitorStationInfo::getReportId,monitorStationInfo.getReportId());
        MonitorStationInfo monitorStationResp = getOne(lqw);

        if(ObjectUtils.isEmpty(monitorStationResp)){
            boolean saveFlag = save(monitorStationInfo);
            return monitorStationInfo.getStationId();
        }else {
            String dataTypeStr = monitorStationResp.getDataType();
            String[] dataTypeArray = dataTypeStr.split(";");
            if(!(Arrays.asList(dataTypeArray).contains(monitorStationInfo.getDataType()))){
                monitorStationResp.setDataType(dataTypeStr+";"+monitorStationInfo.getDataType());
                updateById(monitorStationResp);

            }
            return monitorStationResp.getStationId();
        }

    }

    /**
     * 根据填报id删除所有站位信息
     * @param reportId
     * @return
     */
    public Boolean deleteByReportId(Long reportId,String dataType){
        LambdaQueryWrapper<MonitorStationInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(MonitorStationInfo::getReportId,reportId);
        List<MonitorStationInfo> monitorStationInfoList = monitorStationInfoMapper.selectList(lqw);

        if(CollectionUtils.isEmpty(monitorStationInfoList)){
            return true;
        }

        for (MonitorStationInfo monitorStationInfo : monitorStationInfoList) {
            //先执行删除操作 如果无法删除则说明此站位被其他数据类型使用着->更新此站位的dataType字段
            try {
                boolean removeFlag = removeById(monitorStationInfo.getStationId());
            } catch (DataIntegrityViolationException e) {
                String[] dataTypeArray = monitorStationInfo.getDataType().split(";");
                List<String> dataTypeList = new ArrayList<String>(Arrays.asList(dataTypeArray));
                if(dataTypeList.contains(dataType)){
                    dataTypeList.remove(dataType);
                }
                String dataTypeStr = StringUtils.join(dataTypeList.toArray(), ";");
                monitorStationInfo.setDataType(dataTypeStr);
                updateById(monitorStationInfo);
            }
        }

        return true;
    }

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
    public List<MonitorStationInfo> queryStationInfobyDataType(String year,String voyage,String dataType){
        LambdaQueryWrapper<MonitorStationInfo> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.like(!Strings.isNullOrEmpty(dataType),MonitorStationInfo::getDataType,dataType)
        .notLike(MonitorStationInfo::getDataType,"沉积物粒度");
        return monitorStationInfoMapper.selectList(lambdaQueryWrapper);
    }

    /**
     * @Description:查询监测站位通过站位Id
     * @Param stationId:
     * @Param reportId:
     * @Param stationName:
     * @Return:
     * @Author: Niko
     * @Date: 2020/12/8 17:12
     */
    @Override
    public List<MonitorStationInfo> queryStationInfoById(Long stationId,Long reportId,String stationName){
        LambdaQueryWrapper<MonitorStationInfo> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(stationId!=null, MonitorStationInfo::getStationId, stationId).eq(reportId!=null,MonitorStationInfo::getReportId,reportId)
        .eq(!Strings.isNullOrEmpty(stationName),MonitorStationInfo::getStationName,stationName);
        return monitorStationInfoMapper.selectList(lambdaQueryWrapper);
    }

}
