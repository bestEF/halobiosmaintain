package com.ltmap.halobiosmaintain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.ltmap.halobiosmaintain.config.Constant;
import com.ltmap.halobiosmaintain.entity.work.FisheggQuantitative;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.base.Strings;
import com.ltmap.halobiosmaintain.entity.work.MonitorDataReport;
import com.ltmap.halobiosmaintain.mapper.work.MonitorDataReportMapper;
import com.ltmap.halobiosmaintain.service.IFisheggQuantitativeService;
import com.ltmap.halobiosmaintain.service.IMonitorDataReportService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ltmap.halobiosmaintain.service.IMonitorStationInfoService;
import com.ltmap.halobiosmaintain.vo.req.FisheggQuantitativeReq;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
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
public class MonitorDataReportServiceImpl extends ServiceImpl<MonitorDataReportMapper, MonitorDataReport> implements IMonitorDataReportService {
    @Resource
    private IMonitorStationInfoService monitorStationInfoService;
    @Resource
    private IFisheggQuantitativeService fisheggQuantitativeService;
    @Resource
    private MonitorDataReportMapper monitorDataReportMapper;

    /**
     * 根据条件更新（即删除）这些条件信息下相同的数据
     * @param monitorDataReport
     * @param excelType
     * @return
     */
    public boolean updateData(MonitorDataReport monitorDataReport, int excelType){
        LambdaQueryWrapper<MonitorDataReport> lqw = Wrappers.lambdaQuery();
        lqw.eq(MonitorDataReport::getMonitoringArea,monitorDataReport.getMonitoringArea());
        lqw.eq(MonitorDataReport::getEcologicalType,monitorDataReport.getEcologicalType());
        lqw.eq(MonitorDataReport::getTaskDate,monitorDataReport.getTaskDate());
        lqw.eq(MonitorDataReport::getMonitorCompany,monitorDataReport.getMonitorCompany());
        lqw.eq(MonitorDataReport::getOrganizationCompany,monitorDataReport.getOrganizationCompany());
        lqw.eq(MonitorDataReport::getReportDate,monitorDataReport.getReportDate());
        lqw.eq(MonitorDataReport::getYear,monitorDataReport.getYear());
        lqw.eq(MonitorDataReport::getVoyage,monitorDataReport.getVoyage());
        MonitorDataReport monitorDataReportInf = getOne(lqw);

        if(ObjectUtils.isEmpty(monitorDataReportInf)){
            return false;
        }

        //如果不为空 那么删除该填报信息数据，该填报信息对应的所有站位数据 该填报信息对应的所有对应数据类型数据。
        //根据填报id删站位信息
        Boolean deleteStationInfoFlag = monitorStationInfoService.deleteByReportId(monitorDataReportInf.getReportId());

        //删对应数据类型信息
        switch (excelType){
            case Constant.fisheggQuantitativeType:
                Boolean deleteFisheggQuantitativeFlag = fisheggQuantitativeService.deleteByReportId(monitorDataReportInf.getReportId());
                break;
            default:
                break;
        }

        //删除此填报信息
        boolean deleteMonitorDataFlag = removeById(monitorDataReportInf.getReportId());

        return false;
    }

    @Override
    public List<MonitorDataReport> monitorDataReportInfo(String year){
        LambdaQueryWrapper<MonitorDataReport> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.eq(!Strings.isNullOrEmpty(year),MonitorDataReport::getYear,year);
        return monitorDataReportMapper.selectList(queryWrapper);
    }
}
