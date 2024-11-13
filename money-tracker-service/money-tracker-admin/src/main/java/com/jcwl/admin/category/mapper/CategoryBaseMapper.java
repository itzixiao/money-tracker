package com.jcwl.admin.category.mapper;

import com.jcwl.admin.category.domain.CategoryBase;
import com.jcwl.admin.category.dto.CategoryBaseQueryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分类基础Mapper接口
 *
 * @author jcwl
 * @date 2024-10-23
 */
public interface CategoryBaseMapper {
    /**
     * 查询分类基础
     */
    public CategoryBase selectByCategoryBaseId(Long categoryBaseId);

    /**
     * 查询分类基础列表
     */
    public List<CategoryBase> selectList(CategoryBaseQueryDTO categoryBase);

    /**
     * 新增分类基础
     */
    public int insert(CategoryBase categoryBase);

    /**
     * 批量新增分类基础
     */
    public int insertBatch(@Param("list") List<CategoryBase> categoryBaseList);

    /**
     * 修改分类基础
     */
    public int update(CategoryBase categoryBase);

    /**
     * 批量修改分类基础
     */
    int updateBatch(@Param("list") List<CategoryBase> categoryBaseList);

    /**
     * 删除分类基础
     */
    public int deleteByCategoryBaseId(Long categoryBaseId);

    /**
     * 批量删除分类基础
     */
    public int deleteByCategoryBaseIds(Long[] categoryBaseIds);
}
