package com.jcwl.admin.minio.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 文件对象
 *
 * @author jcwl
 * @date 2023-12-02
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MinioFile implements Serializable {

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件地址
     */
    private String url;
}
