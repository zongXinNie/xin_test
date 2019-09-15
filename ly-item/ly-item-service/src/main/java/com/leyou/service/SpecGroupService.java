package com.leyou.service;

import com.leyou.dto.SpecGroupDTO;
import com.leyou.entity.SpecGroup;

import java.util.List;

public interface SpecGroupService {
    List<SpecGroupDTO> findSpecGroupByCid(Long cid);
}
