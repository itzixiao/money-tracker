package com.jcwl.admin.category.service.impl;

import com.jcwl.common.core.domain.R;
import java.util.List;
import com.jcwl.common.utils.DateUtils;
import com.jcwl.common.exception.BizPreconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jcwl.admin.category.mapper.CategoryIconMapper;
import com.jcwl.admin.category.domain.CategoryIcon;
import com.jcwl.admin.category.dto.CategoryIconQueryDTO;
import com.jcwl.admin.category.service.ICategoryIconService;

/**
 * 分类图标Service业务层处理
 *
 * @author jcwl
 * @date 2024-11-06
 */
@Service
public class CategoryIconServiceImpl implements ICategoryIconService {
    @Autowired
    private CategoryIconMapper categoryIconMapper;

    /**
     * 查询分类图标
     */
    @Override
    public CategoryIcon selectById(Long id) {
        return categoryIconMapper.selectById(id);
    }

    /**
     * 查询分类图标列表
     */
    @Override
    public List<CategoryIcon> selectList(CategoryIconQueryDTO categoryIcon) {
        return categoryIconMapper.selectList(categoryIcon);
    }

    /**
     * 新增分类图标
     */
    @Override
    public R<Boolean> insert(CategoryIcon categoryIcon) {
        int insert = categoryIconMapper.insert(categoryIcon);
        return R.ok(insert>0);
    }

    /**
     * 修改分类图标
     */
    @Override
    public R<Boolean> update(CategoryIcon categoryIcon) {
        BizPreconditions.ifFalse(canEditOrDelete(categoryIcon.getId()), "分类图标不可编辑！");
        int update = categoryIconMapper.update(categoryIcon);
        return R.ok(update>0);
    }

    /**
     * 批量删除分类图标
     */
    @Override
    public R<Boolean> deleteByIds(Long[] ids) {
        BizPreconditions.ifFalse(canEditOrDelete(ids), "分类图标不可删除！");
        int delete = categoryIconMapper.deleteByIds(ids);
        return R.ok(delete>0);
    }

    /**
     * 删除分类图标信息
     */
    @Override
    public R<Boolean> deleteById(Long id) {
        BizPreconditions.ifFalse(canEditOrDelete(id), "分类图标不可删除！");
        int delete = categoryIconMapper.deleteById(id);
        return R.ok(delete>0);
    }

    @Override
    public R<Boolean> importExcel(List<CategoryIcon> categoryIconList) {
        int insert = categoryIconMapper.insertBatch(categoryIconList);
        return R.ok(insert>0);
    }

    /**
     * 判断分类图标是否可以编辑删除
     */
    private Boolean canEditOrDelete(Long... ids) {
        return true;
    }
}
