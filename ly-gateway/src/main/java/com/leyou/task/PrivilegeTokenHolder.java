package com.leyou.task;

import com.leyou.item.client.AuthClient;
import com.leyou.config.JwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Nie ZongXin
 * @date 2019/9/20 16:14
 * 定时获取自己的token
 */
@Slf4j
@Component
public class PrivilegeTokenHolder {
    @Autowired
    private AuthClient authClient;
    @Autowired
    private JwtProperties jwtProperties;
    private String token;
    /**
     * 刷新token时间， 循环获取token时间，24小时
     */
    private final static Long TOKEN_REFRESH_INTERVAL = 86400000L;
    private final static Long TOKEN_RETRY_INTERVAL=10000L;

    /**
     * - fixedDelay：控制方法执行的间隔时间，是以上一次方法执行完开始算起，
     * 如上一次方法执行阻塞住了，那么直到上一次执行完，并间隔给定的时间后，执行下一次。
     * - fixedRate：是按照一定的速率执行，是从上一次方法执行开始的时间算起，如果上一次方法阻塞住了，
     * 下一次也是不会执行，但是在阻塞这段时间内累计应该执行的次数，当不再阻塞时，一下子把这些全部执行掉，而后再按照固定速率继续执行
     * - cron表达式：可以定制化执行任务，但是执行的方式是与fixedDelay相近的，也是会按照上一次方法结束时间开始算起。
     * @throws InterruptedException
     */
    @Scheduled(fixedDelay = 86400000L)
    public void loadToken() throws InterruptedException {
        while (true){
            try {
                this.token = authClient.authoriz(jwtProperties.getApp().getId(), jwtProperties.getApp().getSecret());
                log.info("网关获取token成功");
                break;
            } catch (Exception e) {
                log.error("网关获取token失败",e);
            }
            // 休眠10秒，再次重试
            Thread.sleep(10000L);
        }

    }

    public String getToken() {
        return token;
    }
}
