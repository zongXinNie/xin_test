package com.leyou.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Nie ZongXin
 * @date 2019/9/10 17:49
 */
@Data
public class SkuDTO {

    private Long id;
    private Long spuId;
    private String title;
    private String images;
    private Long stock;
    private Double price;
    private String indexes;
    private String ownSpec;
    private Boolean enable;
}
