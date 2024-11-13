package com.jcwl.admin.minio.controller;

import com.jcwl.admin.minio.domain.DownloadDTO;
import com.jcwl.admin.minio.domain.MinioFile;
import com.jcwl.admin.minio.service.MinioService;
import com.jcwl.common.annotation.Anonymous;
import com.jcwl.common.annotation.Log;
import com.jcwl.common.core.domain.AjaxResult;
import com.jcwl.common.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * 文件管理
 *
 * @author jcwl
 * @date 2023-12-02
 */
@Api(tags = "文件管理")
@RestController
@RequestMapping("/minio")
@Slf4j
public class MinioController {

    @Autowired
    private MinioService minioService;

    @PostMapping("/upload/file")
    @ApiOperation(value = "文件上传")
    public AjaxResult uploadFile(@RequestPart @ApiParam(name = "file", value = "file", required = true) MultipartFile file) {
        MinioFile minioFile = minioService.putObject(file);
        return AjaxResult.success(minioFile);
    }

    @Anonymous
    @ApiOperation("下载文件")
    @GetMapping(value = "/download/{objectName}/**")
    public void download(HttpServletRequest request, HttpServletResponse response, @PathVariable("objectName") String objectName) {
        log.info("下载文件: {}", objectName);
        objectName = replaceFileName(request, objectName);
        log.info("下载文件replaceFileName : {}", objectName);
        try {
            InputStream in = minioService.getObject(objectName);
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(objectName, "UTF-8"));
            response.setCharacterEncoding("UTF-8");
            // response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            // response.setHeader("Content-Disposition", "attachment; filename=\"" + "aaaaaaa" + "\"");
            IOUtils.copy(in, response.getOutputStream());
            in.close();
        } catch (IOException e) {
            log.error("下载文件失败: {}", e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Anonymous
    @ApiOperation("根据文件下载地址批量下载文件为压缩包")
    @Log(title = "下载文件", businessType = BusinessType.EXPORT)
    @PostMapping("/download/batch")
    public void downloadPackage(HttpServletRequest request, HttpServletResponse response, @RequestBody DownloadDTO downloadDTO) {
        minioService.downloadPackage(request, response, downloadDTO);
    }

    @Anonymous
    @ApiOperation("预览图片")
    @GetMapping(value = "/preview/{objectName}/**")
    public void previewPicture(HttpServletRequest request, HttpServletResponse response, @PathVariable("objectName") String objectName) throws IOException {
        objectName = replaceFileName(request, objectName);
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            response.setContentType("image/jpeg;charset=utf-8");
            inputStream = minioService.getObject(objectName);
            outputStream = response.getOutputStream();

            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            response.flushBuffer();
        } catch (IOException e) {
            log.error("预览图片失败" + e.getMessage());
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    private String replaceFileName(HttpServletRequest request, String objectName) {
        StringBuilder sb = new StringBuilder();
        String pathWithinHandlerMapping = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
        String bestMatchingPattern = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString();
        String arguments = new AntPathMatcher().extractPathWithinPattern(bestMatchingPattern, pathWithinHandlerMapping);
        if (!arguments.isEmpty()) {
            sb.append(objectName);
            sb.append('/');
            sb.append(arguments);
        } else {
            sb.append(objectName);
        }
        return sb.toString();
    }
}
