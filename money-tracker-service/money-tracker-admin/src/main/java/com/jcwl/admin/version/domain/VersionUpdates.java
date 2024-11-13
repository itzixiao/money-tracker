package com.jcwl.admin.version.domain;

import com.jcwl.common.annotation.Excel;
import com.jcwl.common.core.domain.JcwlBaseEntity;
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
public class VersionUpdates extends JcwlBaseEntity{
    private static final long serialVersionUID = 1L;


    /**
     * id
     */
    @ApiModelProperty("id")
    private Long id;

    /**
     * 版本号
     */
    @ApiModelProperty("版本号")
    @Excel(name = "版本号")
    private String version;

    /**
     * url路径
     */
    @ApiModelProperty("url路径")
    @Excel(name = "url路径")
    private String url;

    /**
     * 文件名称
     */
    @ApiModelProperty("文件名称")
    @Excel(name = "文件名称")
    private String fileName;

    /**
     * 上传minio文件名称
     */
    @ApiModelProperty("上传minio文件名称")
    @Excel(name = "上传minio文件名称")
    private String minioFileName;

    /**
     * 文件大小
     */
    @ApiModelProperty("文件大小")
    @Excel(name = "文件大小")
    private String size;

    /**
     * 修复内容
     */
    @ApiModelProperty("修复内容")
    @Excel(name = "修复内容")
    private String fixContent;

    /**
     * 备注
     */
    @ApiModelProperty("备注 ")
    @Excel(name = "备注 ")
    private String remark;

}
