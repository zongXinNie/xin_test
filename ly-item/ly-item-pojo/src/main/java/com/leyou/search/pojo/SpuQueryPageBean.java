package com.leyou.search.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Nie ZongXin
 * @date 2019/9/10 12:13
 */
@Data
public class SpuQueryPageBean implements Serializable {

    private String key;
    private Boolean saleable;
    private Integer page;
    private Integer rows;
}
