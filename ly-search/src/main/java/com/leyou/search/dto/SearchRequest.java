package com.leyou.search.dto;

import java.util.Map;

/**
 * @author Nie ZongXin
 * @date 2019/9/12 10:12
 */
public class SearchRequest {
    private String key;
    private Integer page;
    private Map<String,String> filter;
    private static final Integer DEFAULT_SIZE=20; //固定页面值，不从页面接收
    private static final Integer DEFAULT_PAGE=1;

    public Map<String, String> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, String> filter) {
        this.filter = filter;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPage() {
        if (page==null){
            return DEFAULT_PAGE;
        }
//        效验，页面值不能小于1
        return Math.max(DEFAULT_PAGE,page);
    }

    public void setPage(Integer page) {

        this.page = page;
    }

    public Integer getSize() {
        return DEFAULT_SIZE;
    }


}
