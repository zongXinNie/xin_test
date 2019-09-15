package com.leyou.controller;

import com.leyou.search.pojo.Item;
import com.leyou.service.impl.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Nie ZongXin
 * @date 2019/9/6 22:21
 */
@RestController
public class ItemController {
    @Autowired
    private ItemService itemService;

    @GetMapping("/item")
    public ResponseEntity<Item> saveItem(Item item) {
//        如果价格为空，则抛出异常，返回400状态码，请求参数有误
       /* if (item.getPrice() == null) {
            throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
        }*/
        Item result = itemService.saveItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

}
