package com.leyou.service.impl;

import com.leyou.common.ExceptionEnum;
import com.leyou.dto.SpecGroupDTO;
import com.leyou.dto.SpecParamDTO;
import com.leyou.entity.SpecGroup;
import com.leyou.entity.SpecParam;
import com.leyou.exception.LyException;
import com.leyou.mapper.SpecGroupMapper;
import com.leyou.mapper.SpecParamMapper;
import com.leyou.service.SpecGroupService;
import com.leyou.service.SpecParamService;
import com.leyou.utils.BeanHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Nie ZongXin
 * @date 2019/9/9 22:29
 */
@Service
@Transactional
public class SpecGroupServiceImpl implements SpecGroupService {
    @Autowired
    private SpecGroupMapper specGroupMapper;
    @Autowired
    private SpecParamService specParamService;

    /**
     * 根据分类查询规格参数组
     *
     * @param cid
     * @return
     */
    @Override
    public List<SpecGroupDTO> findSpecGroupByCid(Long cid) {
        if (cid == null) {
            throw new LyException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> specGroups = specGroupMapper.select(specGroup);

        if (CollectionUtils.isEmpty(specGroups)) {
            throw new LyException(ExceptionEnum.SPEC_NOT_FOUND);
        }
        List<SpecGroupDTO> specGroupDTOS = BeanHelper.copyWithCollection(specGroups, SpecGroupDTO.class);
        List<SpecParamDTO> specParamDTOS = specParamService.findSpecParamByGroupId(null, cid, null);
//        分组
        Map<Long, List<SpecParamDTO>> paramMap = specParamDTOS.stream().collect(Collectors.groupingBy(SpecParamDTO::getGroupId));
        for (SpecGroupDTO specGroupDTO : specGroupDTOS) {
            specGroupDTO.setParams(paramMap.get(specGroupDTO.getId()));
        }
        return specGroupDTOS;
    }
}
