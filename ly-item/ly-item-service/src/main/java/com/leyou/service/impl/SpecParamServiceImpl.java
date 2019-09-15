package com.leyou.service.impl;


import com.leyou.common.ExceptionEnum;
import com.leyou.dto.SpecParamDTO;
import com.leyou.entity.SpecGroup;
import com.leyou.entity.SpecParam;
import com.leyou.exception.LyException;
import com.leyou.mapper.SpecParamMapper;
import com.leyou.service.SpecParamService;
import com.leyou.utils.BeanHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.leyou.common.ExceptionEnum.PRARM_NOT_FOUND;

/**
 * @author Nie ZongXin
 * @date 2019/9/9 22:52
 */
@Service
@Transactional
public class SpecParamServiceImpl implements SpecParamService {
    @Autowired
    private SpecParamMapper specParamMapper;

    /**
     * 根据规格分组id查询规格参数
     * @param groupId
     * @return
     */
    @Override
    public List<SpecParamDTO> findSpecParamByGroupId(Long groupId,Long categoryId,Boolean searching) {
        if (groupId == null&&categoryId==null&&searching==null) {
            throw new LyException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(groupId);
        specParam.setCid(categoryId);
        specParam.setSearching(searching);
        List<SpecParam> specParams = specParamMapper.select(specParam);
        if (CollectionUtils.isEmpty(specParams)) {
            throw new LyException(ExceptionEnum.PRARM_NOT_FOUND);
        }

        return BeanHelper.copyWithCollection(specParams, SpecParamDTO.class);
    }
}
