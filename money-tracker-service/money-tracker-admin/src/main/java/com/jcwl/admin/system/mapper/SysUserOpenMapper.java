package com.jcwl.admin.system.mapper;

import com.jcwl.admin.system.domain.SysUserOpen;
import com.jcwl.admin.system.dto.SysUserOpenQueryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户和第三方登录关联Mapper接口
 *
 * @author jcwl
 * @date 2024-10-15
 */
public interface SysUserOpenMapper {

    /**
     * 查询用户和第三方登录关联
     */
    public SysUserOpen selectByUserId(Long userId);

    /**
     * 查询用户和第三方登录关联
     */
    public SysUserOpen selectByOpenId(String openId);

    /**
     * 查询用户和第三方登录关联列表
     */
    public List<SysUserOpen> selectList(SysUserOpenQueryDTO sysUserOpen);

    /**
     * 新增用户和第三方登录关联
     */
    public int insert(SysUserOpen sysUserOpen);

    /**
     * 批量新增用户和第三方登录关联
     */
    public int insertBatch(@Param("list") List<SysUserOpen> sysUserOpenList);

    /**
     * 修改用户和第三方登录关联
     */
    public int update(SysUserOpen sysUserOpen);

    /**
     * 批量修改用户和第三方登录关联
     */
    int updateBatch(@Param("list") List<SysUserOpen> sysUserOpenList);

    /**
     * 删除用户和第三方登录关联
     */
    public int deleteByUserId(Long userId);

    /**
     * 批量删除用户和第三方登录关联
     */
    public int deleteByUserIds(Long[] userIds);
}
