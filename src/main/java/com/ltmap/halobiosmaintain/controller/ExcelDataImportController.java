package com.ltmap.halobiosmaintain.controller;

import com.ltmap.halobiosmaintain.common.result.Response;
import com.ltmap.halobiosmaintain.common.result.Responses;
import com.ltmap.halobiosmaintain.config.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;

/**
 * excel数据导入控制器
 * @author fjh
 * @date 2020/11/28 17:04
 */
@RestController
@Api(tags = "excel数据导入控制器")
@RequestMapping("excelDataImport")
public class ExcelDataImportController {

    /**
     * Exceldao校验
     * @param files
     * @param excelType
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

    public Response checkExcel(MultipartFile[] files, Integer excelType,String year,String voyage){
        return null;
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
