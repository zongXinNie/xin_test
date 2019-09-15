package com.leyou.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Nie ZongXin
 * @date 2019/9/10 11:35
 */
@Data
public class SpuDTO implements Serializable {

    private Long id;
    private String name;
    private String subTitle;
    private Long cid1;
    private Long cid2;
    private Long cid3;
    private Long brandId;
    private Boolean saleable;
    private String categoryName; // 商品分类名称拼接
    private String brandName;// 品牌名称
    private SpuDetailDTO spuDetail;
    private List<SkuDTO> skus;
    private Date createTime;

    /**
     * 方便同时获取3级分类
     *
     * @return
     */
    @JsonIgnore  //忽略方法，防止被序列化到json结果
    public List<Long> getCategoryIds() {
        return Arrays.asList(cid1, cid2, cid3);
    }
}
