package com.jcwl.admin.category.service;

import com.jcwl.admin.category.domain.Category;
import com.jcwl.admin.category.dto.CategoryQueryDTO;
import com.jcwl.admin.category.dto.CategoryTreeSelectVO;
import com.jcwl.common.core.domain.R;

import java.util.List;

/**
 * 分类Service接口
 *
 * @author jcwl
 * @date 2024-10-23
 */
public interface ICategoryService {
    /**
     * 查询分类
     */
    public Category selectById(Long id);

    /**
     * 查询分类列表
     */
    public List<Category> selectList(CategoryQueryDTO category);

    /**
     * 新增分类
     */
    public R<Boolean> insert(Category category);

    /**
     * 修改分类
     */
    public R<Boolean> update(Category category);

    /**
     * 批量删除分类
     */
    public R<Boolean> deleteByIds(Long[] ids);

    /**
     * 删除分类信息
     */
    public R deleteById(Long id);

    /**
     * 导入分类列表
     */
    R<Boolean> importExcel(List<Category> categoryList);

    /**
     * 获取分类树
     */
    List<CategoryTreeSelectVO> getCategoryTree(CategoryQueryDTO category);

    /**
     * 删除账本分类
     */
    public R<Boolean> deleteByBookId(Long[] bookIds);
}
