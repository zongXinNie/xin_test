package com.leyou.listener;


import com.leyou.constants.AbstractMQConstants;
import com.leyou.service.PageService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author Nie ZongXin
 * @date 2019/9/17 16:08
 */
@Component
public class PageListener {
    @Autowired
    private PageService pageService;
    @Value("${ly.static.itemDir}")
    private String itemDir;

    /**
     * 下架商品
     *
     * @param id
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = AbstractMQConstants.Queue.PAGE_ITEM_DOWN, durable = "true"),
            exchange = @Exchange(value = AbstractMQConstants.Exchange.ITEM_EXCHANGE_NAME, ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
            key = {AbstractMQConstants.RoutingKey.ITEM_DOWN_KEY}
    ))
    public void commodityDown(Long id) {
       if (id!=null){
           pageService.deleteItemHtml(id);
       }
    }

    /**
     * 上架商品
     *
     * @param id
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = AbstractMQConstants.Queue.PAGE_ITEM_UP, durable = "true"),
            exchange = @Exchange(value = AbstractMQConstants.Exchange.ITEM_EXCHANGE_NAME, ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
            key = {AbstractMQConstants.RoutingKey.ITEM_UP_KEY}
    ))
    public void commdityUp(Long id) {
        if (id!=null) {
            pageService.createItemHtml(id);
        }
    }

}
