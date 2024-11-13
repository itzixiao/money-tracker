package com.jcwl.admin.version.service;

import com.jcwl.admin.version.domain.VersionUpdates;
import com.jcwl.admin.version.dto.VersionUpdatesQueryDTO;
import com.jcwl.common.core.domain.R;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 版本控制Service接口
 *
 * @author jcwl
 * @date 2024-08-21
 */
public interface IVersionUpdatesService {
    /**
     * 查询版本控制
     */
    public VersionUpdates selectById(Long id);

    /**
     * 查询版本控制列表
     */
    public List<VersionUpdates> selectList(VersionUpdatesQueryDTO versionUpdates);

    /**
     * 新增版本控制
     */
    public R insert(VersionUpdates versionUpdates);

    /**
     * 修改版本控制
     */
    public R update(VersionUpdates versionUpdates);

    /**
     * 批量删除版本控制
     */
    public R deleteByIds(Long[] ids);

    /**
     * 删除版本控制信息
     */
    public R deleteById(Long id);

    /**
     * 导入版本控制列表
     */
    R importExcel(List<VersionUpdates> versionUpdatesList);

    /**
     * 上传文件版本
     */
    R<VersionUpdates> fileUploadVersion(MultipartFile file, VersionUpdates version);


    /**
     *  查询文件版本
     */
    R<VersionUpdates> queryFileVersion(VersionUpdates versionUpdates);
}
