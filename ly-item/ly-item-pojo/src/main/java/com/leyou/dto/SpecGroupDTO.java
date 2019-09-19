package com.leyou.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Nie ZongXin
 * @date 2019/9/9 22:27
 */
@Data
public class SpecGroupDTO {
    private Long id;
    private Long cid;
    private String name;
    private List<SpecParamDTO> params;
}
