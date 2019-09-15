package com.leyou.entity;


import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Table(name = "tb_sku")
public class Sku {

  @Id
  @KeySql(useGeneratedKeys = true)
  private Long id;
  private Long spuId;
  private String title;
  private String images;
  private Long stock;
  private Double price;
  private String indexes;
  private String ownSpec;
  private Boolean enable;
  private Date createTime;
  private Date updateTime;






}
