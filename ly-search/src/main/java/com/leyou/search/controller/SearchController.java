package com.leyou.search.controller;

import com.leyou.pojo.PageResult;
import com.leyou.search.dto.GoodsDTO;
import com.leyou.search.dto.SearchRequest;
import com.leyou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author Nie ZongXin
 * @date 2019/9/12 10:08
 */
@RestController
public class SearchController {
    @Autowired
    private SearchService searchService;

    /**
     * 搜索
     *
     * @param searchRequest
     * @return
     */
    @PostMapping("page")
    public ResponseEntity<PageResult<GoodsDTO>> search(@RequestBody SearchRequest searchRequest) {

        return ResponseEntity.ok(searchService.search(searchRequest));
    }

    /**
     * 过滤条件查询
     *
     * @return
     */
    @PostMapping("filter")
    public ResponseEntity<Map<String, List<?>>> queryFilter(@RequestBody SearchRequest searchRequest) {
        return ResponseEntity.ok(searchService.queryFilter(searchRequest));
    }


}
