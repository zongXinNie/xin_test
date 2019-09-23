package com.leyou.search.service.impl;


import com.leyou.item.client.ItemClient;
import com.leyou.common.ExceptionEnum;
import com.leyou.dto.*;
import com.leyou.exception.LyException;
import com.leyou.pojo.PageResult;
import com.leyou.search.dto.GoodsDTO;
import com.leyou.search.dto.SearchRequest;
import com.leyou.search.pojo.Goods;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.SearchService;
import com.leyou.utils.BeanHelper;
import com.leyou.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilterBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Nie ZongXin
 * @date 2019/9/11 21:43
 */
@Service
@Slf4j
public class SearchServiceImpl implements SearchService {
    @Autowired
    private ItemClient itemClient;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private GoodsRepository goodsRepository;

    /**
     * 从数据库获取所有搜索相关数据
     *
     * @param spuDTO
     * @return
     */
    @Override
    public Goods buildGoods(SpuDTO spuDTO) {
        if (spuDTO == null) {
            throw new LyException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
//获取被搜索的信息  包含标题，分类，甚至品牌
//        获取分类
        String categoryName = spuDTO.getCategoryName();
        if (StringUtils.isBlank(categoryName)) {
            categoryName = itemClient.findCategoryByIds(spuDTO.getCategoryIds())
                    .stream().map(CategoryDTO::getName).collect(Collectors.joining());
        }
//        获取品牌名
        String brandName = spuDTO.getBrandName();
        if (StringUtils.isBlank(brandName)) {
            brandName = itemClient.findBrandById(spuDTO.getBrandId()).getName();
        }
        String all = spuDTO.getName() + categoryName + brandName;

        List<SkuDTO> skuList = spuDTO.getSkus();
        if (CollectionUtils.isEmpty(skuList)) {
            skuList = itemClient.findSkuBySpuId(spuDTO.getId());
        }
        List<Map<String, Object>> skus = new ArrayList<>();
        TreeSet<Double> priceSet = new TreeSet<>();
        for (SkuDTO skuDTO : skuList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", skuDTO.getId());
            map.put("title", skuDTO.getTitle());
            map.put("price", skuDTO.getPrice());
            map.put("image", skuDTO.getImages());
            priceSet.add(skuDTO.getPrice());
            skus.add(map);
        }
//规格参数名称为KEY，参数值为value
        Map<String, Object> specs = new HashMap<>();
        List<SpecParamDTO> specParam = itemClient.findSpecParamByGroupId(null, spuDTO.getCid3(), true);
        SpuDetailDTO spuDetail = spuDTO.getSpuDetail();
        if (spuDetail == null) {
            spuDetail = itemClient.findSpuDetailBySpuId(spuDTO.getId());
        }
//        取出通用属性，josn数据转map
        Map<Long, Object> genericSpec = JsonUtils.toMap(spuDetail.getGenericSpec(), Long.class, Object.class);
//        取出特有属性
        Map<Long, Object> specialSpec = JsonUtils.toMap(spuDetail.getSpecialSpec(), Long.class, Object.class);


        for (SpecParamDTO specParamDTO : specParam) {
            String key = specParamDTO.getName();
            Object value = specParamDTO.getGeneric()?genericSpec.get(specParamDTO.getId()):specialSpec.get(specParamDTO.getId());
//            判断是否为通用规格
          /*  if (specParamDTO.getGeneric()) {
                value = genericSpec.get(specParamDTO.getId());
            } else {
                value = specialSpec.get(specParamDTO.getId());
            }*/
//            判断参数是否为数字类型
            if (specParamDTO.getNumeric()) {
                value = chooseSegment(value, specParamDTO);
            }
            specs.put(key, value);
        }

        Goods goods = new Goods();
        goods.setAll(all);
        goods.setBrandId(spuDTO.getBrandId());
        goods.setCategoryId(spuDTO.getCid3());
        goods.setCreateTime(spuDTO.getCreateTime().getTime());
        goods.setId(spuDTO.getId());
        goods.setPrice(priceSet);
        goods.setSkus(JsonUtils.toString(skus));
        goods.setSpecs(specs);
        goods.setSubTitle(spuDTO.getSubTitle());


        return goods;
    }

    /**
     * 搜索
     *
     * @param searchRequest
     * @return
     */
    @Override
    public PageResult<GoodsDTO> search(SearchRequest searchRequest) {
        if (StringUtils.isBlank(searchRequest.getKey())) {
            throw new LyException(ExceptionEnum.INVALID_PARAM_ERROR);
        }

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "skus", "subTitle"}, null));
//        queryBuilder.withQuery(QueryBuilders.matchQuery("all", searchRequest.getKey()).operator(Operator.AND));
        queryBuilder.withQuery(buildBasicQuery(searchRequest));
//        if (descCommodity!=null&&descCommodity) {
//            queryBuilder.withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC));
//        }

        queryBuilder.withPageable(PageRequest.of(searchRequest.getPage() - 1, searchRequest.getSize()));
        AggregatedPage<Goods> goods = elasticsearchTemplate.queryForPage(queryBuilder.build(), Goods.class);

        return new PageResult<GoodsDTO>(goods.getTotalElements(),
                (long) goods.getTotalPages(),
                BeanHelper.copyWithCollection(goods.getContent(), GoodsDTO.class));
    }

    /**
     * 查询过滤项
     *
     * @param searchRequest
     * @return
     */
    @Override
    public Map<String, List<?>> queryFilter(SearchRequest searchRequest) {
        LinkedHashMap<String, List<?>> filterList = new LinkedHashMap<>();
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withQuery(buildBasicQuery(searchRequest));
        String categoryId = "categoryAdd";
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(categoryId).field("categoryId"));
        String brandId = "brandAgg";
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(brandId).field("brandId"));

        AggregatedPage<Goods> goods = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), Goods.class);
        Aggregations aggregations = goods.getAggregations();
        LongTerms cTerms = aggregations.get(categoryId);
        List<Long> categorys = handleCategory(cTerms, filterList);
//        分类等于1就查询
        if (categorys!=null&&categorys.size()==1){
          handleSpecAgg(categorys.get(0),filterList,searchRequest);
        }
        LongTerms bTerms = aggregations.get(brandId);
        handleBrandAgg(bTerms, filterList);
        return filterList;
    }

    /**
     * 根据ID删除文档
     * @param id
     */
    @Override
    public void deleteItemDoc(Long id) {
        if (id!=null){
            goodsRepository.deleteById(id);
        }

    }

    /**
     * 增加单个搜索商品
     * @param id
     */
    @Override
    public void addGoodsDoc(Long id) {
        if (id!=null){
            SpuDTO spuDTO = itemClient.findSpuById(id);
            Goods goods =buildGoods(spuDTO);
            goodsRepository.save(goods);
        }
    }

    //参数过滤
    private void handleSpecAgg(Long categoryId, LinkedHashMap<String, List<?>> filterList, SearchRequest searchRequest) {
        List<SpecParamDTO> specParams = itemClient.findSpecParamByGroupId(null, categoryId, true);
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withQuery(buildBasicQuery(searchRequest));
        nativeSearchQueryBuilder.withPageable(PageRequest.of(0,1));
//        显示空的source
        nativeSearchQueryBuilder.withSourceFilter(new FetchSourceFilterBuilder().build());
        for (SpecParamDTO specParam : specParams) {
            nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(specParam.getName()).field("specs."+specParam.getName()));
        }
        AggregatedPage<Goods> goods = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), Goods.class);
        Aggregations aggregations = goods.getAggregations();
        for (SpecParamDTO specParam : specParams) {
            Terms aggregation = aggregations.get(specParam.getName());
            List<String> paramValues = aggregation.getBuckets().stream()
                    .map(Terms.Bucket::getKeyAsString)
                    .filter(StringUtils::isNotEmpty)
                    .collect(Collectors.toList());
//            Collections.sort(paramValues); 无用
            filterList.put(specParam.getName(),paramValues);
        }
    }
//获取品牌id
    private void handleBrandAgg(LongTerms terms, Map<String, List<?>> filterList) {
        List<Long> idList = terms.getBuckets().stream()
                .map(LongTerms.Bucket::getKeyAsNumber)
                .map(Number::longValue)
                .collect(Collectors.toList());
        List<BrandDTO> brandByBrandIds = itemClient.findBrandByBrandIds(idList);
        filterList.put("品牌", brandByBrandIds);
    }
//获取分类id值
    private List<Long> handleCategory(LongTerms terms, Map<String, List<?>> filterList) {
        List<Long> idList = terms.getBuckets().stream()
                .map(LongTerms.Bucket::getKeyAsNumber)
                .map(Number::longValue)
                .collect(Collectors.toList());
        List<CategoryDTO> categoryByIds = itemClient.findCategoryByIds(idList);
        filterList.put("分类", categoryByIds);
        return idList;
    }
//搜索条件
    private QueryBuilder buildBasicQuery(SearchRequest searchRequest) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        boolQueryBuilder.must(QueryBuilders.matchQuery("all", searchRequest.getKey()).operator(Operator.AND));
     if (searchRequest.getFilter()!=null){
//         布尔查询
         Map<String, String> filters = searchRequest.getFilter();
         Set<Map.Entry<String, String>> entries = filters.entrySet();
         for (Map.Entry<String, String> entry : entries) {
             String key = entry.getKey();
             if ("分类".equals(key)){
                 key="categoryId";
             }else if ("品牌".equals(key)){
                 key="brandId";
             }else {
                 key="specs."+entry.getKey();
             }
             String value = entry.getValue();
//             过滤
             boolQueryBuilder.filter(QueryBuilders.termQuery(key,value));
         }

     }

        return boolQueryBuilder;
    }
//确定搜索范围
    private Object chooseSegment(Object value, SpecParamDTO specParamDTO) {
        if (value == null || StringUtils.isBlank(value.toString())) {
            return "其他";
        }
        double val = parseDouble(value);
        String result = "其他";
//        保存数值段
        for (String segment : specParamDTO.getSegments().split(",")) {
            String[] segs = segment.split("-");
//            log.debug(Arrays.toString(segs));
//            获取数值范围
            double begin = parseDouble(segs[0]); //1000.00
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = parseDouble(segs[1]);   //1500.00  1000
            }
//            判断是否在范围内
            if (val > begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + specParamDTO.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + specParamDTO.getUnit() + "以下";
                } else {
                    result = segment + specParamDTO.getUnit();
                }
                break;
            }
        }
        return result;
    }

    private double parseDouble(Object value) {
        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
