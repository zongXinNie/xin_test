package com.leyou.advice;

import com.leyou.common.ExceptionResult;
import com.leyou.exception.LyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Nie ZongXin
 * @date 2019/9/6 22:27
 * 统一异常拦截器
 */
@ControllerAdvice
@Slf4j
public class BasicExceptionAdvice {

    @ExceptionHandler(LyException.class)
    public ResponseEntity<ExceptionResult> handleException(LyException e) {
        log.error(" LyException",e);
        return ResponseEntity.status(e.getStaus()).body(new ExceptionResult(e));
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> runtimeException(RuntimeException e){
        log.error(" runtime Exception",e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }


}
