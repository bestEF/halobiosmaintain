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
import com.ltmap.halobiosmaintain.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ltmap.halobiosmaintain.vo.req.FisheggQuantitativeReq;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

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
    private MonitorDataReportMapper monitorDataReportMapper;

    @Resource
    private IBiologicalQualityService biologicalQualityService;
    @Resource
    private IFisheggQualitativeService fisheggQualitativeService;
    @Resource
    private IFisheggQuantitativeService fisheggQuantitativeService;
    @Resource
    private IHydrometeorologicalService hydrometeorologicalService;
    @Resource
    private IIntertidalzonebiologicalQuantitativeService intertidalzonebiologicalQuantitativeServic;
    @Resource
    private ILargezooplanktonInetService largezooplanktonInetService;
    @Resource
    private IMacrobenthosQualitativeService macrobenthosQualitativeService;
    @Resource
    private IMacrobenthosQuantitativeService macrobenthosQuantitativeService;
    @Resource
    private IPhytoplanktonService phytoplanktonService;
    @Resource
    private ISedimentService sedimentService;
    @Resource
    private ISedimentgrainService sedimentgrainService;
    @Resource
    private ISmallfishQualitativeService smallfishQualitativeService;
    @Resource
    private ISmallfishQuantitativeService smallfishQuantitativeService;
    @Resource
    private ISmallzooplanktonIinetService smallzooplanktonIinetService;
    @Resource
    private ISwimminganimalIdentificationService swimminganimalIdentificationService;
    @Resource
    private IWaterqualityService waterqualityService;

    /**
     * 保存填报信息
     * @param monitorDataReport0
     * @return
     */
    public Long addMonitorDataReport(MonitorDataReport monitorDataReport0){
        LambdaQueryWrapper<MonitorDataReport> lqw = Wrappers.lambdaQuery();
        lqw.eq(MonitorDataReport::getMonitoringArea,monitorDataReport0.getMonitoringArea());
        lqw.eq(MonitorDataReport::getEcologicalType,monitorDataReport0.getEcologicalType());
        lqw.eq(MonitorDataReport::getTaskDate,monitorDataReport0.getTaskDate());
        lqw.eq(MonitorDataReport::getMonitorCompany,monitorDataReport0.getMonitorCompany());
        lqw.eq(MonitorDataReport::getOrganizationCompany,monitorDataReport0.getOrganizationCompany());
        lqw.eq(MonitorDataReport::getReportDate,monitorDataReport0.getReportDate());
        lqw.eq(MonitorDataReport::getYear,monitorDataReport0.getYear());
        lqw.eq(MonitorDataReport::getVoyage,monitorDataReport0.getVoyage());
        MonitorDataReport monitorDataReport = getOne(lqw);

        if(Objects.isNull(monitorDataReport)){
            boolean saveFlag = save(monitorDataReport0);
            return monitorDataReport0.getReportId();
        }else {
            return monitorDataReport.getReportId();
        }

    }

    /**
     * 根据条件更新（即删除）这些条件信息下相同的数据
     * @param monitorDataReport
     * @param excelType
     * @return
     */
    public boolean updateData(MonitorDataReport monitorDataReport, int excelType,String dataType){
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
        //删对应数据类型信息
        Boolean deleteFisheggQuantitativeFlag=false;
        switch (excelType){
            case Constant.biologicalQualityType:
                deleteFisheggQuantitativeFlag = biologicalQualityService.deleteByReportId(monitorDataReportInf.getReportId());
                break;
            case Constant.fisheggQualitativeType:
                deleteFisheggQuantitativeFlag = fisheggQualitativeService.deleteByReportId(monitorDataReportInf.getReportId());
                break;
            case Constant.fisheggQuantitativeType:
                deleteFisheggQuantitativeFlag = fisheggQuantitativeService.deleteByReportId(monitorDataReportInf.getReportId());
                break;
            case Constant.hydrometeorologicalType:
                deleteFisheggQuantitativeFlag = hydrometeorologicalService.deleteByReportId(monitorDataReportInf.getReportId());
                break;
            case Constant.intertidalzonebiologicalQuantitativeType:
                deleteFisheggQuantitativeFlag = intertidalzonebiologicalQuantitativeServic.deleteByReportId(monitorDataReportInf.getReportId());
                break;
            case Constant.largezooplanktonInetType:
                deleteFisheggQuantitativeFlag = largezooplanktonInetService.deleteByReportId(monitorDataReportInf.getReportId());
                break;
            case Constant.macrobenthosQualitativeType:
                deleteFisheggQuantitativeFlag = macrobenthosQualitativeService.deleteByReportId(monitorDataReportInf.getReportId());
                break;
            case Constant.macrobenthosQuantitativeType:
                deleteFisheggQuantitativeFlag = macrobenthosQuantitativeService.deleteByReportId(monitorDataReportInf.getReportId());
                break;
            case Constant.phytoplanktonType:
                deleteFisheggQuantitativeFlag = phytoplanktonService.deleteByReportId(monitorDataReportInf.getReportId());
                break;
            case Constant.sedimentType:
                deleteFisheggQuantitativeFlag = sedimentService.deleteByReportId(monitorDataReportInf.getReportId());
                break;
            case Constant.sedimentgrainType:
                deleteFisheggQuantitativeFlag = sedimentgrainService.deleteByReportId(monitorDataReportInf.getReportId());
                break;
            case Constant.smallfishQualitativeType:
                deleteFisheggQuantitativeFlag = smallfishQualitativeService.deleteByReportId(monitorDataReportInf.getReportId());
                break;
            case Constant.smallfishQuantitativeType:
                deleteFisheggQuantitativeFlag = smallfishQuantitativeService.deleteByReportId(monitorDataReportInf.getReportId());
                break;
            case Constant.smallzooplanktonIinetType:
                deleteFisheggQuantitativeFlag = smallzooplanktonIinetService.deleteByReportId(monitorDataReportInf.getReportId());
                break;
            case Constant.swimminganimalIdentificationType:
                deleteFisheggQuantitativeFlag = swimminganimalIdentificationService.deleteByReportId(monitorDataReportInf.getReportId());
                break;
            case Constant.waterqualityType:
                deleteFisheggQuantitativeFlag = waterqualityService.deleteByReportId(monitorDataReportInf.getReportId());
                break;
            default:
                break;
        }

        //根据填报id删站位信息
        Boolean deleteStationInfoFlag = monitorStationInfoService.deleteByReportId(monitorDataReportInf.getReportId(),dataType);


        //删除此填报信息
        try {
            boolean deleteMonitorDataFlag = removeById(monitorDataReportInf.getReportId());
        } catch (Exception e) {

        }

        return false;
    }

    @Override
    public List<MonitorDataReport> monitorDataReportInfo(String monitoringArea,String ecologicalType,String monitorCompany,String startDate,String endDate,String year){
        LambdaQueryWrapper<MonitorDataReport> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.eq(!Strings.isNullOrEmpty(year),MonitorDataReport::getYear,year)
                .like(!Strings.isNullOrEmpty(monitoringArea),MonitorDataReport::getMonitoringArea,monitoringArea)
                .like(!Strings.isNullOrEmpty(ecologicalType),MonitorDataReport::getEcologicalType,ecologicalType)
                .like(!Strings.isNullOrEmpty(monitorCompany),MonitorDataReport::getMonitorCompany,monitorCompany)
                .between(!Strings.isNullOrEmpty(startDate)&&!Strings.isNullOrEmpty(endDate),MonitorDataReport::getTaskDate,startDate,endDate);
        return monitorDataReportMapper.selectList(queryWrapper);
    }

}
