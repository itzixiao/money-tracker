package com.jcwl.admin.system.controller.system;

import com.jcwl.common.annotation.Log;
import com.jcwl.common.core.controller.BaseController;
import com.jcwl.common.core.domain.R;
import com.jcwl.common.core.page.TableDataInfo;
import com.jcwl.common.enums.BusinessType;
import com.jcwl.common.utils.poi.ExcelUtil;
import com.jcwl.system.domain.SysNotice;
import com.jcwl.system.domain.dto.SysNoticeDTO;
import com.jcwl.system.domain.dto.SysNoticeQueryDTO;
import com.jcwl.system.service.ISysNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 通知公告Controller
 *
 * @author jcwl
 * @date 2023-08-23
 */
@Api(tags = "通知公告管理")
@RestController
@RequestMapping("/system/notice")
public class SysNoticeController extends BaseController {
    @Autowired
    private ISysNoticeService sysNoticeService;

    @ApiOperation("查询通知公告列表")
    @PreAuthorize("@ss.hasAnyPermi('system:notice:list')")
    @PostMapping("/list")
    public TableDataInfo<SysNotice> list(@RequestBody SysNoticeQueryDTO sysNotice) {
        startPage(sysNotice);
        List<SysNotice> list = sysNoticeService.selectList(sysNotice);
        return getDataTable(list);
    }

    @ApiOperation("导出通知公告列表")
    @PreAuthorize("@ss.hasAnyPermi('system:notice:export')")
    @Log(title = "通知公告", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody SysNoticeQueryDTO sysNotice) {
        List<SysNotice> list = sysNoticeService.selectList(sysNotice);
        ExcelUtil<SysNotice> util = new ExcelUtil<SysNotice>(SysNotice.class);
        util.exportExcel(response, list, "通知公告数据");
    }

    @ApiOperation("获取通知公告详细信息")
    @GetMapping(value = "/{id}")
    public R<SysNotice> getInfo(@PathVariable("id") Long id) {
        return R.ok(sysNoticeService.selectById(id));
    }

    @ApiOperation("新增通知公告")
    @PreAuthorize("@ss.hasAnyPermi('system:notice:add')")
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Boolean> add(@RequestBody SysNoticeDTO sysNotice) {
        sysNotice.setCreateId(this.getUserId());
        sysNotice.setCreateBy(this.getUsername());
        return R.ok(sysNoticeService.insert(sysNotice) > 0);
    }

    @ApiOperation("修改通知公告")
    @PreAuthorize("@ss.hasAnyPermi('system:notice:edit')")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Boolean> edit(@RequestBody SysNotice sysNotice) {
        sysNotice.setUpdateId(this.getUserId());
        sysNotice.setUpdateBy(this.getUsername());
        return R.ok(sysNoticeService.update(sysNotice) > 0);
    }

    @ApiOperation("删除通知公告")
    @PreAuthorize("@ss.hasAnyPermi('system:notice:remove')")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Boolean> remove(@PathVariable Long[] ids) {
        return R.ok(sysNoticeService.deleteByIds(ids) > 0);
    }


    @ApiOperation("查询当前用户未读通知列表")
    @PostMapping("/unread/list")
    public TableDataInfo<SysNotice> unreadlist(@RequestBody SysNoticeQueryDTO sysNotice) {
        startPage();
        List<SysNotice> list = sysNoticeService.unreadlist(this.getUserId());
        return getDataTable(list);
    }

    @ApiOperation("查询当前用户未读数量")
    @PostMapping("/unread/count")
    public R<Integer> unreadCount() {
        Integer num = sysNoticeService.unreadCount(this.getUserId());
        return R.ok(num);
    }

    @ApiOperation("全部设为已读")
    @PostMapping("/all/read")
    public R<Boolean> allRead() {
        sysNoticeService.allRead(this.getUserId());
        return R.ok(true);
    }

    @ApiOperation("设为已读")
    @PostMapping("/read/{noticeId}")
    public R<Boolean> read(@PathVariable("noticeId") Long noticeId) {
        sysNoticeService.read(this.getUserId(), noticeId);
        return R.ok(true);
    }
}
