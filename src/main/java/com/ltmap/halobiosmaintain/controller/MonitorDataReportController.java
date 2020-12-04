package com.ltmap.halobiosmaintain.controller;


import com.ltmap.halobiosmaintain.common.result.Response;
import com.ltmap.halobiosmaintain.common.result.Responses;
import com.ltmap.halobiosmaintain.entity.work.MonitorDataReport;
import com.ltmap.halobiosmaintain.service.IMonitorDataReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

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


    @ApiOperation(value ="查询所有年份")
    @PostMapping("/yearList")
    public Response<List<String>> yearList(){
        List<String> yearList=new ArrayList();
        List<MonitorDataReport> monitorDataReports=monitorDataReportService.monitorDataReportInfo(null);
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
        List<MonitorDataReport> monitorDataReports=monitorDataReportService.monitorDataReportInfo(year);
        for (int i = 0; i <monitorDataReports.size() ; i++) {
            voyageList.add(monitorDataReports.get(i).getVoyage());
        }
        LinkedHashSet<String> hashSet = new LinkedHashSet<>(voyageList);
        ArrayList<String> listWithoutDuplicates = new ArrayList<>(hashSet);
        return Responses.or(listWithoutDuplicates);
    }


}

