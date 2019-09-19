package com.leyou.controller;

import com.leyou.dto.SpecGroupDTO;
import com.leyou.dto.SpecParamDTO;
import com.leyou.entity.SpecGroup;
import com.leyou.service.SpecGroupService;
import com.leyou.service.SpecParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Nie ZongXin
 * @date 2019/9/9 22:32
 */
@RestController
@RequestMapping("spec")
public class SpecGroupController {
    @Autowired
    private SpecGroupService specGroupService;
    @Autowired
    private SpecParamService specParamService;

    /**
     * 根据分类查询规格参数组
     *
     * @param id
     * @return
     */
    @GetMapping("groups/of/category")
    public ResponseEntity<List<SpecGroupDTO>> findSpecGroupByCid(@RequestParam("id") Long id) {
        return ResponseEntity.ok(specGroupService.findSpecGroupByCid(id));
    }

    @GetMapping("params")
    public ResponseEntity<List<SpecParamDTO>> findSpecParamByGroupId(@RequestParam(value = "gid", required = false) Long gid,
                                                                     @RequestParam(value = "cid", required = false) Long cid,
                                                                     @RequestParam(value = "searching", required = false) Boolean searching) {
        return ResponseEntity.ok(specParamService.findSpecParamByGroupId(gid, cid,searching));
    }

}
