package com.ltmap.halobiosmaintain.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Mybatis-Plus配置类
 * @author fjh
 * @date 2020/11/13 15:15
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@MapperScan(value = {"com.ltmap.halobiosmaintain.mapper"})
public class MPConfig {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        return paginationInterceptor;
    }
}
