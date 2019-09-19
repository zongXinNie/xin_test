package com.leyou.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Nie ZongXin
 * @date 2019/9/10 17:43
 */
@Data
public class SpuDetailDTO {

    private Long spuId;
    private String description;
    private String genericSpec;
    private String specialSpec;
    private String packingList;
    private String afterService;


}
