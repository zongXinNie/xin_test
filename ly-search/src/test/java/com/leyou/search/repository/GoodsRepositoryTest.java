package com.leyou.search.repository;

import com.leyou.LySearchApplication;
import com.leyou.client.ItemClient;
import com.leyou.dto.BrandDTO;
import com.leyou.dto.SpuDTO;
import com.leyou.pojo.PageResult;
import com.leyou.search.pojo.Goods;
import com.leyou.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = LySearchApplication.class)
public class GoodsRepositoryTest {
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private SearchService searchService;
    @Autowired
    private ItemClient itemClient;

    @Test
    public void method() {
        BrandDTO brandById = itemClient.findBrandById(8551L);
        System.out.println("brandById = " + brandById);

    }
    @Test
    public void method1(){
        int page=1;
        int rows=100;
        int size=0;
        do {
            try {
                PageResult<SpuDTO> spuPage = itemClient.findSpuPage(null, true, page, rows);
                List<SpuDTO> items = spuPage.getItems();
                List<Goods> goods = items.stream().map(searchService::buildGoods).collect(Collectors.toList());
                goodsRepository.saveAll(goods);
                page++;
                size=items.size();
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }while (size==100);

    }
}