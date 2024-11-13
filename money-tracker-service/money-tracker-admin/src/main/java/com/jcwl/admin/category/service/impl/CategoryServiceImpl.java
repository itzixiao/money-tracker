package com.jcwl.admin.category.service.impl;

import com.jcwl.admin.category.domain.Category;
import com.jcwl.admin.category.dto.CategoryQueryDTO;
import com.jcwl.admin.category.dto.CategoryTreeSelectVO;
import com.jcwl.admin.category.mapper.CategoryMapper;
import com.jcwl.admin.category.service.ICategoryService;
import com.jcwl.admin.common.constant.CacheConstants;
import com.jcwl.common.core.domain.R;
import com.jcwl.common.core.redis.RedisCache;
import com.jcwl.common.exception.BizPreconditions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分类Service业务层处理
 *
 * @author jcwl
 * @date 2024-10-23
 */
@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private RedisCache redisCache;

    /**
     * 查询分类
     */
    @Override
    public Category selectById(Long id) {
        return categoryMapper.selectById(id);
    }

    /**
     * 查询分类列表
     */
    @Override
    public List<Category> selectList(CategoryQueryDTO category) {
        return categoryMapper.selectList(category);
    }

    @Override
    public List<CategoryTreeSelectVO> getCategoryTree(CategoryQueryDTO category) {
        String categoryCacheKey = CacheConstants.RECENT_USE_CATEGORY + category.getUserId() + "_" + category.getBookId() + "_" + category.getType();
        List<CategoryTreeSelectVO> categoryCacheList = redisCache.getCacheList(categoryCacheKey);
        List<Category> categoryList = categoryMapper.selectList(category);

        // 创建一个 map 来存储所有 category，key 为 categoryBaseId
        Map<Long, CategoryTreeSelectVO> categoryMap = new HashMap<>();
        for (Category entity : categoryList) {
            CategoryTreeSelectVO treeSelectVO = new CategoryTreeSelectVO();
            BeanUtils.copyProperties(entity, treeSelectVO);
            categoryMap.put(treeSelectVO.getCategoryBaseId(), treeSelectVO);
        }

        // 构建树结构
        List<CategoryTreeSelectVO> tree = categoryList.stream()
                .filter(entity -> entity.getParentId() == 0)
                .map(entity -> categoryMap.get(entity.getCategoryBaseId()))
                .peek(entity -> entity.setChildren(getChildren(entity, categoryMap)))
                .collect(Collectors.toList());

        // 将 categoryCacheList 插入到 tree 的第一个位置
        tree.addAll(0, categoryCacheList);
        return tree;
    }

    private List<CategoryTreeSelectVO> getChildren(CategoryTreeSelectVO parent, Map<Long, CategoryTreeSelectVO> categoryMap) {
        return categoryMap.values().stream()
                .filter(entity -> entity.getParentId().equals(parent.getCategoryBaseId()))
                .peek(entity -> entity.setChildren(getChildren(entity, categoryMap)))
                .collect(Collectors.toList());
    }

    /**
     * 新增分类
     */
    @Override
    public R<Boolean> insert(Category category) {
        Long maxBaseId = categoryMapper.selectMaxBaseId(category);
        Long categoryBaseId = (maxBaseId != null) ? maxBaseId + 1 : 0L;
        category.setCategoryBaseId(categoryBaseId);
        int insert = categoryMapper.insert(category);
        String key = CacheConstants.BOOK_CATEGORY + category.getBookId()+"_"+category.getId();
        redisCache.setCacheObject(key, category);
        return R.ok(insert > 0);
    }

    /**
     * 修改分类
     */
    @Override
    public R<Boolean> update(Category category) {
        BizPreconditions.ifFalse(canEditOrDelete(category.getId()), "分类不可编辑！");
        int update = categoryMapper.update(category);
        return R.ok(update > 0);
    }

    /**
     * 批量删除分类
     */
    @Override
    public R<Boolean> deleteByIds(Long[] ids) {
        BizPreconditions.ifFalse(canEditOrDelete(ids), "分类不可删除！");
        int delete = categoryMapper.deleteByIds(ids);
        return R.ok(delete > 0);
    }

    /**
     * 删除分类信息
     */
    @Override
    public R<Boolean> deleteById(Long id) {
        BizPreconditions.ifFalse(canEditOrDelete(id), "分类不可删除！");
        int delete = categoryMapper.deleteById(id);
        return R.ok(delete > 0);
    }

    @Override
    public R<Boolean> deleteByBookId(Long[] bookIds) {
        BizPreconditions.ifFalse(canEditOrDelete(bookIds), "账本明细不可删除！");
        int delete = categoryMapper.deleteByBookId(bookIds);
        return R.ok(delete > 0);
    }

    @Override
    public R<Boolean> importExcel(List<Category> categoryList) {
        int insert = categoryMapper.insertBatch(categoryList);
        return R.ok(insert > 0);
    }

    /**
     * 判断分类是否可以编辑删除
     */
    private Boolean canEditOrDelete(Long... ids) {
        return true;
    }
}
