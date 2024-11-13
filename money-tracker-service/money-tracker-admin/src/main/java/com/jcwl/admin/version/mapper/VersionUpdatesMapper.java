package com.jcwl.admin.version.mapper;

import com.jcwl.admin.version.domain.VersionUpdates;
import com.jcwl.admin.version.dto.VersionUpdatesQueryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 版本控制Mapper接口
 *
 * @author jcwl
 * @date 2024-08-21
 */
public interface VersionUpdatesMapper {
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
    public int insert(VersionUpdates versionUpdates);

    /**
     * 批量新增版本控制
     */
    public int insertBatch(@Param("list") List<VersionUpdates> versionUpdatesList);

    /**
     * 修改版本控制
     */
    public int update(VersionUpdates versionUpdates);

    /**
     * 批量修改版本控制
     */
    int updateBatch(@Param("list") List<VersionUpdates> versionUpdatesList);

    /**
     * 删除版本控制
     */
    public int deleteById(Long id);

    /**
     * 批量删除版本控制
     */
    public int deleteByIds(Long[] ids);

}
