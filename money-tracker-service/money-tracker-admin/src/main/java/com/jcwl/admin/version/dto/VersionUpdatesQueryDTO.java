package com.jcwl.admin.version.dto;

import com.jcwl.common.core.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 版本控制对象 version_updates
 *
 * @author jcwl
 * @date 2024-08-27
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VersionUpdatesQueryDTO extends PageDomain{
    private static final long serialVersionUID = 1L;

    /**
     * 导出文件名
     */
    @ApiModelProperty("导出文件名")
    private String exportFileName;


    /**
     * 版本号
     */
    @ApiModelProperty("版本号")
    private String version;

    /**
     * url路径
     */
    @ApiModelProperty("url路径")
    private String url;

    /**
     * 文件名称
     */
    @ApiModelProperty("文件名称")
    private String fileName;

    /**
     * 上传minio文件名称
     */
    @ApiModelProperty("上传minio文件名称")
    private String minioFileName;

    /**
     * 文件大小
     */
    @ApiModelProperty("文件大小")
    private String size;

    /**
     * 修复内容
     */
    @ApiModelProperty("修复内容")
    private String fixContent;

    /**
     * 备注
     */
    @ApiModelProperty("备注 ")
    private String remark;
}
