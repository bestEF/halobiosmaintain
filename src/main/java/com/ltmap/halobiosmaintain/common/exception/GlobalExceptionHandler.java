package com.ltmap.halobiosmaintain.common.exception;

import com.ltmap.halobiosmaintain.common.result.Response;
import com.ltmap.halobiosmaintain.common.utils.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理器
 * @author fjh
 * @date 2020/11/12 13:55
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //全局异常处理
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response error(Exception e){
        log.info(ExceptionUtil.getMessage(e));
        return Response.fail("出错了");
    }



    //==================特殊异常处理start===============
    @ExceptionHandler(UnauthenticatedException.class)
    @ResponseBody
    public Response unauthenticatedExceptionError(Exception e){
        log.error(ExceptionUtil.getMessage(e));
        return Response.fail("未登录或登录以过期");
    }
    //==================特殊异常处理end==================



    //==================自定义异常处理start==============

    //==================自定义异常处理end================
}
