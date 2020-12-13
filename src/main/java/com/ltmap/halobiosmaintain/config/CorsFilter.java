//package com.ltmap.halobiosmaintain.config;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//@Order(0)
//public class CorsFilter implements Filter {
//
//    private Logger logger = LoggerFactory.getLogger(CorsFilter.class);
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletResponse res = (HttpServletResponse) response;
//        HttpServletRequest req = (HttpServletRequest) request;
//        if (res.getHeader("Access-Control-Allow-Origin") == null) {
//            res.addHeader("Access-Control-Allow-Origin", "*");
//            res.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
//            res.addHeader("Access-Control-Allow-Headers", "Content-Type, x-requested-with, X-Custom-Header, Authorization, authToken");
//        }
//        if (req.getMethod().equals("OPTIONS")) {
//            res.setStatus(HttpServletResponse.SC_OK);
//            return;
//        }
//        chain.doFilter(request, response);
//    }
//
//    @Override
//    public void destroy() {
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) {
//        logger.info("CorsFilter ==> init");
//    }
//
//}
