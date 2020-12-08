package com.ltmap.halobiosmaintain.common.utils.excel_import;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ltmap.halobiosmaintain.config.Constant;
import com.ltmap.halobiosmaintain.controller.ExcelDataImportController;
import com.ltmap.halobiosmaintain.service.IMonitorDataReportService;
import com.ltmap.halobiosmaintain.vo.req.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * excel文件导入主类
 * @author fjh
 * @date 2020/11/30 20:37
 */
@SuppressWarnings("rawtypes")
@Slf4j
@Component
public class FileTestDataUtil {
    @Resource
    private Config config;
    @Resource
    private ExcelDataImportController excelDataImportController;
    @Resource
    private IMonitorDataReportService monitorDataReportService;

    /**年份**/
    public static String YEAR;
    /**航次**/
    public static String VOYAGE;
    /**当前表格数据类型**/
    public static String dataType;


    public static ParseXMLUtil parseXmlUtil;

    static String err1="存在不允许的空数据；";
    static String err2="存在不允许的空值；";
    static String err3="存在不合法的整数格式；";
    static String err4="存在不合法的日期格式；";
    static String err5="存在长度超限的数据；";
    static String err6="含有不存在的站位信息；";
    static String err7="存在与已有数据冲突的数据；";
    static String err8="存在不合法的小数格式；";

    String msg1="上传文件中存在与数据库重复的数据；";
    String msg2="上传文件中存在格式错误数据；";
    //String msg3="上传文件中部分试样编号与委托单位不匹配；";
    String msg4="文件列表中存在重复的数据；";

    public static Integer code=0;//用于标识各文件的code编号

    // 解析出的数据信息的集合
    public static List<Object> dataList = new ArrayList<>();
    public static List<Object> dataList1 = new ArrayList<>();
    public static List<Object> dataList2 = new ArrayList<>();
    public static List<Object> dataList3 = new ArrayList<>();
    public static StringBuffer errorString;
    public static List<Map<String,Object>> errorList = new ArrayList<>();
    public static List<Map<String,Object>> standardList = new ArrayList<>();
    public static StringBuffer existedMsg;
    //-表头map对象：key:entityCode, value:headMap(index,headTitle)
    public static Map curEntityHeadMap;
    /**当前实体类的code**/
    public static String curEntityCode;


    /**
     * 将前台传过来的文件在服务器/本地持久化
     * @param file
     * @param filePath
     * @param fileName
     */
    public static void write(MultipartFile file, String filePath, String fileName) {
        try {
            //在指定目录下建立一个空的文件,作用:别的file文件往里写入
            File targetFile = new File(filePath, fileName);
            if(!targetFile.exists()){
                targetFile.createNewFile();
            }
            //保存
            //将前台传过来的file文件写到targetFile中.
            file.transferTo(targetFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @title isExcel
     * @description 验证格式是否为Excel
     * @author LiLu
     * @date 2018年9月27日 下午2:19:44
     * @param filePath
     * @return
     */
    public static boolean isExcel(String filePath) {
        if (filePath != null && (WDWUtil.isExcel2003(filePath) || WDWUtil.isExcel2007(filePath))) {
            return true;
        }
        return false;
    }

    /**
     * @title validateExcel
     * @description 验证格式是否为Excel
     * @author LiLu
     * @date 2018年8月10日 上午10:15:10
     * @param filePath
     * @return
     */
    public static boolean validateExcel(String filePath){
        if (WDWUtil.isExcel2003(filePath)){
            return true;
        }
        return false;
    }

    /**
     * 从指定路径创建工作表
     * @param absolutePath
     * @param isExcel2003
     * @return
     */
    public static Workbook getWorkBook(String absolutePath, boolean isExcel2003){
        FileInputStream inputStream =null;
        Workbook workbook = null;
        try{
            // 获取一个绝对地址的流
            inputStream = new FileInputStream(absolutePath);
            if(isExcel2003){
                workbook = new HSSFWorkbook(inputStream);
            }
            else{//当excel是2007时
                workbook = new XSSFWorkbook(inputStream);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return workbook;
    }

    // 校验的excel信息放在allMapList作为缓存
    public List<Map<String, Object>> jsonFile2allMapList(String userId){
        String jsonStr = "";
        try {
            String fileName = new File("").getCanonicalPath()+config.getExcelJson()+userId+".json";
            File jsonFile = new File(fileName);
            if(jsonFile.exists()){
                FileReader fileReader = new FileReader(jsonFile);
                Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
                int ch = 0;
                StringBuffer sb = new StringBuffer();
                while ((ch = reader.read()) != -1) {
                    sb.append((char) ch);
                }
                fileReader.close();
                reader.close();
                jsonStr = sb.toString();

                ParserConfig.getGlobalInstance().addAccept("com.taobao.pac.client.sdk.dataobject.");
                ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
                List<Object> list = JSON.parseArray(jsonStr);
                List< Map<String,Object>> allMapList = new ArrayList<>();
                for (Object object : list){
                    Map <String,Object> ret = (Map<String, Object>) object;//取出list里面的值转为map
                    allMapList.add(ret);
                }
                return allMapList;
            }else{
                return new ArrayList<>();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从表中得到数据testDataList
     * @param workbook
     * @param excelType
     * @param allMapList
     * @return
     */
    public Map<String, Object> readExcelValue(Workbook workbook,String excelType,List<Map<String, Object>> allMapList) {
        dataList = new ArrayList<>();
        dataList1 = new ArrayList<>();
        dataList2 = new ArrayList<>();
        dataList3 = new ArrayList<>();

        //用于判断有没有真实数据的变量 真实数据是从第5+1行开始的 但还有一行填报人等信息。所以这里定为7。
        int realDataFlag=7;                                                                                                                                                                                                                                                                                                                                                 ;

        // 用于返回结果
        Map<String, Object> map = new HashMap<>();
        int totalRows = 0;
        int totalCells = 0;
        Map headMap = new HashMap<>();
        File directory = new File("");
        try{
            String filePath = directory.getCanonicalPath()+config.getExcelRules()+excelType+".xml";
            File xmlFile = new File(filePath);
            parseXmlUtil = new ParseXMLUtil(xmlFile);// 解析xml
        }catch (IOException e){
            map.put("result", false);
            map.put("errorCode", 4);
            map.put("error", "未知错误");
            return map;
        }
        errorString = new StringBuffer();
        errorList = new ArrayList<>();
        standardList = new ArrayList<>();

        existedMsg = new StringBuffer();
        List<Integer> exisList = new ArrayList<>();
        int exisNum = 0;
        // 得到第一个sheet
        // 验证方法的参数这里开始
        Sheet sheet = (Sheet) workbook.getSheetAt(0);
        String entityName = workbook.getSheetName(0);

        //Excel标题栏为两行的记录至数组
        String[] tblNames={"PlanktonSiteDataDiversity"};
        int titleRows=1;
        //若Excel标题为1行，titleType=False;若Excel标题为2行，titleType=True
        boolean titleType=false;
        if(Arrays.asList(tblNames).contains(excelType)) {
            titleRows=2;
            titleType=true;
        }

        // 得到Excel的行数
        totalRows = sheet.getLastRowNum();
        totalRows++;
        // 得到Excel的列数(前提是有行数)
        if (totalRows >= titleRows+1) {
            totalCells = sheet.getRow(titleRows).getPhysicalNumberOfCells();
        }

        String[] differTblHead={"SedimentgrainExcelRule"};
        boolean isHeadRight=false;
        if(Arrays.asList(differTblHead).contains(excelType)){
            isHeadRight = readSpecialSheetHeadData(sheet,entityName,totalRows,totalCells,titleType,excelType);
            realDataFlag=realDataFlag+1;
        }else {
            isHeadRight = readSheetHeadData(sheet,entityName,totalRows,totalCells,titleType,excelType);//表头
        }

        if(isHeadRight){
            if (totalRows>=realDataFlag) {
                headMap = (Map) getCurEntityHeadMap().get(getCurEntityCode());

                List<String> returnList=new ArrayList<>();

                //从第5+1行读数据 对有些表特殊处理从第6+1行读数据
                //-2是因为计算机从0开始计数所以-1 又因为有一行填报人等信息所以再次-1
                //-1是因为最后一行填的是填报人等信息
                for (int r = realDataFlag-2; r < totalRows-1; r++) {//for (int r = 1; r < totalRows; r++) {
                    Row row = sheet.getRow(r);
                    if (isBlankRow(row)) {
                        continue;
                    }

                    // 按不同文件类型执行校验
                    returnList=classify(totalCells,totalRows,row,headMap,entityName,r,excelType,allMapList,sheet);

                    // 查重
                    Boolean dataExist=false;//=Boolean.valueOf(returnList.get(0));// 0：数据库重复数据
                    Boolean sameInfo=Boolean.valueOf(returnList.get(1));// 1：Excel文件列表重复数据
                    boolean exist=false;
                    if(dataExist) {
                        if(!existedMsg.toString().contains(msg1)) existedMsg.append(msg1);
                        if(!exist){
                            exisList.add(r);
                            exisNum++;
                            exist=true;
                        }
                    }
                    if(sameInfo) {//excel自我对比查重
                        if(!existedMsg.toString().contains(msg4)) existedMsg.append(msg4);
                        if(!exist){
                            exisList.add(r);
                            exisNum++;
                            exist=true;
                        }
                    }
                    exisList=removeDuplicate(exisList);

                }

                // 将dataList*的数据存入dataList
                if(returnList.size()>=2 && StringUtils.isNotBlank(returnList.get(2))) {
                    switch (returnList.get(2)) {// 2：每个导入页面最多有3种模板，此处用于指示第几种模板
                        case "1":
                            dataList = dataList1;
                            break;
                        case "2":
                            dataList = dataList2;
                            break;
                        case "3":
                            dataList = dataList3;
                            break;
                        default:
                            break;
                    }
                }
                if(dataList.size()==0  && getErrorString().length() == 0 && getExistedMsg().length() == 0) {
                    map.put("result",false);
                    map.put("errorCode", 5);//无有效数据/只有示例数据
                }else if(getErrorString().length() == 0 && getExistedMsg().length() == 0){//如果没有任何错误，就保存
                    map.put("result",true);
                    map.put("data", dataList);
                }else{
                    map.put("result",false);
                    map.put("errorCode", 3);//数据格式不正确或有重复行
                    if(getExistedMsg().length() > 0) {
                        map.put("errorExis", existedMsg.substring(0,existedMsg.length()-1));
                        map.put("errorExisList", exisList);
                        map.put("exisNum", exisNum);

                        Map<String,Object> errMap=new HashMap<>();
                        List<Integer> copyList = new ArrayList<>();
                        copyList.addAll(exisList);
                        //exisList与errorList的重叠项处理
                        ListIterator<Integer> existIterator = copyList.listIterator();
                        Map<String,Object> mapIterator=new HashMap<>();
                        // 重复检验
                        Integer row;
                        while (existIterator.hasNext()) {
                            row=existIterator.next();
                            for(Map<String,Object> errorMap:errorList){
                                // exisList与errorList的重叠项处理
                                if(Integer.parseInt(errorMap.get("curCol").toString())==totalCells && Integer.parseInt(errorMap.get("curRow").toString())==row+1) {
                                    errorMap.put("rulMsg",errorMap.get("rulMsg")+"；重复的数据项");
                                    existIterator.remove();
                                }
                            }
                        }
                        //exisList与errorList的非重叠项处理
                        for(Integer r:copyList){
                            errMap=new HashMap<>();
                            errMap.put("curRow",r+1);
                            errMap.put("curCol",totalCells);
                            errMap.put("rulMsg","重复的数据项");
                            errorList.add(errMap);
                        }
                    }
                    if(getErrorString().length() > 0) {
                        // standardList→errorList
                        List<Map<String,Object>> copyList=new ArrayList<>();
                        copyList.addAll(standardList);// 各单元格制定的规范
                        Map<String,Object> errMap=new HashMap<>();
                        ListIterator<Map<String,Object>> standardIterator = copyList.listIterator();
                        while (standardIterator.hasNext()) {
                            errMap=standardIterator.next();
                            for(Map<String,Object> errorMap:errorList) {
                                Integer r = Integer.parseInt(errMap.get("r").toString());
                                Integer c = Integer.parseInt(errMap.get("c").toString());
                                // standardList与errorList的重叠项处理
                                if (Integer.parseInt(errorMap.get("curCol").toString()) == c && Integer.parseInt(errorMap.get("curRow").toString()) == r) {
                                    errorMap.put("rulMsg", errorMap.get("rulMsg") + "；"+errMap.get("msg"));
                                    standardIterator.remove();
                                }
                            }
                        }
                        for(Map<String,Object> copyMap:copyList){
                            errMap=new HashMap<>();
                            errMap.put("curRow",Integer.parseInt(copyMap.get("r").toString()));
                            errMap.put("curCol",copyMap.get("c"));
                            errMap.put("rulMsg",copyMap.get("msg"));
                            errorList.add(errMap);
                        }
                        map.put("errorFormat", errorString.substring(0,errorString.length()-1));
                        map.put("errorFormatList", errorList);
                    }else if(getExistedMsg().length() > 0){
                        map.put("errorFormat", null);
                        map.put("errorFormatList", errorList);
                    }
                }

            } else {
                map.put("result", false);
                map.put("errorCode", 2);
                map.put("error", "Excel文件无数据");
            }
        }else {
            map.put("result", false);
            map.put("errorCode", 2);
            map.put("error", "Excel文件格式错误，请下载正确模板再导入！");
        }
        map.put("code",code++);//code用于标识列表内不同文件，所有文件统一添加code是为了防止取不到值
        return map;
    }

    public static StringBuffer getExistedMsg() {
        return existedMsg;
    }

    public static StringBuffer getErrorString() {
        return errorString;
    }

    // List去重
    public static List removeDuplicate(List list){
        List listTemp = new ArrayList();
        for(int i=0;i<list.size();i++){
            if(!listTemp.contains(list.get(i))){
                listTemp.add(list.get(i));
            }
        }
        return listTemp;
    }

    // 按不同类型划分不同方法
    private List<String> classify(Integer totalCells,Integer totalRows,Row row,Map headMap,String entityName,int r,String type,List<Map<String, Object>> allMapList,Sheet sheet){
        List<String> list=new ArrayList<>();
        switch (type){
            case "BiologicalQualityExcelRule":
                list = biologicalQualityExcelRule(totalCells,totalRows,row,headMap,entityName,r,allMapList,sheet);
                list.add("1");
                break;
            case "FisheggQualitativeExcelRule":
                list = fisheggQualitativeExcelRule(totalCells,totalRows,row,headMap,entityName,r,allMapList,sheet);
                list.add("1");
                break;
            case "FisheggQuantitativeExcelRule":// 鱼卵定量数据表
                list = fisheggQuantitativeExcelRule(totalCells,totalRows,row,headMap,entityName,r,allMapList,sheet);
                list.add("1");
                break;
            case "HydrometeorologicalExcelRule":
                list = hydrometeorologicalExcelRule(totalCells,totalRows,row,headMap,entityName,r,allMapList,sheet);
                list.add("1");
                break;
            case "IntertidalzonebiologicalQuantitativeExcelRule":
                list = intertidalzonebiologicalQuantitativeExcelRule(totalCells,totalRows,row,headMap,entityName,r,allMapList,sheet);
                list.add("1");
                break;
            case "LargezooplanktonInetExcelRule":
                list = largezooplanktonInetExcelRule(totalCells,totalRows,row,headMap,entityName,r,allMapList,sheet);
                list.add("1");
                break;
            case "MacrobenthosQualitativeExcelRule":
                list = macrobenthosQualitativeExcelRule(totalCells,totalRows,row,headMap,entityName,r,allMapList,sheet);
                list.add("1");
                break;
            case "MacrobenthosQuantitativeExcelRule":
                list = macrobenthosQuantitativeExcelRule(totalCells,totalRows,row,headMap,entityName,r,allMapList,sheet);
                list.add("1");
                break;
            case "PhytoplanktonExcelRule":
                list = phytoplanktonExcelRule(totalCells,totalRows,row,headMap,entityName,r,allMapList,sheet);
                list.add("1");
                break;
            case "SedimentExcelRule":
                list = sedimentExcelRule(totalCells,totalRows,row,headMap,entityName,r,allMapList,sheet);
                list.add("1");
                break;
            case "SedimentgrainExcelRule":
                list = sedimentgrainExcelRule(totalCells,totalRows,row,headMap,entityName,r,allMapList,sheet);
                list.add("1");
                break;
            case "SmallfishQualitativeExcelRule":
                list = smallfishQualitativeExcelRule(totalCells,totalRows,row,headMap,entityName,r,allMapList,sheet);
                list.add("1");
                break;
            case "SmallfishQuantitativeExcelRule":
                list = smallfishQuantitativeExcelRule(totalCells,totalRows,row,headMap,entityName,r,allMapList,sheet);
                list.add("1");
                break;
            case "SmallzooplanktonIinetExcelRule":
                list = smallzooplanktonIinetExcelRule(totalCells,totalRows,row,headMap,entityName,r,allMapList,sheet);
                list.add("1");
                break;
            case "SwimminganimalIdentificationExcelRule":
                list = swimminganimalIdentificationExcelRule(totalCells,totalRows,row,headMap,entityName,r,allMapList,sheet);
                list.add("1");
                break;
            case "WaterqualityExcelRule":
                list = waterqualityExcelRule(totalCells,totalRows,row,headMap,entityName,r,allMapList,sheet);
                list.add("1");
                break;
            default:
                break;
        }
        return list;
    }

    //鱼卵定量数据表
    private List<String> waterqualityExcelRule(Integer totalCells,Integer totalRows,Row row,Map headMap,String entityName,int r,List<Map<String, Object>> allMapList,Sheet sheet){
        List<String> returnList=new ArrayList<>();
        WaterqualityReq waterqualityReq = new WaterqualityReq();
        int totalRst = 0;//每行格式错误单元格的数目
        int validaterst = 0;

        //将年份和航次信息写入对象
        waterqualityReq.setYear(YEAR);
        waterqualityReq.setVoyage(VOYAGE);
        waterqualityReq.setDataType(dataType);

        //对9种特殊表头value值 按xml规则校验，并写入对象
        specialHandlingWaterquality(sheet,headMap,entityName,waterqualityReq,totalRows);

        //取
        Row excelheadRow1 = sheet.getRow(Constant.differInfoStartRow);
        int excelLastCellNum = excelheadRow1.getLastCellNum();

        // 循环row的列，按xml规则校验，并写入对象
        for (int c = 0; c < excelLastCellNum; c++) {
            Cell cell = row.getCell(c);
            String headTitle = headMap.get(c+Constant.constantTableHeadCount).toString();
            /**按规则验证cell格式**/
            validaterst = validateCellData(r+1,c+1,cell,entityName,headTitle);
            totalRst += validaterst;

            if(totalRst == 0 && cell != null) {             // 定制
                //6代表xml文件第6个 下面同理
                if(c+Constant.officialDataStartSign==6){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    waterqualityReq.setStationName(cell.getStringCellValue());
                } else if(c+Constant.officialDataStartSign==7){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    waterqualityReq.setPlanLon(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==8){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    waterqualityReq.setPlanLat(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==9){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    waterqualityReq.setRealLon(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==10){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    waterqualityReq.setRealLat(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==11){
                    if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                    }
                    try{
                        String strDate=getStringCellValue(cell);
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                        LocalDate localDate=null;
                        if(strDate.contains("-")){
                            dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else if(strDate.contains(".")){
                            dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else if(strDate.contains("/")){
                            dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else{
                            Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                            localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        }
                        waterqualityReq.setMonitorDate(localDate);
                    } catch (Exception e){
                        waterqualityReq.setMonitorDate(null);
                    }
                } else if(c+Constant.officialDataStartSign==12){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)){
                        waterqualityReq.setWaterDepth(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==13){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        waterqualityReq.setSamplingLevel(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==14){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        waterqualityReq.setSamplingDepth(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==15){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        waterqualityReq.setDorjy(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==16){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        waterqualityReq.setCod(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==17){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        waterqualityReq.setYd(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==18){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        waterqualityReq.setXfw(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==19){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        waterqualityReq.setHxlsy(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==20){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        waterqualityReq.setYxsyD(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==21){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        waterqualityReq.setXsyD(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==22){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        waterqualityReq.setAD(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==23){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        waterqualityReq.setGsy(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==24){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        waterqualityReq.setYlsA(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==25){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        waterqualityReq.setSyl(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==26){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        waterqualityReq.setPh(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==27){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        waterqualityReq.setYjl(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==28){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        waterqualityReq.setToc(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==29){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        waterqualityReq.setTp(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==30){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        waterqualityReq.setTn(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==31){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        waterqualityReq.setAss(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==32){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        waterqualityReq.setHg(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==33){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        waterqualityReq.setCu(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==34){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        waterqualityReq.setPb(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==35){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        waterqualityReq.setCd(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==36){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        waterqualityReq.setZn(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==37){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        waterqualityReq.setCr(new BigDecimal(cellValue));
                    }
                }
            }

        }

        //数据库更新 注意返回false代表更新成功
//        MonitorDataReport monitorDataReport = new MonitorDataReport();
//        BeanUtils.copyProperties(waterqualityReq,monitorDataReport);
        boolean dataExist=false; //= monitorDataReportService.updateData(monitorDataReport, Constant.fisheggQuantitativeType);
        returnList.add(String.valueOf(dataExist));

        // Excel文件列表查重(遍历allMapList中底栖站位多样性数据，检索是否已包含当前数据)
        boolean sameInfo=false;
        WaterqualityReq item;
        for(Map<String,Object> map:allMapList){//for(Map<String,Object> map:dominantSpeciesController.allMapList){
            if(map.get("excelType")!=null && map.get("excelType").toString().equals("WaterqualityExcelRule")) {
                for(Object obj:(List<Object>)map.get("data")) {
                    item=(WaterqualityReq)obj;
                    if(
                            item.getMonitoringArea().equals(waterqualityReq.getMonitoringArea()) &&
                                    item.getEcologicalType().equals(waterqualityReq.getEcologicalType()) &&
                                    item.getTaskDate().equals(waterqualityReq.getTaskDate()) &&
                                    item.getMonitorCompany().equals(waterqualityReq.getMonitorCompany()) &&
                                    item.getOrganizationCompany().equals(waterqualityReq.getOrganizationCompany()) &&
                                    item.getReportDate().equals(waterqualityReq.getReportDate()) &&
                                    item.getYear().equals(waterqualityReq.getYear()) &&
                                    item.getVoyage().equals(waterqualityReq.getVoyage())) {
                        sameInfo = true;
                    }
                }
            }
        }
        returnList.add(String.valueOf(sameInfo));

        // 校验合格：写入dataList
        if(totalRst==0){
            dataList1.add(waterqualityReq);
            excelDataImportController.excelDataList.add(waterqualityReq);
        }
        return returnList;
    }

    /**
     * 对鱼卵定量-9种特殊表头value值 按xml规则校验，并写入对象
     * @param sheet
     * @param headMap
     * @param entityName
     * @param waterqualityReq
     */
    public void specialHandlingWaterquality(Sheet sheet,Map headMap,String entityName,WaterqualityReq waterqualityReq,Integer totalRows){
        int totalRst = 0;//每行格式错误单元格的数目
        int validaterst = 0;

        //取第1+1行
        Row row = sheet.getRow(1);
        //存放监控区值
        //1代表第1+1列
        Cell cell = row.getCell(1);
        //0代表监控区
        String headTitle = headMap.get(Constant.monitoringAreaCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            waterqualityReq.setMonitoringArea(cell.getStringCellValue());
        }

        //存放生态类型值
        //10代表10+1列
        cell = row.getCell(10);
        headTitle = headMap.get(Constant.ecologicaltypeCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,10+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            waterqualityReq.setEcologicalType(cell.getStringCellValue());
        }

        //存放任务日期
        //18代表18+1列
        cell = row.getCell(18);
        headTitle = headMap.get(Constant.missionDateCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,18+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            //判断该日期在excel表格的格式是否是日期格式
            if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }

            try{
                String strDate=getStringCellValue(cell);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate=null;
                if(strDate.contains("-")){
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains(".")){
                    dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains("/")){
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else{
                    Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                waterqualityReq.setTaskDate(localDate);
            } catch (Exception e){
                waterqualityReq.setTaskDate(null);
            }
        }

        //取第2+1行
        row = sheet.getRow(2);
        //存放监测单位
        //1代表第1+1列
        cell = row.getCell(1);
        headTitle = headMap.get(Constant.monitoringUnitCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            waterqualityReq.setMonitorCompany(cell.getStringCellValue());
        }

        //存放组织单位
        //10代表10+1列
        cell = row.getCell(10);
        headTitle = headMap.get(Constant.organizationalUnitCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,10+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            waterqualityReq.setOrganizationCompany(cell.getStringCellValue());
        }

        //存放填报日期
        //18代表18+1列
        cell = row.getCell(18);
        headTitle = headMap.get(Constant.completionDateCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,18+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制

            //判断该日期在excel表格的格式是否是日期格式
            if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }

            try{
                String strDate = getStringCellValue(cell);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate=null;
                if(strDate.contains("-")){
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains(".")){
                    dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains("/")){
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else{
                    Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                waterqualityReq.setReportDate(localDate);
            } catch (Exception e){
                waterqualityReq.setReportDate(null);
            }
        }


        //取最后一行
        row = sheet.getRow(totalRows - 1);
        //存放填报人
        //1代表第1+1列
        cell = row.getCell(1);
        headTitle = headMap.get(Constant.informantCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            waterqualityReq.setByzd1(cell.getStringCellValue());
        }

        //存放校对人
        cell = row.getCell(4);
        headTitle = headMap.get(Constant.proofreaderCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,4+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            waterqualityReq.setByzd2(cell.getStringCellValue());
        }

        //存放审核人
        cell = row.getCell(7);
        headTitle = headMap.get(Constant.auditorCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,7+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            waterqualityReq.setByzd3(cell.getStringCellValue());
        }

    }

    //生物质量数据表
    private List<String> swimminganimalIdentificationExcelRule(Integer totalCells,Integer totalRows,Row row,Map headMap,String entityName,int r,List<Map<String, Object>> allMapList,Sheet sheet){
        List<String> returnList=new ArrayList<>();
        SwimminganimalIdentificationReq swimminganimalIdentificationReq = new SwimminganimalIdentificationReq();
        int totalRst = 0;//每行格式错误单元格的数目
        int validaterst = 0;

        //将年份和航次信息写入对象
        swimminganimalIdentificationReq.setYear(YEAR);
        swimminganimalIdentificationReq.setVoyage(VOYAGE);
        swimminganimalIdentificationReq.setDataType(dataType);

        //对9种特殊表头value值 按xml规则校验，并写入对象
        specialHandlingSwimminganimalIdentification(sheet,headMap,entityName,swimminganimalIdentificationReq,totalRows);

        //取
        Row excelheadRow1 = sheet.getRow(Constant.differInfoStartRow);
        int excelLastCellNum = excelheadRow1.getLastCellNum();

        // 循环row的列，按xml规则校验，并写入对象
        for (int c = 0; c < excelLastCellNum; c++) {
            Cell cell = row.getCell(c);
            String headTitle = headMap.get(c+Constant.constantTableHeadCount).toString();
            /**按规则验证cell格式**/
            validaterst = validateCellData(r+1,c+1,cell,entityName,headTitle);
            totalRst += validaterst;

            if(totalRst == 0 && cell != null) {             // 定制
                //代表xml文件第c+6个 下面同理
                if(c+Constant.officialDataStartSpecialSign==5){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    swimminganimalIdentificationReq.setStationName(cell.getStringCellValue());
                } else if(c+Constant.officialDataStartSpecialSign==6){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    swimminganimalIdentificationReq.setPlanLon(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSpecialSign==7){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    swimminganimalIdentificationReq.setPlanLat(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSpecialSign==8){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    swimminganimalIdentificationReq.setRealLon(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSpecialSign==9){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    swimminganimalIdentificationReq.setRealLat(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSpecialSign==10){
                    if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                    }
                    try{
                        String strDate=getStringCellValue(cell);
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                        LocalDate localDate=null;
                        if(strDate.contains("-")){
                            dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else if(strDate.contains(".")){
                            dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else if(strDate.contains("/")){
                            dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else{
                            Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                            localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        }
                        swimminganimalIdentificationReq.setMonitorDate(localDate);
                    } catch (Exception e){
                        swimminganimalIdentificationReq.setMonitorDate(null);
                    }
                } else if(c+Constant.officialDataStartSpecialSign==11){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)){
                        swimminganimalIdentificationReq.setSpecificName(cellValue);
                    }
                } else if(c+Constant.officialDataStartSpecialSign==12){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        swimminganimalIdentificationReq.setOverallLength(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSpecialSign==13){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        swimminganimalIdentificationReq.setBodyLength(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSpecialSign==14){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        swimminganimalIdentificationReq.setWeight(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSpecialSign==15){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        swimminganimalIdentificationReq.setPureWeight(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSpecialSign==16){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        swimminganimalIdentificationReq.setStomachContent(cellValue);
                    }
                } else if(c+Constant.officialDataStartSpecialSign==17){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        swimminganimalIdentificationReq.setSex(cellValue);
                    }
                } else if(c+Constant.officialDataStartSpecialSign==18){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        swimminganimalIdentificationReq.setFishgonadMaturity(cellValue);
                    }
                } else if(c+Constant.officialDataStartSpecialSign==19){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        swimminganimalIdentificationReq.setAge(cellValue);
                    }
                } else if(c+Constant.officialDataStartSpecialSign==20){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        swimminganimalIdentificationReq.setRemark(cellValue);
                    }
                }
            }

        }

        //数据库更新 注意返回false代表更新成功
//        MonitorDataReport monitorDataReport = new MonitorDataReport();
//        BeanUtils.copyProperties(swimminganimalIdentificationReq,monitorDataReport);
        boolean dataExist=false; //= monitorDataReportService.updateData(monitorDataReport, Constant.fisheggQuantitativeType);
        returnList.add(String.valueOf(dataExist));

        // Excel文件列表查重(遍历allMapList中底栖站位多样性数据，检索是否已包含当前数据)
        boolean sameInfo=false;
        SwimminganimalIdentificationReq item;
        for(Map<String,Object> map:allMapList){//for(Map<String,Object> map:dominantSpeciesController.allMapList){
            if(map.get("excelType")!=null && map.get("excelType").toString().equals("SwimminganimalIdentificationExcelRule")) {
                for(Object obj:(List<Object>)map.get("data")) {
                    item=(SwimminganimalIdentificationReq)obj;
                    if(
                            item.getMonitoringArea().equals(swimminganimalIdentificationReq.getMonitoringArea()) &&
                                    item.getEcologicalType().equals(swimminganimalIdentificationReq.getEcologicalType()) &&
                                    item.getTaskDate().equals(swimminganimalIdentificationReq.getTaskDate()) &&
                                    item.getMonitorCompany().equals(swimminganimalIdentificationReq.getMonitorCompany()) &&
                                    item.getOrganizationCompany().equals(swimminganimalIdentificationReq.getOrganizationCompany()) &&
                                    item.getReportDate().equals(swimminganimalIdentificationReq.getReportDate()) &&
                                    item.getYear().equals(swimminganimalIdentificationReq.getYear()) &&
                                    item.getVoyage().equals(swimminganimalIdentificationReq.getVoyage())) {
                        sameInfo = true;
                    }
                }
            }
        }
        returnList.add(String.valueOf(sameInfo));

        // 校验合格：写入dataList
        if(totalRst==0){
            dataList1.add(swimminganimalIdentificationReq);
            excelDataImportController.excelDataList.add(swimminganimalIdentificationReq);
        }
        return returnList;
    }

    /**
     * 对生物质量-9种特殊表头value值 按xml规则校验，并写入对象
     * @param sheet
     * @param headMap
     * @param entityName
     * @param swimminganimalIdentificationReq
     */
    public void specialHandlingSwimminganimalIdentification(Sheet sheet,Map headMap,String entityName,SwimminganimalIdentificationReq swimminganimalIdentificationReq,Integer totalRows){
        int totalRst = 0;//每行格式错误单元格的数目
        int validaterst = 0;

        //取第1+1行
        Row row = sheet.getRow(1);
        //存放监控区值
        //1代表第1+1列
        Cell cell = row.getCell(1);
        //0代表监控区
        String headTitle = headMap.get(Constant.monitoringAreaCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            swimminganimalIdentificationReq.setMonitoringArea(cell.getStringCellValue());
        }

        //存放任务日期
        //10代表10+1列
        cell = row.getCell(10);
        headTitle = headMap.get(Constant.missionDateCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,10+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            //判断该日期在excel表格的格式是否是日期格式
            if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }

            try{
                String strDate=getStringCellValue(cell);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate=null;
                if(strDate.contains("-")){
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains(".")){
                    dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains("/")){
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else{
                    Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                swimminganimalIdentificationReq.setTaskDate(localDate);
            } catch (Exception e){
                swimminganimalIdentificationReq.setTaskDate(null);
            }
        }

        //取第2+1行
        row = sheet.getRow(2);
        //存放监测单位
        //1代表第1+1列
        cell = row.getCell(1);
        headTitle = headMap.get(Constant.monitoringUnitCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            swimminganimalIdentificationReq.setMonitorCompany(cell.getStringCellValue());
        }

        //存放组织单位
        //6代表6+1列
        cell = row.getCell(6);
        headTitle = headMap.get(Constant.organizationalUnitCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,6+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            swimminganimalIdentificationReq.setOrganizationCompany(cell.getStringCellValue());
        }

        //存放填报日期
        //10代表10+1列
        cell = row.getCell(10);
        headTitle = headMap.get(Constant.completionDateCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,10+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制

            //判断该日期在excel表格的格式是否是日期格式
            if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }

            try{
                String strDate = getStringCellValue(cell);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate=null;
                if(strDate.contains("-")){
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains(".")){
                    dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains("/")){
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else{
                    Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                swimminganimalIdentificationReq.setReportDate(localDate);
            } catch (Exception e){
                swimminganimalIdentificationReq.setReportDate(null);
            }
        }


        //取最后一行
        row = sheet.getRow(totalRows - 1);
        //存放填报人
        //1代表第1+1列
        cell = row.getCell(1);
        headTitle = headMap.get(Constant.informantCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            swimminganimalIdentificationReq.setByzd1(cell.getStringCellValue());
        }

        //存放校对人
        cell = row.getCell(4);
        headTitle = headMap.get(Constant.proofreaderCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,4+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            swimminganimalIdentificationReq.setByzd2(cell.getStringCellValue());
        }

        //存放审核人
        cell = row.getCell(7);
        headTitle = headMap.get(Constant.auditorCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,7+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            swimminganimalIdentificationReq.setByzd3(cell.getStringCellValue());
        }

    }

    //小型浮游动物（II型网）数据表
    private List<String> smallzooplanktonIinetExcelRule(Integer totalCells,Integer totalRows,Row row,Map headMap,String entityName,int r,List<Map<String, Object>> allMapList,Sheet sheet){
        List<String> returnList=new ArrayList<>();
        SmallzooplanktonIinetReq smallzooplanktonIinetReq = new SmallzooplanktonIinetReq();
        int totalRst = 0;//每行格式错误单元格的数目
        int validaterst = 0;

        //将年份和航次信息写入对象
        smallzooplanktonIinetReq.setYear(YEAR);
        smallzooplanktonIinetReq.setVoyage(VOYAGE);
        smallzooplanktonIinetReq.setDataType(dataType);

        //对9种特殊表头value值 按xml规则校验，并写入对象
        specialHandlingSmallzooplanktonIinet(sheet,headMap,entityName,smallzooplanktonIinetReq,totalRows);

        //取
        Row excelheadRow1 = sheet.getRow(Constant.differInfoStartRow);
        int excelLastCellNum = excelheadRow1.getLastCellNum();

        // 循环row的列，按xml规则校验，并写入对象
        for (int c = 0; c < excelLastCellNum; c++) {
            Cell cell = row.getCell(c);
            String headTitle = headMap.get(c+Constant.constantTableHeadCount).toString();
            /**按规则验证cell格式**/
            validaterst = validateCellData(r+1,c+1,cell,entityName,headTitle);
            totalRst += validaterst;

            if(totalRst == 0 && cell != null) {             // 定制
                //6代表xml文件第6个 下面同理
                if(c+Constant.officialDataStartSign==6){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    smallzooplanktonIinetReq.setStationName(cell.getStringCellValue());
                } else if(c+Constant.officialDataStartSign==7){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    smallzooplanktonIinetReq.setPlanLon(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==8){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    smallzooplanktonIinetReq.setPlanLat(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==9){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    smallzooplanktonIinetReq.setRealLon(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==10){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    smallzooplanktonIinetReq.setRealLat(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==11){
                    if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                    }
                    try{
                        String strDate=getStringCellValue(cell);
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                        LocalDate localDate=null;
                        if(strDate.contains("-")){
                            dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else if(strDate.contains(".")){
                            dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else if(strDate.contains("/")){
                            dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else{
                            Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                            localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        }
                        smallzooplanktonIinetReq.setMonitorDate(localDate);
                    } catch (Exception e){
                        smallzooplanktonIinetReq.setMonitorDate(null);
                    }
                } else if(c+Constant.officialDataStartSign==12){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)){
                        smallzooplanktonIinetReq.setWaterDepth(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==13){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        smallzooplanktonIinetReq.setRopeLength(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==14){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        smallzooplanktonIinetReq.setWaterFiltration(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==15){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        smallzooplanktonIinetReq.setCategory(cellValue);
                    }
                }else if(c+Constant.officialDataStartSign==16){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        smallzooplanktonIinetReq.setBiologicalChineseName(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==17){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        smallzooplanktonIinetReq.setBiologicalLatinName(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==18){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        smallzooplanktonIinetReq.setDensity(new BigDecimal(cellValue));
                    }
                }
            }

        }

        //数据库更新 注意返回false代表更新成功
//        MonitorDataReport monitorDataReport = new MonitorDataReport();
//        BeanUtils.copyProperties(smallzooplanktonIinetReq,monitorDataReport);
        boolean dataExist=false; //= monitorDataReportService.updateData(monitorDataReport, Constant.fisheggQuantitativeType);
        returnList.add(String.valueOf(dataExist));

        // Excel文件列表查重(遍历allMapList中底栖站位多样性数据，检索是否已包含当前数据)
        boolean sameInfo=false;
        SmallzooplanktonIinetReq item;
        for(Map<String,Object> map:allMapList){//for(Map<String,Object> map:dominantSpeciesController.allMapList){
            if(map.get("excelType")!=null && map.get("excelType").toString().equals("SmallzooplanktonIinetExcelRule")) {
                for(Object obj:(List<Object>)map.get("data")) {
                    item=(SmallzooplanktonIinetReq)obj;
                    if(
                            item.getMonitoringArea().equals(smallzooplanktonIinetReq.getMonitoringArea()) &&
                                    item.getEcologicalType().equals(smallzooplanktonIinetReq.getEcologicalType()) &&
                                    item.getTaskDate().equals(smallzooplanktonIinetReq.getTaskDate()) &&
                                    item.getMonitorCompany().equals(smallzooplanktonIinetReq.getMonitorCompany()) &&
                                    item.getOrganizationCompany().equals(smallzooplanktonIinetReq.getOrganizationCompany()) &&
                                    item.getReportDate().equals(smallzooplanktonIinetReq.getReportDate()) &&
                                    item.getYear().equals(smallzooplanktonIinetReq.getYear()) &&
                                    item.getVoyage().equals(smallzooplanktonIinetReq.getVoyage())) {
                        sameInfo = true;
                    }
                }
            }
        }
        returnList.add(String.valueOf(sameInfo));

        // 校验合格：写入dataList
        if(totalRst==0){
            dataList1.add(smallzooplanktonIinetReq);
            excelDataImportController.excelDataList.add(smallzooplanktonIinetReq);
        }
        return returnList;
    }

    /**
     * 对小型浮游动物（II型网）-9种特殊表头value值 按xml规则校验，并写入对象
     * @param sheet
     * @param headMap
     * @param entityName
     * @param smallzooplanktonIinetReq
     */
    public void specialHandlingSmallzooplanktonIinet(Sheet sheet,Map headMap,String entityName,SmallzooplanktonIinetReq smallzooplanktonIinetReq,Integer totalRows){
        int totalRst = 0;//每行格式错误单元格的数目
        int validaterst = 0;

        //取第1+1行
        Row row = sheet.getRow(1);
        //存放监控区值
        //1代表第1+1列
        Cell cell = row.getCell(1);
        //0代表监控区
        String headTitle = headMap.get(Constant.monitoringAreaCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            smallzooplanktonIinetReq.setMonitoringArea(cell.getStringCellValue());
        }

        //存放生态类型值
        //6代表6+1列
        cell = row.getCell(6);
        headTitle = headMap.get(Constant.ecologicaltypeCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,6+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            smallzooplanktonIinetReq.setEcologicalType(cell.getStringCellValue());
        }

        //存放任务日期
        //11代表11+1列
        cell = row.getCell(11);
        headTitle = headMap.get(Constant.missionDateCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,11+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            //判断该日期在excel表格的格式是否是日期格式
            if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }

            try{
                String strDate=getStringCellValue(cell);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate=null;
                if(strDate.contains("-")){
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains(".")){
                    dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains("/")){
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else{
                    Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                smallzooplanktonIinetReq.setTaskDate(localDate);
            } catch (Exception e){
                smallzooplanktonIinetReq.setTaskDate(null);
            }
        }

        //取第2+1行
        row = sheet.getRow(2);
        //存放监测单位
        //1代表第1+1列
        cell = row.getCell(1);
        headTitle = headMap.get(Constant.monitoringUnitCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            smallzooplanktonIinetReq.setMonitorCompany(cell.getStringCellValue());
        }

        //存放组织单位
        //6代表6+1列
        cell = row.getCell(6);
        headTitle = headMap.get(Constant.organizationalUnitCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,6+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            smallzooplanktonIinetReq.setOrganizationCompany(cell.getStringCellValue());
        }

        //存放填报日期
        //11代表11+1列
        cell = row.getCell(11);
        headTitle = headMap.get(Constant.completionDateCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,11+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制

            //判断该日期在excel表格的格式是否是日期格式
            if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }

            try{
                String strDate = getStringCellValue(cell);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate=null;
                if(strDate.contains("-")){
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains(".")){
                    dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains("/")){
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else{
                    Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                smallzooplanktonIinetReq.setReportDate(localDate);
            } catch (Exception e){
                smallzooplanktonIinetReq.setReportDate(null);
            }
        }


        //取最后一行
        row = sheet.getRow(totalRows - 1);
        //存放填报人
        //1代表第1+1列
        cell = row.getCell(1);
        headTitle = headMap.get(Constant.informantCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            smallzooplanktonIinetReq.setByzd1(cell.getStringCellValue());
        }

        //存放校对人
        cell = row.getCell(4);
        headTitle = headMap.get(Constant.proofreaderCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,4+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            smallzooplanktonIinetReq.setByzd2(cell.getStringCellValue());
        }

        //存放审核人
        cell = row.getCell(7);
        headTitle = headMap.get(Constant.auditorCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,7+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            smallzooplanktonIinetReq.setByzd3(cell.getStringCellValue());
        }

    }

    //仔鱼定量数据表
    private List<String> smallfishQuantitativeExcelRule(Integer totalCells,Integer totalRows,Row row,Map headMap,String entityName,int r,List<Map<String, Object>> allMapList,Sheet sheet){
        List<String> returnList=new ArrayList<>();
        SmallfishQuantitativeReq smallfishQuantitativeReq = new SmallfishQuantitativeReq();
        int totalRst = 0;//每行格式错误单元格的数目
        int validaterst = 0;

        //将年份和航次信息写入对象
        smallfishQuantitativeReq.setYear(YEAR);
        smallfishQuantitativeReq.setVoyage(VOYAGE);
        smallfishQuantitativeReq.setDataType(dataType);

        //对9种特殊表头value值 按xml规则校验，并写入对象
        specialHandlingSmallfishQuantitative(sheet,headMap,entityName,smallfishQuantitativeReq,totalRows);

        //取
        Row excelheadRow1 = sheet.getRow(Constant.differInfoStartRow);
        int excelLastCellNum = excelheadRow1.getLastCellNum();

        // 循环row的列，按xml规则校验，并写入对象
        for (int c = 0; c < excelLastCellNum; c++) {
            Cell cell = row.getCell(c);
            String headTitle = headMap.get(c+Constant.constantTableHeadCount).toString();
            /**按规则验证cell格式**/
            validaterst = validateCellData(r+1,c+1,cell,entityName,headTitle);
            totalRst += validaterst;

            if(totalRst == 0 && cell != null) {             // 定制
                //6代表xml文件第6个 下面同理
                if(c+Constant.officialDataStartSign==6){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    smallfishQuantitativeReq.setStationName(cell.getStringCellValue());
                } else if(c+Constant.officialDataStartSign==7){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    smallfishQuantitativeReq.setPlanLon(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==8){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    smallfishQuantitativeReq.setPlanLat(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==9){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    smallfishQuantitativeReq.setRealLon(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==10){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    smallfishQuantitativeReq.setRealLat(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==11){
                    if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                    }
                    try{
                        String strDate=getStringCellValue(cell);
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                        LocalDate localDate=null;
                        if(strDate.contains("-")){
                            dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else if(strDate.contains(".")){
                            dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else if(strDate.contains("/")){
                            dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else{
                            Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                            localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        }
                        smallfishQuantitativeReq.setMonitorDate(localDate);
                    } catch (Exception e){
                        smallfishQuantitativeReq.setMonitorDate(null);
                    }
                } else if(c+Constant.officialDataStartSign==12){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)){
                        smallfishQuantitativeReq.setWaterDepth(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==13){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        smallfishQuantitativeReq.setRopeLength(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==14){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        smallfishQuantitativeReq.setWaterFiltration(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==15){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        smallfishQuantitativeReq.setBiologicalChineseName(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==16){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        smallfishQuantitativeReq.setBiologicalLatinName(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==17){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        smallfishQuantitativeReq.setDevelopStage(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==18){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        smallfishQuantitativeReq.setBodyLength(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==19){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        smallfishQuantitativeReq.setCount(Integer.parseInt(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==20){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        smallfishQuantitativeReq.setDensity(new BigDecimal(cellValue));
                    }
                }
            }

        }

        //数据库更新 注意返回false代表更新成功
//        MonitorDataReport monitorDataReport = new MonitorDataReport();
//        BeanUtils.copyProperties(smallfishQuantitativeReq,monitorDataReport);
        boolean dataExist=false; //= monitorDataReportService.updateData(monitorDataReport, Constant.fisheggQuantitativeType);
        returnList.add(String.valueOf(dataExist));

        // Excel文件列表查重(遍历allMapList中底栖站位多样性数据，检索是否已包含当前数据)
        boolean sameInfo=false;
        SmallfishQuantitativeReq item;
        for(Map<String,Object> map:allMapList){//for(Map<String,Object> map:dominantSpeciesController.allMapList){
            if(map.get("excelType")!=null && map.get("excelType").toString().equals("SmallfishQuantitativeExcelRule")) {
                for(Object obj:(List<Object>)map.get("data")) {
                    item=(SmallfishQuantitativeReq)obj;
                    if(
                            item.getMonitoringArea().equals(smallfishQuantitativeReq.getMonitoringArea()) &&
                                    item.getEcologicalType().equals(smallfishQuantitativeReq.getEcologicalType()) &&
                                    item.getTaskDate().equals(smallfishQuantitativeReq.getTaskDate()) &&
                                    item.getMonitorCompany().equals(smallfishQuantitativeReq.getMonitorCompany()) &&
                                    item.getOrganizationCompany().equals(smallfishQuantitativeReq.getOrganizationCompany()) &&
                                    item.getReportDate().equals(smallfishQuantitativeReq.getReportDate()) &&
                                    item.getYear().equals(smallfishQuantitativeReq.getYear()) &&
                                    item.getVoyage().equals(smallfishQuantitativeReq.getVoyage())) {
                        sameInfo = true;
                    }
                }
            }
        }
        returnList.add(String.valueOf(sameInfo));

        // 校验合格：写入dataList
        if(totalRst==0){
            dataList1.add(smallfishQuantitativeReq);
            excelDataImportController.excelDataList.add(smallfishQuantitativeReq);
        }
        return returnList;
    }

    /**
     * 对仔鱼定量-9种特殊表头value值 按xml规则校验，并写入对象
     * @param sheet
     * @param headMap
     * @param entityName
     * @param smallfishQuantitativeReq
     */
    public void specialHandlingSmallfishQuantitative(Sheet sheet,Map headMap,String entityName,SmallfishQuantitativeReq smallfishQuantitativeReq,Integer totalRows){
        int totalRst = 0;//每行格式错误单元格的数目
        int validaterst = 0;

        //取第1+1行
        Row row = sheet.getRow(1);
        //存放监控区值
        //1代表第1+1列
        Cell cell = row.getCell(1);
        //0代表监控区
        String headTitle = headMap.get(Constant.monitoringAreaCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            smallfishQuantitativeReq.setMonitoringArea(cell.getStringCellValue());
        }

        //存放生态类型值
        //6代表6+1列
        cell = row.getCell(6);
        headTitle = headMap.get(Constant.ecologicaltypeCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,6+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            smallfishQuantitativeReq.setEcologicalType(cell.getStringCellValue());
        }

        //存放任务日期
        //11代表11+1列
        cell = row.getCell(11);
        headTitle = headMap.get(Constant.missionDateCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,11+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            //判断该日期在excel表格的格式是否是日期格式
            if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }

            try{
                String strDate=getStringCellValue(cell);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate=null;
                if(strDate.contains("-")){
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains(".")){
                    dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains("/")){
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else{
                    Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                smallfishQuantitativeReq.setTaskDate(localDate);
            } catch (Exception e){
                smallfishQuantitativeReq.setTaskDate(null);
            }
        }

        //取第2+1行
        row = sheet.getRow(2);
        //存放监测单位
        //1代表第1+1列
        cell = row.getCell(1);
        headTitle = headMap.get(Constant.monitoringUnitCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            smallfishQuantitativeReq.setMonitorCompany(cell.getStringCellValue());
        }

        //存放组织单位
        //6代表6+1列
        cell = row.getCell(6);
        headTitle = headMap.get(Constant.organizationalUnitCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,6+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            smallfishQuantitativeReq.setOrganizationCompany(cell.getStringCellValue());
        }

        //存放填报日期
        //11代表11+1列
        cell = row.getCell(11);
        headTitle = headMap.get(Constant.completionDateCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,11+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制

            //判断该日期在excel表格的格式是否是日期格式
            if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }

            try{
                String strDate = getStringCellValue(cell);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate=null;
                if(strDate.contains("-")){
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains(".")){
                    dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains("/")){
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else{
                    Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                smallfishQuantitativeReq.setReportDate(localDate);
            } catch (Exception e){
                smallfishQuantitativeReq.setReportDate(null);
            }
        }


        //取最后一行
        row = sheet.getRow(totalRows - 1);
        //存放填报人
        //1代表第1+1列
        cell = row.getCell(1);
        headTitle = headMap.get(Constant.informantCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            smallfishQuantitativeReq.setByzd1(cell.getStringCellValue());
        }

        //存放校对人
        cell = row.getCell(4);
        headTitle = headMap.get(Constant.proofreaderCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,4+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            smallfishQuantitativeReq.setByzd2(cell.getStringCellValue());
        }

        //存放审核人
        cell = row.getCell(7);
        headTitle = headMap.get(Constant.auditorCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,7+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            smallfishQuantitativeReq.setByzd3(cell.getStringCellValue());
        }

    }

    //仔鱼定性数据表
    private List<String> smallfishQualitativeExcelRule(Integer totalCells,Integer totalRows,Row row,Map headMap,String entityName,int r,List<Map<String, Object>> allMapList,Sheet sheet){
        List<String> returnList=new ArrayList<>();
        SmallfishQualitativeReq smallfishQualitativeReq = new SmallfishQualitativeReq();
        int totalRst = 0;//每行格式错误单元格的数目
        int validaterst = 0;

        //将年份和航次信息写入对象
        smallfishQualitativeReq.setYear(YEAR);
        smallfishQualitativeReq.setVoyage(VOYAGE);
        smallfishQualitativeReq.setDataType(dataType);

        //对9种特殊表头value值 按xml规则校验，并写入对象
        specialHandlingSmallfishQualitative(sheet,headMap,entityName,smallfishQualitativeReq,totalRows);

        //取
        Row excelheadRow1 = sheet.getRow(Constant.differInfoStartRow);
        int excelLastCellNum = excelheadRow1.getLastCellNum();

        // 循环row的列，按xml规则校验，并写入对象
        for (int c = 0; c < excelLastCellNum; c++) {
            Cell cell = row.getCell(c);
            String headTitle = headMap.get(c+Constant.constantTableHeadCount).toString();
            /**按规则验证cell格式**/
            validaterst = validateCellData(r+1,c+1,cell,entityName,headTitle);
            totalRst += validaterst;

            if(totalRst == 0 && cell != null) {             // 定制
                //6代表xml文件第6个 下面同理
                if(c+Constant.officialDataStartSign==6){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    smallfishQualitativeReq.setStationName(cell.getStringCellValue());
                } else if(c+Constant.officialDataStartSign==7){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    smallfishQualitativeReq.setPlanLon(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==8){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    smallfishQualitativeReq.setPlanLat(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==9){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    smallfishQualitativeReq.setRealLon(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==10){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    smallfishQualitativeReq.setRealLat(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==11){
                    if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                    }
                    try{
                        String strDate=getStringCellValue(cell);
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                        LocalDate localDate=null;
                        if(strDate.contains("-")){
                            dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else if(strDate.contains(".")){
                            dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else if(strDate.contains("/")){
                            dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else{
                            Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                            localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        }
                        smallfishQualitativeReq.setMonitorDate(localDate);
                    } catch (Exception e){
                        smallfishQualitativeReq.setMonitorDate(null);
                    }
                } else if(c+Constant.officialDataStartSign==12){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)){
                        smallfishQualitativeReq.setWaterDepth(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==13){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        smallfishQualitativeReq.setNetType(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==14){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        smallfishQualitativeReq.setTrawlDistance(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==15){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        smallfishQualitativeReq.setTrawlTime(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==16){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        smallfishQualitativeReq.setBiologicalChineseName(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==17){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        smallfishQualitativeReq.setBiologicalLatinName(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==18){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        smallfishQualitativeReq.setDevelopStage(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==19){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        smallfishQualitativeReq.setBodyLength(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==20){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        smallfishQualitativeReq.setCount(Integer.parseInt(cellValue));
                    }
                }
            }

        }

        //数据库更新 注意返回false代表更新成功
//        MonitorDataReport monitorDataReport = new MonitorDataReport();
//        BeanUtils.copyProperties(smallfishQualitativeReq,monitorDataReport);
        boolean dataExist=false; //= monitorDataReportService.updateData(monitorDataReport, Constant.fisheggQuantitativeType);
        returnList.add(String.valueOf(dataExist));

        // Excel文件列表查重(遍历allMapList中底栖站位多样性数据，检索是否已包含当前数据)
        boolean sameInfo=false;
        SmallfishQualitativeReq item;
        for(Map<String,Object> map:allMapList){//for(Map<String,Object> map:dominantSpeciesController.allMapList){
            if(map.get("excelType")!=null && map.get("excelType").toString().equals("SmallfishQualitativeExcelRule")) {
                for(Object obj:(List<Object>)map.get("data")) {
                    item=(SmallfishQualitativeReq)obj;
                    if(
                            item.getMonitoringArea().equals(smallfishQualitativeReq.getMonitoringArea()) &&
                                    item.getEcologicalType().equals(smallfishQualitativeReq.getEcologicalType()) &&
                                    item.getTaskDate().equals(smallfishQualitativeReq.getTaskDate()) &&
                                    item.getMonitorCompany().equals(smallfishQualitativeReq.getMonitorCompany()) &&
                                    item.getOrganizationCompany().equals(smallfishQualitativeReq.getOrganizationCompany()) &&
                                    item.getReportDate().equals(smallfishQualitativeReq.getReportDate()) &&
                                    item.getYear().equals(smallfishQualitativeReq.getYear()) &&
                                    item.getVoyage().equals(smallfishQualitativeReq.getVoyage())) {
                        sameInfo = true;
                    }
                }
            }
        }
        returnList.add(String.valueOf(sameInfo));

        // 校验合格：写入dataList
        if(totalRst==0){
            dataList1.add(smallfishQualitativeReq);
            excelDataImportController.excelDataList.add(smallfishQualitativeReq);
        }
        return returnList;
    }

    /**
     * 对仔鱼定性-9种特殊表头value值 按xml规则校验，并写入对象
     * @param sheet
     * @param headMap
     * @param entityName
     * @param smallfishQualitativeReq
     */
    public void specialHandlingSmallfishQualitative(Sheet sheet,Map headMap,String entityName,SmallfishQualitativeReq smallfishQualitativeReq,Integer totalRows){
        int totalRst = 0;//每行格式错误单元格的数目
        int validaterst = 0;

        //取第1+1行
        Row row = sheet.getRow(1);
        //存放监控区值
        //1代表第1+1列
        Cell cell = row.getCell(1);
        //0代表监控区
        String headTitle = headMap.get(Constant.monitoringAreaCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            smallfishQualitativeReq.setMonitoringArea(cell.getStringCellValue());
        }

        //存放生态类型值
        //6代表6+1列
        cell = row.getCell(6);
        headTitle = headMap.get(Constant.ecologicaltypeCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,6+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            smallfishQualitativeReq.setEcologicalType(cell.getStringCellValue());
        }

        //存放任务日期
        //11代表11+1列
        cell = row.getCell(11);
        headTitle = headMap.get(Constant.missionDateCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,11+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            //判断该日期在excel表格的格式是否是日期格式
            if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }

            try{
                String strDate=getStringCellValue(cell);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate=null;
                if(strDate.contains("-")){
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains(".")){
                    dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains("/")){
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else{
                    Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                smallfishQualitativeReq.setTaskDate(localDate);
            } catch (Exception e){
                smallfishQualitativeReq.setTaskDate(null);
            }
        }

        //取第2+1行
        row = sheet.getRow(2);
        //存放监测单位
        //1代表第1+1列
        cell = row.getCell(1);
        headTitle = headMap.get(Constant.monitoringUnitCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            smallfishQualitativeReq.setMonitorCompany(cell.getStringCellValue());
        }

        //存放组织单位
        //6代表6+1列
        cell = row.getCell(6);
        headTitle = headMap.get(Constant.organizationalUnitCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,6+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            smallfishQualitativeReq.setOrganizationCompany(cell.getStringCellValue());
        }

        //存放填报日期
        //11代表11+1列
        cell = row.getCell(11);
        headTitle = headMap.get(Constant.completionDateCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,11+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制

            //判断该日期在excel表格的格式是否是日期格式
            if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }

            try{
                String strDate = getStringCellValue(cell);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate=null;
                if(strDate.contains("-")){
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains(".")){
                    dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains("/")){
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else{
                    Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                smallfishQualitativeReq.setReportDate(localDate);
            } catch (Exception e){
                smallfishQualitativeReq.setReportDate(null);
            }
        }


        //取最后一行
        row = sheet.getRow(totalRows - 1);
        //存放填报人
        //1代表第1+1列
        cell = row.getCell(1);
        headTitle = headMap.get(Constant.informantCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            smallfishQualitativeReq.setByzd1(cell.getStringCellValue());
        }

        //存放校对人
        cell = row.getCell(4);
        headTitle = headMap.get(Constant.proofreaderCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,4+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            smallfishQualitativeReq.setByzd2(cell.getStringCellValue());
        }

        //存放审核人
        cell = row.getCell(7);
        headTitle = headMap.get(Constant.auditorCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,7+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            smallfishQualitativeReq.setByzd3(cell.getStringCellValue());
        }

    }

    //沉积物粒度数据表
    private List<String> sedimentgrainExcelRule(Integer totalCells,Integer totalRows,Row row,Map headMap,String entityName,int r,List<Map<String, Object>> allMapList,Sheet sheet){
        List<String> returnList=new ArrayList<>();
        SedimentgrainReq sedimentgrainReq = new SedimentgrainReq();
        int totalRst = 0;//每行格式错误单元格的数目
        int validaterst = 0;

        //将年份和航次信息写入对象
        sedimentgrainReq.setYear(YEAR);
        sedimentgrainReq.setVoyage(VOYAGE);
        sedimentgrainReq.setDataType(dataType);

        //对9种特殊表头value值 按xml规则校验，并写入对象
        specialHandlingSedimentgrain(sheet,headMap,entityName,sedimentgrainReq,totalRows);

        //取
        Row excelheadRow1 = sheet.getRow(Constant.differInfoStartRow);
        int excelLastCellNum = excelheadRow1.getLastCellNum();

        // 循环row的列，按xml规则校验，并写入对象
        for (int c = 0; c < excelLastCellNum; c++) {
            Cell cell = row.getCell(c);
            String headTitle = headMap.get(c+Constant.constantTableHeadCount).toString();
            /**按规则验证cell格式**/
            validaterst = validateCellData(r+1,c+1,cell,entityName,headTitle);
            totalRst += validaterst;

            if(totalRst == 0 && cell != null) {             // 定制
                //6代表xml文件第6个 下面同理
                if(c+Constant.officialDataStartSign==6){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    sedimentgrainReq.setStationName(cell.getStringCellValue());
                } else if(c+Constant.officialDataStartSign==7){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    sedimentgrainReq.setPlanLon(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==8){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    sedimentgrainReq.setPlanLat(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==9){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    sedimentgrainReq.setRealLon(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==10){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    sedimentgrainReq.setRealLat(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==11){
                    if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                    }
                    try{
                        String strDate=getStringCellValue(cell);
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                        LocalDate localDate=null;
                        if(strDate.contains("-")){
                            dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else if(strDate.contains(".")){
                            dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else if(strDate.contains("/")){
                            dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else{
                            Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                            localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        }
                        sedimentgrainReq.setMonitorDate(localDate);
                    } catch (Exception e){
                        sedimentgrainReq.setMonitorDate(null);
                    }
                } else if(c+Constant.officialDataStartSign==12){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)){
                        sedimentgrainReq.setSamplingDepth(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==13){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentgrainReq.setGravel1(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==14){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentgrainReq.setGravel2(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==15){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentgrainReq.setSand1(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==16){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentgrainReq.setSand2(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==17){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentgrainReq.setSand3(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==18){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentgrainReq.setSand4(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==19){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentgrainReq.setSand5(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==20){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentgrainReq.setPinksand1(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==21){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentgrainReq.setPinksand2(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==22){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentgrainReq.setClay1(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==23){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentgrainReq.setClay2(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==24){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentgrainReq.setGranulecontent1(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==25){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentgrainReq.setGranulecontent2(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==26){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentgrainReq.setGranulecontent3(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==27){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentgrainReq.setGranulecontent4(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==28){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentgrainReq.setNameAndCode(cellValue);
                    }
                }else if(c+Constant.officialDataStartSign==29){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentgrainReq.setParticlecoefficient1(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==30){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentgrainReq.setParticlecoefficient2(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==31){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentgrainReq.setParticlecoefficient3(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==32){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentgrainReq.setPinksand1(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==33){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentgrainReq.setPinksand1(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==34){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentgrainReq.setPinksand1(new BigDecimal(cellValue));
                    }
                }
            }

        }

        //数据库更新 注意返回false代表更新成功
//        MonitorDataReport monitorDataReport = new MonitorDataReport();
//        BeanUtils.copyProperties(sedimentgrainReq,monitorDataReport);
        boolean dataExist=false; //= monitorDataReportService.updateData(monitorDataReport, Constant.fisheggQuantitativeType);
        returnList.add(String.valueOf(dataExist));

        // Excel文件列表查重(遍历allMapList中底栖站位多样性数据，检索是否已包含当前数据)
        boolean sameInfo=false;
        SedimentgrainReq item;
        for(Map<String,Object> map:allMapList){//for(Map<String,Object> map:dominantSpeciesController.allMapList){
            if(map.get("excelType")!=null && map.get("excelType").toString().equals("SedimentgrainExcelRule")) {
                for(Object obj:(List<Object>)map.get("data")) {
                    item=(SedimentgrainReq)obj;
                    if(
                            item.getMonitoringArea().equals(sedimentgrainReq.getMonitoringArea()) &&
                                    item.getEcologicalType().equals(sedimentgrainReq.getEcologicalType()) &&
                                    item.getTaskDate().equals(sedimentgrainReq.getTaskDate()) &&
                                    item.getMonitorCompany().equals(sedimentgrainReq.getMonitorCompany()) &&
                                    item.getOrganizationCompany().equals(sedimentgrainReq.getOrganizationCompany()) &&
                                    item.getReportDate().equals(sedimentgrainReq.getReportDate()) &&
                                    item.getYear().equals(sedimentgrainReq.getYear()) &&
                                    item.getVoyage().equals(sedimentgrainReq.getVoyage())) {
                        sameInfo = true;
                    }
                }
            }
        }
        returnList.add(String.valueOf(sameInfo));

        // 校验合格：写入dataList
        if(totalRst==0){
            dataList1.add(sedimentgrainReq);
            excelDataImportController.excelDataList.add(sedimentgrainReq);
        }
        return returnList;
    }

    /**
     * 对沉积物粒度-9种特殊表头value值 按xml规则校验，并写入对象
     * @param sheet
     * @param headMap
     * @param entityName
     * @param sedimentgrainReq
     */
    public void specialHandlingSedimentgrain(Sheet sheet,Map headMap,String entityName,SedimentgrainReq sedimentgrainReq,Integer totalRows){
        int totalRst = 0;//每行格式错误单元格的数目
        int validaterst = 0;

        //取第1+1行
        Row row = sheet.getRow(1);
        //存放监控区值
        //1代表第1+1列
        Cell cell = row.getCell(1);
        //0代表监控区
        String headTitle = headMap.get(Constant.monitoringAreaCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            sedimentgrainReq.setMonitoringArea(cell.getStringCellValue());
        }

        //存放生态类型值
        //6代表6+1列
        cell = row.getCell(8);
        headTitle = headMap.get(Constant.ecologicaltypeCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,8+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            sedimentgrainReq.setEcologicalType(cell.getStringCellValue());
        }

        //存放任务日期
        //11代表11+1列
        cell = row.getCell(16);
        headTitle = headMap.get(Constant.missionDateCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,16+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            //判断该日期在excel表格的格式是否是日期格式
            if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }

            try{
                String strDate=getStringCellValue(cell);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate=null;
                if(strDate.contains("-")){
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains(".")){
                    dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains("/")){
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else{
                    Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                sedimentgrainReq.setTaskDate(localDate);
            } catch (Exception e){
                sedimentgrainReq.setTaskDate(null);
            }
        }

        //取第2+1行
        row = sheet.getRow(2);
        //存放监测单位
        //1代表第1+1列
        cell = row.getCell(1);
        headTitle = headMap.get(Constant.monitoringUnitCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            sedimentgrainReq.setMonitorCompany(cell.getStringCellValue());
        }

        //存放组织单位
        //6代表6+1列
        cell = row.getCell(8);
        headTitle = headMap.get(Constant.organizationalUnitCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,8+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            sedimentgrainReq.setOrganizationCompany(cell.getStringCellValue());
        }

        //存放填报日期
        //11代表11+1列
        cell = row.getCell(16);
        headTitle = headMap.get(Constant.completionDateCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,16+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制

            //判断该日期在excel表格的格式是否是日期格式
            if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }

            try{
                String strDate = getStringCellValue(cell);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate=null;
                if(strDate.contains("-")){
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains(".")){
                    dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains("/")){
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else{
                    Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                sedimentgrainReq.setReportDate(localDate);
            } catch (Exception e){
                sedimentgrainReq.setReportDate(null);
            }
        }


        //取最后一行
        row = sheet.getRow(totalRows - 1);
        //存放填报人
        //1代表第1+1列
        cell = row.getCell(1);
        headTitle = headMap.get(Constant.informantCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            sedimentgrainReq.setByzd1(cell.getStringCellValue());
        }

        //存放校对人
        cell = row.getCell(4);
        headTitle = headMap.get(Constant.proofreaderCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,4+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            sedimentgrainReq.setByzd2(cell.getStringCellValue());
        }

        //存放审核人
        cell = row.getCell(7);
        headTitle = headMap.get(Constant.auditorCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,7+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            sedimentgrainReq.setByzd3(cell.getStringCellValue());
        }

    }

    //沉积物数据表
    private List<String> sedimentExcelRule(Integer totalCells,Integer totalRows,Row row,Map headMap,String entityName,int r,List<Map<String, Object>> allMapList,Sheet sheet){
        List<String> returnList=new ArrayList<>();
        SedimentReq sedimentReq = new SedimentReq();
        int totalRst = 0;//每行格式错误单元格的数目
        int validaterst = 0;

        //将年份和航次信息写入对象
        sedimentReq.setYear(YEAR);
        sedimentReq.setVoyage(VOYAGE);
        sedimentReq.setDataType(dataType);

        //对9种特殊表头value值 按xml规则校验，并写入对象
        specialHandlingSediment(sheet,headMap,entityName,sedimentReq,totalRows);

        //取
        Row excelheadRow1 = sheet.getRow(Constant.differInfoStartRow);
        int excelLastCellNum = excelheadRow1.getLastCellNum();

        // 循环row的列，按xml规则校验，并写入对象
        for (int c = 0; c < excelLastCellNum; c++) {
            Cell cell = row.getCell(c);
            String headTitle = headMap.get(c+Constant.constantTableHeadCount).toString();
            /**按规则验证cell格式**/
            validaterst = validateCellData(r+1,c+1,cell,entityName,headTitle);
            totalRst += validaterst;

            if(totalRst == 0 && cell != null) {             // 定制
                //6代表xml文件第6个 下面同理
                if(c+Constant.officialDataStartSign==6){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    sedimentReq.setStationName(cell.getStringCellValue());
                } else if(c+Constant.officialDataStartSign==7){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    sedimentReq.setPlanLon(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==8){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    sedimentReq.setPlanLat(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==9){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    sedimentReq.setRealLon(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==10){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    sedimentReq.setRealLat(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==11){
                    if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                    }
                    try{
                        String strDate=getStringCellValue(cell);
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                        LocalDate localDate=null;
                        if(strDate.contains("-")){
                            dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else if(strDate.contains(".")){
                            dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else if(strDate.contains("/")){
                            dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else{
                            Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                            localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        }
                        sedimentReq.setMonitorDate(localDate);
                    } catch (Exception e){
                        sedimentReq.setMonitorDate(null);
                    }
                } else if(c+Constant.officialDataStartSign==12){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)){
                        sedimentReq.setWaterDepth(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==13){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentReq.setSamplingLevel(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==14){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentReq.setSamplingDepth(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==15){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentReq.setLhw(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==16){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentReq.setYjt(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==17){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentReq.setSyl(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==18){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentReq.setTp(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==19){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentReq.setTn(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==20){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentReq.setHg(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==21){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentReq.setAss(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==22){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentReq.setZn(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==23){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentReq.setCd(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==24){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentReq.setPb(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==25){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentReq.setCu(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==26){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentReq.setTcr(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==27){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        sedimentReq.setEh(new BigDecimal(cellValue));
                    }
                }
            }

        }

        //数据库更新 注意返回false代表更新成功
//        MonitorDataReport monitorDataReport = new MonitorDataReport();
//        BeanUtils.copyProperties(sedimentReq,monitorDataReport);
        boolean dataExist=false; //= monitorDataReportService.updateData(monitorDataReport, Constant.fisheggQuantitativeType);
        returnList.add(String.valueOf(dataExist));

        // Excel文件列表查重(遍历allMapList中底栖站位多样性数据，检索是否已包含当前数据)
        boolean sameInfo=false;
        SedimentReq item;
        for(Map<String,Object> map:allMapList){//for(Map<String,Object> map:dominantSpeciesController.allMapList){
            if(map.get("excelType")!=null && map.get("excelType").toString().equals("SedimentExcelRule")) {
                for(Object obj:(List<Object>)map.get("data")) {
                    item=(SedimentReq)obj;
                    if(
                            item.getMonitoringArea().equals(sedimentReq.getMonitoringArea()) &&
                                    item.getEcologicalType().equals(sedimentReq.getEcologicalType()) &&
                                    item.getTaskDate().equals(sedimentReq.getTaskDate()) &&
                                    item.getMonitorCompany().equals(sedimentReq.getMonitorCompany()) &&
                                    item.getOrganizationCompany().equals(sedimentReq.getOrganizationCompany()) &&
                                    item.getReportDate().equals(sedimentReq.getReportDate()) &&
                                    item.getYear().equals(sedimentReq.getYear()) &&
                                    item.getVoyage().equals(sedimentReq.getVoyage())) {
                        sameInfo = true;
                    }
                }
            }
        }
        returnList.add(String.valueOf(sameInfo));

        // 校验合格：写入dataList
        if(totalRst==0){
            dataList1.add(sedimentReq);
            excelDataImportController.excelDataList.add(sedimentReq);
        }
        return returnList;
    }

    /**
     * 对沉积物-9种特殊表头value值 按xml规则校验，并写入对象
     * @param sheet
     * @param headMap
     * @param entityName
     * @param sedimentReq
     */
    public void specialHandlingSediment(Sheet sheet,Map headMap,String entityName,SedimentReq sedimentReq,Integer totalRows){
        int totalRst = 0;//每行格式错误单元格的数目
        int validaterst = 0;

        //取第1+1行
        Row row = sheet.getRow(1);
        //存放监控区值
        //1代表第1+1列
        Cell cell = row.getCell(1);
        //0代表监控区
        String headTitle = headMap.get(Constant.monitoringAreaCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            sedimentReq.setMonitoringArea(cell.getStringCellValue());
        }

        //存放生态类型值
        //8代表8+1列
        cell = row.getCell(8);
        headTitle = headMap.get(Constant.ecologicaltypeCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,8+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            sedimentReq.setEcologicalType(cell.getStringCellValue());
        }

        //存放任务日期
        //16代表16+1列
        cell = row.getCell(16);
        headTitle = headMap.get(Constant.missionDateCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,16+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            //判断该日期在excel表格的格式是否是日期格式
            if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }

            try{
                String strDate=getStringCellValue(cell);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate=null;
                if(strDate.contains("-")){
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains(".")){
                    dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains("/")){
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else{
                    Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                sedimentReq.setTaskDate(localDate);
            } catch (Exception e){
                sedimentReq.setTaskDate(null);
            }
        }

        //取第2+1行
        row = sheet.getRow(2);
        //存放监测单位
        //1代表第1+1列
        cell = row.getCell(1);
        headTitle = headMap.get(Constant.monitoringUnitCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            sedimentReq.setMonitorCompany(cell.getStringCellValue());
        }

        //存放组织单位
        //代表8+1列
        cell = row.getCell(8);
        headTitle = headMap.get(Constant.organizationalUnitCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,8+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            sedimentReq.setOrganizationCompany(cell.getStringCellValue());
        }

        //存放填报日期
        //16代表16+1列
        cell = row.getCell(16);
        headTitle = headMap.get(Constant.completionDateCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,16+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制

            //判断该日期在excel表格的格式是否是日期格式
            if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }

            try{
                String strDate = getStringCellValue(cell);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate=null;
                if(strDate.contains("-")){
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains(".")){
                    dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains("/")){
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else{
                    Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                sedimentReq.setReportDate(localDate);
            } catch (Exception e){
                sedimentReq.setReportDate(null);
            }
        }


        //取最后一行
        row = sheet.getRow(totalRows - 1);
        //存放填报人
        //1代表第1+1列
        cell = row.getCell(1);
        headTitle = headMap.get(Constant.informantCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            sedimentReq.setByzd1(cell.getStringCellValue());
        }

        //存放校对人
        cell = row.getCell(4);
        headTitle = headMap.get(Constant.proofreaderCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,4+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            sedimentReq.setByzd2(cell.getStringCellValue());
        }

        //存放审核人
        cell = row.getCell(7);
        headTitle = headMap.get(Constant.auditorCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,7+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            sedimentReq.setByzd3(cell.getStringCellValue());
        }

    }

    //浮游植物
    private List<String> phytoplanktonExcelRule(Integer totalCells,Integer totalRows,Row row,Map headMap,String entityName,int r,List<Map<String, Object>> allMapList,Sheet sheet){
        List<String> returnList=new ArrayList<>();
        PhytoplanktonReq phytoplanktonReq = new PhytoplanktonReq();
        int totalRst = 0;//每行格式错误单元格的数目
        int validaterst = 0;

        //将年份和航次信息写入对象
        phytoplanktonReq.setYear(YEAR);
        phytoplanktonReq.setVoyage(VOYAGE);
        phytoplanktonReq.setDataType(dataType);

        //对9种特殊表头value值 按xml规则校验，并写入对象
        specialHandlingPhytoplankton(sheet,headMap,entityName,phytoplanktonReq,totalRows);

        //取
        Row excelheadRow1 = sheet.getRow(Constant.differInfoStartRow);
        int excelLastCellNum = excelheadRow1.getLastCellNum();

        // 循环row的列，按xml规则校验，并写入对象
        for (int c = 0; c < excelLastCellNum; c++) {
            Cell cell = row.getCell(c);
            String headTitle = headMap.get(c+Constant.constantTableHeadCount).toString();
            /**按规则验证cell格式**/
            validaterst = validateCellData(r+1,c+1,cell,entityName,headTitle);
            totalRst += validaterst;

            if(totalRst == 0 && cell != null) {             // 定制
                //6代表xml文件第6个 下面同理
                if(c+Constant.officialDataStartSign==6){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    phytoplanktonReq.setStationName(cell.getStringCellValue());
                } else if(c+Constant.officialDataStartSign==7){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    phytoplanktonReq.setPlanLon(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==8){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    phytoplanktonReq.setPlanLat(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==9){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    phytoplanktonReq.setRealLon(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==10){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    phytoplanktonReq.setRealLat(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==11){
                    if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                    }
                    try{
                        String strDate=getStringCellValue(cell);
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                        LocalDate localDate=null;
                        if(strDate.contains("-")){
                            dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else if(strDate.contains(".")){
                            dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else if(strDate.contains("/")){
                            dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else{
                            Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                            localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        }
                        phytoplanktonReq.setMonitorDate(localDate);
                    } catch (Exception e){
                        phytoplanktonReq.setMonitorDate(null);
                    }
                } else if(c+Constant.officialDataStartSign==12){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)){
                        phytoplanktonReq.setWaterDepth(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==13){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        phytoplanktonReq.setWaterType(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==14){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        phytoplanktonReq.setRopeLength(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==15){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        phytoplanktonReq.setWaterFiltration(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==16){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        phytoplanktonReq.setCategory(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==17){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        phytoplanktonReq.setBiologicalChineseName(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==18){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        phytoplanktonReq.setBiologicalLatinName(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==19){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        phytoplanktonReq.setCellCount(cellValue);
                    }
                }
            }
        }

        //数据库更新 注意返回false代表更新成功
//        MonitorDataReport monitorDataReport = new MonitorDataReport();
//        BeanUtils.copyProperties(fisheggQuantitativeReq,monitorDataReport);
        boolean dataExist=false; //= monitorDataReportService.updateData(monitorDataReport, Constant.fisheggQuantitativeType);
        returnList.add(String.valueOf(dataExist));

        // Excel文件列表查重(遍历allMapList中底栖站位多样性数据，检索是否已包含当前数据)
        boolean sameInfo=false;
        PhytoplanktonReq item;
        for(Map<String,Object> map:allMapList){//for(Map<String,Object> map:dominantSpeciesController.allMapList){
            if(map.get("excelType")!=null && map.get("excelType").toString().equals("FisheggQuantitativeExcelRule")) {
                for(Object obj:(List<Object>)map.get("data")) {
                    item=(PhytoplanktonReq)obj;
                    if(
                            item.getMonitoringArea().equals(phytoplanktonReq.getMonitoringArea()) &&
                                    item.getEcologicalType().equals(phytoplanktonReq.getEcologicalType()) &&
                                    item.getTaskDate().equals(phytoplanktonReq.getTaskDate()) &&
                                    item.getMonitorCompany().equals(phytoplanktonReq.getMonitorCompany()) &&
                                    item.getOrganizationCompany().equals(phytoplanktonReq.getOrganizationCompany()) &&
                                    item.getReportDate().equals(phytoplanktonReq.getReportDate()) &&
                                    item.getYear().equals(phytoplanktonReq.getYear()) &&
                                    item.getVoyage().equals(phytoplanktonReq.getVoyage())) {
                        sameInfo = true;
                    }
                }
            }
        }
        returnList.add(String.valueOf(sameInfo));

        // 校验合格：写入dataList
        if(totalRst==0){
            dataList1.add(phytoplanktonReq);
            excelDataImportController.excelDataList.add(phytoplanktonReq);
        }
        return returnList;
    }

    /**
     * 对浮游植物-9种特殊表头value值 按xml规则校验，并写入对象
     * @param sheet
     * @param headMap
     * @param entityName
     * @param phytoplanktonReq
     */
    public void specialHandlingPhytoplankton(Sheet sheet,Map headMap,String entityName,PhytoplanktonReq phytoplanktonReq,Integer totalRows){
        int totalRst = 0;//每行格式错误单元格的数目
        int validaterst = 0;

        //取第1+1行
        Row row = sheet.getRow(1);
        //存放监控区值
        //1代表第1+1列
        Cell cell = row.getCell(1);
        //0代表监控区
        String headTitle = headMap.get(Constant.monitoringAreaCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            phytoplanktonReq.setMonitoringArea(cell.getStringCellValue());
        }

        //存放生态类型值
        //6代表6+1列
        cell = row.getCell(6);
        headTitle = headMap.get(Constant.ecologicaltypeCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,6+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            phytoplanktonReq.setEcologicalType(cell.getStringCellValue());
        }

        //存放任务日期
        //11代表11+1列
        cell = row.getCell(11);
        headTitle = headMap.get(Constant.missionDateCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,11+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            //判断该日期在excel表格的格式是否是日期格式
            if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }

            try{
                String strDate=getStringCellValue(cell);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate=null;
                if(strDate.contains("-")){
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains(".")){
                    dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains("/")){
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else{
                    Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                phytoplanktonReq.setTaskDate(localDate);
            } catch (Exception e){
                phytoplanktonReq.setTaskDate(null);
            }
        }

        //取第2+1行
        row = sheet.getRow(2);
        //存放监测单位
        //1代表第1+1列
        cell = row.getCell(1);
        headTitle = headMap.get(Constant.monitoringUnitCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            phytoplanktonReq.setMonitorCompany(cell.getStringCellValue());
        }

        //存放组织单位
        //6代表6+1列
        cell = row.getCell(6);
        headTitle = headMap.get(Constant.organizationalUnitCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,6+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            phytoplanktonReq.setOrganizationCompany(cell.getStringCellValue());
        }

        //存放填报日期
        //11代表11+1列
        cell = row.getCell(11);
        headTitle = headMap.get(Constant.completionDateCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,11+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制

            //判断该日期在excel表格的格式是否是日期格式
            if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }

            try{
                String strDate = getStringCellValue(cell);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate=null;
                if(strDate.contains("-")){
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains(".")){
                    dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains("/")){
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else{
                    Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                phytoplanktonReq.setReportDate(localDate);
            } catch (Exception e){
                phytoplanktonReq.setReportDate(null);
            }
        }


        //取最后一行
        row = sheet.getRow(totalRows - 1);
        //存放填报人
        //1代表第1+1列
        cell = row.getCell(1);
        headTitle = headMap.get(Constant.informantCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            phytoplanktonReq.setByzd1(cell.getStringCellValue());
        }

        //存放校对人
        cell = row.getCell(4);
        headTitle = headMap.get(Constant.proofreaderCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,4+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            phytoplanktonReq.setByzd2(cell.getStringCellValue());
        }

        //存放审核人
        cell = row.getCell(7);
        headTitle = headMap.get(Constant.auditorCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,7+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            phytoplanktonReq.setByzd3(cell.getStringCellValue());
        }

    }

    //大型底栖动物定量数据表
    private List<String> macrobenthosQuantitativeExcelRule(Integer totalCells,Integer totalRows,Row row,Map headMap,String entityName,int r,List<Map<String, Object>> allMapList,Sheet sheet){
        List<String> returnList=new ArrayList<>();
        MacrobenthosQuantitativeReq macrobenthosQuantitativeReq = new MacrobenthosQuantitativeReq();
        int totalRst = 0;//每行格式错误单元格的数目
        int validaterst = 0;

        //将年份和航次信息写入对象
        macrobenthosQuantitativeReq.setYear(YEAR);
        macrobenthosQuantitativeReq.setVoyage(VOYAGE);
        macrobenthosQuantitativeReq.setDataType(dataType);

        //对9种特殊表头value值 按xml规则校验，并写入对象
        specialHandlingMacrobenthosQuantitative(sheet,headMap,entityName,macrobenthosQuantitativeReq,totalRows);

        //取
        Row excelheadRow1 = sheet.getRow(Constant.differInfoStartRow);
        int excelLastCellNum = excelheadRow1.getLastCellNum();

        // 循环row的列，按xml规则校验，并写入对象
        for (int c = 0; c < excelLastCellNum; c++) {
            Cell cell = row.getCell(c);
            String headTitle = headMap.get(c+Constant.constantTableHeadCount).toString();
            /**按规则验证cell格式**/
            validaterst = validateCellData(r+1,c+1,cell,entityName,headTitle);
            totalRst += validaterst;

            if(totalRst == 0 && cell != null) {             // 定制
                //6代表xml文件第6个 下面同理
                if(c+Constant.officialDataStartSign==6){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    macrobenthosQuantitativeReq.setStationName(cell.getStringCellValue());
                } else if(c+Constant.officialDataStartSign==7){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    macrobenthosQuantitativeReq.setPlanLon(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==8){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    macrobenthosQuantitativeReq.setPlanLat(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==9){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    macrobenthosQuantitativeReq.setRealLon(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==10){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    macrobenthosQuantitativeReq.setRealLat(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==11){
                    if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                    }
                    try{
                        String strDate=getStringCellValue(cell);
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                        LocalDate localDate=null;
                        if(strDate.contains("-")){
                            dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else if(strDate.contains(".")){
                            dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else if(strDate.contains("/")){
                            dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else{
                            Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                            localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        }
                        macrobenthosQuantitativeReq.setMonitorDate(localDate);
                    } catch (Exception e){
                        macrobenthosQuantitativeReq.setMonitorDate(null);
                    }
                } else if(c+Constant.officialDataStartSign==12){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)){
                        macrobenthosQuantitativeReq.setWaterDepth(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==13){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        macrobenthosQuantitativeReq.setSedimentType(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==14){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        macrobenthosQuantitativeReq.setDredgerType(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==15){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        macrobenthosQuantitativeReq.setSamplingTimes(Integer.parseInt(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==16){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        macrobenthosQuantitativeReq.setQuadratArea(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==17){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        macrobenthosQuantitativeReq.setSampleThickness(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==18){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        macrobenthosQuantitativeReq.setBiologicalChineseName(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==19){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        macrobenthosQuantitativeReq.setBiologicalLatinName(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==20){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        macrobenthosQuantitativeReq.setCount(Integer.parseInt(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==21){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        macrobenthosQuantitativeReq.setDensity(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==22){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        macrobenthosQuantitativeReq.setWeight(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==23){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        macrobenthosQuantitativeReq.setBiomass(new BigDecimal(cellValue));
                    }
                }
            }

        }

        //数据库更新 注意返回false代表更新成功
//        MonitorDataReport monitorDataReport = new MonitorDataReport();
//        BeanUtils.copyProperties(macrobenthosQuantitativeReq,monitorDataReport);
        boolean dataExist=false; //= monitorDataReportService.updateData(monitorDataReport, Constant.fisheggQuantitativeType);
        returnList.add(String.valueOf(dataExist));

        // Excel文件列表查重(遍历allMapList中底栖站位多样性数据，检索是否已包含当前数据)
        boolean sameInfo=false;
        MacrobenthosQuantitativeReq item;
        for(Map<String,Object> map:allMapList){//for(Map<String,Object> map:dominantSpeciesController.allMapList){
            if(map.get("excelType")!=null && map.get("excelType").toString().equals("MacrobenthosQuantitativeExcelRule")) {
                for(Object obj:(List<Object>)map.get("data")) {
                    item=(MacrobenthosQuantitativeReq)obj;
                    if(
                            item.getMonitoringArea().equals(macrobenthosQuantitativeReq.getMonitoringArea()) &&
                                    item.getEcologicalType().equals(macrobenthosQuantitativeReq.getEcologicalType()) &&
                                    item.getTaskDate().equals(macrobenthosQuantitativeReq.getTaskDate()) &&
                                    item.getMonitorCompany().equals(macrobenthosQuantitativeReq.getMonitorCompany()) &&
                                    item.getOrganizationCompany().equals(macrobenthosQuantitativeReq.getOrganizationCompany()) &&
                                    item.getReportDate().equals(macrobenthosQuantitativeReq.getReportDate()) &&
                                    item.getYear().equals(macrobenthosQuantitativeReq.getYear()) &&
                                    item.getVoyage().equals(macrobenthosQuantitativeReq.getVoyage())) {
                        sameInfo = true;
                    }
                }
            }
        }
        returnList.add(String.valueOf(sameInfo));

        // 校验合格：写入dataList
        if(totalRst==0){
            dataList1.add(macrobenthosQuantitativeReq);
            excelDataImportController.excelDataList.add(macrobenthosQuantitativeReq);
        }
        return returnList;
    }

    /**
     * 对大型底栖动物定量-9种特殊表头value值 按xml规则校验，并写入对象
     * @param sheet
     * @param headMap
     * @param entityName
     * @param macrobenthosQuantitativeReq
     */
    public void specialHandlingMacrobenthosQuantitative(Sheet sheet,Map headMap,String entityName,MacrobenthosQuantitativeReq macrobenthosQuantitativeReq,Integer totalRows){
        int totalRst = 0;//每行格式错误单元格的数目
        int validaterst = 0;

        //取第1+1行
        Row row = sheet.getRow(1);
        //存放监控区值
        //1代表第1+1列
        Cell cell = row.getCell(1);
        //0代表监控区
        String headTitle = headMap.get(Constant.monitoringAreaCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            macrobenthosQuantitativeReq.setMonitoringArea(cell.getStringCellValue());
        }

        //存放生态类型值
        //6代表6+1列
        cell = row.getCell(6);
        headTitle = headMap.get(Constant.ecologicaltypeCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,6+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            macrobenthosQuantitativeReq.setEcologicalType(cell.getStringCellValue());
        }

        //存放任务日期
        //11代表11+1列
        cell = row.getCell(11);
        headTitle = headMap.get(Constant.missionDateCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,11+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            //判断该日期在excel表格的格式是否是日期格式
            if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }

            try{
                String strDate=getStringCellValue(cell);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate=null;
                if(strDate.contains("-")){
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains(".")){
                    dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains("/")){
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else{
                    Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                macrobenthosQuantitativeReq.setTaskDate(localDate);
            } catch (Exception e){
                macrobenthosQuantitativeReq.setTaskDate(null);
            }
        }

        //取第2+1行
        row = sheet.getRow(2);
        //存放监测单位
        //1代表第1+1列
        cell = row.getCell(1);
        headTitle = headMap.get(Constant.monitoringUnitCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            macrobenthosQuantitativeReq.setMonitorCompany(cell.getStringCellValue());
        }

        //存放组织单位
        //6代表6+1列
        cell = row.getCell(6);
        headTitle = headMap.get(Constant.organizationalUnitCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,6+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            macrobenthosQuantitativeReq.setOrganizationCompany(cell.getStringCellValue());
        }

        //存放填报日期
        //11代表11+1列
        cell = row.getCell(11);
        headTitle = headMap.get(Constant.completionDateCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,11+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制

            //判断该日期在excel表格的格式是否是日期格式
            if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }

            try{
                String strDate = getStringCellValue(cell);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate=null;
                if(strDate.contains("-")){
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains(".")){
                    dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains("/")){
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else{
                    Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                macrobenthosQuantitativeReq.setReportDate(localDate);
            } catch (Exception e){
                macrobenthosQuantitativeReq.setReportDate(null);
            }
        }


        //取最后一行
        row = sheet.getRow(totalRows - 1);
        //存放填报人
        //1代表第1+1列
        cell = row.getCell(1);
        headTitle = headMap.get(Constant.informantCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            macrobenthosQuantitativeReq.setByzd1(cell.getStringCellValue());
        }

        //存放校对人
        cell = row.getCell(4);
        headTitle = headMap.get(Constant.proofreaderCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,4+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            macrobenthosQuantitativeReq.setByzd2(cell.getStringCellValue());
        }

        //存放审核人
        cell = row.getCell(7);
        headTitle = headMap.get(Constant.auditorCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,7+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            macrobenthosQuantitativeReq.setByzd3(cell.getStringCellValue());
        }

    }

    //大型底栖动物定性数据表
    private List<String> macrobenthosQualitativeExcelRule(Integer totalCells,Integer totalRows,Row row,Map headMap,String entityName,int r,List<Map<String, Object>> allMapList,Sheet sheet){
        List<String> returnList=new ArrayList<>();
        MacrobenthosQualitativeReq macrobenthosQualitativeReq = new MacrobenthosQualitativeReq();
        int totalRst = 0;//每行格式错误单元格的数目
        int validaterst = 0;

        //将年份和航次信息写入对象
        macrobenthosQualitativeReq.setYear(YEAR);
        macrobenthosQualitativeReq.setVoyage(VOYAGE);
        macrobenthosQualitativeReq.setDataType(dataType);

        //对9种特殊表头value值 按xml规则校验，并写入对象
        specialHandlingMacrobenthosQualitative(sheet,headMap,entityName,macrobenthosQualitativeReq,totalRows);

        //取
        Row excelheadRow1 = sheet.getRow(Constant.differInfoStartRow);
        int excelLastCellNum = excelheadRow1.getLastCellNum();

        // 循环row的列，按xml规则校验，并写入对象
        for (int c = 0; c < excelLastCellNum; c++) {
            Cell cell = row.getCell(c);
            String headTitle = headMap.get(c+Constant.constantTableHeadCount).toString();
            /**按规则验证cell格式**/
            validaterst = validateCellData(r+1,c+1,cell,entityName,headTitle);
            totalRst += validaterst;

            if(totalRst == 0 && cell != null) {             // 定制
                //6代表xml文件第6个 下面同理
                if(c+Constant.officialDataStartSign==6){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    macrobenthosQualitativeReq.setStationName(cell.getStringCellValue());
                } else if(c+Constant.officialDataStartSign==7){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    macrobenthosQualitativeReq.setPlanLon(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==8){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    macrobenthosQualitativeReq.setPlanLat(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==9){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    macrobenthosQualitativeReq.setRealLon(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==10){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    macrobenthosQualitativeReq.setRealLat(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==11){
                    if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                    }
                    try{
                        String strDate=getStringCellValue(cell);
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                        LocalDate localDate=null;
                        if(strDate.contains("-")){
                            dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else if(strDate.contains(".")){
                            dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else if(strDate.contains("/")){
                            dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else{
                            Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                            localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        }
                        macrobenthosQualitativeReq.setMonitorDate(localDate);
                    } catch (Exception e){
                        macrobenthosQualitativeReq.setMonitorDate(null);
                    }
                } else if(c+Constant.officialDataStartSign==12){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)){
                        macrobenthosQualitativeReq.setNetType(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==13){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        macrobenthosQualitativeReq.setNetPortDensity(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==14){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        macrobenthosQualitativeReq.setTrawlDistance(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==15){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        macrobenthosQualitativeReq.setTrawlTime(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==16){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        macrobenthosQualitativeReq.setBiologicalChineseName(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==17){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        macrobenthosQualitativeReq.setBiologicalLatinName(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==18){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        macrobenthosQualitativeReq.setCount(Integer.parseInt(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==19){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        macrobenthosQualitativeReq.setDensity(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==20){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        macrobenthosQualitativeReq.setWeight(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==21){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        macrobenthosQualitativeReq.setBiomass(new BigDecimal(cellValue));
                    }
                }
            }

        }

        //数据库更新 注意返回false代表更新成功
//        MonitorDataReport monitorDataReport = new MonitorDataReport();
//        BeanUtils.copyProperties(macrobenthosQualitativeReq,monitorDataReport);
        boolean dataExist=false; //= monitorDataReportService.updateData(monitorDataReport, Constant.fisheggQuantitativeType);
        returnList.add(String.valueOf(dataExist));

        // Excel文件列表查重(遍历allMapList中底栖站位多样性数据，检索是否已包含当前数据)
        boolean sameInfo=false;
        MacrobenthosQualitativeReq item;
        for(Map<String,Object> map:allMapList){//for(Map<String,Object> map:dominantSpeciesController.allMapList){
            if(map.get("excelType")!=null && map.get("excelType").toString().equals("MacrobenthosQualitativeExcelRule")) {
                for(Object obj:(List<Object>)map.get("data")) {
                    item=(MacrobenthosQualitativeReq)obj;
                    if(
                            item.getMonitoringArea().equals(macrobenthosQualitativeReq.getMonitoringArea()) &&
                                    item.getEcologicalType().equals(macrobenthosQualitativeReq.getEcologicalType()) &&
                                    item.getTaskDate().equals(macrobenthosQualitativeReq.getTaskDate()) &&
                                    item.getMonitorCompany().equals(macrobenthosQualitativeReq.getMonitorCompany()) &&
                                    item.getOrganizationCompany().equals(macrobenthosQualitativeReq.getOrganizationCompany()) &&
                                    item.getReportDate().equals(macrobenthosQualitativeReq.getReportDate()) &&
                                    item.getYear().equals(macrobenthosQualitativeReq.getYear()) &&
                                    item.getVoyage().equals(macrobenthosQualitativeReq.getVoyage())) {
                        sameInfo = true;
                    }
                }
            }
        }
        returnList.add(String.valueOf(sameInfo));

        // 校验合格：写入dataList
        if(totalRst==0){
            dataList1.add(macrobenthosQualitativeReq);
            excelDataImportController.excelDataList.add(macrobenthosQualitativeReq);
        }
        return returnList;
    }

    /**
     * 对大型底栖动物定性-9种特殊表头value值 按xml规则校验，并写入对象
     * @param sheet
     * @param headMap
     * @param entityName
     * @param macrobenthosQualitativeReq
     */
    public void specialHandlingMacrobenthosQualitative(Sheet sheet,Map headMap,String entityName,MacrobenthosQualitativeReq macrobenthosQualitativeReq,Integer totalRows){
        int totalRst = 0;//每行格式错误单元格的数目
        int validaterst = 0;

        //取第1+1行
        Row row = sheet.getRow(1);
        //存放监控区值
        //1代表第1+1列
        Cell cell = row.getCell(1);
        //0代表监控区
        String headTitle = headMap.get(Constant.monitoringAreaCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            macrobenthosQualitativeReq.setMonitoringArea(cell.getStringCellValue());
        }

        //存放生态类型值
        //6代表6+1列
        cell = row.getCell(6);
        headTitle = headMap.get(Constant.ecologicaltypeCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,6+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            macrobenthosQualitativeReq.setEcologicalType(cell.getStringCellValue());
        }

        //存放任务日期
        //11代表11+1列
        cell = row.getCell(11);
        headTitle = headMap.get(Constant.missionDateCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,11+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            //判断该日期在excel表格的格式是否是日期格式
            if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }

            try{
                String strDate=getStringCellValue(cell);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate=null;
                if(strDate.contains("-")){
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains(".")){
                    dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains("/")){
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else{
                    Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                macrobenthosQualitativeReq.setTaskDate(localDate);
            } catch (Exception e){
                macrobenthosQualitativeReq.setTaskDate(null);
            }
        }

        //取第2+1行
        row = sheet.getRow(2);
        //存放监测单位
        //1代表第1+1列
        cell = row.getCell(1);
        headTitle = headMap.get(Constant.monitoringUnitCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            macrobenthosQualitativeReq.setMonitorCompany(cell.getStringCellValue());
        }

        //存放组织单位
        //6代表6+1列
        cell = row.getCell(6);
        headTitle = headMap.get(Constant.organizationalUnitCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,6+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            macrobenthosQualitativeReq.setOrganizationCompany(cell.getStringCellValue());
        }

        //存放填报日期
        //11代表11+1列
        cell = row.getCell(11);
        headTitle = headMap.get(Constant.completionDateCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,11+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制

            //判断该日期在excel表格的格式是否是日期格式
            if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }

            try{
                String strDate = getStringCellValue(cell);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate=null;
                if(strDate.contains("-")){
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains(".")){
                    dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains("/")){
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else{
                    Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                macrobenthosQualitativeReq.setReportDate(localDate);
            } catch (Exception e){
                macrobenthosQualitativeReq.setReportDate(null);
            }
        }


        //取最后一行
        row = sheet.getRow(totalRows - 1);
        //存放填报人
        //1代表第1+1列
        cell = row.getCell(1);
        headTitle = headMap.get(Constant.informantCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            macrobenthosQualitativeReq.setByzd1(cell.getStringCellValue());
        }

        //存放校对人
        cell = row.getCell(4);
        headTitle = headMap.get(Constant.proofreaderCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,4+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            macrobenthosQualitativeReq.setByzd2(cell.getStringCellValue());
        }

        //存放审核人
        cell = row.getCell(7);
        headTitle = headMap.get(Constant.auditorCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,7+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            macrobenthosQualitativeReq.setByzd3(cell.getStringCellValue());
        }

    }

    //大型浮游动物1型网数据表
    private List<String> largezooplanktonInetExcelRule(Integer totalCells,Integer totalRows,Row row,Map headMap,String entityName,int r,List<Map<String, Object>> allMapList,Sheet sheet){
        List<String> returnList=new ArrayList<>();
        LargezooplanktonInetReq largezooplanktonInetReq = new LargezooplanktonInetReq();
        int totalRst = 0;//每行格式错误单元格的数目
        int validaterst = 0;

        //将年份和航次信息写入对象
        largezooplanktonInetReq.setYear(YEAR);
        largezooplanktonInetReq.setVoyage(VOYAGE);
        largezooplanktonInetReq.setDataType(dataType);

        //对9种特殊表头value值 按xml规则校验，并写入对象
        specialHandlingLargezooplanktonInet(sheet,headMap,entityName,largezooplanktonInetReq,totalRows);

        //取
        Row excelheadRow1 = sheet.getRow(Constant.differInfoStartRow);
        int excelLastCellNum = excelheadRow1.getLastCellNum();

        // 循环row的列，按xml规则校验，并写入对象
        for (int c = 0; c < excelLastCellNum; c++) {
            Cell cell = row.getCell(c);
            String headTitle = headMap.get(c+Constant.constantTableHeadCount).toString();
            /**按规则验证cell格式**/
            validaterst = validateCellData(r+1,c+1,cell,entityName,headTitle);
            totalRst += validaterst;

            if(totalRst == 0 && cell != null) {             // 定制
                //6代表xml文件第6个 下面同理
                if(c+Constant.officialDataStartSign==6){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    largezooplanktonInetReq.setStationName(cell.getStringCellValue());
                } else if(c+Constant.officialDataStartSign==7){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    largezooplanktonInetReq.setPlanLon(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==8){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    largezooplanktonInetReq.setPlanLat(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==9){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    largezooplanktonInetReq.setRealLon(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==10){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    largezooplanktonInetReq.setRealLat(new BigDecimal(cellValue));
                }  else if(c+Constant.officialDataStartSign==11){
                    if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                    }
                    try{
                        String strDate=getStringCellValue(cell);
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                        LocalDate localDate=null;
                        if(strDate.contains("-")){
                            dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else if(strDate.contains(".")){
                            dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else if(strDate.contains("/")){
                            dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else{
                            Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                            localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        }
                        largezooplanktonInetReq.setMonitorDate(localDate);
                    } catch (Exception e){
                        largezooplanktonInetReq.setMonitorDate(null);
                    }
                } else if(c+Constant.officialDataStartSign==12){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)){
                        largezooplanktonInetReq.setWaterDepth(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==13){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        largezooplanktonInetReq.setRopeLength(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==14){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        largezooplanktonInetReq.setWaterFiltration(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==15){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        largezooplanktonInetReq.setTotalBiomass(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==16){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        largezooplanktonInetReq.setCategory(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==17){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        largezooplanktonInetReq.setBiologicalChineseName(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==18){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        largezooplanktonInetReq.setBiologicalLatinName(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==19){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        largezooplanktonInetReq.setDensity(new BigDecimal(cellValue));
                    }
                }
            }

        }

        //数据库更新 注意返回false代表更新成功
//        MonitorDataReport monitorDataReport = new MonitorDataReport();
//        BeanUtils.copyProperties(largezooplanktonInetReq,monitorDataReport);
        boolean dataExist=false; //= monitorDataReportService.updateData(monitorDataReport, Constant.fisheggQuantitativeType);
        returnList.add(String.valueOf(dataExist));

        // Excel文件列表查重(遍历allMapList中底栖站位多样性数据，检索是否已包含当前数据)
        boolean sameInfo=false;
        LargezooplanktonInetReq item;
        for(Map<String,Object> map:allMapList){//for(Map<String,Object> map:dominantSpeciesController.allMapList){
            if(map.get("excelType")!=null && map.get("excelType").toString().equals("LargezooplanktonInetExcelRule")) {
                for(Object obj:(List<Object>)map.get("data")) {
                    item=(LargezooplanktonInetReq)obj;
                    if(
                            item.getMonitoringArea().equals(largezooplanktonInetReq.getMonitoringArea()) &&
                                    item.getEcologicalType().equals(largezooplanktonInetReq.getEcologicalType()) &&
                                    item.getTaskDate().equals(largezooplanktonInetReq.getTaskDate()) &&
                                    item.getMonitorCompany().equals(largezooplanktonInetReq.getMonitorCompany()) &&
                                    item.getOrganizationCompany().equals(largezooplanktonInetReq.getOrganizationCompany()) &&
                                    item.getReportDate().equals(largezooplanktonInetReq.getReportDate()) &&
                                    item.getYear().equals(largezooplanktonInetReq.getYear()) &&
                                    item.getVoyage().equals(largezooplanktonInetReq.getVoyage())) {
                        sameInfo = true;
                    }
                }
            }
        }
        returnList.add(String.valueOf(sameInfo));

        // 校验合格：写入dataList
        if(totalRst==0){
            dataList1.add(largezooplanktonInetReq);
            excelDataImportController.excelDataList.add(largezooplanktonInetReq);
        }
        return returnList;
    }

    /**
     * 对型浮游动物1型网数据-9种特殊表头value值 按xml规则校验，并写入对象
     * @param sheet
     * @param headMap
     * @param entityName
     * @param largezooplanktonInetReq
     */
    public void specialHandlingLargezooplanktonInet(Sheet sheet,Map headMap,String entityName,LargezooplanktonInetReq largezooplanktonInetReq,Integer totalRows){
        int totalRst = 0;//每行格式错误单元格的数目
        int validaterst = 0;

        //取第1+1行
        Row row = sheet.getRow(1);
        //存放监控区值
        //1代表第1+1列
        Cell cell = row.getCell(1);
        //0代表监控区
        String headTitle = headMap.get(Constant.monitoringAreaCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            largezooplanktonInetReq.setMonitoringArea(cell.getStringCellValue());
        }

        //存放生态类型值
        //6代表6+1列
        cell = row.getCell(6);
        headTitle = headMap.get(Constant.ecologicaltypeCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,6+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            largezooplanktonInetReq.setEcologicalType(cell.getStringCellValue());
        }

        //存放任务日期
        //11代表11+1列
        cell = row.getCell(11);
        headTitle = headMap.get(Constant.missionDateCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,11+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            //判断该日期在excel表格的格式是否是日期格式
            if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }

            try{
                String strDate=getStringCellValue(cell);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate=null;
                if(strDate.contains("-")){
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains(".")){
                    dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains("/")){
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else{
                    Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                largezooplanktonInetReq.setTaskDate(localDate);
            } catch (Exception e){
                largezooplanktonInetReq.setTaskDate(null);
            }
        }

        //取第2+1行
        row = sheet.getRow(2);
        //存放监测单位
        //1代表第1+1列
        cell = row.getCell(1);
        headTitle = headMap.get(Constant.monitoringUnitCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            largezooplanktonInetReq.setMonitorCompany(cell.getStringCellValue());
        }

        //存放组织单位
        //6代表6+1列
        cell = row.getCell(6);
        headTitle = headMap.get(Constant.organizationalUnitCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,6+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            largezooplanktonInetReq.setOrganizationCompany(cell.getStringCellValue());
        }

        //存放填报日期
        //11代表11+1列
        cell = row.getCell(11);
        headTitle = headMap.get(Constant.completionDateCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,11+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制

            //判断该日期在excel表格的格式是否是日期格式
            if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }

            try{
                String strDate = getStringCellValue(cell);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate=null;
                if(strDate.contains("-")){
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains(".")){
                    dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains("/")){
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else{
                    Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                largezooplanktonInetReq.setReportDate(localDate);
            } catch (Exception e){
                largezooplanktonInetReq.setReportDate(null);
            }
        }


        //取最后一行
        row = sheet.getRow(totalRows - 1);
        //存放填报人
        //1代表第1+1列
        cell = row.getCell(1);
        headTitle = headMap.get(Constant.informantCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            largezooplanktonInetReq.setByzd1(cell.getStringCellValue());
        }

        //存放校对人
        cell = row.getCell(4);
        headTitle = headMap.get(Constant.proofreaderCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,4+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            largezooplanktonInetReq.setByzd2(cell.getStringCellValue());
        }

        //存放审核人
        cell = row.getCell(7);
        headTitle = headMap.get(Constant.auditorCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,7+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            largezooplanktonInetReq.setByzd3(cell.getStringCellValue());
        }

    }

    //潮间带数据表
    private List<String> intertidalzonebiologicalQuantitativeExcelRule(Integer totalCells,Integer totalRows,Row row,Map headMap,String entityName,int r,List<Map<String, Object>> allMapList,Sheet sheet){
        List<String> returnList=new ArrayList<>();
        IntertidalzonebiologicalQuantitativeReq intertidalzonebiologicalQuantitativeReq = new IntertidalzonebiologicalQuantitativeReq();
        int totalRst = 0;//每行格式错误单元格的数目
        int validaterst = 0;

        //将年份和航次信息写入对象
        intertidalzonebiologicalQuantitativeReq.setYear(YEAR);
        intertidalzonebiologicalQuantitativeReq.setVoyage(VOYAGE);
        intertidalzonebiologicalQuantitativeReq.setDataType(dataType);

        //对9种特殊表头value值 按xml规则校验，并写入对象
        specialHandlingIntertidalzonebiologicalQuantitative(sheet,headMap,entityName,intertidalzonebiologicalQuantitativeReq,totalRows);

        //取
        Row excelheadRow1 = sheet.getRow(Constant.differInfoStartRow);
        int excelLastCellNum = excelheadRow1.getLastCellNum();

        // 循环row的列，按xml规则校验，并写入对象
        for (int c = 0; c < excelLastCellNum; c++) {
            Cell cell = row.getCell(c);
            String headTitle = headMap.get(c+Constant.constantTableHeadCount).toString();
            /**按规则验证cell格式**/
            validaterst = validateCellData(r+1,c+1,cell,entityName,headTitle);
            totalRst += validaterst;

            if(totalRst == 0 && cell != null) {             // 定制
                //6代表xml文件第6个 下面同理
                if(c+Constant.officialDataStartSign==6){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    intertidalzonebiologicalQuantitativeReq.setStationName(cell.getStringCellValue());
                } else if(c+Constant.officialDataStartSign==7){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    intertidalzonebiologicalQuantitativeReq.setPlanLon(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==8){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    intertidalzonebiologicalQuantitativeReq.setPlanLat(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==9){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    intertidalzonebiologicalQuantitativeReq.setRealLon(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==10){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    intertidalzonebiologicalQuantitativeReq.setRealLat(new BigDecimal(cellValue));
                }  else if(c+Constant.officialDataStartSign==11){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    intertidalzonebiologicalQuantitativeReq.setStationLocation(cellValue);
                } else if(c+Constant.officialDataStartSign==12){
                    if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                    }
                    try{
                        String strDate=getStringCellValue(cell);
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                        LocalDate localDate=null;
                        if(strDate.contains("-")){
                            dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else if(strDate.contains(".")){
                            dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else if(strDate.contains("/")){
                            dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else{
                            Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                            localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        }
                        intertidalzonebiologicalQuantitativeReq.setMonitorDate(localDate);
                    } catch (Exception e){
                        intertidalzonebiologicalQuantitativeReq.setMonitorDate(null);
                    }
                } else if(c+Constant.officialDataStartSign==13){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)){
                        intertidalzonebiologicalQuantitativeReq.setIntertidalZone(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==14){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        intertidalzonebiologicalQuantitativeReq.setSedimentType(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==15){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        intertidalzonebiologicalQuantitativeReq.setSamplingTimes(Integer.parseInt(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==16){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        intertidalzonebiologicalQuantitativeReq.setQuadratArea(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==17){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        intertidalzonebiologicalQuantitativeReq.setSampleThickness(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==18){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        intertidalzonebiologicalQuantitativeReq.setBiologicalChineseName(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==19){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        intertidalzonebiologicalQuantitativeReq.setBiologicalLatinName(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==20){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        intertidalzonebiologicalQuantitativeReq.setCount(Integer.parseInt(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==21){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        intertidalzonebiologicalQuantitativeReq.setDensity(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==22){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        intertidalzonebiologicalQuantitativeReq.setWeight(new BigDecimal(cellValue));
                    }
                }else if(c+Constant.officialDataStartSign==23){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        intertidalzonebiologicalQuantitativeReq.setBiomass(new BigDecimal(cellValue));
                    }
                }
            }

        }

        //数据库更新 注意返回false代表更新成功
//        MonitorDataReport monitorDataReport = new MonitorDataReport();
//        BeanUtils.copyProperties(intertidalzonebiologicalQuantitativeReq,monitorDataReport);
        boolean dataExist=false; //= monitorDataReportService.updateData(monitorDataReport, Constant.fisheggQuantitativeType);
        returnList.add(String.valueOf(dataExist));

        // Excel文件列表查重(遍历allMapList中底栖站位多样性数据，检索是否已包含当前数据)
        boolean sameInfo=false;
        IntertidalzonebiologicalQuantitativeReq item;
        for(Map<String,Object> map:allMapList){//for(Map<String,Object> map:dominantSpeciesController.allMapList){
            if(map.get("excelType")!=null && map.get("excelType").toString().equals("IntertidalzonebiologicalQuantitativeExcelRule")) {
                for(Object obj:(List<Object>)map.get("data")) {
                    item=(IntertidalzonebiologicalQuantitativeReq)obj;
                    if(
                            item.getMonitoringArea().equals(intertidalzonebiologicalQuantitativeReq.getMonitoringArea()) &&
                                    item.getEcologicalType().equals(intertidalzonebiologicalQuantitativeReq.getEcologicalType()) &&
                                    item.getTaskDate().equals(intertidalzonebiologicalQuantitativeReq.getTaskDate()) &&
                                    item.getMonitorCompany().equals(intertidalzonebiologicalQuantitativeReq.getMonitorCompany()) &&
                                    item.getOrganizationCompany().equals(intertidalzonebiologicalQuantitativeReq.getOrganizationCompany()) &&
                                    item.getReportDate().equals(intertidalzonebiologicalQuantitativeReq.getReportDate()) &&
                                    item.getYear().equals(intertidalzonebiologicalQuantitativeReq.getYear()) &&
                                    item.getVoyage().equals(intertidalzonebiologicalQuantitativeReq.getVoyage())) {
                        sameInfo = true;
                    }
                }
            }
        }
        returnList.add(String.valueOf(sameInfo));

        // 校验合格：写入dataList
        if(totalRst==0){
            dataList1.add(intertidalzonebiologicalQuantitativeReq);
            excelDataImportController.excelDataList.add(intertidalzonebiologicalQuantitativeReq);
        }
        return returnList;
    }

    /**
     * 对潮间带-9种特殊表头value值 按xml规则校验，并写入对象
     * @param sheet
     * @param headMap
     * @param entityName
     * @param intertidalzonebiologicalQuantitativeReq
     */
    public void specialHandlingIntertidalzonebiologicalQuantitative(Sheet sheet,Map headMap,String entityName,IntertidalzonebiologicalQuantitativeReq intertidalzonebiologicalQuantitativeReq,Integer totalRows){
        int totalRst = 0;//每行格式错误单元格的数目
        int validaterst = 0;

        //取第1+1行
        Row row = sheet.getRow(1);
        //存放监控区值
        //1代表第1+1列
        Cell cell = row.getCell(1);
        //0代表监控区
        String headTitle = headMap.get(Constant.monitoringAreaCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            intertidalzonebiologicalQuantitativeReq.setMonitoringArea(cell.getStringCellValue());
        }

        //存放生态类型值
        //6代表6+1列
        cell = row.getCell(6);
        headTitle = headMap.get(Constant.ecologicaltypeCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,6+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            intertidalzonebiologicalQuantitativeReq.setEcologicalType(cell.getStringCellValue());
        }

        //存放任务日期
        //11代表11+1列
        cell = row.getCell(11);
        headTitle = headMap.get(Constant.missionDateCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,11+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            //判断该日期在excel表格的格式是否是日期格式
            if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }

            try{
                String strDate=getStringCellValue(cell);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate=null;
                if(strDate.contains("-")){
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains(".")){
                    dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains("/")){
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else{
                    Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                intertidalzonebiologicalQuantitativeReq.setTaskDate(localDate);
            } catch (Exception e){
                intertidalzonebiologicalQuantitativeReq.setTaskDate(null);
            }
        }

        //取第2+1行
        row = sheet.getRow(2);
        //存放监测单位
        //1代表第1+1列
        cell = row.getCell(1);
        headTitle = headMap.get(Constant.monitoringUnitCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            intertidalzonebiologicalQuantitativeReq.setMonitorCompany(cell.getStringCellValue());
        }

        //存放组织单位
        //6代表6+1列
        cell = row.getCell(6);
        headTitle = headMap.get(Constant.organizationalUnitCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,6+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            intertidalzonebiologicalQuantitativeReq.setOrganizationCompany(cell.getStringCellValue());
        }

        //存放填报日期
        //11代表11+1列
        cell = row.getCell(11);
        headTitle = headMap.get(Constant.completionDateCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,11+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制

            //判断该日期在excel表格的格式是否是日期格式
            if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }

            try{
                String strDate = getStringCellValue(cell);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate=null;
                if(strDate.contains("-")){
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains(".")){
                    dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains("/")){
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else{
                    Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                intertidalzonebiologicalQuantitativeReq.setReportDate(localDate);
            } catch (Exception e){
                intertidalzonebiologicalQuantitativeReq.setReportDate(null);
            }
        }


        //取最后一行
        row = sheet.getRow(totalRows - 1);
        //存放填报人
        //1代表第1+1列
        cell = row.getCell(1);
        headTitle = headMap.get(Constant.informantCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            intertidalzonebiologicalQuantitativeReq.setByzd1(cell.getStringCellValue());
        }

        //存放校对人
        cell = row.getCell(4);
        headTitle = headMap.get(Constant.proofreaderCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,4+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            intertidalzonebiologicalQuantitativeReq.setByzd2(cell.getStringCellValue());
        }

        //存放审核人
        cell = row.getCell(7);
        headTitle = headMap.get(Constant.auditorCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,7+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            intertidalzonebiologicalQuantitativeReq.setByzd3(cell.getStringCellValue());
        }

    }

    //水文气象数据表
    private List<String> hydrometeorologicalExcelRule(Integer totalCells,Integer totalRows,Row row,Map headMap,String entityName,int r,List<Map<String, Object>> allMapList,Sheet sheet){
        List<String> returnList=new ArrayList<>();
        HydrometeorologicalReq hydrometeorologicalReq = new HydrometeorologicalReq();
        int totalRst = 0;//每行格式错误单元格的数目
        int validaterst = 0;

        //将年份和航次信息写入对象
        hydrometeorologicalReq.setYear(YEAR);
        hydrometeorologicalReq.setVoyage(VOYAGE);
        hydrometeorologicalReq.setDataType(dataType);

        //对9种特殊表头value值 按xml规则校验，并写入对象
        specialHandlingHydrometeorological(sheet,headMap,entityName,hydrometeorologicalReq,totalRows);

        //取
        Row excelheadRow1 = sheet.getRow(Constant.differInfoStartRow);
        int excelLastCellNum = excelheadRow1.getLastCellNum();

        // 循环row的列，按xml规则校验，并写入对象
        for (int c = 0; c < excelLastCellNum; c++) {
            Cell cell = row.getCell(c);
            String headTitle = headMap.get(c+Constant.constantTableHeadCount).toString();
            /**按规则验证cell格式**/
            validaterst = validateCellData(r+1,c+1,cell,entityName,headTitle);
            totalRst += validaterst;

            if(totalRst == 0 && cell != null) {             // 定制
                //6代表xml文件第6个 下面同理
                if(c+Constant.officialDataStartSign==6){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    hydrometeorologicalReq.setStationName(cell.getStringCellValue());
                } else if(c+Constant.officialDataStartSign==7){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    hydrometeorologicalReq.setPlanLon(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==8){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    hydrometeorologicalReq.setPlanLat(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==9){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    hydrometeorologicalReq.setRealLon(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==10){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    hydrometeorologicalReq.setRealLat(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==11){
                    if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                    }
                    try{
                        String strDate=getStringCellValue(cell);
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                        LocalDate localDate=null;
                        if(strDate.contains("-")){
                            dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else if(strDate.contains(".")){
                            dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else if(strDate.contains("/")){
                            dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else{
                            Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                            localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        }
                        hydrometeorologicalReq.setMonitorDate(localDate);
                    } catch (Exception e){
                        hydrometeorologicalReq.setMonitorDate(null);
                    }
                } else if(c+Constant.officialDataStartSign==12){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)){
                        hydrometeorologicalReq.setWaterDepth(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==13){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        hydrometeorologicalReq.setSamplingLevel(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==14){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        hydrometeorologicalReq.setSamplingDepth(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==15){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        hydrometeorologicalReq.setWatertemperature(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==16){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        hydrometeorologicalReq.setPellucidity(new BigDecimal(cellValue));
                    }
                }
            }

        }

        //数据库更新 注意返回false代表更新成功
//        MonitorDataReport monitorDataReport = new MonitorDataReport();
//        BeanUtils.copyProperties(hydrometeorologicalReq,monitorDataReport);
        boolean dataExist=false; //= monitorDataReportService.updateData(monitorDataReport, Constant.fisheggQuantitativeType);
        returnList.add(String.valueOf(dataExist));

        // Excel文件列表查重(遍历allMapList中底栖站位多样性数据，检索是否已包含当前数据)
        boolean sameInfo=false;
        HydrometeorologicalReq item;
        for(Map<String,Object> map:allMapList){//for(Map<String,Object> map:dominantSpeciesController.allMapList){
            if(map.get("excelType")!=null && map.get("excelType").toString().equals("HydrometeorologicalExcelRule")) {
                for(Object obj:(List<Object>)map.get("data")) {
                    item=(HydrometeorologicalReq)obj;
                    if(
                            item.getMonitoringArea().equals(hydrometeorologicalReq.getMonitoringArea()) &&
                                    item.getEcologicalType().equals(hydrometeorologicalReq.getEcologicalType()) &&
                                    item.getTaskDate().equals(hydrometeorologicalReq.getTaskDate()) &&
                                    item.getMonitorCompany().equals(hydrometeorologicalReq.getMonitorCompany()) &&
                                    item.getOrganizationCompany().equals(hydrometeorologicalReq.getOrganizationCompany()) &&
                                    item.getReportDate().equals(hydrometeorologicalReq.getReportDate()) &&
                                    item.getYear().equals(hydrometeorologicalReq.getYear()) &&
                                    item.getVoyage().equals(hydrometeorologicalReq.getVoyage())) {
                        sameInfo = true;
                    }
                }
            }
        }
        returnList.add(String.valueOf(sameInfo));

        // 校验合格：写入dataList
        if(totalRst==0){
            dataList1.add(hydrometeorologicalReq);
            excelDataImportController.excelDataList.add(hydrometeorologicalReq);
        }
        return returnList;
    }

    /**
     * 对水文气象-9种特殊表头value值 按xml规则校验，并写入对象
     * @param sheet
     * @param headMap
     * @param entityName
     * @param hydrometeorologicalReq
     */
    public void specialHandlingHydrometeorological(Sheet sheet,Map headMap,String entityName,HydrometeorologicalReq hydrometeorologicalReq,Integer totalRows){
        int totalRst = 0;//每行格式错误单元格的数目
        int validaterst = 0;

        //取第1+1行
        Row row = sheet.getRow(1);
        //存放监控区值
        //1代表第1+1列
        Cell cell = row.getCell(1);
        //0代表监控区
        String headTitle = headMap.get(Constant.monitoringAreaCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            hydrometeorologicalReq.setMonitoringArea(cell.getStringCellValue());
        }

        //存放生态类型值
        //8代表8+1列
        cell = row.getCell(8);
        headTitle = headMap.get(Constant.ecologicaltypeCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,8+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            hydrometeorologicalReq.setEcologicalType(cell.getStringCellValue());
        }

        //存放任务日期
        //11代表11+1列
        cell = row.getCell(15);
        headTitle = headMap.get(Constant.missionDateCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,15+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            //判断该日期在excel表格的格式是否是日期格式
            if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }

            try{
                String strDate=getStringCellValue(cell);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate=null;
                if(strDate.contains("-")){
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains(".")){
                    dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains("/")){
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else{
                    Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                hydrometeorologicalReq.setTaskDate(localDate);
            } catch (Exception e){
                hydrometeorologicalReq.setTaskDate(null);
            }
        }

        //取第2+1行
        row = sheet.getRow(2);
        //存放监测单位
        //1代表第1+1列
        cell = row.getCell(1);
        headTitle = headMap.get(Constant.monitoringUnitCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            hydrometeorologicalReq.setMonitorCompany(cell.getStringCellValue());
        }

        //存放组织单位
        //6代表6+1列
        cell = row.getCell(8);
        headTitle = headMap.get(Constant.organizationalUnitCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,8+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            hydrometeorologicalReq.setOrganizationCompany(cell.getStringCellValue());
        }

        //存放填报日期
        //11代表11+1列
        cell = row.getCell(15);
        headTitle = headMap.get(Constant.completionDateCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,15+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制

            //判断该日期在excel表格的格式是否是日期格式
            if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }

            try{
                String strDate = getStringCellValue(cell);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate=null;
                if(strDate.contains("-")){
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains(".")){
                    dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains("/")){
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else{
                    Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                hydrometeorologicalReq.setReportDate(localDate);
            } catch (Exception e){
                hydrometeorologicalReq.setReportDate(null);
            }
        }


        //取最后一行
        row = sheet.getRow(totalRows - 1);
        //存放填报人
        //1代表第1+1列
        cell = row.getCell(1);
        headTitle = headMap.get(Constant.informantCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            hydrometeorologicalReq.setByzd1(cell.getStringCellValue());
        }

        //存放校对人
        cell = row.getCell(4);
        headTitle = headMap.get(Constant.proofreaderCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,4+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            hydrometeorologicalReq.setByzd2(cell.getStringCellValue());
        }

        //存放审核人
        cell = row.getCell(7);
        headTitle = headMap.get(Constant.auditorCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,7+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            hydrometeorologicalReq.setByzd3(cell.getStringCellValue());
        }

    }

    //鱼卵定性数据表
    private List<String> fisheggQualitativeExcelRule(Integer totalCells,Integer totalRows,Row row,Map headMap,String entityName,int r,List<Map<String, Object>> allMapList,Sheet sheet){
        List<String> returnList=new ArrayList<>();
        FisheggQualitativeReq fisheggQualitativeReq = new FisheggQualitativeReq();
        int totalRst = 0;//每行格式错误单元格的数目
        int validaterst = 0;

        //将年份和航次信息写入对象
        fisheggQualitativeReq.setYear(YEAR);
        fisheggQualitativeReq.setVoyage(VOYAGE);
        fisheggQualitativeReq.setDataType(dataType);

        //对9种特殊表头value值 按xml规则校验，并写入对象
        specialHandlingFisheggQualitative(sheet,headMap,entityName,fisheggQualitativeReq,totalRows);

        //取
        Row excelheadRow1 = sheet.getRow(Constant.differInfoStartRow);
        int excelLastCellNum = excelheadRow1.getLastCellNum();

        // 循环row的列，按xml规则校验，并写入对象
        for (int c = 0; c < excelLastCellNum; c++) {
            Cell cell = row.getCell(c);
            String headTitle = headMap.get(c+Constant.constantTableHeadCount).toString();
            /**按规则验证cell格式**/
            validaterst = validateCellData(r+1,c+1,cell,entityName,headTitle);
            totalRst += validaterst;

            if(totalRst == 0 && cell != null) {             // 定制
                //6代表xml文件第6个 下面同理
                if(c+Constant.officialDataStartSign==6){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    fisheggQualitativeReq.setStationName(cell.getStringCellValue());
                } else if(c+Constant.officialDataStartSign==7){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    fisheggQualitativeReq.setPlanLon(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==8){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    fisheggQualitativeReq.setPlanLat(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==9){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    fisheggQualitativeReq.setRealLon(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==10){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    fisheggQualitativeReq.setRealLat(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==11){
                    if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                    }
                    try{
                        String strDate=getStringCellValue(cell);
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                        LocalDate localDate=null;
                        if(strDate.contains("-")){
                            dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else if(strDate.contains(".")){
                            dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else if(strDate.contains("/")){
                            dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else{
                            Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                            localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        }
                        fisheggQualitativeReq.setMonitorDate(localDate);
                    } catch (Exception e){
                        fisheggQualitativeReq.setMonitorDate(null);
                    }
                } else if(c+Constant.officialDataStartSign==12){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)){
                        fisheggQualitativeReq.setWaterDepth(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==13){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        fisheggQualitativeReq.setNetType(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==14){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        fisheggQualitativeReq.setTrawlDistance(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==15){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        fisheggQualitativeReq.setTrawlTime(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==16){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        fisheggQualitativeReq.setBiologicalChineseName(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==17){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        fisheggQualitativeReq.setBiologicalLatinName(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==18){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        fisheggQualitativeReq.setDevelopStage(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==19){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        fisheggQualitativeReq.setEggRadius(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==20){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        fisheggQualitativeReq.setCount(Integer.parseInt(cellValue));
                    }
                }
            }

        }

        //数据库更新 注意返回false代表更新成功
//        MonitorDataReport monitorDataReport = new MonitorDataReport();
//        BeanUtils.copyProperties(fisheggQualitativeReq,monitorDataReport);
        boolean dataExist=false; //= monitorDataReportService.updateData(monitorDataReport, Constant.fisheggQuantitativeType);
        returnList.add(String.valueOf(dataExist));

        // Excel文件列表查重(遍历allMapList中底栖站位多样性数据，检索是否已包含当前数据)
        boolean sameInfo=false;
        FisheggQualitativeReq item;
        for(Map<String,Object> map:allMapList){//for(Map<String,Object> map:dominantSpeciesController.allMapList){
            if(map.get("excelType")!=null && map.get("excelType").toString().equals("FisheggQualitativeExcelRule")) {
                for(Object obj:(List<Object>)map.get("data")) {
                    item=(FisheggQualitativeReq)obj;
                    if(
                            item.getMonitoringArea().equals(fisheggQualitativeReq.getMonitoringArea()) &&
                                    item.getEcologicalType().equals(fisheggQualitativeReq.getEcologicalType()) &&
                                    item.getTaskDate().equals(fisheggQualitativeReq.getTaskDate()) &&
                                    item.getMonitorCompany().equals(fisheggQualitativeReq.getMonitorCompany()) &&
                                    item.getOrganizationCompany().equals(fisheggQualitativeReq.getOrganizationCompany()) &&
                                    item.getReportDate().equals(fisheggQualitativeReq.getReportDate()) &&
                                    item.getYear().equals(fisheggQualitativeReq.getYear()) &&
                                    item.getVoyage().equals(fisheggQualitativeReq.getVoyage())) {
                        sameInfo = true;
                    }
                }
            }
        }
        returnList.add(String.valueOf(sameInfo));

        // 校验合格：写入dataList
        if(totalRst==0){
            dataList1.add(fisheggQualitativeReq);
            excelDataImportController.excelDataList.add(fisheggQualitativeReq);
        }
        return returnList;
    }

    /**
     * 对鱼卵定性-9种特殊表头value值 按xml规则校验，并写入对象
     * @param sheet
     * @param headMap
     * @param entityName
     * @param fisheggQualitativeReq
     */
    public void specialHandlingFisheggQualitative(Sheet sheet,Map headMap,String entityName,FisheggQualitativeReq fisheggQualitativeReq,Integer totalRows){
        int totalRst = 0;//每行格式错误单元格的数目
        int validaterst = 0;

        //取第1+1行
        Row row = sheet.getRow(1);
        //存放监控区值
        //1代表第1+1列
        Cell cell = row.getCell(1);
        //0代表监控区
        String headTitle = headMap.get(Constant.monitoringAreaCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            fisheggQualitativeReq.setMonitoringArea(cell.getStringCellValue());
        }

        //存放生态类型值
        //6代表6+1列
        cell = row.getCell(6);
        headTitle = headMap.get(Constant.ecologicaltypeCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,6+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            fisheggQualitativeReq.setEcologicalType(cell.getStringCellValue());
        }

        //存放任务日期
        //11代表11+1列
        cell = row.getCell(11);
        headTitle = headMap.get(Constant.missionDateCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,11+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            //判断该日期在excel表格的格式是否是日期格式
            if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }

            try{
                String strDate=getStringCellValue(cell);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate=null;
                if(strDate.contains("-")){
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains(".")){
                    dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains("/")){
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else{
                    Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                fisheggQualitativeReq.setTaskDate(localDate);
            } catch (Exception e){
                fisheggQualitativeReq.setTaskDate(null);
            }
        }

        //取第2+1行
        row = sheet.getRow(2);
        //存放监测单位
        //1代表第1+1列
        cell = row.getCell(1);
        headTitle = headMap.get(Constant.monitoringUnitCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            fisheggQualitativeReq.setMonitorCompany(cell.getStringCellValue());
        }

        //存放组织单位
        //6代表6+1列
        cell = row.getCell(6);
        headTitle = headMap.get(Constant.organizationalUnitCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,6+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            fisheggQualitativeReq.setOrganizationCompany(cell.getStringCellValue());
        }

        //存放填报日期
        //11代表11+1列
        cell = row.getCell(11);
        headTitle = headMap.get(Constant.completionDateCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,11+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制

            //判断该日期在excel表格的格式是否是日期格式
            if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }

            try{
                String strDate = getStringCellValue(cell);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate=null;
                if(strDate.contains("-")){
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains(".")){
                    dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains("/")){
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else{
                    Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                fisheggQualitativeReq.setReportDate(localDate);
            } catch (Exception e){
                fisheggQualitativeReq.setReportDate(null);
            }
        }


        //取最后一行
        row = sheet.getRow(totalRows - 1);
        //存放填报人
        //1代表第1+1列
        cell = row.getCell(1);
        headTitle = headMap.get(Constant.informantCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            fisheggQualitativeReq.setByzd1(cell.getStringCellValue());
        }

        //存放校对人
        cell = row.getCell(4);
        headTitle = headMap.get(Constant.proofreaderCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,4+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            fisheggQualitativeReq.setByzd2(cell.getStringCellValue());
        }

        //存放审核人
        cell = row.getCell(7);
        headTitle = headMap.get(Constant.auditorCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,7+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            fisheggQualitativeReq.setByzd3(cell.getStringCellValue());
        }

    }

    //生物质量数据表
    private List<String> biologicalQualityExcelRule(Integer totalCells,Integer totalRows,Row row,Map headMap,String entityName,int r,List<Map<String, Object>> allMapList,Sheet sheet){
        List<String> returnList=new ArrayList<>();
        BiologicalQualityReq biologicalQualityReq = new BiologicalQualityReq();
        int totalRst = 0;//每行格式错误单元格的数目
        int validaterst = 0;

        //将年份和航次信息写入对象
        biologicalQualityReq.setYear(YEAR);
        biologicalQualityReq.setVoyage(VOYAGE);
        biologicalQualityReq.setDataType(dataType);

        //对9种特殊表头value值 按xml规则校验，并写入对象
        specialHandlingBiologicalQuality(sheet,headMap,entityName,biologicalQualityReq,totalRows);

        //取
        Row excelheadRow1 = sheet.getRow(Constant.differInfoStartRow);
        int excelLastCellNum = excelheadRow1.getLastCellNum();

        // 循环row的列，按xml规则校验，并写入对象
        for (int c = 0; c < excelLastCellNum; c++) {
            Cell cell = row.getCell(c);
            String headTitle = headMap.get(c+Constant.constantTableHeadCount).toString();
            /**按规则验证cell格式**/
            validaterst = validateCellData(r+1,c+1,cell,entityName,headTitle);
            totalRst += validaterst;

            if(totalRst == 0 && cell != null) {             // 定制
                //代表xml文件第c+6个 下面同理
                if(c+Constant.officialDataStartSpecialSign==5){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    biologicalQualityReq.setStationName(cell.getStringCellValue());
                } else if(c+Constant.officialDataStartSpecialSign==6){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    biologicalQualityReq.setPlanLon(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSpecialSign==7){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    biologicalQualityReq.setPlanLat(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSpecialSign==8){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    biologicalQualityReq.setRealLon(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSpecialSign==9){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    biologicalQualityReq.setRealLat(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSpecialSign==10){
                    if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                    }
                    try{
                        String strDate=getStringCellValue(cell);
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                        LocalDate localDate=null;
                        if(strDate.contains("-")){
                            dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else if(strDate.contains(".")){
                            dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else if(strDate.contains("/")){
                            dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else{
                            Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                            localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        }
                        biologicalQualityReq.setMonitorDate(localDate);
                    } catch (Exception e){
                        biologicalQualityReq.setMonitorDate(null);
                    }
                } else if(c+Constant.officialDataStartSpecialSign==11){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)){
                        biologicalQualityReq.setWaterDepth(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSpecialSign==12){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        biologicalQualityReq.setCategory(cellValue);
                    }
                } else if(c+Constant.officialDataStartSpecialSign==13){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        biologicalQualityReq.setBiologicalChineseName(cellValue);
                    }
                } else if(c+Constant.officialDataStartSpecialSign==14){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        biologicalQualityReq.setBiologicalChineseName(cellValue);
                    }
                } else if(c+Constant.officialDataStartSpecialSign==15){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        biologicalQualityReq.setSampleBodyLength(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSpecialSign==16){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        biologicalQualityReq.setThg(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSpecialSign==17){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        biologicalQualityReq.setCd(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSpecialSign==18){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        biologicalQualityReq.setPb(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSpecialSign==19){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        biologicalQualityReq.setCu(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSpecialSign==20){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        biologicalQualityReq.setAss(new BigDecimal(cellValue));
                    }
                }
                else if(c+Constant.officialDataStartSpecialSign==21){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        biologicalQualityReq.setSixsixsix(new BigDecimal(cellValue));
                    }
                }
                else if(c+Constant.officialDataStartSpecialSign==22){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        biologicalQualityReq.setCr(new BigDecimal(cellValue));
                    }
                }
                else if(c+Constant.officialDataStartSpecialSign==23){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        biologicalQualityReq.setDdt(new BigDecimal(cellValue));
                    }
                }
                else if(c+Constant.officialDataStartSpecialSign==24){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        biologicalQualityReq.setPcbs(new BigDecimal(cellValue));
                    }
                }
                else if(c+Constant.officialDataStartSpecialSign==25){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        biologicalQualityReq.setSyt(new BigDecimal(cellValue));
                    }
                }
                else if(c+Constant.officialDataStartSpecialSign==26){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        biologicalQualityReq.setFdcjq(new BigDecimal(cellValue));
                    }
                }
                else if(c+Constant.officialDataStartSpecialSign==27){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        biologicalQualityReq.setLms(new BigDecimal(cellValue));
                    }
                }
                else if(c+Constant.officialDataStartSpecialSign==28){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        biologicalQualityReq.setKss(new BigDecimal(cellValue));
                    }
                }
                else if(c+Constant.officialDataStartSpecialSign==29){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        biologicalQualityReq.setDsp(new BigDecimal(cellValue));
                    }
                }
                else if(c+Constant.officialDataStartSpecialSign==30){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        biologicalQualityReq.setPsp(new BigDecimal(cellValue));
                    }
                }
                else if(c+Constant.officialDataStartSpecialSign==31){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        biologicalQualityReq.setZn(new BigDecimal(cellValue));
                    }
                }
            }

        }

        //数据库更新 注意返回false代表更新成功
//        MonitorDataReport monitorDataReport = new MonitorDataReport();
//        BeanUtils.copyProperties(biologicalQualityReq,monitorDataReport);
        boolean dataExist=false; //= monitorDataReportService.updateData(monitorDataReport, Constant.fisheggQuantitativeType);
        returnList.add(String.valueOf(dataExist));

        // Excel文件列表查重(遍历allMapList中底栖站位多样性数据，检索是否已包含当前数据)
        boolean sameInfo=false;
        BiologicalQualityReq item;
        for(Map<String,Object> map:allMapList){//for(Map<String,Object> map:dominantSpeciesController.allMapList){
            if(map.get("excelType")!=null && map.get("excelType").toString().equals("BiologicalQualityExcelRule")) {
                for(Object obj:(List<Object>)map.get("data")) {
                    item=(BiologicalQualityReq)obj;
                    if(
                            item.getMonitoringArea().equals(biologicalQualityReq.getMonitoringArea()) &&
                                    item.getEcologicalType().equals(biologicalQualityReq.getEcologicalType()) &&
                                    item.getTaskDate().equals(biologicalQualityReq.getTaskDate()) &&
                                    item.getMonitorCompany().equals(biologicalQualityReq.getMonitorCompany()) &&
                                    item.getOrganizationCompany().equals(biologicalQualityReq.getOrganizationCompany()) &&
                                    item.getReportDate().equals(biologicalQualityReq.getReportDate()) &&
                                    item.getYear().equals(biologicalQualityReq.getYear()) &&
                                    item.getVoyage().equals(biologicalQualityReq.getVoyage())) {
                        sameInfo = true;
                    }
                }
            }
        }
        returnList.add(String.valueOf(sameInfo));

        // 校验合格：写入dataList
        if(totalRst==0){
            dataList1.add(biologicalQualityReq);
            excelDataImportController.excelDataList.add(biologicalQualityReq);
        }
        return returnList;
    }

    /**
     * 对生物质量-9种特殊表头value值 按xml规则校验，并写入对象
     * @param sheet
     * @param headMap
     * @param entityName
     * @param biologicalQualityReq
     */
    public void specialHandlingBiologicalQuality(Sheet sheet,Map headMap,String entityName,BiologicalQualityReq biologicalQualityReq,Integer totalRows){
        int totalRst = 0;//每行格式错误单元格的数目
        int validaterst = 0;

        //取第1+1行
        Row row = sheet.getRow(1);
        //存放监控区值
        //1代表第1+1列
        Cell cell = row.getCell(1);
        //0代表监控区
        String headTitle = headMap.get(Constant.monitoringAreaCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            biologicalQualityReq.setMonitoringArea(cell.getStringCellValue());
        }

        //存放任务日期
        //11代表11+1列
        cell = row.getCell(11);
        headTitle = headMap.get(Constant.missionDateCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,11+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            //判断该日期在excel表格的格式是否是日期格式
            if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }

            try{
                String strDate=getStringCellValue(cell);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate=null;
                if(strDate.contains("-")){
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains(".")){
                    dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains("/")){
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else{
                    Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                biologicalQualityReq.setTaskDate(localDate);
            } catch (Exception e){
                biologicalQualityReq.setTaskDate(null);
            }
        }

        //取第2+1行
        row = sheet.getRow(2);
        //存放监测单位
        //1代表第1+1列
        cell = row.getCell(1);
        headTitle = headMap.get(Constant.monitoringUnitCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            biologicalQualityReq.setMonitorCompany(cell.getStringCellValue());
        }

        //存放组织单位
        //6代表6+1列
        cell = row.getCell(6);
        headTitle = headMap.get(Constant.organizationalUnitCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,6+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            biologicalQualityReq.setOrganizationCompany(cell.getStringCellValue());
        }

        //存放填报日期
        //11代表11+1列
        cell = row.getCell(11);
        headTitle = headMap.get(Constant.completionDateCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,11+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制

            //判断该日期在excel表格的格式是否是日期格式
            if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }

            try{
                String strDate = getStringCellValue(cell);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate=null;
                if(strDate.contains("-")){
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains(".")){
                    dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains("/")){
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else{
                    Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                biologicalQualityReq.setReportDate(localDate);
            } catch (Exception e){
                biologicalQualityReq.setReportDate(null);
            }
        }


        //取最后一行
        row = sheet.getRow(totalRows - 1);
        //存放填报人
        //1代表第1+1列
        cell = row.getCell(1);
        headTitle = headMap.get(Constant.informantCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            biologicalQualityReq.setByzd1(cell.getStringCellValue());
        }

        //存放校对人
        cell = row.getCell(4);
        headTitle = headMap.get(Constant.proofreaderCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,4+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            biologicalQualityReq.setByzd2(cell.getStringCellValue());
        }

        //存放审核人
        cell = row.getCell(7);
        headTitle = headMap.get(Constant.auditorCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,7+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            biologicalQualityReq.setByzd3(cell.getStringCellValue());
        }

    }

    //鱼卵定量数据表
    private List<String> fisheggQuantitativeExcelRule(Integer totalCells,Integer totalRows,Row row,Map headMap,String entityName,int r,List<Map<String, Object>> allMapList,Sheet sheet){
        List<String> returnList=new ArrayList<>();
        FisheggQuantitativeReq fisheggQuantitativeReq = new FisheggQuantitativeReq();
        int totalRst = 0;//每行格式错误单元格的数目
        int validaterst = 0;

        //将年份和航次信息写入对象
        fisheggQuantitativeReq.setYear(YEAR);
        fisheggQuantitativeReq.setVoyage(VOYAGE);
        fisheggQuantitativeReq.setDataType(dataType);

        //对9种特殊表头value值 按xml规则校验，并写入对象
        specialHandlingFisheggQuantitative(sheet,headMap,entityName,fisheggQuantitativeReq,totalRows);

        //取
        Row excelheadRow1 = sheet.getRow(Constant.differInfoStartRow);
        int excelLastCellNum = excelheadRow1.getLastCellNum();

        // 循环row的列，按xml规则校验，并写入对象
        for (int c = 0; c < excelLastCellNum; c++) {
            Cell cell = row.getCell(c);
            String headTitle = headMap.get(c+Constant.constantTableHeadCount).toString();
            /**按规则验证cell格式**/
            validaterst = validateCellData(r+1,c+1,cell,entityName,headTitle);
            totalRst += validaterst;

            if(totalRst == 0 && cell != null) {             // 定制
                //6代表xml文件第6个 下面同理
                if(c+Constant.officialDataStartSign==6){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    fisheggQuantitativeReq.setStationName(cell.getStringCellValue());
                } else if(c+Constant.officialDataStartSign==7){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    fisheggQuantitativeReq.setPlanLon(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==8){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    fisheggQuantitativeReq.setPlanLat(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==9){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    fisheggQuantitativeReq.setRealLon(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==10){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    fisheggQuantitativeReq.setRealLat(new BigDecimal(cellValue));
                } else if(c+Constant.officialDataStartSign==11){
                    if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                    }
                    try{
                        String strDate=getStringCellValue(cell);
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                        LocalDate localDate=null;
                        if(strDate.contains("-")){
                            dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else if(strDate.contains(".")){
                            dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else if(strDate.contains("/")){
                            dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                            localDate=LocalDate.parse(strDate,dtf);
                        }else{
                            Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                            localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        }
                        fisheggQuantitativeReq.setMonitorDate(localDate);
                    } catch (Exception e){
                        fisheggQuantitativeReq.setMonitorDate(null);
                    }
                } else if(c+Constant.officialDataStartSign==12){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)){
                        fisheggQuantitativeReq.setWaterDepth(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==13){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        fisheggQuantitativeReq.setRopeLength(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==14){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        fisheggQuantitativeReq.setWaterFiltration(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==15){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        fisheggQuantitativeReq.setBiologicalChineseName(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==16){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        fisheggQuantitativeReq.setBiologicalLatinName(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==17){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        fisheggQuantitativeReq.setDevelopStage(cellValue);
                    }
                } else if(c+Constant.officialDataStartSign==18){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        fisheggQuantitativeReq.setEggRadius(new BigDecimal(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==19){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        fisheggQuantitativeReq.setCount(Integer.parseInt(cellValue));
                    }
                } else if(c+Constant.officialDataStartSign==20){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                    String cellValue = cell.getStringCellValue();
                    if(StringUtils.isNotBlank(cellValue)) {
                        fisheggQuantitativeReq.setDensity(new BigDecimal(cellValue));
                    }
                }
            }

        }

        //数据库更新 注意返回false代表更新成功
//        MonitorDataReport monitorDataReport = new MonitorDataReport();
//        BeanUtils.copyProperties(fisheggQuantitativeReq,monitorDataReport);
        boolean dataExist=false; //= monitorDataReportService.updateData(monitorDataReport, Constant.fisheggQuantitativeType);
        returnList.add(String.valueOf(dataExist));

        // Excel文件列表查重(遍历allMapList中底栖站位多样性数据，检索是否已包含当前数据)
        boolean sameInfo=false;
        FisheggQuantitativeReq item;
        for(Map<String,Object> map:allMapList){//for(Map<String,Object> map:dominantSpeciesController.allMapList){
            if(map.get("excelType")!=null && map.get("excelType").toString().equals("FisheggQuantitativeExcelRule")) {
                for(Object obj:(List<Object>)map.get("data")) {
                    item=(FisheggQuantitativeReq)obj;
                    if(
                            item.getMonitoringArea().equals(fisheggQuantitativeReq.getMonitoringArea()) &&
                            item.getEcologicalType().equals(fisheggQuantitativeReq.getEcologicalType()) &&
                            item.getTaskDate().equals(fisheggQuantitativeReq.getTaskDate()) &&
                            item.getMonitorCompany().equals(fisheggQuantitativeReq.getMonitorCompany()) &&
                            item.getOrganizationCompany().equals(fisheggQuantitativeReq.getOrganizationCompany()) &&
                            item.getReportDate().equals(fisheggQuantitativeReq.getReportDate()) &&
                            item.getYear().equals(fisheggQuantitativeReq.getYear()) &&
                            item.getVoyage().equals(fisheggQuantitativeReq.getVoyage())) {
                        sameInfo = true;
                    }
                }
            }
        }
        returnList.add(String.valueOf(sameInfo));

        // 校验合格：写入dataList
        if(totalRst==0){
            dataList1.add(fisheggQuantitativeReq);
            excelDataImportController.excelDataList.add(fisheggQuantitativeReq);
        }
        return returnList;
    }

    /**
     * 对鱼卵定量-9种特殊表头value值 按xml规则校验，并写入对象
     * @param sheet
     * @param headMap
     * @param entityName
     * @param fisheggQuantitativeReq
     */
    public void specialHandlingFisheggQuantitative(Sheet sheet,Map headMap,String entityName,FisheggQuantitativeReq fisheggQuantitativeReq,Integer totalRows){
        int totalRst = 0;//每行格式错误单元格的数目
        int validaterst = 0;

        //取第1+1行
        Row row = sheet.getRow(1);
        //存放监控区值
        //1代表第1+1列
        Cell cell = row.getCell(1);
        //0代表监控区
        String headTitle = headMap.get(Constant.monitoringAreaCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            fisheggQuantitativeReq.setMonitoringArea(cell.getStringCellValue());
        }

        //存放生态类型值
        //6代表6+1列
        cell = row.getCell(6);
        headTitle = headMap.get(Constant.ecologicaltypeCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,6+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            fisheggQuantitativeReq.setEcologicalType(cell.getStringCellValue());
        }

        //存放任务日期
        //11代表11+1列
        cell = row.getCell(11);
        headTitle = headMap.get(Constant.missionDateCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(1+1,11+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            //判断该日期在excel表格的格式是否是日期格式
            if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }

            try{
                String strDate=getStringCellValue(cell);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate=null;
                if(strDate.contains("-")){
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains(".")){
                    dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains("/")){
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else{
                    Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                fisheggQuantitativeReq.setTaskDate(localDate);
            } catch (Exception e){
                fisheggQuantitativeReq.setTaskDate(null);
            }
        }

        //取第2+1行
        row = sheet.getRow(2);
        //存放监测单位
        //1代表第1+1列
        cell = row.getCell(1);
        headTitle = headMap.get(Constant.monitoringUnitCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            fisheggQuantitativeReq.setMonitorCompany(cell.getStringCellValue());
        }

        //存放组织单位
        //6代表6+1列
        cell = row.getCell(6);
        headTitle = headMap.get(Constant.organizationalUnitCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,6+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            fisheggQuantitativeReq.setOrganizationCompany(cell.getStringCellValue());
        }

        //存放填报日期
        //11代表11+1列
        cell = row.getCell(11);
        headTitle = headMap.get(Constant.completionDateCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(2+1,11+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制

            //判断该日期在excel表格的格式是否是日期格式
            if(!(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)){
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }

            try{
                String strDate = getStringCellValue(cell);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate=null;
                if(strDate.contains("-")){
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains(".")){
                    dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else if(strDate.contains("/")){
                    dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    localDate=LocalDate.parse(strDate,dtf);
                }else{
                    Date date = HSSFDateUtil.getJavaDate(Double.valueOf(strDate));
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                fisheggQuantitativeReq.setReportDate(localDate);
            } catch (Exception e){
                fisheggQuantitativeReq.setReportDate(null);
            }
        }


        //取最后一行
        row = sheet.getRow(totalRows - 1);
        //存放填报人
        //1代表第1+1列
        cell = row.getCell(1);
        headTitle = headMap.get(Constant.informantCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,1+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            fisheggQuantitativeReq.setByzd1(cell.getStringCellValue());
        }

        //存放校对人
        cell = row.getCell(4);
        headTitle = headMap.get(Constant.proofreaderCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,4+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            fisheggQuantitativeReq.setByzd2(cell.getStringCellValue());
        }

        //存放审核人
        cell = row.getCell(7);
        headTitle = headMap.get(Constant.auditorCode).toString();
        /**按规则验证cell格式**/
        validaterst = validateCellData(totalRows-1+1,7+1,cell,entityName,headTitle);
        totalRst += validaterst;
        if(totalRst == 0 && cell != null) {             // 定制
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
            fisheggQuantitativeReq.setByzd3(cell.getStringCellValue());
        }

    }

    /**
     * @title validateCellData
     * @description 验证单元格数据
     * @author LiLu
     * @date 2018年9月5日 上午11:00:57
     * @param curRow 行
     * @param curCol 列
     * @param colCell 单元格
     * @param entityName 表名
     * @param headName 字段名
     */
    public static int validateCellData(int curRow,int curCol,Cell colCell,String entityName,String headName){
        //MaterialGradeDao materialGradeDao = (MaterialGradeDao) SpringTool.getBean("materialGradeDao");
        List rulList = (List) parseXmlUtil.getColumnRulesMap().get(entityName+"_"+headName);
        int result = 0;
        int cellType;
        double upper;
        double lower;
        String nullable = "true";
        String allowLine="true";
        String curColStr = excelColIndexToStr(curCol);
        Integer cellSize;
        if(rulList != null && rulList.size()>0){
            for(int i=0 ; i<rulList.size() ; i++){
                Map rulM = (Map) rulList.get(i);
                String rulName = (String) rulM.get("name");
                String rulMsg = (String) rulM.get("message");
                String cellValue = getStringCellValue(colCell);//.trim();
                Map<String,Object> errorMap = new HashMap<>();
                //规则1：是否非空
                if(rulName.equals(ParseConstans.RULE_NAME_NULLABLE)){
                    if(headName.equals("总种数")&&curRow>2) continue;
                    nullable = (String)rulM.get("nullable");
                    if(nullable.equals("false")) {
                        if(cellValue == null||cellValue.equals("")){
                            if(!errorString.toString().contains(err1)) errorString.append(err1);
                            errorMap.put("curRow", curRow);
                            errorMap.put("curCol", curCol);
                            errorMap.put("rulMsg", rulMsg);
                            errorList.add(errorMap);
                            result = -1;
                            break;
                        }
                    }else {
                        if(cellValue == null||cellValue.equals("")) {
                            break;
                        }
                        continue;
                    }
                }
                //规则2：校验是否为横线
                if(rulName.equals(ParseConstans.RULE_NAME_LINE)){
                    allowLine = (String)rulM.get("allowLine");
                    if(allowLine.equals("false")) {
                        if(cellValue != null && cellValue.equals("——")){
                            if(!errorString.toString().contains(err1)) errorString.append(err1);
                            errorMap.put("curRow", curRow);
                            errorMap.put("curCol", curCol);
                            errorMap.put("rulMsg", rulMsg);
                            errorList.add(errorMap);
                            result = -1;
                            break;
                        }
                    }
                }
                //规则3：是否超出长度检验
                if(rulName.equals(ParseConstans.RULE_NAME_LENGTH)){
                    if(!StringUtils.isNotBlank(cellValue)) break;
                    cellSize = Integer.valueOf(String.valueOf(rulM.get("length")));
                    if(cellValue.length()>cellSize) {
                        if(!errorString.toString().contains(err5)) errorString.append(err5);
                        errorMap.put("curRow", curRow);
                        errorMap.put("curCol", curCol);
                        errorMap.put("rulMsg", rulMsg);
                        errorList.add(errorMap);
                        result = -1;
                        break;
                    }
                }
                //规则4：是否是数字
                else if(rulName.equals(ParseConstans.RULE_NAME_NUMERIC)){
                    cellType = colCell.getCellType();
                    if((cellType != Cell.CELL_TYPE_NUMERIC && cellType!= Cell.CELL_TYPE_FORMULA && StringUtils.isNotEmpty(cellValue))) {

                        //原本只要excel在填写时格式正确 就不需要try里面的内容 加上try是为了防止excel内出现：以文本形式存储的数字的情况
                        try {
                            Double doubleValue = Double.valueOf(cellValue);
                            break;
                        }catch (NumberFormatException e){
                            if(!errorString.toString().contains(err3)) errorString.append(err3);
                            errorMap.put("curRow", curRow);
                            errorMap.put("curCol", curCol);
                            errorMap.put("rulMsg", rulMsg);
                            errorList.add(errorMap);
                            result = -1;
                            break;
                        }

                    }
                }
                //规则5：是否是日期格式
                else if(rulName.equals(ParseConstans.RULE_NAME_DATE)){
                    if(cellValue.indexOf("E")>0) cellValue=customExcelNumericFormat(cellValue);
                    if(!DateUtils.isValidDate(cellValue)) {
                        if(!errorString.toString().contains(err4)) errorString.append(err4);
                        //errorString.append(curColStr+curRow+"："+rulMsg+"<br>");
                        errorMap.put("curRow", curRow);
                        errorMap.put("curCol", curCol);
                        errorMap.put("rulMsg", rulMsg);
                        errorList.add(errorMap);
                        result = -1;
                    }
                }
                //规则6：是否是正整数
                else if(rulName.equals(ParseConstans.RULE_NAME_DIGIT)){
                    for (int j = cellValue.length();--j>=0;){
                        if (!Character.isDigit(cellValue.charAt(j))){
                            if(cellValue.charAt(j)=='.'){
                                for(int n = j+1;n<cellValue.length();n++){
                                    if(!(cellValue.charAt(n)=='0')){
                                        if(!errorString.toString().contains(err8)) errorString.append(err8);
                                        errorMap.put("curRow", curRow);
                                        errorMap.put("curCol", curCol);
                                        errorMap.put("rulMsg", rulMsg);
                                        errorList.add(errorMap);
                                        result = -1;
                                        break;
                                    }
                                }
                                break;
                            }
                            else{
                                if(!errorString.toString().contains(err8)) errorString.append(err8);
                                errorMap.put("curRow", curRow);
                                errorMap.put("curCol", curCol);
                                errorMap.put("rulMsg", rulMsg);
                                errorList.add(errorMap);
                                result = -1;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    // 对纯数字和小数点的单元格进行处理（防止java读入double自动补.0和自动读成科学计数法）
    private static String customExcelNumericFormat(Object input) {
        Double db = Double.parseDouble(input.toString());
        double d=db==null?0:db;
        NumberFormat nf = NumberFormat.getNumberInstance();
        String temp = d + "";
        // 科学计数法中的n（10的n次方）
        int n = 0;
        // 判断有多少位有效小数
        int a = 0;
        // 如果该数字使用了科学计数法
        if (temp.indexOf("E") >= 0) {
            // 判断出要移多少位
            String auxiliaryStr = temp.split("E")[1];
            String realStr = temp.split("E")[0];
            n = Integer.parseInt(auxiliaryStr);
            // 有多少位有效小数（科学计数法）
            a = (realStr).length() - (realStr).indexOf(".") - 1 - n;
        } else {
            // 有多少位有效小数（非科学计数法）
            a = (d + "").length() - (d + "").indexOf(".") - 1;
        }
        if (a == 1 && (d + "").endsWith(".0")) {
            // 如果excel里本无小数是java读取时自动加的.0那就直接将小数位数设0
            nf.setMinimumFractionDigits(0);
        } else {
            // 有小数的按小数位数设置
            nf.setMinimumFractionDigits(a);
        }
        String s = nf.format(d);
        if (s.indexOf(",") >= 0) {
            s = s.replace(",", "");
        }
        return s;
    }

    /**
     * Excel column index begin 1
     * @param columnIndex
     * @return
     */
    public static String excelColIndexToStr(int columnIndex) {
        if (columnIndex <= 0) {
            return null;
        }
        String columnStr = "";
        columnIndex--;
        do {
            if (columnStr.length() > 0) {
                columnIndex--;
            }
            columnStr = ((char) (columnIndex % 26 + (int) 'A')) + columnStr;
            columnIndex = (int) ((columnIndex - columnIndex % 26) / 26);
        } while (columnIndex > 0);
        return columnStr;
    }

    /**
     * @title isBlankRow
     * @description 判断空行
     * @author lovaxixi
     * @URL https://bbs.csdn.net/topics/360265527
     * @date 2018年11月8日 上午10:53:29
     * @param row
     * @return
     */
    public static boolean isBlankRow(Row row){
        if(row == null) return true;
        int blank = 0;
        int totalCells = row.getLastCellNum();
        totalCells++;
        for(int i = 0; i < totalCells; i++){
            Cell cell = row.getCell(i, Row.RETURN_BLANK_AS_NULL);
            if(cell==null || ("")==cell.toString()) {
                blank++;
            }
        }
        if(blank==totalCells) {
            return true;
        }
        return false;
    }

    /** get set方法 **/
    public static Map getCurEntityHeadMap() {
        return curEntityHeadMap;
    }

    /**
     * @title readSheetHeadData
     * @description 读取特殊的表头数据
     * @author LiLu
     * @date 2018年9月10日 下午2:02:30
     * @param sheet
     */
    @SuppressWarnings({ "unchecked" })
    public static boolean readSpecialSheetHeadData(Sheet sheet,String entityName,int totalRows,int totalCells,boolean titleType,String excelType){
        Map headMap = new HashMap();
        curEntityHeadMap = new HashMap();
        int nominalNum_1=Constant.constantTableHeadCount;
        //6是因为.xml文件中是code为6处和excel表格读取表头key信息对应的 针对没有生物类型的将-1
        int nominalNum_2=6;

        //将固定不变的9个表头key放入headMap
        headMap.put(Constant.monitoringAreaCode, Constant.monitoringArea);

        //因为有两张表没有生物类型 需要做特殊处理
        headMap.put(Constant.ecologicaltypeCode, Constant.ecologicaltype);
        headMap.put(Constant.missionDateCode, Constant.missionDate);
        headMap.put(Constant.monitoringUnitCode, Constant.monitoringUnit);
        headMap.put(Constant.organizationalUnitCode, Constant.organizationalUnit);
        headMap.put(Constant.completionDateCode, Constant.completionDate);
        headMap.put(Constant.informantCode, Constant.informant);
        headMap.put(Constant.proofreaderCode, Constant.proofreader);
        headMap.put(Constant.auditorCode, Constant.auditor);

        //取行 3+1是因为16种表不一样的表头key信息是从第3+1行开始的
        Row excelheadRow1 = sheet.getRow(Constant.differInfoStartRow);
        int excelLastCellNum = excelheadRow1.getLastCellNum();
        if(excelLastCellNum == ParseXMLUtil.headMap.size()-nominalNum_1) {//if(excelLastRow == totalCells) { // Excel列数
            Map<String,Map<String,String>> columnMap = parseXmlUtil.getColumnMap();
            String headTitle = "";
            for (int i = 0; i < excelLastCellNum; i++) {
                if((i>=0&&i<=6)||(i==22)){
                    Cell cell = null;
                    cell = excelheadRow1.getCell(i);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    headTitle = getStringCellValue(cell).replaceAll(" ", "");//headTitle = getStringCellValue(cell).trim();
                    headTitle=headTitle.replaceAll("<","");
                    if (columnMap.get(entityName + "_" + headTitle) != null) {
                        if (headTitle.equals("")) continue;// wt20190715针对Excel两行标题修改
                        if (!columnMap.get(entityName + "_" + headTitle).get("code").equals(i+nominalNum_2 + "")) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                    headMap.put(i + Constant.constantTableHeadCount, headTitle);
                }else if((i>=7&&i<=21)||(i>=23&&i<=25)){
                    Cell cell = null;
                    Row excelHeadRow2 = sheet.getRow(Constant.differInfoStartRow + 1);
                    cell = excelHeadRow2.getCell(i);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    headTitle = getStringCellValue(cell).replaceAll(" ", "");
                    headTitle=headTitle.replaceAll("<","");
                    if (columnMap.get(entityName + "_" + headTitle) != null) {
                        if (headTitle.equals("")) continue;// wt20190715针对Excel两行标题修改
                        if (!columnMap.get(entityName + "_" + headTitle).get("code").equals(i+nominalNum_2 + "")) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                    headMap.put(i + Constant.constantTableHeadCount, headTitle);
                }

            }
            curEntityHeadMap.put(getCurEntityCode(), headMap);

        }else {
            return false;
        }
        return true;
    }

    /**
     * @title readSheetHeadData
     * @description 读取表头数据
     * @author LiLu
     * @date 2018年9月10日 下午2:02:30
     * @param sheet
     */
    @SuppressWarnings({ "unchecked" })
    public static boolean readSheetHeadData(Sheet sheet,String entityName,int totalRows,int totalCells,boolean titleType,String excelType){
        Map headMap = new HashMap();
        curEntityHeadMap = new HashMap();
        int nominalNum_1=Constant.constantTableHeadCount;
        //6是因为.xml文件中是code为6处和excel表格读取表头key信息对应的 针对没有生物类型的将-1
        int nominalNum_2=6;

        //将固定不变的9个表头key放入headMap
        headMap.put(Constant.monitoringAreaCode, Constant.monitoringArea);

        //因为有两张表没有生物类型 需要做特殊处理
        String[] tblRules={"BiologicalQualityExcelRule","SwimminganimalIdentificationExcelRule"};
        if(!Arrays.asList(tblRules).contains(excelType)) {
            headMap.put(Constant.ecologicaltypeCode, Constant.ecologicaltype);
        }else {
            nominalNum_1=nominalNum_1-1;
            nominalNum_2=nominalNum_2-1;
        }

        headMap.put(Constant.missionDateCode, Constant.missionDate);
        headMap.put(Constant.monitoringUnitCode, Constant.monitoringUnit);
        headMap.put(Constant.organizationalUnitCode, Constant.organizationalUnit);
        headMap.put(Constant.completionDateCode, Constant.completionDate);
        headMap.put(Constant.informantCode, Constant.informant);
        headMap.put(Constant.proofreaderCode, Constant.proofreader);
        headMap.put(Constant.auditorCode, Constant.auditor);

        //取行 3+1是因为16种表不一样的表头key信息是从第3+1行开始的
        Row excelheadRow1 = sheet.getRow(Constant.differInfoStartRow);
        int excelLastCellNum = excelheadRow1.getLastCellNum();
        if(excelLastCellNum == ParseXMLUtil.headMap.size()-nominalNum_1) {//if(excelLastRow == totalCells) { // Excel列数
            Map<String,Map<String,String>> columnMap = parseXmlUtil.getColumnMap();
            if(titleType) {
                log.info("getCurEntityCode():"+getCurEntityCode());
                log.info("ParseXMLUtil.headMap.toString():"+ParseXMLUtil.headMap.toString());
                curEntityHeadMap.put(getCurEntityCode(), ParseXMLUtil.headMap);// Excel表头为两行时进入此方法，将表头字段放入curEntityHeadMap
            }
            else {
                String headTitle = "";
                for (int i = 0; i < excelLastCellNum; i++) {
                    Cell cell = null;
                    cell = excelheadRow1.getCell(i);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    headTitle = getStringCellValue(cell).replaceAll(" ", "");//headTitle = getStringCellValue(cell).trim();
                    if (columnMap.get(entityName + "_" + headTitle) != null) {
                        if (headTitle.equals("")) continue;// wt20190715针对Excel两行标题修改
                        if (!columnMap.get(entityName + "_" + headTitle).get("code").equals(i+nominalNum_2 + "")) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                    headMap.put(i + Constant.constantTableHeadCount, headTitle);
                }
                curEntityHeadMap.put(getCurEntityCode(), headMap);
            }
        }else {
            return false;
        }
        return true;
    }

    /**
     * 获得单元格字符串
     * throws UnSupportedCellTypeException
     */
    public static String getStringCellValue(Cell cell) {
        if (cell == null){
            return null;
        }
        String result = "";
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                result = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    java.text.SimpleDateFormat TIME_FORMATTER = new java.text.SimpleDateFormat("yyyy-MM-dd");
                    result = TIME_FORMATTER.format(cell.getDateCellValue());
                }
                else{
                    double doubleValue = cell.getNumericCellValue();
                    result = "" + doubleValue;
                }
                break;
            case Cell.CELL_TYPE_STRING:
                if (cell.getRichStringCellValue() == null){
                    result = null;
                }
                else{
                    result = cell.getRichStringCellValue().getString();
                }
                break;
            case Cell.CELL_TYPE_BLANK:
                result = null;
                break;
            case Cell.CELL_TYPE_FORMULA:
                try{
                    result = String.valueOf(cell.getNumericCellValue());
                }catch(Exception e){
                    result = cell.getRichStringCellValue().getString();
                }
                break;
            default:
                result = "";
        }
        return result;
    }

    public static String getCurEntityCode() {
        return curEntityCode;
    }

    public void allMapList2jsonFile(List<Map<String, Object>> allMapList,String userId){
        try{
            // allMapList→json
            /*net.sf.json.JSONArray allMapJson = new net.sf.json.JSONArray();
            allMapJson=net.sf.json.JSONArray.fromObject(allMapList);//将java对象转换为json对象
            StringBuilder jsonStr=new StringBuilder();
            jsonStr.append(allMapJson);*/
            String jsonStr = JSON.toJSONString(allMapList, SerializerFeature.WriteClassName);
            // 保证创建一个新文件
            /*SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateStr=df.format(new Date());*/
            String fileName = new File("").getCanonicalPath()+config.getExcelJson()+userId+".json";
            File file = new File(fileName);
            if (!file.getParentFile().exists()){ // 如果父目录不存在，创建父目录
                file.getParentFile().mkdirs();
            }
            if (file.exists()) { // 如果已存在,删除旧文件
                file.delete();
            }
            file.createNewFile();
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            write.write(jsonStr);
            write.flush();
            write.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}