package com.leyou.user.mapper;

import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@RegisterMapper
public interface BaseMapper<T> extends Mapper<T>, IdsMapper<T>, IdListMapper<T,Long>, MySqlMapper<T> {
}
