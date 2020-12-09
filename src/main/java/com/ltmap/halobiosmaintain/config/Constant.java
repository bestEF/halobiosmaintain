package com.ltmap.halobiosmaintain.config;

/**
 * 常量帮助类
 */
public class Constant {
    //===============以下为需要从配置文件注入的静态常量================
    /**
     * 服务上下文路径
     */
    public static String serverServletContext="";

    //16种excel模板模板路径
    public static String biologicalQualityPath="";
    public static String fisheggQualitativePath="";
    public static String fisheggQuantitativePath="";
    public static String hydrometeorologicalPath="";
    public static String intertidalzonebiologicalQuantitativePath="";
    public static String largezooplanktonInetPath="";
    public static String macrobenthosQualitativePath="";
    public static String macrobenthosQuantitativePath="";
    public static String phytoplanktonPath="";
    public static String sedimentPath="";
    public static String sedimentgrainPath="";
    public static String smallfishQualitativePath="";
    public static String smallfishQuantitativePath="";
    public static String smallzooplanktonIinetPath="";
    public static String swimminganimalIdentificationPath="";
    public static String waterqualityPath="";


    //================以下为直接定义的静态常量========================
    /**正式数据第一个cell开始位 因为有两张表没有生物类型**/
    public static final int officialDataStartSpecialSign=5;

    /**正式数据第一个cell开始位**/
    public static final int officialDataStartSign=6;

    /**16种表固定不变的表头key信息数量**/
    public static final int constantTableHeadCount=9;

    /**16种表不一样的表头key信息开始的行数**/
    public static final int differInfoStartRow=3;

    //18种数据模板名称
    public static final String hydrometeorologicalName="水文气象";
    public static final String waterqualityName="水质";
    public static final String sedimentName="沉积物";
    public static final String sedimentgrainName="沉积物粒度";
    public static final String phytoplanktonName="浮游植物";
    public static final String largezooplankton_1_netName="浮游动物（I型网）";
    public static final String smallzooplankton_2_netName="浮游动物（II型网）";
    public static final String macrobenthosQuantitativeName="大型底栖动物定量";
    public static final String macrobenthosQualitativeName="大型底栖动物定性";
    public static final String intertidalzonebiologicalQuantitativeName="潮间带生物定量";
    public static final String fisheggQuantitativeName="鱼卵定量";
    public static final String fisheggQualitativeName="鱼卵定性";
    public static final String smallfishQuantitativeName="仔鱼定量";
    public static final String smallfishQualitativeName="仔鱼定性";
    public static final String swimminganimalIdentificationName="游泳动物";
    public static final String biologicalQualityName="生物质量";
    public static final String birdObserveRecordName="鸟类";
    public static final String vegetationSurveyRecordName="植被";

    //16中excel模板固定不变的表头key信息对应的标号
    /**监控区Code**/
    public static final int monitoringAreaCode=0;
    /**生态类型Code**/
    public static final int ecologicaltypeCode=1;
    /**任务日期Code**/
    public static final int missionDateCode=2;
    /**监测单位Code**/
    public static final int monitoringUnitCode=3;
    /**组织单位Code**/
    public static final int organizationalUnitCode=4;
    /**填报日期Code**/
    public static final int completionDateCode=5;
    /**填报人Code**/
    public static final int informantCode=6;
    /**校对人Code**/
    public static final int proofreaderCode=7;
    /**审核人Code**/
    public static final int auditorCode=8;

    //16中excel模板固定不变的表头key信息
    public static final String monitoringArea="监测区域：";
    public static final String ecologicaltype="生态类型：";
    public static final String missionDate="任务日期：";
    public static final String monitoringUnit="监测单位：";
    public static final String organizationalUnit="组织单位：";
    public static final String completionDate="填报日期：";
    public static final String informant="填报人：";
    public static final String proofreader="校对人：";
    public static final String auditor="审核人：";

    //16种excel模板类型标志
    /**
     * 鱼卵定量excel模板类型标志
     */
    public static final int biologicalQualityType=1;
    public static final int fisheggQualitativeType=2;
    public static final int fisheggQuantitativeType=3;
    public static final int hydrometeorologicalType=4;
    public static final int intertidalzonebiologicalQuantitativeType=5;
    public static final int largezooplanktonInetType=6;
    public static final int macrobenthosQualitativeType=7;
    public static final int macrobenthosQuantitativeType=8;
    public static final int phytoplanktonType=9;
    public static final int sedimentType=10;
    public static final int sedimentgrainType=11;
    public static final int smallfishQualitativeType=12;
    public static final int smallfishQuantitativeType=13;
    public static final int smallzooplanktonIinetType=14;
    public static final int swimminganimalIdentificationType=15;
    public static final int waterqualityType=16;
    public static final int birdObserveRecordType=17;
    public static final int vegetationSurveyRecordType=18;

    //0 1标志
    /**
     * 标志 0
     */
    public static final int flag_0=0;
    /**
     * 标志 1
     */
    public static final int flag_1=1;

    /**
     * 功能模块（用作日志存储）
     */
    public static final String loginAndLogout="登录退出";

}
