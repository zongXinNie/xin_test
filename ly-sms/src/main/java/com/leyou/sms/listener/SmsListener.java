package com.leyou.sms.listener;

import com.leyou.constants.AbstractMQConstants;
import com.leyou.exception.LyException;
import com.leyou.sms.config.SmsProperties;
import com.leyou.sms.utils.SmsHelper;
import com.leyou.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Nie ZongXin
 * @date 2019/9/17 20:49
 */
@Slf4j
@Component
public class SmsListener {
    @Autowired
    private SmsProperties properties;
    @Autowired
    private SmsHelper smsHelper;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = AbstractMQConstants.Queue.SMS_VERIFY_CODE_QUEUE,durable = "true"),
            exchange = @Exchange(name = AbstractMQConstants.Exchange.SMS_EXCHANGE_NAME,ignoreDeclarationExceptions ="true",type = ExchangeTypes.TOPIC),
            key = AbstractMQConstants.RoutingKey.VERIFY_CODE_KEY
    ))
    public void listtenVerifyCode(Map<String, String> msg) {
        if (msg == null) {
            return;
        }
//        移除手机数据，剩下的是短信参数
        String phone = msg.remove("phone");
        if (StringUtils.isBlank(phone)){
            return;
        }
        try {
            smsHelper.sendMessage(phone,properties.getSignName(),properties.getVerifyCodeTemplate(), JsonUtils.toString(msg));
        } catch (LyException e) {
           log.error("【SMS服务】短信验证码发送失败",e);
        }
    }
}
