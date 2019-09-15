package com.leyou.dto;

import lombok.Data;

/**
 * @author Nie ZongXin
 * @date 2019/9/9 22:27
 */
@Data
public class SpecParamDTO {
    private Long id;
    private Long cid;
    private Long groupId;
    private String name;
    private Boolean numeric;
    private String unit;
    private Boolean generic;
    private Boolean searching;
    private String segments;
}
