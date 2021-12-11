package com.foling.community.advice;

import com.alibaba.fastjson.JSON;
import com.foling.community.dto.ResultDTO;
import com.foling.community.exception.CustomizeErrorCode;
import com.foling.community.exception.CustomizeException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author foling
 * @Date2021-07-25 11:21
 * @Version 1.0
 * @Other Be happy~
 **/
@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    ModelAndView handlerControllerException(HttpServletRequest request, Throwable ex, HttpServletResponse response) {
        ModelAndView model = new ModelAndView();
        String contentType = request.getContentType();
        if ("application/json".equals(contentType)) {//判断是否为json类型的请求 如果是的话以json方式进行返回
            //JSON返回
            ResultDTO resultDTO = null;
            if (ex instanceof CustomizeException) {
                resultDTO = ResultDTO.errorOf((CustomizeException) ex);
            } else {
                resultDTO = ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
            try {
                PrintWriter writer = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            //错误页面跳转
            System.out.println(ex.getMessage());
            if (ex instanceof CustomizeException) {
                model.addObject("message", ex.getMessage());
            } else {
                model.addObject("message", "服务器冒烟了,试试其它的网页");
            }
            model.setViewName("error");
            return model;
        }
    }
}
