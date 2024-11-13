package com.jcwl.admin.category.controller;

import com.jcwl.admin.category.domain.Category;
import com.jcwl.admin.category.dto.CategoryQueryDTO;
import com.jcwl.admin.category.dto.CategoryTreeSelectVO;
import com.jcwl.admin.category.service.ICategoryService;
import com.jcwl.common.annotation.Log;
import com.jcwl.common.core.controller.BaseController;
import com.jcwl.common.core.domain.R;
import com.jcwl.common.core.page.TableDataInfo;
import com.jcwl.common.enums.BusinessType;
import com.jcwl.common.utils.DateUtils;
import com.jcwl.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 分类Controller
 *
 * @author jcwl
 * @date 2024-10-23
 */
@Api(tags = "分类管理")
@RestController
@RequestMapping("/category/category")
public class CategoryController extends BaseController {
    @Autowired
    private ICategoryService categoryService;

    @ApiOperation("查询分类列表")
    @PreAuthorize("@ss.hasAnyPermi('category:category:list')")
    @PostMapping("/list")
    public TableDataInfo<Category> list(@RequestBody CategoryQueryDTO category) {
        startPage(category);
        List<Category> list = categoryService.selectList(category);
        return getDataTable(list);
    }

    @ApiOperation("查询分类树")
    @PreAuthorize("@ss.hasAnyPermi('category:category:list')")
    @PostMapping("/tree")
    public R<List<CategoryTreeSelectVO>> getCategoryTree(@Validated @RequestBody CategoryQueryDTO category) {
        category.setUserId(this.getUserId());
        List<CategoryTreeSelectVO> list = categoryService.getCategoryTree(category);
        return R.ok(list);
    }

    @ApiOperation("获取分类详细信息")
    @GetMapping(value = "/{id}")
    public R<Category> getInfo(@PathVariable("id") Long id) {
        return R.ok(categoryService.selectById(id));
    }

    @ApiOperation("新增分类")
    @PreAuthorize("@ss.hasAnyPermi('category:category:add')")
    @Log(title = "分类", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Boolean> add(@RequestBody Category category) {
        category.setCreateId(this.getUserId());
        category.setCreateBy(this.getUsername());
        category.setCreateTime(DateUtils.getNowDate());
        return categoryService.insert(category);
    }

    @ApiOperation("修改分类")
    @PreAuthorize("@ss.hasAnyPermi('category:category:edit')")
    @Log(title = "分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Boolean> edit(@RequestBody Category category) {
        category.setUpdateId(this.getUserId());
        category.setUpdateBy(this.getUsername());
        category.setUpdateTime(DateUtils.getNowDate());
        return categoryService.update(category);
    }

    @ApiOperation("删除分类")
    @PreAuthorize("@ss.hasAnyPermi('category:category:remove')")
    @Log(title = "分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Boolean> remove(@PathVariable Long[] ids) {
        return categoryService.deleteByIds(ids);
    }

    /**
     * 下载模板
     */
    @ApiOperation("下载模板")
    @Log(title = "下载模板", businessType = BusinessType.EXPORT)
    @GetMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response) {
        ExcelUtil<Category> util = new ExcelUtil<>(Category.class);
        util.exportExcel(response, new ArrayList<>(), "下载模板", "分类数据", "分类");
    }

    @ApiOperation("导出分类列表")
    @PreAuthorize("@ss.hasAnyPermi('category:category:export')")
    @Log(title = "分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody CategoryQueryDTO category) {
        List<Category> list = categoryService.selectList(category);
        ExcelUtil<Category> util = new ExcelUtil<>(Category.class);
        util.exportExcel(response, list, category.getExportFileName(), "分类数据", "分类");
    }

    @ApiOperation("导入分类列表")
    @PreAuthorize("@ss.hasAnyPermi('category:category:export')")
    @Log(title = "分类", businessType = BusinessType.EXPORT)
    @PostMapping("/importExcel")
    public R<Boolean> importExcel(@RequestPart @ApiParam(name = "file", value = "文件", required = true) MultipartFile file) {
        ExcelUtil<Category> util = new ExcelUtil<>(Category.class);
        List<Category> categoryList = null;
        try {
            categoryList = util.importExcel(file.getInputStream(), 1);
        } catch (Exception e) {
            logger.error("导入分类列表异常：{}", e.getMessage());
            return R.fail("导入分类列表异常，请稍后重试！");
        }
        if (CollectionUtils.isEmpty(categoryList)) {
            return R.fail("数据为空！");
        }
        categoryList.forEach(t -> {
            t.setCreateId(this.getUserId());
            t.setCreateBy(this.getUsername());
            t.setCreateTime(DateUtils.getNowDate());
            t.setDelFlag("0");
        });
        return categoryService.importExcel(categoryList);
    }

}
