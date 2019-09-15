package com.leyou.search.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Nie ZongXin
 * @date 2019/9/7 21:37
 */
@Data
public class QueryPageBean implements Serializable {
    // 搜索条件
    private String key;
    // 当前页
    private Integer page;
    //    每页大小
    private Integer rows;
    //    排序字段
    private String sortBy;
    //    是否降序
    private Boolean desc;


}
