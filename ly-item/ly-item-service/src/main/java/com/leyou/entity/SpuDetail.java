package com.leyou.entity;


import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "tb_spu_detail")
public class SpuDetail {

  @Id
  @KeySql(useGeneratedKeys = true)
  private Long spuId;
  private String description;
  private String genericSpec;
  private String specialSpec;
  private String packingList;
  private String afterService;
  private Date createTime;
  private Date updateTime;




}
