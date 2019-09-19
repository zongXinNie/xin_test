package com.leyou.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author Nie ZongXin
 * @date 2019/9/18 23:10
 */
@Data
public class Payload<T> {
    private String id;
    private T userInfo;
    private Date expiration;
}
