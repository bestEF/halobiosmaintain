package com.ltmap.halobiosmaintain.service.impl;

import com.ltmap.halobiosmaintain.common.result.Response;
import com.ltmap.halobiosmaintain.common.utils.excel_import.Config;
import com.ltmap.halobiosmaintain.common.utils.excel_import.FileTestDataUtil;
import com.ltmap.halobiosmaintain.common.utils.excel_import.IdText;
import com.ltmap.halobiosmaintain.config.Constant;
import com.ltmap.halobiosmaintain.entity.work.*;
import com.ltmap.halobiosmaintain.service.*;
import com.ltmap.halobiosmaintain.vo.req.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * excel数据导入ServiceImpl
 * @author fjh
 * @date 2020/12/1 9:47
 */
@Service
public class ExcelDataImportServiceImpl implements ExcelDataImportService {
    @Resource
    private Config config;
    @Resource
    private FileTestDataUtil fileTestDataUtil;
    @Resource
    private IMonitorDataReportService monitorDataReportService;
    @Resource
    private IMonitorStationInfoService monitorStationInfoService;
    @Resource
    private IBirdObserveService birdObserveService;
    @Resource
    private IVegetationSurveyService vegetationSurveyService;

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
    @Resource
    private IBirdObserveRecordService birdObserveRecordService;
    @Resource
    private IVegetationSurveyRecordService vegetationSurveyRecordService;


    /**
     * 文件入库
     * @param allMapList
     * @param codes
     * @param userId
     * @param userName
     * @return
     */
    public Response<Object> fileInsert(List<Map<String, Object>> allMapList, String codes, String userId, String userName){
        try{
            Integer rightFileCount=0;
            StringBuilder sb=new StringBuilder();// 成功入库的code
            String[] code=codes.split("\\,");
            IdText it;
            List<IdText> returnList=new ArrayList<>();
            List<String> codeList=Arrays.asList(code);
            for(Map<String,Object> resultMap:allMapList){
                // 若数组中不包含此resultMap，直接跳转下一循环
                String code0=resultMap.get("code").toString();
                Boolean isContains=codeList.contains(code0);
                if(!isContains)
                    continue;
                // 仅抽取全部正确的Excel入库
                if(resultMap.get("result").equals(true)){
                    rightFileCount++;
                    try{
                        Object data=resultMap.get("data");
                        if(data==null) {
                            return Response.fail("文件无数据");
                        }

                        //记录填报信息表主键
                        Long monitorDataReportId=0L;

                        switch (resultMap.get("excelType").toString()){
                            case "BiologicalQualityExcelRule":
                                List<BiologicalQualityReq> testDataList0 = new ArrayList<>((List<BiologicalQualityReq>) data);

                                //鱼仔定量list
                                List<BiologicalQuality> biologicalQualityList = new ArrayList<>();
                                //监测数据填报信息
                                MonitorDataReport monitorDataReport0 = new MonitorDataReport();
                                if(CollectionUtils.isNotEmpty(testDataList0)){
                                    BeanUtils.copyProperties(testDataList0.get(0),monitorDataReport0);

                                    //如果数据库中有和此填报表一样的数据 那么执行删除操作（删除（站位信息s+对应数据s+填报信息））
                                    monitorDataReportService.updateData(monitorDataReport0, Constant.biologicalQualityType,testDataList0.get(0).getDataType());

                                    //保存填报信息
                                    monitorDataReportId = monitorDataReportService.addMonitorDataReport(monitorDataReport0);
                                }


                                //将扩展类对拷到相应的实体类
                                for (BiologicalQualityReq biologicalQualityReq : testDataList0) {

                                    BiologicalQuality biologicalQuality = new BiologicalQuality();
                                    MonitorStationInfo monitorStationInfo = new MonitorStationInfo();

                                    BeanUtils.copyProperties(biologicalQualityReq,monitorStationInfo);
                                    monitorStationInfo.setReportId(monitorDataReportId);
                                    //保存站位信息
                                    Long monitorStationInfoId = monitorStationInfoService.addMonitorStation(monitorStationInfo);

                                    BeanUtils.copyProperties(biologicalQualityReq,biologicalQuality);
                                    biologicalQuality.setReportId(monitorDataReportId);
                                    biologicalQuality.setStationId(monitorStationInfoId);

                                    biologicalQualityList.add(biologicalQuality);
                                }

                                if(testDataList0.size()>0){
                                    it=new IdText();
                                    it.setId(Integer.valueOf(code0));
                                    if(biologicalQualityService.saveBatch(biologicalQualityList)) {
                                        sb.append(code0+",");
                                        it.setText("true");
                                        //日志
                                        //sysLogService.saveLog(ShiroUtils.getUserId(),Constant.manualSurveyDataImport,"水体数据导入");
                                    }else {
                                        it.setText("false");
                                    }
                                    returnList.add(it);
                                }
                                testDataList0=null;
                                break;
                            case "FisheggQualitativeExcelRule":
                                List<FisheggQualitativeReq> testDataList1 = new ArrayList<>((List<FisheggQualitativeReq>) data);

                                //鱼仔定量list
                                List<FisheggQualitative> fisheggQualitativeList = new ArrayList<>();
                                //监测数据填报信息
                                MonitorDataReport monitorDataReport1 = new MonitorDataReport();
                                if(CollectionUtils.isNotEmpty(testDataList1)){
                                    BeanUtils.copyProperties(testDataList1.get(0),monitorDataReport1);

                                    //如果数据库中有和此填报表一样的数据 那么执行删除操作（删除（站位信息s+对应数据s+填报信息））
                                    monitorDataReportService.updateData(monitorDataReport1, Constant.fisheggQualitativeType,testDataList1.get(0).getDataType());

                                    //保存填报信息
                                    monitorDataReportId = monitorDataReportService.addMonitorDataReport(monitorDataReport1);
                                }


                                //将扩展类对拷到相应的实体类
                                for (FisheggQualitativeReq fisheggQualitativeReq : testDataList1) {

                                    FisheggQualitative fisheggQualitative = new FisheggQualitative();
                                    MonitorStationInfo monitorStationInfo = new MonitorStationInfo();

                                    BeanUtils.copyProperties(fisheggQualitativeReq,monitorStationInfo);
                                    monitorStationInfo.setReportId(monitorDataReportId);
                                    //保存站位信息
                                    Long monitorStationInfoId = monitorStationInfoService.addMonitorStation(monitorStationInfo);

                                    BeanUtils.copyProperties(fisheggQualitativeReq,fisheggQualitative);
                                    fisheggQualitative.setReportId(monitorDataReportId);
                                    fisheggQualitative.setStationId(monitorStationInfoId);

                                    fisheggQualitativeList.add(fisheggQualitative);
                                }

                                if(testDataList1.size()>0){
                                    it=new IdText();
                                    it.setId(Integer.valueOf(code0));
                                    if(fisheggQualitativeService.saveBatch(fisheggQualitativeList)) {
                                        sb.append(code0+",");
                                        it.setText("true");
                                        //日志
                                        //sysLogService.saveLog(ShiroUtils.getUserId(),Constant.manualSurveyDataImport,"水体数据导入");
                                    }else {
                                        it.setText("false");
                                    }
                                    returnList.add(it);
                                }
                                testDataList1=null;
                                break;
                            case "FisheggQuantitativeExcelRule":
                                List<FisheggQuantitativeReq> testDataList2 = new ArrayList<>((List<FisheggQuantitativeReq>) data);

                                //鱼仔定量list
                                List<FisheggQuantitative> fisheggQuantitativeList = new ArrayList<>();
                                //监测数据填报信息
                                MonitorDataReport monitorDataReport2 = new MonitorDataReport();
                                if(CollectionUtils.isNotEmpty(testDataList2)){
                                    BeanUtils.copyProperties(testDataList2.get(0),monitorDataReport2);

                                    //如果数据库中有和此填报表一样的数据 那么执行删除操作（删除（站位信息s+对应数据s+填报信息））
                                    monitorDataReportService.updateData(monitorDataReport2, Constant.fisheggQuantitativeType,testDataList2.get(0).getDataType());

                                    //保存填报信息
                                    monitorDataReportId = monitorDataReportService.addMonitorDataReport(monitorDataReport2);
                                }


                                //将扩展类对拷到相应的实体类
                                for (FisheggQuantitativeReq fisheggQuantitativeReq : testDataList2) {

                                    FisheggQuantitative fisheggQuantitative = new FisheggQuantitative();
                                    MonitorStationInfo monitorStationInfo = new MonitorStationInfo();

                                    BeanUtils.copyProperties(fisheggQuantitativeReq,monitorStationInfo);
                                    monitorStationInfo.setReportId(monitorDataReportId);
                                    //保存站位信息
                                    Long monitorStationInfoId = monitorStationInfoService.addMonitorStation(monitorStationInfo);

                                    BeanUtils.copyProperties(fisheggQuantitativeReq,fisheggQuantitative);
                                    fisheggQuantitative.setReportId(monitorDataReportId);
                                    fisheggQuantitative.setStationId(monitorStationInfoId);

                                    fisheggQuantitativeList.add(fisheggQuantitative);
                                }

                                if(testDataList2.size()>0){
                                    it=new IdText();
                                    it.setId(Integer.valueOf(code0));
                                    if(fisheggQuantitativeService.saveBatch(fisheggQuantitativeList)) {
                                        sb.append(code0+",");
                                        it.setText("true");
                                    }else {
                                        it.setText("false");
                                    }
                                    returnList.add(it);
                                }
                                testDataList2=null;
                                break;
                            case "HydrometeorologicalExcelRule":
                                List<HydrometeorologicalReq> testDataList3 = new ArrayList<>((List<HydrometeorologicalReq>) data);

                                //鱼仔定量list
                                List<Hydrometeorological> hydrometeorologicalList = new ArrayList<>();
                                //监测数据填报信息
                                MonitorDataReport monitorDataReport3 = new MonitorDataReport();
                                if(CollectionUtils.isNotEmpty(testDataList3)){
                                    BeanUtils.copyProperties(testDataList3.get(0),monitorDataReport3);

                                    //如果数据库中有和此填报表一样的数据 那么执行删除操作（删除（站位信息s+对应数据s+填报信息））
                                    monitorDataReportService.updateData(monitorDataReport3, Constant.hydrometeorologicalType,testDataList3.get(0).getDataType());

                                    //保存填报信息
                                    monitorDataReportId = monitorDataReportService.addMonitorDataReport(monitorDataReport3);
                                }


                                //将扩展类对拷到相应的实体类
                                for (HydrometeorologicalReq hydrometeorologicalReq : testDataList3) {

                                    Hydrometeorological hydrometeorological = new Hydrometeorological();
                                    MonitorStationInfo monitorStationInfo = new MonitorStationInfo();

                                    BeanUtils.copyProperties(hydrometeorologicalReq,monitorStationInfo);
                                    monitorStationInfo.setReportId(monitorDataReportId);
                                    //保存站位信息
                                    Long monitorStationInfoId = monitorStationInfoService.addMonitorStation(monitorStationInfo);

                                    BeanUtils.copyProperties(hydrometeorologicalReq,hydrometeorological);
                                    hydrometeorological.setReportId(monitorDataReportId);
                                    hydrometeorological.setStationId(monitorStationInfoId);

                                    hydrometeorologicalList.add(hydrometeorological);
                                }

                                if(testDataList3.size()>0){
                                    it=new IdText();
                                    it.setId(Integer.valueOf(code0));
                                    if(hydrometeorologicalService.saveBatch(hydrometeorologicalList)) {
                                        sb.append(code0+",");
                                        it.setText("true");
                                    }else {
                                        it.setText("false");
                                    }
                                    returnList.add(it);
                                }
                                testDataList3=null;
                                break;
                            case "IntertidalzonebiologicalQuantitativeExcelRule":
                                List<IntertidalzonebiologicalQuantitativeReq> testDataList4 = new ArrayList<>((List<IntertidalzonebiologicalQuantitativeReq>) data);

                                //鱼仔定量list
                                List<IntertidalzonebiologicalQuantitative> intertidalzonebiologicalQuantitativeList = new ArrayList<>();
                                //监测数据填报信息
                                MonitorDataReport monitorDataReport4 = new MonitorDataReport();
                                if(CollectionUtils.isNotEmpty(testDataList4)){
                                    BeanUtils.copyProperties(testDataList4.get(0),monitorDataReport4);

                                    //如果数据库中有和此填报表一样的数据 那么执行删除操作（删除（站位信息s+对应数据s+填报信息））
                                    monitorDataReportService.updateData(monitorDataReport4, Constant.intertidalzonebiologicalQuantitativeType,testDataList4.get(0).getDataType());

                                    //保存填报信息
                                    monitorDataReportId = monitorDataReportService.addMonitorDataReport(monitorDataReport4);
                                }


                                //将扩展类对拷到相应的实体类
                                for (IntertidalzonebiologicalQuantitativeReq intertidalzonebiologicalQuantitativeReq : testDataList4) {

                                    IntertidalzonebiologicalQuantitative intertidalzonebiologicalQuantitative = new IntertidalzonebiologicalQuantitative();
                                    MonitorStationInfo monitorStationInfo = new MonitorStationInfo();

                                    BeanUtils.copyProperties(intertidalzonebiologicalQuantitativeReq,monitorStationInfo);
                                    monitorStationInfo.setReportId(monitorDataReportId);
                                    //保存站位信息
                                    Long monitorStationInfoId = monitorStationInfoService.addMonitorStation(monitorStationInfo);

                                    BeanUtils.copyProperties(intertidalzonebiologicalQuantitativeReq,intertidalzonebiologicalQuantitative);
                                    intertidalzonebiologicalQuantitative.setReportId(monitorDataReportId);
                                    intertidalzonebiologicalQuantitative.setStationId(monitorStationInfoId);

                                    intertidalzonebiologicalQuantitativeList.add(intertidalzonebiologicalQuantitative);
                                }

                                if(testDataList4.size()>0){
                                    it=new IdText();
                                    it.setId(Integer.valueOf(code0));
                                    if(intertidalzonebiologicalQuantitativeServic.saveBatch(intertidalzonebiologicalQuantitativeList)) {
                                        sb.append(code0+",");
                                        it.setText("true");
                                        //日志
                                        //sysLogService.saveLog(ShiroUtils.getUserId(),Constant.manualSurveyDataImport,"水体数据导入");
                                    }else {
                                        it.setText("false");
                                    }
                                    returnList.add(it);
                                }
                                testDataList4=null;
                                break;
                            case "LargezooplanktonInetExcelRule":
                                List<LargezooplanktonInetReq> testDataList5 = new ArrayList<>((List<LargezooplanktonInetReq>) data);

                                //鱼仔定量list
                                List<LargezooplanktonInet> largezooplanktonInetList = new ArrayList<>();
                                //监测数据填报信息
                                MonitorDataReport monitorDataReport5 = new MonitorDataReport();
                                if(CollectionUtils.isNotEmpty(testDataList5)){
                                    BeanUtils.copyProperties(testDataList5.get(0),monitorDataReport5);

                                    //如果数据库中有和此填报表一样的数据 那么执行删除操作（删除（站位信息s+对应数据s+填报信息））
                                    monitorDataReportService.updateData(monitorDataReport5, Constant.largezooplanktonInetType,testDataList5.get(0).getDataType());

                                    //保存填报信息
                                    monitorDataReportId = monitorDataReportService.addMonitorDataReport(monitorDataReport5);
                                }


                                //将扩展类对拷到相应的实体类
                                for (LargezooplanktonInetReq largezooplanktonInetReq : testDataList5) {

                                    LargezooplanktonInet largezooplanktonInet = new LargezooplanktonInet();
                                    MonitorStationInfo monitorStationInfo = new MonitorStationInfo();

                                    BeanUtils.copyProperties(largezooplanktonInetReq,monitorStationInfo);
                                    monitorStationInfo.setReportId(monitorDataReportId);
                                    //保存站位信息
                                    Long monitorStationInfoId = monitorStationInfoService.addMonitorStation(monitorStationInfo);

                                    BeanUtils.copyProperties(largezooplanktonInetReq,largezooplanktonInet);
                                    largezooplanktonInet.setReportId(monitorDataReportId);
                                    largezooplanktonInet.setStationId(monitorStationInfoId);

                                    largezooplanktonInetList.add(largezooplanktonInet);
                                }

                                if(testDataList5.size()>0){
                                    it=new IdText();
                                    it.setId(Integer.valueOf(code0));
                                    if(largezooplanktonInetService.saveBatch(largezooplanktonInetList)) {
                                        sb.append(code0+",");
                                        it.setText("true");
                                        //日志
                                        //sysLogService.saveLog(ShiroUtils.getUserId(),Constant.manualSurveyDataImport,"水体数据导入");
                                    }else {
                                        it.setText("false");
                                    }
                                    returnList.add(it);
                                }
                                testDataList5=null;
                                break;
                            case "MacrobenthosQualitativeExcelRule":
                                List<MacrobenthosQualitativeReq> testDataList6 = new ArrayList<>((List<MacrobenthosQualitativeReq>) data);

                                //鱼仔定量list
                                List<MacrobenthosQualitative> macrobenthosQualitativeList = new ArrayList<>();
                                //监测数据填报信息
                                MonitorDataReport monitorDataReport6 = new MonitorDataReport();
                                if(CollectionUtils.isNotEmpty(testDataList6)){
                                    BeanUtils.copyProperties(testDataList6.get(0),monitorDataReport6);

                                    //如果数据库中有和此填报表一样的数据 那么执行删除操作（删除（站位信息s+对应数据s+填报信息））
                                    monitorDataReportService.updateData(monitorDataReport6, Constant.macrobenthosQualitativeType,testDataList6.get(0).getDataType());

                                    //保存填报信息
                                    monitorDataReportId = monitorDataReportService.addMonitorDataReport(monitorDataReport6);
                                }


                                //将扩展类对拷到相应的实体类
                                for (MacrobenthosQualitativeReq macrobenthosQualitativeReq : testDataList6) {

                                    MacrobenthosQualitative macrobenthosQualitative = new MacrobenthosQualitative();
                                    MonitorStationInfo monitorStationInfo = new MonitorStationInfo();

                                    BeanUtils.copyProperties(macrobenthosQualitativeReq,monitorStationInfo);
                                    monitorStationInfo.setReportId(monitorDataReportId);
                                    //保存站位信息
                                    Long monitorStationInfoId = monitorStationInfoService.addMonitorStation(monitorStationInfo);

                                    BeanUtils.copyProperties(macrobenthosQualitativeReq,macrobenthosQualitative);
                                    macrobenthosQualitative.setReportId(monitorDataReportId);
                                    macrobenthosQualitative.setStationId(monitorStationInfoId);

                                    macrobenthosQualitativeList.add(macrobenthosQualitative);
                                }

                                if(testDataList6.size()>0){
                                    it=new IdText();
                                    it.setId(Integer.valueOf(code0));
                                    if(macrobenthosQualitativeService.saveBatch(macrobenthosQualitativeList)) {
                                        sb.append(code0+",");
                                        it.setText("true");
                                        //日志
                                        //sysLogService.saveLog(ShiroUtils.getUserId(),Constant.manualSurveyDataImport,"水体数据导入");
                                    }else {
                                        it.setText("false");
                                    }
                                    returnList.add(it);
                                }
                                testDataList6=null;
                                break;
                            case "MacrobenthosQuantitativeExcelRule":
                                List<MacrobenthosQuantitativeReq> testDataList7 = new ArrayList<>((List<MacrobenthosQuantitativeReq>) data);

                                //鱼仔定量list
                                List<MacrobenthosQuantitative> macrobenthosQuantitativeList = new ArrayList<>();
                                //监测数据填报信息
                                MonitorDataReport monitorDataReport7 = new MonitorDataReport();
                                if(CollectionUtils.isNotEmpty(testDataList7)){
                                    BeanUtils.copyProperties(testDataList7.get(0),monitorDataReport7);

                                    //如果数据库中有和此填报表一样的数据 那么执行删除操作（删除（站位信息s+对应数据s+填报信息））
                                    monitorDataReportService.updateData(monitorDataReport7, Constant.macrobenthosQuantitativeType,testDataList7.get(0).getDataType());

                                    //保存填报信息
                                    monitorDataReportId = monitorDataReportService.addMonitorDataReport(monitorDataReport7);
                                }


                                //将扩展类对拷到相应的实体类
                                for (MacrobenthosQuantitativeReq macrobenthosQuantitativeReq : testDataList7) {

                                    MacrobenthosQuantitative macrobenthosQuantitative = new MacrobenthosQuantitative();
                                    MonitorStationInfo monitorStationInfo = new MonitorStationInfo();

                                    BeanUtils.copyProperties(macrobenthosQuantitativeReq,monitorStationInfo);
                                    monitorStationInfo.setReportId(monitorDataReportId);
                                    //保存站位信息
                                    Long monitorStationInfoId = monitorStationInfoService.addMonitorStation(monitorStationInfo);

                                    BeanUtils.copyProperties(macrobenthosQuantitativeReq,macrobenthosQuantitative);
                                    macrobenthosQuantitative.setReportId(monitorDataReportId);
                                    macrobenthosQuantitative.setStationId(monitorStationInfoId);

                                    macrobenthosQuantitativeList.add(macrobenthosQuantitative);
                                }

                                if(testDataList7.size()>0){
                                    it=new IdText();
                                    it.setId(Integer.valueOf(code0));
                                    if(macrobenthosQuantitativeService.saveBatch(macrobenthosQuantitativeList)) {
                                        sb.append(code0+",");
                                        it.setText("true");
                                    }else {
                                        it.setText("false");
                                    }
                                    returnList.add(it);
                                }
                                testDataList7=null;
                                break;
                            case "PhytoplanktonExcelRule":
                                List<PhytoplanktonReq> testDataList8 = new ArrayList<>((List<PhytoplanktonReq>) data);

                                //鱼仔定量list
                                List<Phytoplankton> phytoplanktonList = new ArrayList<>();
                                //监测数据填报信息
                                MonitorDataReport monitorDataReport8 = new MonitorDataReport();
                                if(CollectionUtils.isNotEmpty(testDataList8)){
                                    BeanUtils.copyProperties(testDataList8.get(0),monitorDataReport8);

                                    //如果数据库中有和此填报表一样的数据 那么执行删除操作（删除（站位信息s+对应数据s+填报信息））
                                    monitorDataReportService.updateData(monitorDataReport8, Constant.phytoplanktonType,testDataList8.get(0).getDataType());

                                    //保存填报信息
                                    monitorDataReportId = monitorDataReportService.addMonitorDataReport(monitorDataReport8);
                                }


                                //将扩展类对拷到相应的实体类
                                for (PhytoplanktonReq phytoplanktonReq : testDataList8) {

                                    Phytoplankton phytoplankton = new Phytoplankton();
                                    MonitorStationInfo monitorStationInfo = new MonitorStationInfo();

                                    BeanUtils.copyProperties(phytoplanktonReq,monitorStationInfo);
                                    monitorStationInfo.setReportId(monitorDataReportId);
                                    //保存站位信息
                                    Long monitorStationInfoId = monitorStationInfoService.addMonitorStation(monitorStationInfo);

                                    BeanUtils.copyProperties(phytoplanktonReq,phytoplankton);
                                    phytoplankton.setReportId(monitorDataReportId);
                                    phytoplankton.setStationId(monitorStationInfoId);

                                    phytoplanktonList.add(phytoplankton);
                                }

                                if(testDataList8.size()>0){
                                    it=new IdText();
                                    it.setId(Integer.valueOf(code0));
                                    if(phytoplanktonService.saveBatch(phytoplanktonList)) {
                                        sb.append(code0+",");
                                        it.setText("true");
                                    }else {
                                        it.setText("false");
                                    }
                                    returnList.add(it);
                                }
                                testDataList8=null;
                                break;
                            case "SedimentExcelRule":
                                List<SedimentReq> testDataList9 = new ArrayList<>((List<SedimentReq>) data);

                                //鱼仔定量list
                                List<Sediment> sedimentList = new ArrayList<>();
                                //监测数据填报信息
                                MonitorDataReport monitorDataReport9 = new MonitorDataReport();
                                if(CollectionUtils.isNotEmpty(testDataList9)){
                                    BeanUtils.copyProperties(testDataList9.get(0),monitorDataReport9);

                                    //如果数据库中有和此填报表一样的数据 那么执行删除操作（删除（站位信息s+对应数据s+填报信息））
                                    monitorDataReportService.updateData(monitorDataReport9, Constant.sedimentType,testDataList9.get(0).getDataType());

                                    //保存填报信息
                                    monitorDataReportId = monitorDataReportService.addMonitorDataReport(monitorDataReport9);
                                }


                                //将扩展类对拷到相应的实体类
                                for (SedimentReq sedimentReq : testDataList9) {

                                    Sediment sediment = new Sediment();
                                    MonitorStationInfo monitorStationInfo = new MonitorStationInfo();

                                    BeanUtils.copyProperties(sedimentReq,monitorStationInfo);
                                    monitorStationInfo.setReportId(monitorDataReportId);
                                    //保存站位信息
                                    Long monitorStationInfoId = monitorStationInfoService.addMonitorStation(monitorStationInfo);

                                    BeanUtils.copyProperties(sedimentReq,sediment);
                                    sediment.setReportId(monitorDataReportId);
                                    sediment.setStationId(monitorStationInfoId);

                                    sedimentList.add(sediment);
                                }

                                if(testDataList9.size()>0){
                                    it=new IdText();
                                    it.setId(Integer.valueOf(code0));
                                    if(sedimentService.saveBatch(sedimentList)) {
                                        sb.append(code0+",");
                                        it.setText("true");
                                        //日志
                                        //sysLogService.saveLog(ShiroUtils.getUserId(),Constant.manualSurveyDataImport,"水体数据导入");
                                    }else {
                                        it.setText("false");
                                    }
                                    returnList.add(it);
                                }
                                testDataList9=null;
                                break;
                            case "SedimentgrainExcelRule":
                                List<SedimentgrainReq> testDataList10 = new ArrayList<>((List<SedimentgrainReq>) data);

                                //鱼仔定量list
                                List<Sedimentgrain> sedimentgrainList = new ArrayList<>();
                                //监测数据填报信息
                                MonitorDataReport monitorDataReport10 = new MonitorDataReport();
                                if(CollectionUtils.isNotEmpty(testDataList10)){
                                    BeanUtils.copyProperties(testDataList10.get(0),monitorDataReport10);

                                    //如果数据库中有和此填报表一样的数据 那么执行删除操作（删除（站位信息s+对应数据s+填报信息））
                                    monitorDataReportService.updateData(monitorDataReport10, Constant.sedimentgrainType,testDataList10.get(0).getDataType());

                                    //保存填报信息
                                    monitorDataReportId = monitorDataReportService.addMonitorDataReport(monitorDataReport10);
                                }


                                //将扩展类对拷到相应的实体类
                                for (SedimentgrainReq sedimentgrainReq : testDataList10) {

                                    Sedimentgrain sedimentgrain = new Sedimentgrain();
                                    MonitorStationInfo monitorStationInfo = new MonitorStationInfo();

                                    BeanUtils.copyProperties(sedimentgrainReq,monitorStationInfo);
                                    monitorStationInfo.setReportId(monitorDataReportId);
                                    //保存站位信息
                                    Long monitorStationInfoId = monitorStationInfoService.addMonitorStation(monitorStationInfo);

                                    BeanUtils.copyProperties(sedimentgrainReq,sedimentgrain);
                                    sedimentgrain.setReportId(monitorDataReportId);
                                    sedimentgrain.setStationId(monitorStationInfoId);

                                    sedimentgrainList.add(sedimentgrain);
                                }

                                if(testDataList10.size()>0){
                                    it=new IdText();
                                    it.setId(Integer.valueOf(code0));
                                    if(sedimentgrainService.saveBatch(sedimentgrainList)) {
                                        sb.append(code0+",");
                                        it.setText("true");
                                        //日志
                                        //sysLogService.saveLog(ShiroUtils.getUserId(),Constant.manualSurveyDataImport,"水体数据导入");
                                    }else {
                                        it.setText("false");
                                    }
                                    returnList.add(it);
                                }
                                testDataList10=null;
                                break;
                            case "SmallfishQualitativeExcelRule":
                                List<SmallfishQualitativeReq> testDataList11 = new ArrayList<>((List<SmallfishQualitativeReq>) data);

                                //鱼仔定量list
                                List<SmallfishQualitative> smallfishQualitativeList = new ArrayList<>();
                                //监测数据填报信息
                                MonitorDataReport monitorDataReport11 = new MonitorDataReport();
                                if(CollectionUtils.isNotEmpty(testDataList11)){
                                    BeanUtils.copyProperties(testDataList11.get(0),monitorDataReport11);

                                    //如果数据库中有和此填报表一样的数据 那么执行删除操作（删除（站位信息s+对应数据s+填报信息））
                                    monitorDataReportService.updateData(monitorDataReport11, Constant.smallfishQualitativeType,testDataList11.get(0).getDataType());

                                    //保存填报信息
                                    monitorDataReportId = monitorDataReportService.addMonitorDataReport(monitorDataReport11);
                                }


                                //将扩展类对拷到相应的实体类
                                for (SmallfishQualitativeReq smallfishQualitativeReq : testDataList11) {

                                    SmallfishQualitative smallfishQualitative = new SmallfishQualitative();
                                    MonitorStationInfo monitorStationInfo = new MonitorStationInfo();

                                    BeanUtils.copyProperties(smallfishQualitativeReq,monitorStationInfo);
                                    monitorStationInfo.setReportId(monitorDataReportId);
                                    //保存站位信息
                                    Long monitorStationInfoId = monitorStationInfoService.addMonitorStation(monitorStationInfo);

                                    BeanUtils.copyProperties(smallfishQualitativeReq,smallfishQualitative);
                                    smallfishQualitative.setReportId(monitorDataReportId);
                                    smallfishQualitative.setStationId(monitorStationInfoId);

                                    smallfishQualitativeList.add(smallfishQualitative);
                                }

                                if(testDataList11.size()>0){
                                    it=new IdText();
                                    it.setId(Integer.valueOf(code0));
                                    if(smallfishQualitativeService.saveBatch(smallfishQualitativeList)) {
                                        sb.append(code0+",");
                                        it.setText("true");
                                        //日志
                                        //sysLogService.saveLog(ShiroUtils.getUserId(),Constant.manualSurveyDataImport,"水体数据导入");
                                    }else {
                                        it.setText("false");
                                    }
                                    returnList.add(it);
                                }
                                testDataList11=null;
                                break;
                            case "SmallfishQuantitativeExcelRule":
                                List<SmallfishQuantitativeReq> testDataList12 = new ArrayList<>((List<SmallfishQuantitativeReq>) data);

                                //鱼仔定量list
                                List<SmallfishQuantitative> smallfishQuantitativeList = new ArrayList<>();
                                //监测数据填报信息
                                MonitorDataReport monitorDataReport12 = new MonitorDataReport();
                                if(CollectionUtils.isNotEmpty(testDataList12)){
                                    BeanUtils.copyProperties(testDataList12.get(0),monitorDataReport12);

                                    //如果数据库中有和此填报表一样的数据 那么执行删除操作（删除（站位信息s+对应数据s+填报信息））
                                    monitorDataReportService.updateData(monitorDataReport12, Constant.smallfishQuantitativeType,testDataList12.get(0).getDataType());

                                    //保存填报信息
                                    monitorDataReportId = monitorDataReportService.addMonitorDataReport(monitorDataReport12);
                                }


                                //将扩展类对拷到相应的实体类
                                for (SmallfishQuantitativeReq smallfishQuantitativeReq : testDataList12) {

                                    SmallfishQuantitative smallfishQuantitative = new SmallfishQuantitative();
                                    MonitorStationInfo monitorStationInfo = new MonitorStationInfo();

                                    BeanUtils.copyProperties(smallfishQuantitativeReq,monitorStationInfo);
                                    monitorStationInfo.setReportId(monitorDataReportId);
                                    //保存站位信息
                                    Long monitorStationInfoId = monitorStationInfoService.addMonitorStation(monitorStationInfo);

                                    BeanUtils.copyProperties(smallfishQuantitativeReq,smallfishQuantitative);
                                    smallfishQuantitative.setReportId(monitorDataReportId);
                                    smallfishQuantitative.setStationId(monitorStationInfoId);

                                    smallfishQuantitativeList.add(smallfishQuantitative);
                                }

                                if(testDataList12.size()>0){
                                    it=new IdText();
                                    it.setId(Integer.valueOf(code0));
                                    if(smallfishQuantitativeService.saveBatch(smallfishQuantitativeList)) {
                                        sb.append(code0+",");
                                        it.setText("true");
                                        //日志
                                        //sysLogService.saveLog(ShiroUtils.getUserId(),Constant.manualSurveyDataImport,"水体数据导入");
                                    }else {
                                        it.setText("false");
                                    }
                                    returnList.add(it);
                                }
                                testDataList12=null;
                                break;
                            case "SmallzooplanktonIinetExcelRule":
                                List<SmallzooplanktonIinetReq> testDataList13 = new ArrayList<>((List<SmallzooplanktonIinetReq>) data);

                                //鱼仔定量list
                                List<SmallzooplanktonIinet> smallzooplanktonIinetList = new ArrayList<>();
                                //监测站位信息List
                                List<MonitorStationInfo> monitorStationInfoList = new ArrayList<>();
                                //监测数据填报信息
                                MonitorDataReport monitorDataReport13 = new MonitorDataReport();
                                if(CollectionUtils.isNotEmpty(testDataList13)){
                                    BeanUtils.copyProperties(testDataList13.get(0),monitorDataReport13);

                                    //如果数据库中有和此填报表一样的数据 那么执行删除操作（删除（站位信息s+对应数据s+填报信息））
                                    monitorDataReportService.updateData(monitorDataReport13, Constant.smallzooplanktonIinetType,testDataList13.get(0).getDataType());

                                    //保存填报信息
                                    monitorDataReportId = monitorDataReportService.addMonitorDataReport(monitorDataReport13);
                                }


                                //将扩展类对拷到相应的实体类
                                for (SmallzooplanktonIinetReq smallzooplanktonIinetReq : testDataList13) {

                                    SmallzooplanktonIinet smallzooplanktonIinet = new SmallzooplanktonIinet();
                                    MonitorStationInfo monitorStationInfo = new MonitorStationInfo();

                                    BeanUtils.copyProperties(smallzooplanktonIinetReq,monitorStationInfo);
                                    monitorStationInfo.setReportId(monitorDataReportId);
                                    //保存站位信息
                                    Long monitorStationInfoId = monitorStationInfoService.addMonitorStation(monitorStationInfo);

                                    BeanUtils.copyProperties(smallzooplanktonIinetReq,smallzooplanktonIinet);
                                    smallzooplanktonIinet.setReportId(monitorDataReportId);
                                    smallzooplanktonIinet.setStationId(monitorStationInfoId);

                                    smallzooplanktonIinetList.add(smallzooplanktonIinet);
                                }

                                if(testDataList13.size()>0){
                                    it=new IdText();
                                    it.setId(Integer.valueOf(code0));
                                    if(smallzooplanktonIinetService.saveBatch(smallzooplanktonIinetList)) {
                                        sb.append(code0+",");
                                        it.setText("true");
                                        //日志
                                        //sysLogService.saveLog(ShiroUtils.getUserId(),Constant.manualSurveyDataImport,"水体数据导入");
                                    }else {
                                        it.setText("false");
                                    }
                                    returnList.add(it);
                                }
                                testDataList13=null;
                                break;
                            case "SwimminganimalIdentificationExcelRule":
                                List<SwimminganimalIdentificationReq> testDataList14 = new ArrayList<>((List<SwimminganimalIdentificationReq>) data);

                                //鱼仔定量list
                                List<SwimminganimalIdentification> swimminganimalIdentificationList = new ArrayList<>();
                                //监测数据填报信息
                                MonitorDataReport monitorDataReport14 = new MonitorDataReport();
                                if(CollectionUtils.isNotEmpty(testDataList14)){
                                    BeanUtils.copyProperties(testDataList14.get(0),monitorDataReport14);

                                    //如果数据库中有和此填报表一样的数据 那么执行删除操作（删除（站位信息s+对应数据s+填报信息））
                                    monitorDataReportService.updateData(monitorDataReport14, Constant.swimminganimalIdentificationType,testDataList14.get(0).getDataType());

                                    //保存填报信息
                                    monitorDataReportId = monitorDataReportService.addMonitorDataReport(monitorDataReport14);
                                }


                                //将扩展类对拷到相应的实体类
                                for (SwimminganimalIdentificationReq swimminganimalIdentificationReq : testDataList14) {

                                    SwimminganimalIdentification swimminganimalIdentification = new SwimminganimalIdentification();
                                    MonitorStationInfo monitorStationInfo = new MonitorStationInfo();

                                    BeanUtils.copyProperties(swimminganimalIdentificationReq,monitorStationInfo);
                                    monitorStationInfo.setReportId(monitorDataReportId);
                                    //保存站位信息
                                    Long monitorStationInfoId = monitorStationInfoService.addMonitorStation(monitorStationInfo);

                                    BeanUtils.copyProperties(swimminganimalIdentificationReq,swimminganimalIdentification);
                                    swimminganimalIdentification.setReportId(monitorDataReportId);
                                    swimminganimalIdentification.setStationId(monitorStationInfoId);

                                    swimminganimalIdentificationList.add(swimminganimalIdentification);
                                }

                                if(testDataList14.size()>0){
                                    it=new IdText();
                                    it.setId(Integer.valueOf(code0));
                                    if(swimminganimalIdentificationService.saveBatch(swimminganimalIdentificationList)) {
                                        sb.append(code0+",");
                                        it.setText("true");
                                        //日志
                                        //sysLogService.saveLog(ShiroUtils.getUserId(),Constant.manualSurveyDataImport,"水体数据导入");
                                    }else {
                                        it.setText("false");
                                    }
                                    returnList.add(it);
                                }
                                testDataList14=null;
                                break;
                            case "WaterqualityExcelRule":
                                List<WaterqualityReq> testDataList15 = new ArrayList<>((List<WaterqualityReq>) data);

                                //鱼仔定量list
                                List<Waterquality> waterqualityList = new ArrayList<>();
                                //监测数据填报信息
                                MonitorDataReport monitorDataReport15 = new MonitorDataReport();
                                if(CollectionUtils.isNotEmpty(testDataList15)){
                                    BeanUtils.copyProperties(testDataList15.get(0),monitorDataReport15);

                                    //如果数据库中有和此填报表一样的数据 那么执行删除操作（删除（站位信息s+对应数据s+填报信息））
                                    monitorDataReportService.updateData(monitorDataReport15, Constant.waterqualityType,testDataList15.get(0).getDataType());

                                    //保存填报信息
                                    monitorDataReportId = monitorDataReportService.addMonitorDataReport(monitorDataReport15);
                                }


                                //将扩展类对拷到相应的实体类
                                for (WaterqualityReq waterqualityReq : testDataList15) {

                                    Waterquality waterquality = new Waterquality();
                                    MonitorStationInfo monitorStationInfo = new MonitorStationInfo();

                                    BeanUtils.copyProperties(waterqualityReq,monitorStationInfo);
                                    monitorStationInfo.setReportId(monitorDataReportId);
                                    //保存站位信息
                                    Long monitorStationInfoId = monitorStationInfoService.addMonitorStation(monitorStationInfo);

                                    BeanUtils.copyProperties(waterqualityReq,waterquality);
                                    waterquality.setReportId(monitorDataReportId);
                                    waterquality.setStationId(monitorStationInfoId);

                                    waterqualityList.add(waterquality);
                                }

                                if(testDataList15.size()>0){
                                    it=new IdText();
                                    it.setId(Integer.valueOf(code0));
                                    if(waterqualityService.saveBatch(waterqualityList)) {
                                        sb.append(code0+",");
                                        it.setText("true");
                                        //日志
                                        //sysLogService.saveLog(ShiroUtils.getUserId(),Constant.manualSurveyDataImport,"水体数据导入");
                                    }else {
                                        it.setText("false");
                                    }
                                    returnList.add(it);
                                }
                                testDataList15=null;
                                break;
                            case "BirdObserveRecordRule":
                                List<BirdObserveRecordReq> testDataList16 = new ArrayList<>((List<BirdObserveRecordReq>) data);

                                //鸟类lsit
                                List<BirdObserveRecord> birdObserveRecordList = new ArrayList<>();
                                //鸟类主表
                                BirdObserve birdObserve = new BirdObserve();

                                //将扩展类对拷到相应的鸟类主表
                                Long birdObserveId=-1L;
                                if(CollectionUtils.isNotEmpty(testDataList16)){
                                    BeanUtils.copyProperties(testDataList16.get(0),birdObserve);
                                    //保存鸟类主表
                                    birdObserveId = birdObserveService.addBirdObserve(birdObserve);
                                }

                                //将扩展类对拷到相应的实体类
                                for (BirdObserveRecordReq birdObserveRecordReq : testDataList16) {
                                    BirdObserveRecord birdObserveRecord = new BirdObserveRecord();
                                    BeanUtils.copyProperties(birdObserveRecordReq,birdObserveRecord);
                                    birdObserveRecord.setId(birdObserveId);
                                    birdObserveRecordList.add(birdObserveRecord);
                                }

                                if(testDataList16.size()>0){
                                    it=new IdText();
                                    it.setId(Integer.valueOf(code0));
                                    if(birdObserveRecordService.saveBatch(birdObserveRecordList)) {
                                        sb.append(code0+",");
                                        it.setText("true");
                                        //日志
                                        //sysLogService.saveLog(ShiroUtils.getUserId(),Constant.manualSurveyDataImport,"水体数据导入");
                                    }else {
                                        it.setText("false");
                                    }
                                    returnList.add(it);
                                }
                                testDataList16=null;
                                break;
                            default:
                                break;
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }finally {

                    }

                }

            }

            if(rightFileCount==0)
                return Response.fail("列表内无有效数据");
            String successCode=sb.length()>0?sb.substring(0,sb.length()-1):"";
            // 从allMapList移除导入成功的数据项
            ListIterator<Map<String,Object>> mapIterator = allMapList.listIterator();
            while (mapIterator.hasNext()) {
                Map<String, Object> itMap = mapIterator.next();
                if(Arrays.asList(successCode.split("\\,")).contains(itMap.get("code").toString())){
                    mapIterator.remove();
                }
            }

            clearCache();//清空缓存文件夹

            if(allMapList.size()==0) {
                String fileName = new File("").getCanonicalPath()+config.getExcelJson()+userId+".json";
                File jsonFile = new File(fileName);
                if(jsonFile.exists()) jsonFile.delete();
            }else{
                // allMapList写回json文件
                fileTestDataUtil.allMapList2jsonFile(allMapList,userId);
            }
            return Response.ok(returnList);

        }catch (Exception e){
            return Response.fail("服务器故障");
        }
    }

    public void clearCache() {
        try {
            String filePath = config.getExcelUpLoad();
            File file = new File(filePath);
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    f.delete();
                }
            }
        } catch (Exception e) {

        }
    }

    /**
     * 文件持久化
     * @param file
     * @param i
     * @param excelType
     * @param allMapList
     * @return
     */
    public Map<String, Object> getAbsolutePath(MultipartFile file, int i, String excelType, List<Map<String, Object>> allMapList) throws IOException {
        //存储到服务器上的路径。
        String filePath = config.getExcelUpLoad();
        mkdirs(filePath);
        //获取文件名xxx.xls或xxx.xlsx
        String fileName = file.getOriginalFilename();

        //判断是否为IE浏览器的文件名，IE浏览器下文件名会带有盘符信息
        // Check for Unix-style path
        int unixSep = fileName.lastIndexOf('/');
        // Check for Windows-style path
        int winSep = fileName.lastIndexOf('\\');
        // Cut off at latest possible point
        int pos = (winSep > unixSep ? winSep : unixSep);
        if (pos != -1) {
            // Any sort of path separator found...
            fileName = fileName.substring(pos + 1);
        }
        //防止重名(时间戳+文件名)
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        fileName = df.format(new Date()) + fileName;
        //文件写入到指定位置
        FileTestDataUtil.write(file, filePath, fileName);
        String absolutePath = filePath + "/" + fileName;
        return importMetalCorr(absolutePath, i, excelType, allMapList);
    }

    /**
     *
     * @param absolutePath
     * @param no
     * @param excelType
     * @param allMapList
     * @return
     * @throws IOException
     */
    public Map<String, Object> importMetalCorr(String absolutePath, int no, String excelType, List<Map<String, Object>> allMapList) throws IOException {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        int result = 0;
        int errorCode = 0;
        List<Object> testDataList;
        try {
            //判断是否是Excel文件，不是返回错误信息
            boolean isExcel = FileTestDataUtil.isExcel(absolutePath);
            if (!isExcel) {//如果不是Excel格式文件
                result = -1;
            } else {
                //判断Excel格式（.xls还是.xlsx）
                boolean isExcel2003 = FileTestDataUtil.validateExcel(absolutePath);
                //从存储路径得到工作表
                Workbook workbook = FileTestDataUtil.getWorkBook(absolutePath, isExcel2003);
                //从表中得到数据testDataList
                map = fileTestDataUtil.readExcelValue(workbook, excelType, allMapList);

                if (map.get("result").equals(true)) {
                    testDataList = (List<Object>) map.get("data");
                    resultMap.put("result", true);
                    resultMap.put("data", testDataList);
                    resultMap.put("code", map.get("code"));
                    resultMap.put("excelType", excelType);
                    return resultMap;
                } else {
                    errorCode = (int) map.get("errorCode");
                    resultMap.put("code", map.get("code"));
                }
            }
            if (result == -1) {//上传的文件不是Excel文件
                resultMap.put("result", false);
                resultMap.put("errorCode", 1);
                resultMap.put("error", "请上传Excel文件");
            } else if (errorCode == 2) {//上传的Excel文件格式不正确
                resultMap.put("result", false);
                resultMap.put("errorCode", 2);
                resultMap.put("error", map.get("error"));
            } else if (errorCode == 3) {//数据格式不正确或有重复行
                resultMap.put("result", false);
                resultMap.put("errorCode", 3);
                resultMap.put("errorFormat", map.get("errorFormat"));
                resultMap.put("errorFormatList", map.get("errorFormatList"));
                resultMap.put("errorExis", map.get("errorExis"));
                resultMap.put("errorExisList", map.get("errorExisList"));
                resultMap.put("exisNum", map.get("exisNum"));
                resultMap.put("filePath", absolutePath);
            } else if (errorCode == 5) {//无有效数据/只有示例数据
                resultMap.put("result", false);
                resultMap.put("errorCode", 2);
                resultMap.put("error", "不存在有效数据");
            } else {
                resultMap.put("result", false);
                resultMap.put("errorCode", 4);
                resultMap.put("error", "未知错误");
            }
        } catch (Exception e) {
            if (resultMap.get("result") == null) {
                resultMap.put("result", false);
            }
            if (resultMap.get("errorCode") == null) {
                resultMap.put("errorCode", 4);
            }
            resultMap.put("error", "读取失败");
        } finally {
            if (resultMap.get("code") == null) resultMap.put("code", "无");
        }
        return resultMap;
    }

    /**
     * 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
     * @param destPath
     */
    public static void mkdirs(String destPath) {
        File file = new File(destPath);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }

    /**
     * 获得是那一种数据类型的Excel校验类型 对应xml校验文件
     * @param dataType
     * @return
     */
    public String getExcelType(Integer dataType){
        String excelType=null;
        switch (dataType){
            case Constant.biologicalQualityType:
                excelType="BiologicalQualityExcelRule";
                FileTestDataUtil.dataType=Constant.biologicalQualityName;
                break;
            case Constant.fisheggQualitativeType:
                excelType="FisheggQualitativeExcelRule";
                FileTestDataUtil.dataType=Constant.fisheggQualitativeName;
                break;
            case Constant.fisheggQuantitativeType:
                excelType="FisheggQuantitativeExcelRule";
                FileTestDataUtil.dataType=Constant.fisheggQuantitativeName;
                break;
            case Constant.hydrometeorologicalType:
                excelType="HydrometeorologicalExcelRule";
                FileTestDataUtil.dataType=Constant.hydrometeorologicalName;
                break;
            case Constant.intertidalzonebiologicalQuantitativeType:
                excelType="IntertidalzonebiologicalQuantitativeExcelRule";
                FileTestDataUtil.dataType=Constant.intertidalzonebiologicalQuantitativeName;
                break;
            case Constant.largezooplanktonInetType:
                excelType="LargezooplanktonInetExcelRule";
                FileTestDataUtil.dataType=Constant.largezooplankton_1_netName;
                break;
            case Constant.macrobenthosQualitativeType:
                excelType="MacrobenthosQualitativeExcelRule";
                FileTestDataUtil.dataType=Constant.macrobenthosQualitativeName;
                break;
            case Constant.macrobenthosQuantitativeType:
                excelType="MacrobenthosQuantitativeExcelRule";
                FileTestDataUtil.dataType=Constant.macrobenthosQuantitativeName;
                break;
            case Constant.phytoplanktonType:
                excelType="PhytoplanktonExcelRule";
                FileTestDataUtil.dataType=Constant.phytoplanktonName;
                break;
            case Constant.sedimentType:
                excelType="SedimentExcelRule";
                FileTestDataUtil.dataType=Constant.sedimentName;
                break;
            case Constant.sedimentgrainType:
                excelType="SedimentgrainExcelRule";
                FileTestDataUtil.dataType=Constant.sedimentgrainName;
                break;
            case Constant.smallfishQualitativeType:
                excelType="SmallfishQualitativeExcelRule";
                FileTestDataUtil.dataType=Constant.smallfishQualitativeName;
                break;
            case Constant.smallfishQuantitativeType:
                excelType="SmallfishQuantitativeExcelRule";
                FileTestDataUtil.dataType=Constant.smallfishQuantitativeName;
                break;
            case Constant.smallzooplanktonIinetType:
                excelType="SmallzooplanktonIinetExcelRule";
                FileTestDataUtil.dataType=Constant.smallzooplankton_2_netName;
                break;
            case Constant.swimminganimalIdentificationType:
                excelType="SwimminganimalIdentificationExcelRule";
                FileTestDataUtil.dataType=Constant.swimminganimalIdentificationName;
                break;
            case Constant.waterqualityType:
                excelType="WaterqualityExcelRule";
                FileTestDataUtil.dataType=Constant.waterqualityName;
                break;
            case Constant.birdObserveRecordType:
                excelType="BirdObserveRecordRule";
                FileTestDataUtil.dataType=Constant.birdObserveRecordName;
                break;
            case Constant.vegetationSurveyRecordType:
                excelType="VegetationSurveyRecordRule";
                FileTestDataUtil.dataType=Constant.vegetationSurveyRecordName;
                break;
            default:
                excelType="";
                break;
        }
        return excelType;
    }
}
