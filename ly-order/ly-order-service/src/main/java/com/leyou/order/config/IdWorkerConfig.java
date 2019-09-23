package com.leyou.order.config;

import com.leyou.utils.IdWorker;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Nie ZongXin
 * @date 2019/9/22 22:07
 */
@Configuration
@EnableConfigurationProperties(IdWorkerPerperties.class)
public class IdWorkerConfig {
    @Bean
    public IdWorker idWorker(IdWorkerPerperties idWorkerPerperties) {
        return new IdWorker(idWorkerPerperties.getWorkerId(), idWorkerPerperties.getDataCenterId());
    }
}
