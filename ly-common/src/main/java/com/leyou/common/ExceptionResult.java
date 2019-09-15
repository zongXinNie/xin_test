package com.leyou.common;

import com.leyou.exception.LyException;
import lombok.Getter;
import org.joda.time.DateTime;

/**
 * @author Nie ZongXin
 * @date 2019/9/6 22:52
 */
@Getter
public class ExceptionResult {
    private int status;
    private String message;
    private String timestamp;

    public ExceptionResult(LyException e) {
        this.status=e.getStaus();
        this.message=e.getMessage();
        this.timestamp= DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
    }
}
