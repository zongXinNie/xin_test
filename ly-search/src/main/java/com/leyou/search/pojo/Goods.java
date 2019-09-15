package com.leyou.search.pojo;

import com.leyou.dto.SkuDTO;
import com.leyou.dto.SpecParamDTO;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Nie ZongXin
 * @date 2019/9/11 11:09
 */
@Data
@Document(indexName = "goods",type = "docs",shards = 1,replicas = 1)
public class Goods {
//    页面显示的
    @Id
    private Long id;
    private String skus;  //sku信息的json结构
    private String subTitle; //卖点
//    搜索过滤的条件
    private String all;  //所有需要被搜索的信息，包含标题，分类，甚至品牌
    private Long categoryId;
    private Long brandId;
    private Long createTime;
    private Set<Double> price;
    private Map<String,Object> specs;  //可搜索的规格参数，key是参数名，值是参数值


}
