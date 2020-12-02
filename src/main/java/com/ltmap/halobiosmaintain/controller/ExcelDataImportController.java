package com.ltmap.halobiosmaintain.controller;

import com.ltmap.halobiosmaintain.common.result.Response;
import com.ltmap.halobiosmaintain.common.result.Responses;
import com.ltmap.halobiosmaintain.common.utils.ShiroUtils;
import com.ltmap.halobiosmaintain.common.utils.excel_import.DateUtils;
import com.ltmap.halobiosmaintain.common.utils.excel_import.ExcelCheck;
import com.ltmap.halobiosmaintain.common.utils.excel_import.FileTestDataUtil;
import com.ltmap.halobiosmaintain.common.utils.excel_import.JsonUtils;
import com.ltmap.halobiosmaintain.config.Constant;
import com.ltmap.halobiosmaintain.service.ExcelDataImportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * excel数据导入控制器
 * @author fjh
 * @date 2020/11/28 17:04
 */
@RestController
@Api(tags = "excel数据导入控制器")
@RequestMapping("excelDataImport")
public class ExcelDataImportController {
    @Resource
    private FileTestDataUtil fileTestDataUtil;
    @Resource
    private ExcelDataImportService excelDataImportService;

    //选择文件一次性选择的多个文件数据查重使用
    public static List<Object> excelDataList;

    /**
     * Excel校验
     * @param files
     * @param dataType
     * @param year
     * @param voyage
     * @return
     */
    @PostMapping(value = "checkExcel")
    @ApiOperation(value = "Excel校验")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "files",value = "文件",required = true),
            @ApiImplicitParam(name = "excelType",value = "excel类别 1-鱼卵定量",required = true),
            @ApiImplicitParam(name = "year",value = "年份",required = true),
            @ApiImplicitParam(name = "voyage",value = "航次",required = true)
    })
    @RequiresAuthentication
    public Response<Object> checkExcel(MultipartFile[] files, Integer dataType, String year, String voyage) throws IOException {
        Response<Object> res = new Response<>();

        //定义excel校验结果返回集合
        List<ExcelCheck> checkList=new ArrayList<>();

        FileTestDataUtil.YEAR=year;
        FileTestDataUtil.VOYAGE=voyage;

        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> partMap = new HashMap<>();

        // 判断file数组不能为空并且长度大于0
        if (files != null && files.length > 0) {
            // 校验的excel信息放在allMapList作为缓存
            List<Map<String, Object>> allMapList = fileTestDataUtil.jsonFile2allMapList(ShiroUtils.getUserId());
            excelDataList=new ArrayList<>();
            // 循环获取file数组中得文件
            for (int i = 0; i < files.length; i++) {
                ExcelCheck ec=new ExcelCheck();
                MultipartFile file = files[i];
                resultMap = excelDataImportService.getAbsolutePath(file, i,excelDataImportService.getExcelType(dataType),allMapList);
                allMapList.add(resultMap);
                partMap = new HashMap<>();
                partMap.put("code", resultMap.get("code"));
                partMap.put("result", resultMap.get("result"));
                if (resultMap.get("result").equals(false)) {
                    int errorCode = (int) resultMap.get("errorCode");
                    partMap.put("errorCode", errorCode);
                    if (errorCode == 1 || errorCode == 2 || errorCode == 4) {
                        partMap.put("error", resultMap.get("error"));
                        ec.setResult(resultMap.get("error").toString());
                    } else if (errorCode == 3) {
                        partMap.put("error", "");
                        if (resultMap.get("errorFormat") != null) {
                            partMap.put("error", resultMap.get("errorFormat")+"；");
                            if (resultMap.get("exisNum") == null) partMap.put("error", resultMap.get("errorFormat"));
                        }
                        if (resultMap.get("exisNum") != null) {
                            String error=resultMap.get("errorExis").toString();
                            if(StringUtils.isNotBlank(error))
                                partMap.put("error", partMap.get("error") + error);
                        }
                        ec.setHasNote(true);//指示需要下载批注
                        ec.setResult(partMap.get("error").toString());
                    }
                } else {
                    partMap.put("error", "");
                    ec.setRight(true);
                    res.setSuccess(true);
                    ec.setResult("校验合格");
                }
                ec.setCode(resultMap.get("code").toString());
                ec.setFileName(file.getOriginalFilename());
                checkList.add(ec);
            }
            // allMapJson固化json文件
            fileTestDataUtil.allMapList2jsonFile(allMapList,ShiroUtils.getUserId());
            // 返回校验结果json
            String json = JsonUtils.objectToJson(checkList);
            res.setResult(json);
        }else{
            res.setError("未收到文件");
        }
        return res;
    }


    /**
     * excel模板下载
     * @param excelType
     * @return
     */
    @PostMapping("downTemplate")
    @ApiOperation(value = "excel模板下载")
    @ApiImplicitParam(name = "excelType",value = "excel模板类型 1-鱼卵定量",dataType = "int",required = true)
    public Response<String> downTemplate(Integer excelType){
        switch (excelType){
            case Constant.fisheggQuantitativeType:
                return Responses.or(Constant.serverServletContext+Constant.fisheggQuantitativePath);
            default:
                break;
        }
        return Response.fail("未取到模板下载路径");
    }
}
