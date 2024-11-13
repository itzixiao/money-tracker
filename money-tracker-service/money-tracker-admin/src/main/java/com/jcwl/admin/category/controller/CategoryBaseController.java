package com.jcwl.admin.category.controller;

import com.jcwl.admin.category.domain.CategoryBase;
import com.jcwl.admin.category.dto.CategoryBaseQueryDTO;
import com.jcwl.admin.category.service.ICategoryBaseService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 分类基础Controller
 *
 * @author jcwl
 * @date 2024-10-23
 */
@Api(tags = "分类基础管理")
@RestController
@RequestMapping("/category/base")
public class CategoryBaseController extends BaseController {
    @Autowired
    private ICategoryBaseService categoryBaseService;

    @ApiOperation("查询分类基础列表")
    @PreAuthorize("@ss.hasAnyPermi('category:base:list')")
    @PostMapping("/list")
    public TableDataInfo<CategoryBase> list(@RequestBody CategoryBaseQueryDTO categoryBase) {
        startPage(categoryBase);
        List<CategoryBase> list = categoryBaseService.selectList(categoryBase);
        return getDataTable(list);
    }

    @ApiOperation("获取分类基础详细信息")
    @GetMapping(value = "/{categoryBaseId}")
    public R<CategoryBase> getInfo(@PathVariable("categoryBaseId") Long categoryBaseId) {
        return R.ok(categoryBaseService.selectByCategoryBaseId(categoryBaseId));
    }

    @ApiOperation("新增分类基础")
    @PreAuthorize("@ss.hasAnyPermi('category:base:add')")
    @Log(title = "分类基础", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Boolean> add(@RequestBody CategoryBase categoryBase) {
        categoryBase.setCreateId(this.getUserId());
        categoryBase.setCreateBy(this.getUsername());
        categoryBase.setCreateTime(DateUtils.getNowDate());
        return categoryBaseService.insert(categoryBase);
    }

    @ApiOperation("修改分类基础")
    @PreAuthorize("@ss.hasAnyPermi('category:base:edit')")
    @Log(title = "分类基础", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Boolean> edit(@RequestBody CategoryBase categoryBase) {
        categoryBase.setUpdateId(this.getUserId());
        categoryBase.setUpdateBy(this.getUsername());
        categoryBase.setUpdateTime(DateUtils.getNowDate());
        return categoryBaseService.update(categoryBase);
    }

    @ApiOperation("删除分类基础")
    @PreAuthorize("@ss.hasAnyPermi('category:base:remove')")
    @Log(title = "分类基础", businessType = BusinessType.DELETE)
    @DeleteMapping("/{categoryBaseIds}")
    public R<Boolean> remove(@PathVariable Long[] categoryBaseIds) {
        return categoryBaseService.deleteByCategoryBaseIds(categoryBaseIds);
    }

    /**
     * 下载模板
     */
    @ApiOperation("下载模板")
    @Log(title = "下载模板", businessType = BusinessType.EXPORT)
    @GetMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response) {
        ExcelUtil<CategoryBase> util = new ExcelUtil<>(CategoryBase.class);
        util.exportExcel(response, new ArrayList<>(), "下载模板", "分类基础数据", "分类基础");
    }

    @ApiOperation("导出分类基础列表")
    @PreAuthorize("@ss.hasAnyPermi('category:base:export')")
    @Log(title = "分类基础", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody CategoryBaseQueryDTO categoryBase) {
        List<CategoryBase> list = categoryBaseService.selectList(categoryBase);
        ExcelUtil<CategoryBase> util = new ExcelUtil<>(CategoryBase.class);
        util.exportExcel(response, list, categoryBase.getExportFileName(), "分类基础数据", "分类基础");
    }

    @ApiOperation("导入分类基础列表")
    @PreAuthorize("@ss.hasAnyPermi('category:base:export')")
    @Log(title = "分类基础", businessType = BusinessType.EXPORT)
    @PostMapping("/importExcel")
    public R<Boolean> importExcel(@RequestPart @ApiParam(name = "file", value = "文件", required = true) MultipartFile file) {
        ExcelUtil<CategoryBase> util = new ExcelUtil<>(CategoryBase.class);
        List<CategoryBase> categoryBaseList = null;
        try {
            categoryBaseList = util.importExcel(file.getInputStream(), 1);
        } catch (Exception e) {
            logger.error("导入分类基础列表异常：{}", e.getMessage());
            return R.fail("导入分类基础列表异常，请稍后重试！");
        }
        if (CollectionUtils.isEmpty(categoryBaseList)) {
            return R.fail("数据为空！");
        }
        categoryBaseList.forEach(t -> {
            t.setCreateId(this.getUserId());
            t.setCreateBy(this.getUsername());
            t.setCreateTime(DateUtils.getNowDate());
            t.setDelFlag("0");
        });
        return categoryBaseService.importExcel(categoryBaseList);
    }

}
