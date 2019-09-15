package com.leyou.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Nie ZongXin
 * @date 2019/9/7 16:46
 */
@Data
public class CategoryDTO implements Serializable {

    private Long id;
    private String name;
    private Long parentId;
    private Boolean isParent;
    private Integer sort;

}
