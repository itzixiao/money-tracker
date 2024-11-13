package com.jcwl.admin.category.controller;

import com.jcwl.common.utils.DateUtils;
import com.jcwl.common.core.domain.R;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.ArrayList;
import org.springframework.util.CollectionUtils;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestPart;
import com.jcwl.common.annotation.Log;
import com.jcwl.common.core.controller.BaseController;
import com.jcwl.common.enums.BusinessType;
import com.jcwl.admin.category.domain.CategoryIcon;
import com.jcwl.admin.category.dto.CategoryIconQueryDTO;
import com.jcwl.admin.category.service.ICategoryIconService;
import com.jcwl.common.utils.poi.ExcelUtil;
import org.springframework.web.multipart.MultipartFile;
import com.jcwl.common.core.page.TableDataInfo;

/**
 * 分类图标Controller
 *
 * @author jcwl
 * @date 2024-11-06
 */
@Api(tags = "分类图标管理")
@RestController
@RequestMapping("/category/icon")
public class CategoryIconController extends BaseController {
    @Autowired
    private ICategoryIconService categoryIconService;

    @ApiOperation("查询分类图标列表")
    @PreAuthorize("@ss.hasAnyPermi('category:icon:list')")
    @PostMapping("/list")
    public TableDataInfo<CategoryIcon> list(@RequestBody CategoryIconQueryDTO categoryIcon) {
        startPage(categoryIcon);
        List<CategoryIcon> list = categoryIconService.selectList(categoryIcon);
        return getDataTable(list);
    }

    @ApiOperation("获取分类图标详细信息")
    @GetMapping(value = "/{id}")
    public R<CategoryIcon> getInfo(@PathVariable("id") Long id) {
        return R.ok(categoryIconService.selectById(id));
    }

    @ApiOperation("新增分类图标")
    @PreAuthorize("@ss.hasAnyPermi('category:icon:add')")
    @Log(title = "分类图标", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Boolean> add(@RequestBody CategoryIcon categoryIcon) {
        categoryIcon.setCreateId(this.getUserId());
        categoryIcon.setCreateBy(this.getUsername());
        categoryIcon.setCreateTime(DateUtils.getNowDate());
        return categoryIconService.insert(categoryIcon);
    }

    @ApiOperation("修改分类图标")
    @PreAuthorize("@ss.hasAnyPermi('category:icon:edit')")
    @Log(title = "分类图标", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Boolean> edit(@RequestBody CategoryIcon categoryIcon) {
        categoryIcon.setUpdateId(this.getUserId());
        categoryIcon.setUpdateBy(this.getUsername());
        categoryIcon.setUpdateTime(DateUtils.getNowDate());
        return categoryIconService.update(categoryIcon);
    }

    @ApiOperation("删除分类图标")
    @PreAuthorize("@ss.hasAnyPermi('category:icon:remove')")
    @Log(title = "分类图标", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public R<Boolean> remove(@PathVariable Long[] ids) {
        return categoryIconService.deleteByIds(ids);
    }

    /**
     * 下载模板
     */
    @ApiOperation("下载模板")
    @Log(title = "下载模板", businessType = BusinessType.EXPORT)
    @GetMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response) {
        ExcelUtil<CategoryIcon> util = new ExcelUtil<>(CategoryIcon.class);
        util.exportExcel(response, new ArrayList<>(), "下载模板", "分类图标数据", "分类图标");
    }

    @ApiOperation("导出分类图标列表")
    @PreAuthorize("@ss.hasAnyPermi('category:icon:export')")
    @Log(title = "分类图标", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response,@RequestBody CategoryIconQueryDTO categoryIcon) {
        List<CategoryIcon> list = categoryIconService.selectList(categoryIcon);
        ExcelUtil<CategoryIcon> util = new ExcelUtil<>(CategoryIcon.class);
        util.exportExcel(response, list, categoryIcon.getExportFileName(), "分类图标数据", "分类图标");
    }

    @ApiOperation("导入分类图标列表")
    @PreAuthorize("@ss.hasAnyPermi('category:icon:export')")
    @Log(title = "分类图标", businessType = BusinessType.EXPORT)
    @PostMapping("/importExcel")
    public R<Boolean> importExcel(@RequestPart @ApiParam(name = "file", value = "文件", required = true) MultipartFile file) {
        ExcelUtil<CategoryIcon> util = new ExcelUtil<>(CategoryIcon.class);
        List<CategoryIcon> categoryIconList = null;
        try {
            categoryIconList = util.importExcel(file.getInputStream(), 1);
        } catch (Exception e) {
            logger.error("导入分类图标列表异常：{}", e.getMessage());
            return R.fail("导入分类图标列表异常，请稍后重试！");
        }
        if(CollectionUtils.isEmpty(categoryIconList)){
            return R.fail("数据为空！");
        }
        categoryIconList.forEach(t->{
            t.setCreateId(this.getUserId());
            t.setCreateBy(this.getUsername());
            t.setCreateTime(DateUtils.getNowDate());
            t.setDelFlag("0");
        });
        return categoryIconService.importExcel(categoryIconList);
    }

}
