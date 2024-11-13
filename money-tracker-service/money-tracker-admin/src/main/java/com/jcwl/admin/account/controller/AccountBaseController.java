package com.jcwl.admin.account.controller;

import com.jcwl.admin.account.domain.AccountBase;
import com.jcwl.admin.account.dto.AccountBaseQueryDTO;
import com.jcwl.admin.account.service.IAccountBaseService;
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
 * 账户基础Controller
 *
 * @author jcwl
 * @date 2024-10-24
 */
@Api(tags = "账户基础管理")
@RestController
@RequestMapping("/account/base")
public class AccountBaseController extends BaseController {
    @Autowired
    private IAccountBaseService accountBaseService;

    @ApiOperation("查询账户基础列表")
    @PreAuthorize("@ss.hasAnyPermi('account:base:list')")
    @PostMapping("/list")
    public TableDataInfo<AccountBase> list(@RequestBody AccountBaseQueryDTO accountBase) {
        startPage(accountBase);
        List<AccountBase> list = accountBaseService.selectList(accountBase);
        return getDataTable(list);
    }

    @ApiOperation("获取账户基础详细信息")
    @GetMapping(value = "/{accountBaseId}")
    public R<AccountBase> getInfo(@PathVariable("accountBaseId") Long accountBaseId) {
        return R.ok(accountBaseService.selectByAccountBaseId(accountBaseId));
    }

    @ApiOperation("新增账户基础")
    @PreAuthorize("@ss.hasAnyPermi('account:base:add')")
    @Log(title = "账户基础", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Boolean> add(@RequestBody AccountBase accountBase) {
        accountBase.setCreateId(this.getUserId());
        accountBase.setCreateBy(this.getUsername());
        accountBase.setCreateTime(DateUtils.getNowDate());
        return accountBaseService.insert(accountBase);
    }

    @ApiOperation("修改账户基础")
    @PreAuthorize("@ss.hasAnyPermi('account:base:edit')")
    @Log(title = "账户基础", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Boolean> edit(@RequestBody AccountBase accountBase) {
        accountBase.setUpdateId(this.getUserId());
        accountBase.setUpdateBy(this.getUsername());
        accountBase.setUpdateTime(DateUtils.getNowDate());
        return accountBaseService.update(accountBase);
    }

    @ApiOperation("删除账户基础")
    @PreAuthorize("@ss.hasAnyPermi('account:base:remove')")
    @Log(title = "账户基础", businessType = BusinessType.DELETE)
    @DeleteMapping("/{accountBaseIds}")
    public R<Boolean> remove(@PathVariable Long[] accountBaseIds) {
        return accountBaseService.deleteByAccountBaseIds(accountBaseIds);
    }

    /**
     * 下载模板
     */
    @ApiOperation("下载模板")
    @Log(title = "下载模板", businessType = BusinessType.EXPORT)
    @GetMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response) {
        ExcelUtil<AccountBase> util = new ExcelUtil<>(AccountBase.class);
        util.exportExcel(response, new ArrayList<>(), "下载模板", "账户基础数据", "账户基础");
    }

    @ApiOperation("导出账户基础列表")
    @PreAuthorize("@ss.hasAnyPermi('account:base:export')")
    @Log(title = "账户基础", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody AccountBaseQueryDTO accountBase) {
        List<AccountBase> list = accountBaseService.selectList(accountBase);
        ExcelUtil<AccountBase> util = new ExcelUtil<>(AccountBase.class);
        util.exportExcel(response, list, accountBase.getExportFileName(), "账户基础数据", "账户基础");
    }

    @ApiOperation("导入账户基础列表")
    @PreAuthorize("@ss.hasAnyPermi('account:base:export')")
    @Log(title = "账户基础", businessType = BusinessType.EXPORT)
    @PostMapping("/importExcel")
    public R<Boolean> importExcel(@RequestPart @ApiParam(name = "file", value = "文件", required = true) MultipartFile file) {
        ExcelUtil<AccountBase> util = new ExcelUtil<>(AccountBase.class);
        List<AccountBase> accountBaseList = null;
        try {
            accountBaseList = util.importExcel(file.getInputStream(), 1);
        } catch (Exception e) {
            logger.error("导入账户基础列表异常：{}", e.getMessage());
            return R.fail("导入账户基础列表异常，请稍后重试！");
        }
        if (CollectionUtils.isEmpty(accountBaseList)) {
            return R.fail("数据为空！");
        }
        accountBaseList.forEach(t -> {
            t.setCreateId(this.getUserId());
            t.setCreateBy(this.getUsername());
            t.setCreateTime(DateUtils.getNowDate());
            t.setDelFlag("0");
        });
        return accountBaseService.importExcel(accountBaseList);
    }

}
