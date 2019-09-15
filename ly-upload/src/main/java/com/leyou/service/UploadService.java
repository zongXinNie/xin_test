package com.leyou.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface UploadService {
    /**
     * 上传图片
     * @param file
     * @return
     */
    String uploadImage(MultipartFile file);

    Map<String,Object> getSignature();
}
