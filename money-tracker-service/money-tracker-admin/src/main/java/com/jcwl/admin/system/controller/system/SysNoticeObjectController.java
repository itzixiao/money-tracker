package com.jcwl.admin.system.controller.system;


import com.jcwl.common.annotation.Log;
import com.jcwl.common.core.controller.BaseController;
import com.jcwl.common.core.domain.R;
import com.jcwl.common.core.page.TableDataInfo;
import com.jcwl.common.enums.BusinessType;
import com.jcwl.common.utils.poi.ExcelUtil;
import com.jcwl.system.domain.SysNoticeObject;
import com.jcwl.system.domain.dto.SysNoticeObjectQueryDTO;
import com.jcwl.system.service.ISysNoticeObjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 通知公告对象Controller
 *
 * @author jcwl
 * @date 2023-08-23
 */
@Api(tags = "通知公告对象管理")
@RestController
@RequestMapping("/system/notice/object")
public class SysNoticeObjectController extends BaseController {
    @Autowired
    private ISysNoticeObjectService sysNoticeObjectService;

    @ApiOperation("查询通知公告对象列表")
    @PreAuthorize("@ss.hasAnyPermi('notice:object:list')")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody SysNoticeObjectQueryDTO sysNoticeObject) {
        startPage(sysNoticeObject);
        List<SysNoticeObject> list = sysNoticeObjectService.selectList(sysNoticeObject);
        return getDataTable(list);
    }

    @ApiOperation("导出通知公告对象列表")
    @PreAuthorize("@ss.hasAnyPermi('notice:object:export')")
    @Log(title = "通知公告对象", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody SysNoticeObjectQueryDTO sysNoticeObject) {
        List<SysNoticeObject> list = sysNoticeObjectService.selectList(sysNoticeObject);
        ExcelUtil<SysNoticeObject> util = new ExcelUtil<SysNoticeObject>(SysNoticeObject.class);
        util.exportExcel(response, list, "通知公告对象数据");
    }

    @ApiOperation("获取通知公告对象详细信息")
    @GetMapping(value = "/{id}")
    public R<SysNoticeObject> getInfo(@PathVariable("id") Long id) {
        return R.ok(sysNoticeObjectService.selectById(id));
    }

    @ApiOperation("新增通知公告对象")
    @PreAuthorize("@ss.hasAnyPermi('notice:object:add')")
    @Log(title = "通知公告对象", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Boolean> add(@RequestBody SysNoticeObject sysNoticeObject) {
        return R.ok(sysNoticeObjectService.insert(sysNoticeObject) > 0);
    }

    @ApiOperation("修改通知公告对象")
    @PreAuthorize("@ss.hasAnyPermi('notice:object:edit')")
    @Log(title = "通知公告对象", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Boolean> edit(@RequestBody SysNoticeObject sysNoticeObject) {
        return R.ok(sysNoticeObjectService.update(sysNoticeObject) > 0);
    }

    @ApiOperation("删除通知公告对象")
    @PreAuthorize("@ss.hasAnyPermi('notice:object:remove')")
    @Log(title = "通知公告对象", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Boolean> remove(@PathVariable Long[] ids) {
        return R.ok(sysNoticeObjectService.deleteByIds(ids) > 0);
    }
}
