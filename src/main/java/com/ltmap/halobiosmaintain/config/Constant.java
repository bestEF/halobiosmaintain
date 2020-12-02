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
    /**
     * 鱼卵定量
     */
    public static String fisheggQuantitativePath = "";


    //================以下为直接定义的静态常量========================
    //16种excel模板类型标志
    /**
     * 鱼卵定量excel模板类型标志
     */
    public static final int fisheggQuantitativeType=1;

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
