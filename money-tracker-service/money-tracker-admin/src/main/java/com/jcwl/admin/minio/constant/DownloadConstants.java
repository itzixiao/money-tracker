package com.jcwl.admin.minio.constant;

/**
 * 文件下载常量
 */
public class DownloadConstants {
    public static final int BUFFER_SIZE = 1024;
    public static final int CONNECTION_TIMEOUT_MS = 6 * 1000;
    public static final String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)";
    public static final String PREFIX = "file";
    public static final String SUFFIX = ".pdf";
    public static final String FILE_NAME = "导出文件.zip";
}
