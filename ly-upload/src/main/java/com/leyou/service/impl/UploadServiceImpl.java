package com.leyou.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.leyou.common.ExceptionEnum;
import com.leyou.config.OSSProperties;
import com.leyou.exception.LyException;
import com.leyou.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.*;


/**
 * @author Nie ZongXin
 * @date 2019/9/8 10:40
 */
@Service
@Slf4j
public class UploadServiceImpl implements UploadService {

    private static final String IMAGE_URL = "http://image.leyou.com/images/";
    private static final String IMAGE_DIR = "D:\\nginx-1.12.2\\html\\images";
    private static final List<String> ALLOW_IMAGE_TYPES = Arrays.asList("image/jpeg", "image/png", "image/bmp");
    @Autowired
    private OSSProperties ossProperties;
    @Autowired
    private OSS client;

    @Override
    public String uploadImage(MultipartFile file) {

        try {
            if (file == null) {
                log.error("文件为空");
                throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
            }
            String contentType = file.getContentType();
            log.debug(contentType);
            if (!ALLOW_IMAGE_TYPES.contains(contentType)) {
                log.error("文件类型不符");
                throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
            }
            String filename = file.getOriginalFilename();
            String suffix = StringUtils.substringAfter(filename, ".");

            filename = UUID.randomUUID().toString() + "." + suffix;
            File filePath = new File(IMAGE_DIR);
            if (!filePath.isDirectory()) {
                filePath.mkdirs();
            }
//            int i = 1 / 0;
            file.transferTo(new File(filePath, filename));

            return IMAGE_URL + filename;
        } catch (IOException e) {
            log.error("uploadImage IOException文件保存失败", e);
            throw new LyException(ExceptionEnum.FILE_UPLOAD_ERROR);
        } catch (RuntimeException e) {
            log.error("运行时异常", e);
            throw new RuntimeException(e);
//            throw new LyException(ExceptionEnum.FILE_UPLOAD_ERROR,e);
        }

    }

    @Override
    public Map<String, Object> getSignature() {

        try {
            long expireTime = ossProperties.getExpireTime();
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, ossProperties.getMaxFileSize());
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, ossProperties.getDir());

            String postPolicy = client.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = client.calculatePostSignature(postPolicy);

            Map<String, Object> respMap = new LinkedHashMap<>();
            respMap.put("accessId", ossProperties.getAccessKeyId());
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", ossProperties.getDir());
            respMap.put("host", ossProperties.getHost());
            respMap.put("expire", expireEndTime);
            // respMap.put("expire", formatISO8601Date(expiration));

        return respMap;

        } catch (Exception e) {
            // Assert.fail(e.getMessage());
            throw new LyException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }

    }
}
