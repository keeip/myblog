package com.suancaiyu.handler;

import com.suancaiyu.resultcommon.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//对控制器进行增强
@ControllerAdvice
public class exceptionHandler {
    /**
     *  全局异常处理器
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody//返回json数据
    public Result doExceptionHandler(Exception ex){
        ex.printStackTrace();
        return Result.fail(500,"系统异常");
    }

}
