package com.ii.springboot.exception;

import com.ii.springboot.result.CodeMsg;
import com.ii.springboot.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@ControllerAdvice
@ResponseBody
public class GloblaExceptionHandler {
    @ExceptionHandler(value=Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request,Exception e){
            if(e instanceof GloblaException){
                GloblaException globlaException = (GloblaException)e;
                return Result.error(globlaException.getCm());
            }
            else if(e instanceof BindException){
                BindException bindException = (BindException)e;
                List<ObjectError> allErrors = bindException.getAllErrors();
                ObjectError error = allErrors.get(0);
                String message = error.getDefaultMessage();
                return Result.error(CodeMsg.BIND_ERROR.fillArgs(message));
            }else{
                return Result.error(CodeMsg.SERVER_ERROR);
            }
    }







}
