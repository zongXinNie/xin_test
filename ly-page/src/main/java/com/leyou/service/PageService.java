package com.leyou.service;

import java.util.Map;

public interface PageService {

    Map<String, Object> loadItemData(Long id);

    void createItemHtml(Long id);

    void deleteItemHtml(Long id);
}
