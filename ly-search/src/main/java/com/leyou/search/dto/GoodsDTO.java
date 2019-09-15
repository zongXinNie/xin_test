package com.leyou.search.dto;

import lombok.Data;

/**
 * @author Nie ZongXin
 * @date 2019/9/12 10:10
 */
@Data
public class GoodsDTO {
    private Long id;
    private String skus;  //sku信息的json结构
    private String subTitle; //卖点
}
