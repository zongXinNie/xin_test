package com.leyou.service.impl;

import com.leyou.item.client.ItemClient;
import com.leyou.common.ExceptionEnum;
import com.leyou.dto.BrandDTO;
import com.leyou.dto.CategoryDTO;
import com.leyou.dto.SpecGroupDTO;
import com.leyou.dto.SpuDTO;
import com.leyou.exception.LyException;
import com.leyou.service.PageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Nie ZongXin
 * @date 2019/9/15 23:01
 */
@Service
@Slf4j
public class PageServiceImpl implements PageService {
    @Autowired
    private ItemClient itemClient;
    @Value("${ly.static.itemDir}")
    private String itemDir;
    @Value("${ly.static.itemTemplate}")
    private String itemTemplate;
    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 根据商品id查询商品详细信息
     *
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> loadItemData(Long id) {


        SpuDTO spuDTO = itemClient.findSpuById(id);
        if (spuDTO == null) {
            throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
        }
//        分类信息
        List<CategoryDTO> categoryies = itemClient.findCategoryByIds(spuDTO.getCategoryIds());
//        品牌信息
        BrandDTO brand = itemClient.findBrandById(spuDTO.getBrandId());
        List<SpecGroupDTO> specGroups = itemClient.findSpecGroupByCid(spuDTO.getCid3());

        Map<String, Object> loadItemData = new HashMap<>(0);
        loadItemData.put("categories", categoryies);
        loadItemData.put("brand", brand);
        loadItemData.put("spuName", spuDTO.getName());
        loadItemData.put("subTitle", spuDTO.getSubTitle());
        loadItemData.put("detail", spuDTO.getSpuDetail());
        loadItemData.put("skus", spuDTO.getSkus());
        loadItemData.put("specs", specGroups);


        return loadItemData;
    }

    @Override
    public void createItemHtml(Long id) {
//获取上下文
        Context context = new Context();
//    保存模型数据
        context.setVariables(loadItemData(id));
//准备文件路径
        File dir = new File(itemDir);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                log.error("【静态页服务】创建静态页面目录失败，目录地址:{}", dir.getAbsolutePath());
                throw new LyException(ExceptionEnum.DIRECTORY_WRITER_ERROR);
            }
        }
        File filePath = new File(itemDir, id + ".html");
        try (PrintWriter writer = new PrintWriter(filePath, "UTF-8")) {
            templateEngine.process(itemTemplate, context, writer);
        } catch (IOException e) {
            log.error("【静态页服务】静态页面生成失败,商品id：{}", id, e);
            throw new LyException(ExceptionEnum.FILE_WRITER_ERROR);
        }

    }

    /**
     * 删除html
     *
     * @param id
     */
    @Override
    public void deleteItemHtml(Long id) {
        if (id != null) {
            File file = new File(itemDir, id + ".html");

//    private StringRedisTemplate redisTemplate;
//            生命时长设置为0；
//            redisTemplate.boundValueOps(id.toString()).expire(0, TimeUnit.SECONDS);

            if (file.exists()) {
                if (!file.delete()) {
                    log.error("【静态页服务】静态页删除失败，商品id：{}", id);
                    throw new LyException(ExceptionEnum.DELETE_OPERATION_FAIL);
                }
            }
        }
    }

}
