package com.leyou.entity;


import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
@Data
@Table(name = "tb_spu")
public class Spu {

  @Id
  @KeySql(useGeneratedKeys = true)
  private Long id;
  private String name;
  private String subTitle;
  private Long cid1;
  private Long cid2;
  private Long cid3;
  private Long brandId;
  private Boolean saleable;
  private Date createTime;
  private Date updateTime;



}
