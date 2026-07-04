package com.bugucloud.common.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLConnection;

/**
 * 功能描述:
 *
 * @author achen
 * @version 1.0
 * @date 2026/6/2 - 23:42
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class OssUtil {

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.access-key-id}")
    private String accessKeyId;

    @Value("${aliyun.oss.access-key-secret}")
    private String accessKeySecret;

    @Value("${aliyun.oss.bucket-name}")
    private String bucketName;

    @Value("${aliyun.oss.custom-domain}")
    private String customDomain;

    private OSS getOssClient() {
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 上传文件到阿里云 OSS
     * @param ossKey OSS存储路径（如：articles/123/xxx.jpg）
     * @param file 文件
     * @return 文件访问URL
     */
    public String uploadFile(String ossKey, MultipartFile file) throws IOException {
        OSS ossClient = getOssClient();
        try {
            // 设置文件元数据
            ObjectMetadata metadata = new ObjectMetadata();
            String contentType = URLConnection.guessContentTypeFromName(file.getOriginalFilename());
            if (contentType != null) {
                metadata.setContentType(contentType);
            } else {
                metadata.setContentType("application/octet-stream");
            }
            metadata.setContentLength(file.getSize());

            // 上传文件到OSS
            ossClient.putObject(bucketName, ossKey, file.getInputStream(), metadata);

            // 直接使用自定义域名拼接URL
            String fileUrl = customDomain + "/" + ossKey;

            log.info("文件上传成功, URL路径: {}", fileUrl);
            return fileUrl;
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 删除文件（从OSS删除）
     */
    public void deleteFile(String ossKey) {
        OSS ossClient = getOssClient();
        try {
            ossClient.deleteObject(bucketName, ossKey);
            log.info("文件从OSS删除成功, OSS路径: {}", ossKey);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
