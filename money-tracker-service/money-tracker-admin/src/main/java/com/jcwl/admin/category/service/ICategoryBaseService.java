package com.jcwl.admin.category.service;

import com.jcwl.admin.category.domain.CategoryBase;
import com.jcwl.admin.category.dto.CategoryBaseQueryDTO;
import com.jcwl.common.core.domain.R;

import java.util.List;

/**
 * 分类基础Service接口
 *
 * @author jcwl
 * @date 2024-10-23
 */
public interface ICategoryBaseService {
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
    public R<Boolean> insert(CategoryBase categoryBase);

    /**
     * 修改分类基础
     */
    public R<Boolean> update(CategoryBase categoryBase);

    /**
     * 批量删除分类基础
     */
    public R<Boolean> deleteByCategoryBaseIds(Long[] categoryBaseIds);

    /**
     * 删除分类基础信息
     */
    public R deleteByCategoryBaseId(Long categoryBaseId);

    /**
     * 导入分类基础列表
     */
    R<Boolean> importExcel(List<CategoryBase> categoryBaseList);
}
