package com.jcwl.admin.category.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.jcwl.admin.category.domain.CategoryIcon;
import com.jcwl.admin.category.dto.CategoryIconQueryDTO;

/**
 * 分类图标Mapper接口
 * 
 * @author jcwl
 * @date 2024-11-06
 */
public interface CategoryIconMapper 
{
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
    public int insert(CategoryIcon categoryIcon);

    /**
     * 批量新增分类图标
     */
    public int insertBatch(@Param("list") List<CategoryIcon> categoryIconList);

    /**
     * 修改分类图标
     */
    public int update(CategoryIcon categoryIcon);

    /**
     * 批量修改分类图标
     */
    int updateBatch(@Param("list") List<CategoryIcon> categoryIconList);

    /**
     * 删除分类图标
     */
    public int deleteById(Long id);

    /**
     * 批量删除分类图标
     */
    public int deleteByIds(Long[] ids);
}
