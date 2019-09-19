package com.leyou.search.listener;

import com.leyou.client.ItemClient;
import com.leyou.constants.AbstractMQConstants;
import com.leyou.dto.SpuDTO;
import com.leyou.search.pojo.Goods;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.SearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Nie ZongXin
 * @date 2019/9/17 15:37
 */
@Component
public class SearchListener {
    @Autowired
    private SearchService searchService;

    /**
     * 下架商品
     * @param id
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = AbstractMQConstants.Queue.SEARCH_ITEM_DOWN, durable = "true"),
            exchange = @Exchange(value = AbstractMQConstants.Exchange.ITEM_EXCHANGE_NAME, ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
            key = {AbstractMQConstants.RoutingKey.ITEM_DOWN_KEY}
    ))
    public void commodityDown(Long id) {
       if (id!=null){
           searchService.deleteItemDoc(id);
       }

    }

    /**
     * 上架商品
     * @param id
     */
    @RabbitListener(bindings =@QueueBinding(
            value = @Queue(value = AbstractMQConstants.Queue.SEARCH_ITEM_UP,durable = "true"),
            exchange = @Exchange(value =AbstractMQConstants.Exchange.ITEM_EXCHANGE_NAME,ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {AbstractMQConstants.RoutingKey.ITEM_UP_KEY}
    ) )
    public void commdityUp(Long id){
        if (id!=null) {
           searchService.addGoodsDoc(id);
        }
    }


}
