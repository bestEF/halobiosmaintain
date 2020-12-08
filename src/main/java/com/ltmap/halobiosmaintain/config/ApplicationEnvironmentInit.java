package com.ltmap.halobiosmaintain.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

/**
 * 应用环境初始化
 * @author fjh
 * @date 2020/11/28 16:00
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApplicationEnvironmentInit {

    /**
     * 服务上下文路径
     */
    @Value("${server.ip}${server.servlet.context-path}")
    public String serverServletContext;

    //16种excel模板
    @Value("${excel-template.biologicalQualityTemp}")
    public String biologicalQualityPath="";
    @Value("${excel-template.fisheggQualitativeTemp}")
    public String fisheggQualitativePath="";
    @Value("${excel-template.fisheggQuantitativeTemp}")
    public String fisheggQuantitativePath="";
    @Value("${excel-template.hydrometeorologicalTemp}")
    public String hydrometeorologicalPath="";
    @Value("${excel-template.intertidalzonebiologicalQuantitativeTemp}")
    public String intertidalzonebiologicalQuantitativePath="";
    @Value("${excel-template.largezooplanktonInetTemp}")
    public String largezooplanktonInetPath="";
    @Value("${excel-template.macrobenthosQualitativeTemp}")
    public String macrobenthosQualitativePath="";
    @Value("${excel-template.macrobenthosQuantitativeTemp}")
    public String macrobenthosQuantitativePath="";
    @Value("${excel-template.phytoplanktonTemp}")
    public String phytoplanktonPath="";
    @Value("${excel-template.sedimentTemp}")
    public String sedimentPath="";
    @Value("${excel-template.sedimentgrainTemp}")
    public String sedimentgrainPath="";
    @Value("${excel-template.smallfishQualitativeTemp}")
    public String smallfishQualitativePath="";
    @Value("${excel-template.smallfishQuantitativeTemp}")
    public String smallfishQuantitativePath="";
    @Value("${excel-template.smallzooplanktonIinetTemp}")
    public String smallzooplanktonIinetPath="";
    @Value("${excel-template.swimminganimalIdentificationTemp}")
    public String swimminganimalIdentificationPath="";
    @Value("${excel-template.waterqualityTemp}")
    public String waterqualityPath="";

    /**
     * @PostConstruct java注释 用于需要执行相关性注入后执行任何初始化的方法
     * 初始化常量
     */
    @PostConstruct
    public void init(){
        Constant.serverServletContext=serverServletContext;

        Constant.fisheggQuantitativePath=fisheggQuantitativePath;;
        Constant.biologicalQualityPath=biologicalQualityPath;
        Constant.fisheggQualitativePath=fisheggQualitativePath;
        Constant.fisheggQuantitativePath=fisheggQuantitativePath;
        Constant.hydrometeorologicalPath=hydrometeorologicalPath;
        Constant.intertidalzonebiologicalQuantitativePath=intertidalzonebiologicalQuantitativePath;
        Constant.largezooplanktonInetPath=largezooplanktonInetPath;
        Constant.macrobenthosQualitativePath=macrobenthosQualitativePath;
        Constant.macrobenthosQuantitativePath=macrobenthosQuantitativePath;
        Constant.phytoplanktonPath=phytoplanktonPath;
        Constant.sedimentPath=sedimentPath;
        Constant.sedimentgrainPath=sedimentgrainPath;
        Constant.smallfishQualitativePath=smallfishQualitativePath;
        Constant.smallfishQuantitativePath=smallfishQuantitativePath;
        Constant.smallzooplanktonIinetPath=smallzooplanktonIinetPath;
        Constant.swimminganimalIdentificationPath=swimminganimalIdentificationPath;
        Constant.waterqualityPath=waterqualityPath;
    }
}
