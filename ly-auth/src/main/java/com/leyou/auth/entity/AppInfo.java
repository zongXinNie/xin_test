package com.leyou.auth.entity;

import lombok.Data;

import java.util.List;

/**
 * @author Nie ZongXin
 * @date 2019/9/19 23:03
 */
@Data
public class AppInfo {
    private Long id;
    private String serviceName;
    private List<Long> targetList;
}
