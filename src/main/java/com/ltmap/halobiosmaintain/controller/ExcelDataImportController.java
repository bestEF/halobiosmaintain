package com.ltmap.halobiosmaintain.controller;

import com.ltmap.halobiosmaintain.common.result.Response;
import com.ltmap.halobiosmaintain.common.result.Responses;
import com.ltmap.halobiosmaintain.common.utils.ShiroUtils;
import com.ltmap.halobiosmaintain.common.utils.excel_import.ExcelCheck;
import com.ltmap.halobiosmaintain.common.utils.excel_import.FileTestDataUtil;
import com.ltmap.halobiosmaintain.common.utils.excel_import.JsonUtils;
import com.ltmap.halobiosmaintain.config.Constant;
import com.ltmap.halobiosmaintain.service.ExcelDataImportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

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
     * 查询缓存数据
     * @return
     */
    @PostMapping("queryCacheData")
    @ApiOperation(value = "查询缓存数据")
    public Response queryCacheData(){
        if(ShiroUtils.isLogin()){
            String userId = ShiroUtils.getUserId();
            // 从json文件读allMapList
            List<Map<String, Object>> allMapList = fileTestDataUtil.jsonFile2allMapList(userId);
            return Responses.or(allMapList);
        }else {
            return Response.fail("无用户登陆信息，请重新登陆");
        }
    }

    /**
     * 从文件列表去除文件
     * @param request
     * @param codes
     * @return
     */
    @PostMapping(value = "fileDelete")
    @ApiOperation(value = "从文件列表去除文件")
    @ApiImplicitParam(name = "codes",value = "需要清除的质控数据",required = true)
    public Object fileDelete(HttpServletRequest request, String codes) {
        if(ShiroUtils.isLogin()){
            String userId = ShiroUtils.getUserId();
            // 从json文件读allMapList
            List<Map<String, Object>> allMapList = fileTestDataUtil.jsonFile2allMapList(userId);
            // 遍历并剔除
            List<String> codeList=new ArrayList<>();
            codeList= Arrays.asList(codes.split("\\,"));
            codeList = new ArrayList<>(codeList);
            Iterator<String> codeIterator = codeList.iterator();
            String code;
            while (codeIterator.hasNext()){
                code=codeIterator.next();
                ListIterator<Map<String,Object>> mapIterator = allMapList.listIterator();
                while (mapIterator.hasNext()) {
                    Map<String, Object> map = mapIterator.next();
                    if(map.get("code").toString().equals(code)) {
                        mapIterator.remove();
                        codeIterator.remove();
                    }
                }
            }

            // allMapList写回json文件
            fileTestDataUtil.allMapList2jsonFile(allMapList,userId);
            // 返回前台消息
            StringBuilder sb=new StringBuilder();
            for(String s:codeList) sb.append(s).append(",");
            if(codeList.size()>0)
                return Response.fail("存在未删除的文件:"+sb.substring(0,sb.length()-1));
            return Response.ok();
        }else {
            return Response.fail("无用户登陆信息，请重新登陆");
        }
    }

    /**
     * 文件入库
     * @param codes
     * @return
     */
    @PostMapping(value = "/fileInsert")
    @ApiOperation(value = "文件入库")
    @ApiImplicitParam(name = "codes",value = "校验成功需要入库的数据编号(字符串用逗号分隔)",required = true)
    @RequiresAuthentication
    public Response<Object> fileInsert(String codes) {
            String userId = ShiroUtils.getUserId();
            List<Map<String, Object>> allMapList = fileTestDataUtil.jsonFile2allMapList(userId);
            String userName = ShiroUtils.getAdminEntity().getUserName();
            return excelDataImportService.fileInsert(allMapList,codes,userId,userName);
    }

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
            @ApiImplicitParam(name = "dataType",value = "excel类别 1-生物质量 2-鱼卵定性 3-鱼卵定量 4-水文气象 5-潮间带生物定量 6-浮游动物（I型网） 7-大型底栖动物定性 8-大型底栖动物定量 9-浮游植物 10-沉积物 11-沉积物粒度 12-鱼卵定性 13-鱼卵定量 14-浮游动物（II型网） 15-游泳动物 16-水质",required = true),
            @ApiImplicitParam(name = "year",value = "年份",required = true),
            @ApiImplicitParam(name = "voyage",value = "航次",required = true)
    })
    @RequiresAuthentication
    public Response<Object> checkExcel(MultipartFile[] files, Integer dataType, String year, String voyage) throws IOException {
        Response<Object> res = new Response<>();

        if(ObjectUtils.isEmpty(dataType)||StringUtils.isBlank(year)){
            res.setError("年份和航次未填写");
            return res;
        }

        //定义excel校验结果返回集合
        List<ExcelCheck> checkList=new ArrayList<>();

        FileTestDataUtil.YEAR=year;
        FileTestDataUtil.VOYAGE=voyage;

        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> partMap = new HashMap<>();

        // 判断file数组不能为空并且长度大于0
        if (files != null && files.length > 0) {
            // 校验的excel信息放在allMapList作为缓存
            String userId = ShiroUtils.getUserId();
            List<Map<String, Object>> allMapList = fileTestDataUtil.jsonFile2allMapList(userId);
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
    @ApiImplicitParam(name = "excelType",value = "excel模板类型 1-生物质量 2-鱼卵定性 3-鱼卵定量 4-水文气象 5-潮间带生物定量 6-浮游动物（I型网） 7-大型底栖动物定性 8-大型底栖动物定量 9-浮游植物 10-沉积物 11-沉积物粒度 12-鱼卵定性 13-鱼卵定量 14-浮游动物（II型网） 15-游泳动物 16-水质",dataType = "int",required = true)
    public Response<String> downTemplate(Integer excelType){
        switch (excelType){
            case Constant.biologicalQualityType:
                return Responses.or(Constant.serverServletContext+Constant.biologicalQualityPath);
            case Constant.fisheggQualitativeType:
                return Responses.or(Constant.serverServletContext+Constant.fisheggQualitativePath);
            case Constant.fisheggQuantitativeType:
                return Responses.or(Constant.serverServletContext+Constant.fisheggQuantitativePath);
            case Constant.hydrometeorologicalType:
                return Responses.or(Constant.serverServletContext+Constant.hydrometeorologicalPath);
            case Constant.intertidalzonebiologicalQuantitativeType:
                return Responses.or(Constant.serverServletContext+Constant.intertidalzonebiologicalQuantitativePath);
            case Constant.largezooplanktonInetType:
                return Responses.or(Constant.serverServletContext+Constant.largezooplanktonInetPath);
            case Constant.macrobenthosQualitativeType:
                return Responses.or(Constant.serverServletContext+Constant.macrobenthosQualitativePath);
            case Constant.macrobenthosQuantitativeType:
                return Responses.or(Constant.serverServletContext+Constant.macrobenthosQuantitativePath);
            case Constant.phytoplanktonType:
                return Responses.or(Constant.serverServletContext+Constant.phytoplanktonPath);
            case Constant.sedimentType:
                return Responses.or(Constant.serverServletContext+Constant.sedimentPath);
            case Constant.sedimentgrainType:
                return Responses.or(Constant.serverServletContext+Constant.sedimentgrainPath);
            case Constant.smallfishQualitativeType:
                return Responses.or(Constant.serverServletContext+Constant.smallfishQualitativePath);
            case Constant.smallfishQuantitativeType:
                return Responses.or(Constant.serverServletContext+Constant.smallfishQuantitativePath);
            case Constant.smallzooplanktonIinetType:
                return Responses.or(Constant.serverServletContext+Constant.smallzooplanktonIinetPath);
            case Constant.swimminganimalIdentificationType:
                return Responses.or(Constant.serverServletContext+Constant.swimminganimalIdentificationPath);
            case Constant.waterqualityType:
                return Responses.or(Constant.serverServletContext+Constant.waterqualityPath);
            case Constant.birdObserveRecordType:
                return Responses.or(Constant.serverServletContext+Constant.birdObserveRecordPath);
            case Constant.vegetationSurveyRecordType:
                return Responses.or(Constant.serverServletContext+Constant.vegetationSurveyRecordPath);
            default:
                break;
        }
        return Response.fail("未取到模板下载路径");
    }
}
