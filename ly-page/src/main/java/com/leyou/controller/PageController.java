package com.leyou.controller;

import com.leyou.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Nie ZongXin
 * @date 2019/9/15 21:20
 */
@Controller
public class PageController {
    @Autowired
    private PageService pageService;
    /**
     * 跳到商品详情页
     * @param model
     * @param id
     * @return
     */
    @GetMapping("item/{id}.html")
    public String toItPage(Model model,@PathVariable("id") Long id){
        Map<String, Object> loadItemData = pageService.loadItemData(id);
        model.addAllAttributes(loadItemData);
        return "item";
    }
}
