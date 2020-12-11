package com.ltmap.halobiosmaintain.service;

import com.ltmap.halobiosmaintain.common.result.Response;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * excel数据导入Service
 * @author fjh
 * @date 2020/12/1 9:46
 */
public interface ExcelDataImportService {

    /**
     * 获得是那一种数据类型的Excel校验类型 对应xml校验文件
     * @param dataType
     * @return
     */
    String getExcelType(Integer dataType);

    /**
     * 文件持久化
     * @param file
     * @param i
     * @param excelType
     * @param allMapList
     * @return
     */
    Map<String, Object> getAbsolutePath(MultipartFile file, int i, String excelType, List<Map<String, Object>> allMapList) throws IOException;

    /**
     * 文件入库
     * @param allMapList
     * @param codes
     * @param userId
     * @param userName
     * @return
     */
    Response<Object> fileInsert(List<Map<String, Object>> allMapList, String codes, String userId, String userName);

    /**
     * excel错误信息导出
     * @param allMapList
     * @param code
     * @param response
     * @param fileFolder
     * @return
     */
    String errorExport(List<Map<String, Object>> allMapList, String code, HttpServletResponse response, String fileFolder);
}
