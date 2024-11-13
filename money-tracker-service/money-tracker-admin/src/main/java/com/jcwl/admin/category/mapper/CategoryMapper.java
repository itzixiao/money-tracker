package com.jcwl.admin.category.mapper;

import com.jcwl.admin.category.domain.Category;
import com.jcwl.admin.category.dto.CategoryQueryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分类Mapper接口
 *
 * @author jcwl
 * @date 2024-10-23
 */
public interface CategoryMapper {
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
    public int insert(Category category);

    /**
     * 批量新增分类
     */
    public int insertBatch(@Param("list") List<Category> categoryList);

    /**
     * 修改分类
     */
    public int update(Category category);

    /**
     * 批量修改分类
     */
    int updateBatch(@Param("list") List<Category> categoryList);

    /**
     * 删除分类
     */
    public int deleteById(Long id);

    /**
     * 批量删除分类
     */
    public int deleteByIds(Long[] ids);

    /**
     * 删除分类
     */
    public int deleteByBookId(Long[] bookIds);

    /**
     * 查询最大基础分类id
     */
    Long selectMaxBaseId(Category category);
}
