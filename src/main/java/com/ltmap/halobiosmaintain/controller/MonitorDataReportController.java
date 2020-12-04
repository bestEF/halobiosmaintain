package com.ltmap.halobiosmaintain.controller;


import com.google.common.base.Strings;
import com.ltmap.halobiosmaintain.common.result.Response;
import com.ltmap.halobiosmaintain.common.result.Responses;
import com.ltmap.halobiosmaintain.entity.work.MacrobenthosQuantitative;
import com.ltmap.halobiosmaintain.entity.work.MonitorDataReport;
import com.ltmap.halobiosmaintain.entity.work.MonitorStationInfo;
import com.ltmap.halobiosmaintain.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.lang.model.element.VariableElement;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  监测数据报表 前端控制器
 * </p>
 *@author fjh
 * @since 2020-11-27
 */
@Api(tags = "监测数据报表")
@RestController
@RequestMapping("/monitorDataReport")
public class MonitorDataReportController {
    @Resource
    private IMonitorDataReportService monitorDataReportService;

    @Resource
    private IMonitorStationInfoService monitorStationInfoService;

    @Resource
    private IMacrobenthosQuantitativeService macrobenthosQuantitativeService;
    @Resource
    private IMacrobenthosQualitativeService macrobenthosQualitativeService;
    @Resource
    private IFisheggQuantitativeService fisheggQuantitativeService;
    @Resource
    private IFisheggQualitativeService fisheggQualitativeService;
    @Resource
    private ISmallfishQuantitativeService smallfishQuantitativeService;
    @Resource
    private ISmallfishQualitativeService smallfishQualitativeService;
    @Resource
    private ISwimminganimalIdentificationService swimminganimalIdentificationService;
    @Resource
    private IIntertidalzonebiologicalQuantitativeService intertidalzonebiologicalQuantitativeService;
    @Resource
    private IPhytoplanktonService phytoplanktonService;
    @Resource
    private ILargezooplanktonInetService largezooplanktonInetService;
    @Resource
    private ISmallzooplanktonIinetService smallzooplanktonIinetService;
    @Resource
    private IBiologicalQualityService biologicalQualityService;
    @Resource
    private IHydrometeorologicalService hydrometeorologicalService;
    @Resource
    private ISedimentService sedimentService;
    @Resource
    private ISedimentgrainService sedimentgrainService;
    @Resource
    private IWaterqualityService waterqualityService;


    @ApiOperation(value ="查询所有年份")
    @PostMapping("/yearList")
    public Response<List<String>> yearList(){
        List<String> yearList=new ArrayList();
        List<MonitorDataReport> monitorDataReports=monitorDataReportService.monitorDataReportInfo(null,null,null,null,null,null);
        for (int i = 0; i <monitorDataReports.size() ; i++) {
            yearList.add(monitorDataReports.get(i).getYear());
        }
        LinkedHashSet<String> hashSet = new LinkedHashSet<>(yearList);
        ArrayList<String> listWithoutDuplicates = new ArrayList<>(hashSet);

        return Responses.or(listWithoutDuplicates);
    }

    @ApiOperation(value ="查询年份对应的航次")
    @PostMapping("/voyageList")
    public Response<List<String>> voyageList(String year){
        List<String> voyageList=new ArrayList();
        List<MonitorDataReport> monitorDataReports=monitorDataReportService.monitorDataReportInfo(null,null,null,null,null,year);
        for (int i = 0; i <monitorDataReports.size() ; i++) {
            voyageList.add(monitorDataReports.get(i).getVoyage());
        }
        LinkedHashSet<String> hashSet = new LinkedHashSet<>(voyageList);
        ArrayList<String> listWithoutDuplicates = new ArrayList<>(hashSet);
        return Responses.or(listWithoutDuplicates);
    }


    @ApiOperation(value ="查询数据填报信息")
    @PostMapping("/queryMonitorDataReport")
    public Response<List<MonitorDataReport>> queryMonitorDataReport(@RequestParam(defaultValue = "1")Integer current,
                                                                    @RequestParam(defaultValue = "10")Integer size,
                                                                    String monitoringArea,String ecologicalType,String monitorCompany,  String dataType,String startDate,String endDate ){

        List<MonitorDataReport> monitorDataReportsResult=new ArrayList<>();

        List<MonitorDataReport> monitorDataReports=monitorDataReportService.monitorDataReportInfo(monitoringArea,ecologicalType,monitorCompany,startDate,endDate,null);
        for (int i = 0; i < monitorDataReports.size(); i++) {
            Long reportId = monitorDataReports.get(i).getReportId();

            List<MonitorStationInfo> monitorStationInfos= monitorStationInfoService.queryStationInfoById(null,reportId,null);
            List<String> dataTypeList=new ArrayList<>();
            for (int j = 0; j <monitorStationInfos.size() ; j++) {
                String dataType2 = monitorStationInfos.get(j).getDataType();
                String[] dataTypeArray=dataType2.split(";");
                for (String item:dataTypeArray
                     ) {
                    dataTypeList.add(item);
                }
            }
            List<String> listDataTypeWithoutDuplicates= dataTypeList.stream().distinct().collect(Collectors.toList());//去重
            if(Strings.isNullOrEmpty(dataType)){
                for (int j = 0; j < listDataTypeWithoutDuplicates.size(); j++) {
                    MonitorDataReport monitorDataReport=new MonitorDataReport();
                    monitorDataReport=monitorDataReports.get(i);
                    monitorDataReport.setByzd1(listDataTypeWithoutDuplicates.get(j));
                    monitorDataReportsResult.add(monitorDataReport);
                }
            }else{
                if(listDataTypeWithoutDuplicates.contains(dataType)){
                    MonitorDataReport monitorDataReport=new MonitorDataReport();
                    monitorDataReport=monitorDataReports.get(i);
                    monitorDataReport.setByzd1(dataType);
                    monitorDataReportsResult.add(monitorDataReport);
                }
            }
        }
        List<MonitorDataReport> monitorDataReportList = startPage(monitorDataReports,current,size);
        return Responses.or(monitorDataReportList);
    }

    /**
     * List分页
     * @param list
     * @param pageNum 页码
     * @param pageSize 每页多少条数据
     * @return
     */
    private List startPage(List list, Integer pageNum,
                                 Integer pageSize) {
        if (list == null) {
            return null;
        }
        if (list.size() == 0) {
            return null;
        }

        Integer count = list.size(); // 记录总数
        Integer pageCount = 0; // 页数
        if (count % pageSize == 0) {
            pageCount = count / pageSize;
        } else {
            pageCount = count / pageSize + 1;
        }

        int fromIndex = 0; // 开始索引
        int toIndex = 0; // 结束索引

        if (pageNum.equals(pageCount)) {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = fromIndex + pageSize;
        } else {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = count;
        }

        List pageList = list.subList(fromIndex, toIndex);

        return pageList;
    }

}

