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
    /**
     * 鱼卵定量模板
     */
    @Value("${excel-template.fisheggQuantitativeTemp}")
    public String fisheggQuantitativePath;

    /**
     * @PostConstruct java注释 用于需要执行相关性注入后执行任何初始化的方法
     * 初始化常量
     */
    @PostConstruct
    public void init(){
        Constant.serverServletContext=serverServletContext;
        Constant.fisheggQuantitativePath=fisheggQuantitativePath;
    }
}
