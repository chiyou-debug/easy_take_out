package com.easy.controller.admin;

import com.easy.result.Result;
import com.easy.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Api(tags = "Common API")
@RestController
@RequestMapping("/admin/common")
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    @ApiOperation("File Upload")
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
        log.info("File Upload: {}", file);
        // 1. Get the file extension
        String originalFilename = file.getOriginalFilename(); // 1.1.11.jpg
        String extName = originalFilename.substring(originalFilename.lastIndexOf("."));

        // 2. Build a new file name
        String objectName = UUID.randomUUID().toString() + extName;

        // 3. Call the utility class to upload the file
        String url = aliOssUtil.upload(file.getBytes(), objectName);
        return Result.success(url);
    }
}
