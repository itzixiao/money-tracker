package com.jcwl.admin.minio.service;

import com.jcwl.admin.minio.constant.DownloadConstants;
import com.jcwl.admin.minio.domain.DownloadDTO;
import com.jcwl.admin.minio.domain.MinioFile;
import com.jcwl.common.exception.BizPreconditions;
import com.jcwl.common.exception.ServiceException;
import com.jcwl.common.utils.StringUtils;
import com.jcwl.common.utils.file.FileUploadUtils;
import com.jcwl.common.utils.file.FileUtils;
import io.minio.*;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件管理服务
 *
 * @author jcwl
 * @date 2023-12-02
 */
@Slf4j
@Data
@Configuration
public class MinioService implements InitializingBean {

    /**
     * MinIO的API地址
     */
    @Value("${minio.endpoint}")
    private String endpoint;

    /**
     * 用户名
     */
    @Value("${minio.access-key}")
    private String accessKey;

    /**
     * 密钥
     */
    @Value("${minio.secret-key}")
    private String secretKey;

    /**
     * 存储桶名称
     */
    @Value("${minio.bucket-name}")
    private String bucketName;

    /**
     * 端口
     */
    @Value("${server.port}")
    private String serverPort;

    /**
     * 应用的访问路径
     */
    @Value("${server.servlet.context-path}")
    private String contextPath;

    private MinioClient minioClient;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Initializing MinioClient...{}",endpoint);
        this.minioClient = MinioClient.builder()
                //.endpoint(endpoint, 443, true)
                .endpoint(endpoint).credentials(accessKey, secretKey).build();
        if (!bucketExists(bucketName)) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(bucketName)
                            .build());
            log.info("minioClient makeBucket:{} success", bucketName);
        }
    }

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    public MinioFile putObject(MultipartFile file) {
        MinioFile minioFile = new MinioFile();
        String fileName = FileUploadUtils.extractFilename(file);
        try (InputStream inputStream = file.getInputStream()) {
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build();
            minioClient.putObject(args);
        } catch (Exception e) {
            log.error("上传文件失败:" + e.getMessage(), e);
            throw new ServiceException("上传文件失败");
        }
        minioFile.setFileName(fileName);
        minioFile.setName(fileName);
        String host = null;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("无法获取本地主机名或IP地址！");
        }
        String url = StringUtils.format("http://{}:{}{}/minio/download/{}", host, serverPort, "/".equals(contextPath) ? "" : contextPath, fileName);

        minioFile.setUrl(url);
        return minioFile;
    }

    /**
     * 以流的形式获取一个文件对象
     *
     * @param objectName 存储桶里的对象名称
     * @return
     */
    @SneakyThrows
    public InputStream getObject(String objectName) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            StatObjectResponse statObject = statObject(bucketName, objectName);
            if (statObject != null && statObject.size() > 0) {
                InputStream stream =
                        minioClient.getObject(
                                GetObjectArgs.builder()
                                        .bucket(bucketName)
                                        .object(objectName)
                                        .build());
                return stream;
            }
        }
        return null;
    }

    /**
     * 获取对象的元数据
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @return
     */
    @SneakyThrows
    public StatObjectResponse statObject(String bucketName, String objectName) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            StatObjectResponse stat =
                    minioClient.statObject(
                            StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
            return stat;
        }
        return null;
    }

    /**
     * 检查存储桶是否存在
     *
     * @param bucketName 存储桶名称
     * @return
     */
    @SneakyThrows
    public boolean bucketExists(String bucketName) {
        boolean found =
                minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            log.info("bucketName:{} does not exist", bucketName);
        }
        return found;
    }


    public void downloadPackage(HttpServletRequest request, HttpServletResponse response, DownloadDTO downloadDTO) {
        // 下载地址前缀
        String prefix = downloadDTO.getPrefix();
        // 下载文件名称
        String[] urls = downloadDTO.getUrls();
        // 设置压缩包的名字
        String downloadName = StringUtils.isNotBlank(downloadDTO.getFileName())
                ? downloadDTO.getFileName() + ".zip"
                : DownloadConstants.FILE_NAME;
        // 响应头的设置
        setDownloadHeader(request, response, downloadName);

        // 设置压缩流：直接写入response，实现边压缩边下载
        try (ZipOutputStream zipos = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()))) {
            zipos.setMethod(ZipOutputStream.DEFLATED);

            for (String url : urls) {
                if (StringUtils.isBlank(url)) {
                    continue;
                }
                File file = getFileByUrl(prefix + url, DownloadConstants.SUFFIX);
                try (InputStream fis = Files.newInputStream(file.toPath())) {
                    addFileToZip(zipos, url, fis);
                } catch (IOException e) {
                    log.error("添加文件到压缩包失败: urls={}, msg={}", url, e.getMessage());
                }
            }
        } catch (IOException e) {
            log.error("创建压缩流失败: {}", e.getMessage(), e);
        }
    }

    private void setDownloadHeader(HttpServletRequest request, HttpServletResponse response, String downloadName) {
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(downloadName, "UTF-8"));
        } catch (IOException e) {
            log.error("下载文件失败: {}", e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    private void addFileToZip(ZipOutputStream zipos, String entryName, InputStream fis) throws IOException {
        zipos.putNextEntry(new ZipEntry(entryName));
        byte[] buffer = new byte[DownloadConstants.BUFFER_SIZE];
        int length;
        while ((length = fis.read(buffer)) != -1) {
            zipos.write(buffer, 0, length);
        }
        zipos.closeEntry();
    }

    private File getFileByUrl(String fileUrl, String suffix) {
        try (ByteArrayOutputStream outStream = new ByteArrayOutputStream();
             BufferedOutputStream stream = new BufferedOutputStream(outStream)) {
            HttpURLConnection conn = openConnectionWithTimeout(fileUrl);

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                BizPreconditions.thrException(conn.getResponseCode(), "获取文件失败" + fileUrl);
            }
            try (InputStream inputStream = conn.getInputStream()) {
                byte[] buffer = new byte[DownloadConstants.BUFFER_SIZE];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    stream.write(buffer, 0, len);
                }
            }
            File file = File.createTempFile(DownloadConstants.PREFIX, suffix);
            try (FileOutputStream fileOutputStream = new FileOutputStream(file);
                 BufferedOutputStream fileStream = new BufferedOutputStream(fileOutputStream)) {
                fileStream.write(outStream.toByteArray());
            }
            return file;
        } catch (Exception e) {
            log.error("从URL获取文件失败,fileUrl:{}, msg:{}", fileUrl, e.getMessage());
            throw new RuntimeException("从URL获取文件失败", e);
        }
    }

    private HttpURLConnection openConnectionWithTimeout(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(DownloadConstants.CONNECTION_TIMEOUT_MS);
        conn.setRequestProperty("User-Agent", DownloadConstants.USER_AGENT);
        return conn;
    }

}
