package com.jcwl.admin.account.controller;

import com.jcwl.admin.account.domain.Account;
import com.jcwl.admin.account.dto.AccountQueryDTO;
import com.jcwl.admin.account.dto.AccountTreeSelectVO;
import com.jcwl.admin.account.service.IAccountService;
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
 * 账户Controller
 *
 * @author jcwl
 * @date 2024-10-24
 */
@Api(tags = "账户管理")
@RestController
@RequestMapping("/account/account")
public class AccountController extends BaseController {
    @Autowired
    private IAccountService accountService;

    @ApiOperation("查询账户列表")
    @PreAuthorize("@ss.hasAnyPermi('account:account:list')")
    @PostMapping("/list")
    public TableDataInfo<Account> list(@RequestBody AccountQueryDTO account) {
        startPage(account);
        List<Account> list = accountService.selectList(account);
        return getDataTable(list);
    }

    @ApiOperation("查询账户树")
    @PreAuthorize("@ss.hasAnyPermi('account:account:list')")
    @PostMapping("/tree")
    public R<List<AccountTreeSelectVO>> getAccountTree(@Validated @RequestBody AccountQueryDTO account) {
        account.setUserId(this.getUserId());
        List<AccountTreeSelectVO> list = accountService.getAccountTree(account);
        return R.ok(list);
    }

    @ApiOperation("获取账户详细信息")
    @GetMapping(value = "/{id}")
    public R<Account> getInfo(@PathVariable("id") Long id) {
        return R.ok(accountService.selectById(id));
    }

    @ApiOperation("新增账户")
    @PreAuthorize("@ss.hasAnyPermi('account:account:add')")
    @Log(title = "账户", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Boolean> add(@RequestBody Account account) {
        account.setCreateId(this.getUserId());
        account.setCreateBy(this.getUsername());
        account.setCreateTime(DateUtils.getNowDate());
        return accountService.insert(account);
    }

    @ApiOperation("修改账户")
    @PreAuthorize("@ss.hasAnyPermi('account:account:edit')")
    @Log(title = "账户", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Boolean> edit(@RequestBody Account account) {
        account.setUpdateId(this.getUserId());
        account.setUpdateBy(this.getUsername());
        account.setUpdateTime(DateUtils.getNowDate());
        return accountService.update(account);
    }

    @ApiOperation("删除账户")
    @PreAuthorize("@ss.hasAnyPermi('account:account:remove')")
    @Log(title = "账户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Boolean> remove(@PathVariable Long[] ids) {
        return accountService.deleteByIds(ids);
    }

    /**
     * 下载模板
     */
    @ApiOperation("下载模板")
    @Log(title = "下载模板", businessType = BusinessType.EXPORT)
    @GetMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response) {
        ExcelUtil<Account> util = new ExcelUtil<>(Account.class);
        util.exportExcel(response, new ArrayList<>(), "下载模板", "账户数据", "账户");
    }

    @ApiOperation("导出账户列表")
    @PreAuthorize("@ss.hasAnyPermi('account:account:export')")
    @Log(title = "账户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody AccountQueryDTO account) {
        List<Account> list = accountService.selectList(account);
        ExcelUtil<Account> util = new ExcelUtil<>(Account.class);
        util.exportExcel(response, list, account.getExportFileName(), "账户数据", "账户");
    }

    @ApiOperation("导入账户列表")
    @PreAuthorize("@ss.hasAnyPermi('account:account:export')")
    @Log(title = "账户", businessType = BusinessType.EXPORT)
    @PostMapping("/importExcel")
    public R<Boolean> importExcel(@RequestPart @ApiParam(name = "file", value = "文件", required = true) MultipartFile file) {
        ExcelUtil<Account> util = new ExcelUtil<>(Account.class);
        List<Account> accountList = null;
        try {
            accountList = util.importExcel(file.getInputStream(), 1);
        } catch (Exception e) {
            logger.error("导入账户列表异常：{}", e.getMessage());
            return R.fail("导入账户列表异常，请稍后重试！");
        }
        if (CollectionUtils.isEmpty(accountList)) {
            return R.fail("数据为空！");
        }
        accountList.forEach(t -> {
            t.setCreateId(this.getUserId());
            t.setCreateBy(this.getUsername());
            t.setCreateTime(DateUtils.getNowDate());
            t.setDelFlag("0");
        });
        return accountService.importExcel(accountList);
    }

}
