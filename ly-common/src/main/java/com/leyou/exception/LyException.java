package com.leyou.exception;

import com.leyou.common.ExceptionEnum;
import lombok.Getter;

/**
 * @author Nie ZongXin
 * @date 2019/9/6 22:06
 */
@Getter
public class LyException extends RuntimeException {
    /**
     * 异常状态码
     */
    private int staus;

    public LyException(ExceptionEnum em){
        super(em.getMessage());
        this.staus=em.getStatus();
    }
    public LyException(ExceptionEnum em,Throwable cause){
        super(em.getMessage(),cause);
        this.staus=em.getStatus();
    }

    public LyException(String message, int staus) {
        super(message);
        this.staus = staus;
    }

    public LyException(String message, Throwable cause, int staus) {
        super(message, cause);
        this.staus = staus;
    }


}
