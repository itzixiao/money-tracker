package com.jcwl.admin.version.service.impl;

import com.jcwl.admin.minio.domain.MinioFile;
import com.jcwl.admin.minio.service.MinioService;
import com.jcwl.admin.version.domain.VersionUpdates;
import com.jcwl.admin.version.dto.VersionUpdatesQueryDTO;
import com.jcwl.admin.version.mapper.VersionUpdatesMapper;
import com.jcwl.admin.version.service.IVersionUpdatesService;
import com.jcwl.common.core.domain.R;
import com.jcwl.common.exception.BizPreconditions;
import com.jcwl.common.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 版本控制Service业务层处理
 *
 * @author jcwl
 * @date 2024-08-21
 */
@Service
public class VersionUpdatesServiceImpl implements IVersionUpdatesService {
    @Autowired
    private VersionUpdatesMapper versionUpdatesMapper;
    @Autowired
    private MinioService minioService;

    /**
     * 查询版本控制
     */
    @Override
    public VersionUpdates selectById(Long id) {
        return versionUpdatesMapper.selectById(id);
    }

    /**
     * 查询版本控制列表
     */
    @Override
    public List<VersionUpdates> selectList(VersionUpdatesQueryDTO versionUpdates) {
        return versionUpdatesMapper.selectList(versionUpdates);
    }

    /**
     * 新增版本控制
     */
    @Override
    public R<Boolean> insert(VersionUpdates versionUpdates) {
        int insert = versionUpdatesMapper.insert(versionUpdates);
        return R.ok(insert > 0);
    }

    /**
     * 修改版本控制
     */
    @Override
    public R<Boolean> update(VersionUpdates versionUpdates) {
        BizPreconditions.ifFalse(canEditOrDelete(versionUpdates.getId()), "版本控制不可编辑！");
        int update = versionUpdatesMapper.update(versionUpdates);
        return R.ok(update > 0);
    }

    /**
     * 批量删除版本控制
     */
    @Override
    public R<Boolean> deleteByIds(Long[] ids) {
        BizPreconditions.ifFalse(canEditOrDelete(ids), "版本控制不可删除！");
        int delete = versionUpdatesMapper.deleteByIds(ids);
        return R.ok(delete > 0);
    }

    /**
     * 删除版本控制信息
     */
    @Override
    public R<Boolean> deleteById(Long id) {
        BizPreconditions.ifFalse(canEditOrDelete(id), "版本控制不可删除！");
        int delete = versionUpdatesMapper.deleteById(id);
        return R.ok(delete > 0);
    }

    @Override
    public R importExcel(List<VersionUpdates> versionUpdatesList) {
        int insert = versionUpdatesMapper.insertBatch(versionUpdatesList);
        return R.ok(insert > 0);
    }

    @Override
    public R<VersionUpdates> fileUploadVersion(MultipartFile file, VersionUpdates version) {
        MinioFile minioFile = minioService.putObject(file);
        if (minioFile != null) {
            // versionUpdatesMapper.deleteByFileName(version.getFileName());
            version.setMinioFileName(minioFile.getName());
            version.setUrl(minioFile.getUrl());
            version.setCreateTime(DateUtils.getNowDate());
            version.setDelFlag("0");
            versionUpdatesMapper.insert(version);
        }
        return R.ok(version);
    }


    @Override
    public R<VersionUpdates> queryFileVersion(VersionUpdates updates) {
        VersionUpdatesQueryDTO versionUpdateDto = new VersionUpdatesQueryDTO();
        BeanUtils.copyProperties(updates, versionUpdateDto);
        List<VersionUpdates> versionUpdates = versionUpdatesMapper.selectList(versionUpdateDto);
        if (CollectionUtils.isEmpty(versionUpdates)) {
            return R.ok(new VersionUpdates());
        }
        return R.ok(versionUpdates.get(0));
    }


    /**
     * 判断版本控制是否可以编辑删除
     */
    private Boolean canEditOrDelete(Long... ids) {
        return true;
    }
}
