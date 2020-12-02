package com.ltmap.halobiosmaintain.common.utils.excel_import;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ltmap.halobiosmaintain.controller.ExcelDataImportController;
import com.ltmap.halobiosmaintain.vo.req.FisheggQuantitativeReq;
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
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    /**
     * 年份
     */
    public static String YEAR;
    /**
     * 航次
     */
    public static String VOYAGE;
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

        boolean isHeadRight = readSheetHeadData(sheet,entityName,totalCells,titleType);//表头

        if(isHeadRight){
            if (totalRows>=titleRows+1) {//if (totalRows>=2) {
                headMap = (Map) getCurEntityHeadMap().get(getCurEntityCode());
                // 循环Excel行数,从第2行开始,1行表头不入库
                List<String> returnList=new ArrayList<>();

                for (int r = titleRows; r < totalRows; r++) {//for (int r = 1; r < totalRows; r++) {
                    Row row = sheet.getRow(r);
                    if (isBlankRow(row)) {
                        continue;
                    }

                    // 按不同文件类型执行校验

                    returnList=classify(totalCells,row,headMap,entityName,r,excelType,allMapList);

                    // 查重
                    Boolean dataExist=Boolean.valueOf(returnList.get(0));// 0：数据库重复数据
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
    private List<String> classify(Integer totalCells,Row row,Map headMap,String entityName,int r,String type,List<Map<String, Object>> allMapList){
        List<String> list=new ArrayList<>();
        switch (type){
            case "FisheggQuantitativeExcelRule":// 鱼卵定量数据表
                list = fisheggQuantitativeExcelRule(totalCells,row,headMap,entityName,r,allMapList);
                list.add("1");
                break;
            default:
                break;
        }
        return list;
    }

    //鱼卵定量数据表
    private List<String> fisheggQuantitativeExcelRule(Integer totalCells,Row row,Map headMap,String entityName,int r,List<Map<String, Object>> allMapList){
        List<String> returnList=new ArrayList<>();
        FisheggQuantitativeReq fisheggQuantitativeReq = new FisheggQuantitativeReq();
        int totalRst = 0;//每行格式错误单元格的数目
        int validaterst = 0;

        // 循环row的列，按xml规则校验，并写入对象
        for (int c = 0; c < totalCells; c++) {
            Cell cell = row.getCell(c);
            String headTitle = headMap.get(c).toString();
            /**按规则验证cell格式**/
            validaterst = validateCellData(r+1,c+1,cell,entityName,headTitle);
            totalRst += validaterst;

            if(totalRst == 0 && cell != null) {             // 定制
                cell.setCellType(Cell.CELL_TYPE_STRING);
                if(cell.getStringCellValue().equals("——")) cell.setCellValue("");
                if(c==0){
                    //msWarterExtReq.setStationCode(cell.getStringCellValue());
                } else if(c==48){
                    String cellValue = cell.getStringCellValue();
                    //msWarterExtReq.setHk(Float.valueOf(cellValue));
                }

            }

        }

        //数据库查重
        boolean dataExist=false;//boolean dataExist = dataExisted(msWarterExtReq, Constant.shuiti);// 定制 // && (!isSampleRepeat);
        returnList.add(String.valueOf(dataExist));

        // Excel文件列表查重(遍历allMapList中底栖站位多样性数据，检索是否已包含当前数据)
        boolean sameInfo=false;
        returnList.add(String.valueOf(sameInfo));

        // 校验合格：写入dataList
        if(totalRst==0){
            dataList1.add(fisheggQuantitativeReq);
            excelDataImportController.excelDataList.add(fisheggQuantitativeReq);
        }
        return returnList;
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
//                  if(!StringUtils.isNotBlank(cellValue) || cellValue.equals("——"))

//                  break;
                    cellType = colCell.getCellType();
                    if((cellType != Cell.CELL_TYPE_NUMERIC && cellType!= Cell.CELL_TYPE_FORMULA && StringUtils.isNotEmpty(cellValue))) {
                        if(!errorString.toString().contains(err3)) errorString.append(err3);
                        errorMap.put("curRow", curRow);
                        errorMap.put("curCol", curCol);
                        errorMap.put("rulMsg", rulMsg);
                        errorList.add(errorMap);
                        result = -1;
                        break;
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
     * @description 读取表头数据
     * @author LiLu
     * @date 2018年9月10日 下午2:02:30
     * @param sheet
     */
    @SuppressWarnings({ "unchecked" })
    public static boolean readSheetHeadData(Sheet sheet,String entityName,int totalCells,boolean titleType){
        Map headMap = new HashMap();
        curEntityHeadMap = new HashMap();
        Row excelheadRow1 = sheet.getRow(titleType?2:0);
        int excelLastRow = excelheadRow1.getLastCellNum();
        if(excelLastRow == ParseXMLUtil.headMap.size()) {//if(excelLastRow == totalCells) { // Excel列数
            Map<String,Map<String,String>> columnMap = parseXmlUtil.getColumnMap();
            if(titleType)
                curEntityHeadMap.put(getCurEntityCode(), ParseXMLUtil.headMap);// Excel表头为两行时进入此方法，将表头字段放入curEntityHeadMap
            else{
                String headTitle = "";
                for(int i=0;i<excelLastRow;i++){
                    Cell cell = null;
                    cell = excelheadRow1.getCell(i);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    headTitle = getStringCellValue(cell).replaceAll(" ", "");//headTitle = getStringCellValue(cell).trim();
                    if(columnMap.get(entityName+"_"+headTitle)!=null) {
                        if(headTitle.equals("")) continue;// wt20190715针对Excel两行标题修改
                        if(!columnMap.get(entityName+"_"+headTitle).get("code").equals(i+"")) {
                            return false;
                        }
                    }else {
                        return false;
                    }
                    headMap.put(i, headTitle);
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