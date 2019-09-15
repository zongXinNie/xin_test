package com.leyou.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author Nie ZongXin
 * @date 2019/9/11 11:28
 */
@Data
public class PageResult<T> {
    private Long total;
    private Long totalPage;
    private List<T> items;

    public PageResult() {
    }

    public PageResult(Long total, List<T> items) {
        this.total = total;
        this.items = items;
    }

    public PageResult(Long total, Long totalPage, List<T> items) {
        this.total = total;
        this.totalPage = totalPage;
        this.items = items;
    }
}
