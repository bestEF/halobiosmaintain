package com.ltmap.halobiosmaintain.service.impl;

import com.ltmap.halobiosmaintain.common.utils.excel_import.Config;
import com.ltmap.halobiosmaintain.common.utils.excel_import.FileTestDataUtil;
import com.ltmap.halobiosmaintain.config.Constant;
import com.ltmap.halobiosmaintain.service.ExcelDataImportService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                break;
            default:
                excelType="";
                break;
        }
        return excelType;
    }
}
