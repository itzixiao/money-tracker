package com.jcwl.admin.category.service.impl;

import com.jcwl.admin.category.domain.CategoryBase;
import com.jcwl.admin.category.dto.CategoryBaseQueryDTO;
import com.jcwl.admin.category.mapper.CategoryBaseMapper;
import com.jcwl.admin.category.service.ICategoryBaseService;
import com.jcwl.common.core.domain.R;
import com.jcwl.common.exception.BizPreconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分类基础Service业务层处理
 *
 * @author jcwl
 * @date 2024-10-23
 */
@Service
public class CategoryBaseServiceImpl implements ICategoryBaseService {
    @Autowired
    private CategoryBaseMapper categoryBaseMapper;

    /**
     * 查询分类基础
     */
    @Override
    public CategoryBase selectByCategoryBaseId(Long categoryBaseId) {
        return categoryBaseMapper.selectByCategoryBaseId(categoryBaseId);
    }

    /**
     * 查询分类基础列表
     */
    @Override
    public List<CategoryBase> selectList(CategoryBaseQueryDTO categoryBase) {
        return categoryBaseMapper.selectList(categoryBase);
    }

    /**
     * 新增分类基础
     */
    @Override
    public R<Boolean> insert(CategoryBase categoryBase) {
        int insert = categoryBaseMapper.insert(categoryBase);
        return R.ok(insert > 0);
    }

    /**
     * 修改分类基础
     */
    @Override
    public R<Boolean> update(CategoryBase categoryBase) {
        BizPreconditions.ifFalse(canEditOrDelete(categoryBase.getCategoryBaseId()), "分类基础不可编辑！");
        int update = categoryBaseMapper.update(categoryBase);
        return R.ok(update > 0);
    }

    /**
     * 批量删除分类基础
     */
    @Override
    public R<Boolean> deleteByCategoryBaseIds(Long[] categoryBaseIds) {
        BizPreconditions.ifFalse(canEditOrDelete(categoryBaseIds), "分类基础不可删除！");
        int delete = categoryBaseMapper.deleteByCategoryBaseIds(categoryBaseIds);
        return R.ok(delete > 0);
    }

    /**
     * 删除分类基础信息
     */
    @Override
    public R<Boolean> deleteByCategoryBaseId(Long categoryBaseId) {
        BizPreconditions.ifFalse(canEditOrDelete(categoryBaseId), "分类基础不可删除！");
        int delete = categoryBaseMapper.deleteByCategoryBaseId(categoryBaseId);
        return R.ok(delete > 0);
    }

    @Override
    public R<Boolean> importExcel(List<CategoryBase> categoryBaseList) {
        int insert = categoryBaseMapper.insertBatch(categoryBaseList);
        return R.ok(insert > 0);
    }

    /**
     * 判断分类基础是否可以编辑删除
     */
    private Boolean canEditOrDelete(Long... categoryBaseIds) {
        return true;
    }
}
