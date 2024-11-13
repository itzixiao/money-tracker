package com.jcwl.admin.book.controller;

import com.jcwl.admin.book.domain.BookUser;
import com.jcwl.admin.book.dto.BookUserQueryDTO;
import com.jcwl.admin.book.service.IBookUserService;
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
 * 账本用户Controller
 *
 * @author jcwl
 * @date 2024-10-15
 */
@Api(tags = "账本用户管理")
@RestController
@RequestMapping("/book/user")
public class BookUserController extends BaseController {
    @Autowired
    private IBookUserService bookUserService;

    @ApiOperation("查询账本用户列表")
    @PreAuthorize("@ss.hasAnyPermi('book:user:list')")
    @PostMapping("/list")
    public TableDataInfo<BookUser> list(@Validated @RequestBody BookUserQueryDTO bookUser) {
        // startPage(bookUser);
        bookUser.setOrderByUserId(this.getUserId());
        List<BookUser> list = bookUserService.selectList(bookUser);
        return getDataTable(list);
    }

    @ApiOperation("获取账本用户详细信息")
    @GetMapping(value = "/{id}")
    public R<BookUser> getInfo(@PathVariable("id") Long id) {
        return R.ok(bookUserService.selectById(id));
    }

    @ApiOperation("新增账本用户")
    @PreAuthorize("@ss.hasAnyPermi('book:user:add')")
    @Log(title = "账本用户", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Boolean> add(@RequestBody BookUser bookUser) {
        bookUser.setCreateId(this.getUserId());
        bookUser.setCreateBy(this.getUsername());
        bookUser.setCreateTime(DateUtils.getNowDate());
        return bookUserService.insert(bookUser);
    }

    @ApiOperation("邀请用户加入账本")
    @PreAuthorize("@ss.hasAnyPermi('book:user:add')")
    @Log(title = "账本用户", businessType = BusinessType.INSERT)
    @PostMapping("/invite/user")
    public R<Boolean> inviteUser(@Validated @RequestBody BookUser bookUser) {
        bookUser.setCreateId(this.getUserId());
        bookUser.setCreateBy(this.getUsername());
        bookUser.setCreateTime(DateUtils.getNowDate());
        return bookUserService.inviteUser(bookUser);
    }

    @ApiOperation("修改账本用户")
    @PreAuthorize("@ss.hasAnyPermi('book:user:edit')")
    @Log(title = "账本用户", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Boolean> edit(@RequestBody BookUser bookUser) {
        bookUser.setUpdateId(this.getUserId());
        bookUser.setUpdateBy(this.getUsername());
        bookUser.setUpdateTime(DateUtils.getNowDate());
        return bookUserService.update(bookUser);
    }

    @ApiOperation("删除账本用户")
    @PreAuthorize("@ss.hasAnyPermi('book:user:remove')")
    @Log(title = "账本用户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Boolean> remove(@PathVariable Long[] ids) {
        return bookUserService.deleteByIds(ids);
    }

    /**
     * 下载模板
     */
    @ApiOperation("下载模板")
    @Log(title = "下载模板", businessType = BusinessType.EXPORT)
    @GetMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response) {
        ExcelUtil<BookUser> util = new ExcelUtil<>(BookUser.class);
        util.exportExcel(response, new ArrayList<>(), "下载模板", "账本用户数据", "账本用户");
    }

    @ApiOperation("导出账本用户列表")
    @PreAuthorize("@ss.hasAnyPermi('book:user:export')")
    @Log(title = "账本用户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody BookUserQueryDTO bookUser) {
        List<BookUser> list = bookUserService.selectList(bookUser);
        ExcelUtil<BookUser> util = new ExcelUtil<>(BookUser.class);
        util.exportExcel(response, list, bookUser.getExportFileName(), "账本用户数据", "账本用户");
    }

    @ApiOperation("导入账本用户列表")
    @PreAuthorize("@ss.hasAnyPermi('book:user:export')")
    @Log(title = "账本用户", businessType = BusinessType.EXPORT)
    @PostMapping("/importExcel")
    public R<Boolean> importExcel(@RequestPart @ApiParam(name = "file", value = "文件", required = true) MultipartFile file) {
        ExcelUtil<BookUser> util = new ExcelUtil<>(BookUser.class);
        List<BookUser> bookUserList = null;
        try {
            bookUserList = util.importExcel(file.getInputStream(), 1);
        } catch (Exception e) {
            logger.error("导入账本用户列表异常：{}", e.getMessage());
            return R.fail("导入账本用户列表异常，请稍后重试！");
        }
        if (CollectionUtils.isEmpty(bookUserList)) {
            return R.fail("数据为空！");
        }
        bookUserList.forEach(t -> {
            t.setCreateId(this.getUserId());
            t.setCreateBy(this.getUsername());
            t.setCreateTime(DateUtils.getNowDate());
            t.setDelFlag("0");
        });
        return bookUserService.importExcel(bookUserList);
    }

}
