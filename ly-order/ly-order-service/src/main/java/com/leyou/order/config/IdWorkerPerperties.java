package com.leyou.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Nie ZongXin
 * @date 2019/9/22 22:05
 */
@Data
@ConfigurationProperties(prefix = "ly.worker")
public class IdWorkerPerperties {

    private Long workerId;  //当前机器Id
    private Long dataCenterId; //序列号
}
