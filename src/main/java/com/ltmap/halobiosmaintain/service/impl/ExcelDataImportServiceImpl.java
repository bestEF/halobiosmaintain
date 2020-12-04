package com.ltmap.halobiosmaintain.service.impl;

import com.ltmap.halobiosmaintain.common.result.Response;
import com.ltmap.halobiosmaintain.common.utils.excel_import.Config;
import com.ltmap.halobiosmaintain.common.utils.excel_import.FileTestDataUtil;
import com.ltmap.halobiosmaintain.common.utils.excel_import.IdText;
import com.ltmap.halobiosmaintain.config.Constant;
import com.ltmap.halobiosmaintain.entity.work.FisheggQuantitative;
import com.ltmap.halobiosmaintain.entity.work.MonitorDataReport;
import com.ltmap.halobiosmaintain.entity.work.MonitorStationInfo;
import com.ltmap.halobiosmaintain.service.ExcelDataImportService;
import com.ltmap.halobiosmaintain.service.IFisheggQuantitativeService;
import com.ltmap.halobiosmaintain.service.IMonitorDataReportService;
import com.ltmap.halobiosmaintain.service.IMonitorStationInfoService;
import com.ltmap.halobiosmaintain.vo.req.FisheggQuantitativeReq;
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
    private IFisheggQuantitativeService fisheggQuantitativeService;
    @Resource
    private IMonitorDataReportService monitorDataReportService;
    @Resource
    private IMonitorStationInfoService monitorStationInfoService;

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
                        switch (resultMap.get("excelType").toString()){
                            case "FisheggQuantitativeExcelRule":
                                List<FisheggQuantitativeReq> testDataList0 = new ArrayList<>((List<FisheggQuantitativeReq>) data);

                                //鱼仔定量list
                                List<FisheggQuantitative> fisheggQuantitativeList = new ArrayList<>();
                                //监测站位信息List
                                List<MonitorStationInfo> monitorStationInfoList = new ArrayList<>();
                                //监测数据填报信息
                                MonitorDataReport monitorDataReport = new MonitorDataReport();
                                if(CollectionUtils.isNotEmpty(testDataList0)){
                                    BeanUtils.copyProperties(testDataList0.get(0),monitorDataReport);

                                    //如果数据库中有和此填报表一样的数据 那么执行删除操作（删除（站位信息s+对应数据s+填报信息））
                                    monitorDataReportService.updateData(monitorDataReport, Constant.fisheggQuantitativeType);

                                    //保存填报信息
                                    boolean saveMonitorDataReportFlag = monitorDataReportService.save(monitorDataReport);
                                }


                                //将扩展类对拷到相应的实体类
                                for (FisheggQuantitativeReq fisheggQuantitativeReq : testDataList0) {

                                    FisheggQuantitative fisheggQuantitative = new FisheggQuantitative();
                                    MonitorStationInfo monitorStationInfo = new MonitorStationInfo();

                                    BeanUtils.copyProperties(fisheggQuantitativeReq,monitorStationInfo);
                                    monitorStationInfo.setReportId(monitorDataReport.getReportId());
                                    //保存站位信息
                                    boolean saveMonitorStationInfoFlag = monitorStationInfoService.save(monitorStationInfo);

                                    BeanUtils.copyProperties(fisheggQuantitativeReq,fisheggQuantitative);
                                    fisheggQuantitative.setReportId(monitorDataReport.getReportId());
                                    fisheggQuantitative.setStationId(monitorStationInfo.getStationId());

                                    fisheggQuantitativeList.add(fisheggQuantitative);
                                }

                                if(testDataList0.size()>0){
                                    it=new IdText();
                                    it.setId(Integer.valueOf(code0));
                                    if(fisheggQuantitativeService.saveBatch(fisheggQuantitativeList)) {
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
            case Constant.fisheggQuantitativeType:
                excelType="FisheggQuantitativeExcelRule";
                FileTestDataUtil.dataType=Constant.fisheggQuantitativeName;
                break;
            default:
                excelType="";
                break;
        }
        return excelType;
    }
}
