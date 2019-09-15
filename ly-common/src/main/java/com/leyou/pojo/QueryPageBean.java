package com.leyou.pojo;

import lombok.Data;

/**
 * @author Nie ZongXin
 * @date 2019/9/11 11:29
 */
@Data
public class QueryPageBean {
    private String key;
    private String sortBy;
    private Integer page;
    private Integer rows;
    private Boolean desc;
}
