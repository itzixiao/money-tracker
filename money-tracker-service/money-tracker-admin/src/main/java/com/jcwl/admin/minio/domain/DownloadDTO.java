package com.jcwl.admin.minio.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 根据文件下载地址批量下载文件为压缩包请求体
 *
 * @author jcwl
 * @date 2024-05-24
 */
@Data
public class DownloadDTO {

    @ApiModelProperty("输出文件名称")
    private String fileName;

    @ApiModelProperty("下载地址前缀")
    private String prefix;

    @ApiModelProperty("下载文件名称列表")
    private String[] urls;

}
