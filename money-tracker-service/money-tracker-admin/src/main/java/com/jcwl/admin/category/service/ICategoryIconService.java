package com.jcwl.admin.category.service;

import com.jcwl.common.core.domain.R;
import java.util.List;
import com.jcwl.admin.category.domain.CategoryIcon;
import com.jcwl.admin.category.dto.CategoryIconQueryDTO;

/**
 * 分类图标Service接口
 *
 * @author jcwl
 * @date 2024-11-06
 */
public interface ICategoryIconService {
    /**
     * 查询分类图标
     */
    public CategoryIcon selectById(Long id);

    /**
     * 查询分类图标列表
     */
    public List<CategoryIcon> selectList(CategoryIconQueryDTO categoryIcon);

    /**
     * 新增分类图标
     */
    public R<Boolean> insert(CategoryIcon categoryIcon);

    /**
     * 修改分类图标
     */
    public R<Boolean> update(CategoryIcon categoryIcon);

    /**
     * 批量删除分类图标
     */
    public R<Boolean> deleteByIds(Long[] ids);

    /**
     * 删除分类图标信息
     */
    public R deleteById(Long id);

    /**
     * 导入分类图标列表
     */
    R<Boolean> importExcel(List<CategoryIcon> categoryIconList);
}
