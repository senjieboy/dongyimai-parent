package com.senjie.sellergoods.controller;

import com.senjie.entity.Result;
import com.senjie.utils.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author SenJie
 * @Data 2021/4/7 15:23
 */
@RestController
public class UploadController {
    @Value("${FLE_SERVER_URL}")
    private String FLE_SERVER_URL;

    @RequestMapping("/upload")
    public Result upload (MultipartFile file) {
        //获取文件的原始名
        String originalFilename = file.getOriginalFilename();
        //获取文件后缀名
        String exName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        try {
            FastDFSClient client = new FastDFSClient("classpath:config/fastdfs_client.conf");
            String path = client.uploadFile(file.getBytes(), exName);
            String url = FLE_SERVER_URL + path;
            return new Result(true, url);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "Upload fail ");
        }
    }
}
