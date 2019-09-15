package com.leyou.service.impl;

import com.leyou.common.ExceptionEnum;
import com.leyou.exception.LyException;
import com.leyou.search.pojo.Item;
import org.springframework.stereotype.Service;

import java.util.Random;


/**
 * @author Nie ZongXin
 * @date 2019/9/6 22:19
 */
@Service
public class ItemService {

    public Item saveItem(Item item){
        if (item.getPrice() == null) {
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        int id = new Random().nextInt(100);
        item.setId(id);
        return item;
    }
}
