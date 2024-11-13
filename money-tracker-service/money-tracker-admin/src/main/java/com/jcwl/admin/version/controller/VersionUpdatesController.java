package com.jcwl.admin.version.controller;

import com.alibaba.fastjson2.JSONObject;
import com.jcwl.admin.version.domain.VersionUpdates;
import com.jcwl.admin.version.dto.VersionUpdatesQueryDTO;
import com.jcwl.admin.version.service.IVersionUpdatesService;
import com.jcwl.common.annotation.Anonymous;
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
 * 版本控制Controller
 *
 * @author jcwl
 * @date 2024-08-21
 */
@Api(tags = "版本控制管理")
@RestController
@RequestMapping("/version/updates")
public class VersionUpdatesController extends BaseController {
    @Autowired
    private IVersionUpdatesService versionUpdatesService;

    @ApiOperation("查询版本控制列表")
    @PreAuthorize("@ss.hasAnyPermi('version:updates:list')")
    @PostMapping("/list")
    public TableDataInfo<VersionUpdates> list(@RequestBody VersionUpdatesQueryDTO versionUpdates) {
        startPage(versionUpdates);
        List<VersionUpdates> list = versionUpdatesService.selectList(versionUpdates);
        return getDataTable(list);
    }

    @ApiOperation("导出版本控制列表")
    @PreAuthorize("@ss.hasAnyPermi('version:updates:export')")
    @Log(title = "版本控制", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody VersionUpdatesQueryDTO versionUpdates) {
        List<VersionUpdates> list = versionUpdatesService.selectList(versionUpdates);
        ExcelUtil<VersionUpdates> util = new ExcelUtil<>(VersionUpdates.class);
        util.exportExcel(response,list, versionUpdates.getExportFileName(), "版本控制数据");
    }

    @ApiOperation("获取版本控制详细信息")
    @GetMapping(value = "/{id}")
    public R<VersionUpdates> getInfo(@PathVariable("id") Long id) {
        return R.ok(versionUpdatesService.selectById(id));
    }

    @ApiOperation("新增版本控制")
    @PreAuthorize("@ss.hasAnyPermi('version:updates:add')")
    @Log(title = "版本控制", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Boolean> add(@RequestBody VersionUpdates versionUpdates) {
        versionUpdates.setCreateId(this.getUserId());
        versionUpdates.setCreateBy(this.getUsername());
        versionUpdates.setCreateTime(DateUtils.getNowDate());
        return versionUpdatesService.insert(versionUpdates);
    }


    @ApiOperation("文件上传版本控制")
    @PostMapping("/upload/version")
    @Anonymous
    public R<VersionUpdates> fileUploadVersion(@RequestParam("file") MultipartFile file,
                          @RequestParam("versionUpdates") String versionUpdates) {
        VersionUpdates version = JSONObject.parseObject(versionUpdates, VersionUpdates.class);
        return versionUpdatesService.fileUploadVersion(file,version);
    }

    @ApiOperation("文件上传根据文件名称查询")
    @PostMapping("/query/file")
    @Anonymous
    public R<VersionUpdates> queryFileVersion(@RequestBody VersionUpdates versionUpdates) {
        return versionUpdatesService.queryFileVersion(versionUpdates);
    }


    @ApiOperation("修改版本控制")
    @PreAuthorize("@ss.hasAnyPermi('version:updates:edit')")
    @Log(title = "版本控制", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Boolean> edit(@RequestBody VersionUpdates versionUpdates) {
        versionUpdates.setUpdateId(this.getUserId());
        versionUpdates.setUpdateBy(this.getUsername());
        versionUpdates.setUpdateTime(DateUtils.getNowDate());
        return versionUpdatesService.update(versionUpdates);
    }

    @ApiOperation("删除版本控制")
    @PreAuthorize("@ss.hasAnyPermi('version:updates:remove')")
    @Log(title = "版本控制", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Boolean> remove(@PathVariable Long[] ids) {
        return versionUpdatesService.deleteByIds(ids);
    }

    /**
     * 下载模板
     */
    @ApiOperation("下载模板")
    @Log(title = "下载模板", businessType = BusinessType.EXPORT)
    @GetMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response) {
        ExcelUtil<VersionUpdates> util = new ExcelUtil<>(VersionUpdates.class);
        util.exportExcel(response,  new ArrayList<>(),"下载模板", "版本控制数据");
    }

    @ApiOperation("导入版本控制列表")
    @PreAuthorize("@ss.hasAnyPermi('version:updates:export')")
    @Log(title = "版本控制", businessType = BusinessType.EXPORT)
    @PostMapping("/importExcel")
    public R importExcel(@RequestPart @ApiParam(name = "file", value = "文件", required = true) MultipartFile file) {
        ExcelUtil<VersionUpdates> util = new ExcelUtil<>(VersionUpdates.class);
        List<VersionUpdates> versionUpdatesList = null;
        try {
            versionUpdatesList = util.importExcel(file.getInputStream());
        } catch (Exception e) {
            logger.error("导入版本控制列表异常：{}", e);
            return R.fail("导入版本控制列表异常，请稍后重试！");
        }
        if (CollectionUtils.isEmpty(versionUpdatesList)) {
            return R.fail("数据为空！");
        }
        versionUpdatesList.forEach(t -> {
            t.setCreateId(this.getUserId());
            t.setCreateBy(this.getUsername());
            t.setCreateTime(DateUtils.getNowDate());
            t.setDelFlag("0");
        });
        return versionUpdatesService.importExcel(versionUpdatesList);
    }

}
