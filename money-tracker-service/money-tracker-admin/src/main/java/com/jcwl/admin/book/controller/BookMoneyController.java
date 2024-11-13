package com.jcwl.admin.book.controller;

import com.jcwl.admin.book.domain.BookMoney;
import com.jcwl.admin.book.dto.*;
import com.jcwl.admin.book.service.IBookMoneyService;
import com.jcwl.common.annotation.Anonymous;
import com.jcwl.common.annotation.Log;
import com.jcwl.common.core.controller.BaseController;
import com.jcwl.common.core.domain.R;
import com.jcwl.common.core.page.TableDataInfo;
import com.jcwl.common.enums.BusinessType;
import com.jcwl.common.exception.BizPreconditions;
import com.jcwl.common.utils.DateUtils;
import com.jcwl.common.utils.poi.CSVUtils;
import com.jcwl.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.jcwl.common.constant.Constants.GBK;
import static com.jcwl.common.constant.Constants.UTF8;

/**
 * 账本明细Controller
 *
 * @author jcwl
 * @date 2024-10-18
 */
@Api(tags = "账本明细管理")
@RestController
@RequestMapping("/book/money")
public class BookMoneyController extends BaseController {
    @Autowired
    private IBookMoneyService bookMoneyService;

    @ApiOperation("查询账本明细列表")
    @PreAuthorize("@ss.hasAnyPermi('book:money:list')")
    @PostMapping("/list")
    public TableDataInfo<BookMoney> list(@RequestBody BookMoneyQueryDTO bookMoney) {
        startPage(bookMoney);
        List<BookMoney> list = bookMoneyService.selectList(bookMoney);
        return getDataTable(list);
    }

    @ApiOperation("查询账本明细聚合列表")
    @PreAuthorize("@ss.hasAnyPermi('book:money:list')")
    @PostMapping("/group/list")
    public TableDataInfo<BookMoneyGroupVO> groupList(@Validated @RequestBody BookMoneyQueryDTO bookMoney) {
        bookMoney.setReasonable(Boolean.FALSE);
        startPage(bookMoney);
        List<BookMoneyGroupVO> list = bookMoneyService.groupList(bookMoney);
        return getDataTable(list);
    }

    @ApiOperation("查询账本明细日历聚合列表")
    @PreAuthorize("@ss.hasAnyPermi('book:money:list')")
    @PostMapping("/calendar/group/list")
    @Anonymous
    public R<List<BookMoneyGroupVO>> calendarGroupList(@Validated @RequestBody BookMoneyQueryDTO bookMoney) {
        List<BookMoneyGroupVO> list = bookMoneyService.calendarGroupList(bookMoney);
        return R.ok(list);
    }

    @ApiOperation("统计账本明细信息")
    @PostMapping("/count/info")
    public R<List<BookMoneyCountVO>> getCountInfo(@Validated @RequestBody BookMoneyQueryDTO bookMoney) {
        return R.ok(bookMoneyService.getCountInfo(bookMoney));
    }

    @ApiOperation("获取账本明细详细信息")
    @GetMapping(value = "/{id}")
    public R<BookMoney> getInfo(@PathVariable("id") Long id) {
        return R.ok(bookMoneyService.selectById(id));
    }

    @ApiOperation("新增账本明细")
    @PreAuthorize("@ss.hasAnyPermi('book:money:add')")
    @Log(title = "账本明细", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Boolean> add(@RequestBody BookMoney bookMoney) {
        bookMoney.setCreateId(this.getUserId());
        bookMoney.setCreateBy(this.getUsername());
        if (bookMoney.getUserId() == null) {
            bookMoney.setUserId(this.getUserId());
        }
        if (bookMoney.getUserName() == null) {
            bookMoney.setUserName(this.getUsername());
        }
        if (bookMoney.getNickName() == null) {
            bookMoney.setNickName(this.getLoginUser().getUser().getNickName());
        }
        bookMoney.setCreateTime(DateUtils.getNowDate());
        return bookMoneyService.insert(bookMoney);
    }

    @ApiOperation("修改账本明细")
    @PreAuthorize("@ss.hasAnyPermi('book:money:edit')")
    @Log(title = "账本明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Boolean> edit(@RequestBody BookMoney bookMoney) {
        bookMoney.setUpdateId(this.getUserId());
        bookMoney.setUpdateBy(this.getUsername());
        bookMoney.setUpdateTime(DateUtils.getNowDate());
        return bookMoneyService.update(bookMoney);
    }

    @ApiOperation("删除账本明细")
    @PreAuthorize("@ss.hasAnyPermi('book:money:remove')")
    @Log(title = "账本明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Boolean> remove(@PathVariable Long[] ids) {
        return bookMoneyService.deleteByIds(ids);
    }

    /**
     * 下载模板
     */
    @ApiOperation("下载模板")
    @Log(title = "下载模板", businessType = BusinessType.EXPORT)
    @GetMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response) {
        ExcelUtil<BookMoney> util = new ExcelUtil<>(BookMoney.class);
        util.exportExcel(response, new ArrayList<>(), "下载模板", "账本明细数据", "账本明细");
    }

    @ApiOperation("导出账本明细列表")
    @PreAuthorize("@ss.hasAnyPermi('book:money:export')")
    @Log(title = "账本明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody BookMoneyQueryDTO bookMoney) {
        List<BookMoney> list = bookMoneyService.selectList(bookMoney);
        ExcelUtil<BookMoney> util = new ExcelUtil<>(BookMoney.class);
        util.exportExcel(response, list, bookMoney.getExportFileName(), "账本明细数据", "账本明细");
    }

    @ApiOperation("导入账本明细列表")
    @PreAuthorize("@ss.hasAnyPermi('book:money:export')")
    @Log(title = "账本明细", businessType = BusinessType.EXPORT)
    @PostMapping("/importExcel")
    public R<Boolean> importExcel(@RequestPart @ApiParam(name = "file", value = "文件", required = true) MultipartFile file) {
        ExcelUtil<BookMoney> util = new ExcelUtil<>(BookMoney.class);
        List<BookMoney> bookMoneyList = null;
        try {
            bookMoneyList = util.importExcel(file.getInputStream(), 1);
        } catch (Exception e) {
            logger.error("导入账本明细列表异常：{}", e.getMessage());
            return R.fail("导入账本明细列表异常，请稍后重试！");
        }
        if (CollectionUtils.isEmpty(bookMoneyList)) {
            return R.fail("数据为空！");
        }
        bookMoneyList.forEach(t -> {
            t.setCreateId(this.getUserId());
            t.setCreateBy(this.getUsername());
            t.setCreateTime(DateUtils.getNowDate());
            t.setDelFlag("0");
        });
        return bookMoneyService.importExcel(bookMoneyList);
    }


    @ApiOperation("导入支付宝对账单表格")
    @PreAuthorize("@ss.hasAnyPermi('book:money:export')")
    @Log(title = "账本明细", businessType = BusinessType.EXPORT)
    @PostMapping("/import/ali/pay/excel")
    public R<Boolean> importAliPayExcel(@RequestPart @ApiParam(name = "file", value = "文件", required = true) MultipartFile file,
                                        @RequestParam("bookId") Long bookId,
                                        @RequestParam("userId") Long userId) {
        ExcelUtil<AliPayImportDTO> util = new ExcelUtil<>(AliPayImportDTO.class);
        List<AliPayImportDTO> importDTOList;
        try {
            importDTOList = util.importExcel(file.getInputStream(), 24);
        } catch (Exception e) {
            logger.error("导入支付宝对账单表格异常：{}", e.getMessage());
            return R.fail("导入支付宝对账单异常，请稍后重试！");
        }
        if (CollectionUtils.isEmpty(importDTOList)) {
            return R.fail("数据为空！");
        }
        return bookMoneyService.importAliPayExcel(importDTOList, bookId, userId);
    }

    @ApiOperation("导入微信对账单表格")
    @PreAuthorize("@ss.hasAnyPermi('book:money:export')")
    @Log(title = "账本明细", businessType = BusinessType.EXPORT)
    @PostMapping("/import/wechat/pay/excel")
    public R<Boolean> importWechatPayExcel(@RequestPart @ApiParam(name = "file", value = "文件", required = true) MultipartFile file,
                                           @RequestParam("bookId") Long bookId,
                                           @RequestParam("userId") Long userId) {
        ExcelUtil<WechatPayImportDTO> util = new ExcelUtil<>(WechatPayImportDTO.class);
        List<WechatPayImportDTO> importDTOList;
        try {
            importDTOList = util.importExcel(file.getInputStream(), 16);
        } catch (Exception e) {
            logger.error("导入微信对账单表格异常：{}", e.getMessage());
            return R.fail("导入微信对账单异常，请稍后重试！");
        }
        if (CollectionUtils.isEmpty(importDTOList)) {
            return R.fail("数据为空！");
        }
        return bookMoneyService.importWechatPayExcel(importDTOList, bookId, userId);
    }

    @ApiOperation("导入支付宝对账单Zip")
    @PreAuthorize("@ss.hasAnyPermi('book:money:export')")
    @PostMapping("/import/ali/pay/zip")
    public R<List<AliPayImportDTO>> importAliPayZip(@RequestPart @ApiParam(name = "file", value = "文件", required = true) MultipartFile file,
                                                    @RequestParam("password") String password,
                                                    @RequestParam("bookId") Long bookId,
                                                    @RequestParam("userId") Long userId) {
        CSVUtils<AliPayImportDTO> util = new CSVUtils<>(AliPayImportDTO.class);
        List<AliPayImportDTO> importDTOList;
        File newFile = null;
        String tempDirPath = null;

        try {
            newFile = convertMultipartFileToFile(file);
            tempDirPath = createAndExtractZip(newFile, password);

            // 递归查找第一个 .csv 文件
            File csvFile = findFirstCSVFile(new File(tempDirPath));
            if (csvFile == null) {
                return R.fail("CSV 文件不存在！");
            }

            try (InputStream inputStream = new FileInputStream(csvFile)) {
                importDTOList = util.importCsv(inputStream, 24, GBK);
            }

            if (CollectionUtils.isEmpty(importDTOList)) {
                return R.fail("数据为空！");
            }

            return R.ok(importDTOList);
        } catch (Exception e) {
            logger.error("导入支付宝对账单失败：{}", e.getMessage(), e);
            return R.fail("导入失败：" + e.getMessage());
        } finally {
            // 清理临时文件
            if (newFile != null && newFile.exists()) {
                newFile.delete();
            }
            if (tempDirPath != null) {
                File tempDir = new File(tempDirPath);
                if (tempDir.exists()) {
                    try {
                        FileUtils.deleteDirectory(tempDir);
                    } catch (IOException e) {
                        logger.error("删除临时目录失败：{}", e.getMessage());
                    }
                }
            }
        }
    }

    @ApiOperation("导入微信对账单Zip")
    @PreAuthorize("@ss.hasAnyPermi('book:money:export')")
    @PostMapping("/import/wechat/pay/zip")
    public R<List<WechatPayImportDTO>> importWechatPayZip(@RequestPart @ApiParam(name = "file", value = "文件", required = true) MultipartFile file,
                                                          @RequestParam("password") String password,
                                                          @RequestParam("bookId") Long bookId,
                                                          @RequestParam("userId") Long userId) {
        CSVUtils<WechatPayImportDTO> util = new CSVUtils<>(WechatPayImportDTO.class);
        List<WechatPayImportDTO> importDTOList;
        File newFile = null;
        String tempDirPath = null;

        try {
            newFile = convertMultipartFileToFile(file);
            tempDirPath = createAndExtractZip(newFile, password);

            // 递归查找第一个 .csv 文件
            File csvFile = findFirstCSVFile(new File(tempDirPath));
            if (csvFile == null) {
                return R.fail("CSV 文件不存在！");
            }

            try (InputStream inputStream = new FileInputStream(csvFile)) {
                importDTOList = util.importCsv(inputStream, 16, UTF8);
            }

            if (CollectionUtils.isEmpty(importDTOList)) {
                return R.fail("数据为空！");
            }

            return R.ok(importDTOList);
        } catch (Exception e) {
            logger.error("导入微信对账单失败：{}", e.getMessage(), e);
            return R.fail("导入失败：" + e.getMessage());
        } finally {
            // 清理临时文件
            if (newFile != null && newFile.exists()) {
                newFile.delete();
            }
            if (tempDirPath != null) {
                File tempDir = new File(tempDirPath);
                if (tempDir.exists()) {
                    try {
                        FileUtils.deleteDirectory(tempDir);
                    } catch (IOException e) {
                        logger.error("删除临时目录失败：{}", e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * 将 MultipartFile 转换为 File
     *
     * @param multipartFile MultipartFile 对象
     * @return 转换后的 File 对象
     * @throws IOException 如果转换过程中发生 IO 异常
     */
    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();

        // 检查文件名是否为空
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("文件名不能为空");
        }

        // 检查文件名是否包含扩展名
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex == -1) {
            // 文件名没有扩展名，使用默认扩展名
            File file = File.createTempFile(originalFilename, ".tmp");
            multipartFile.transferTo(file);
            file.deleteOnExit();
            return file;
        }

        // 文件名包含扩展名
        String namePart = originalFilename.substring(0, dotIndex);
        String extensionPart = originalFilename.substring(dotIndex);
        File file = File.createTempFile(namePart, extensionPart);
        multipartFile.transferTo(file);
        file.deleteOnExit();
        return file;
    }

    /**
     * 创建临时目录并解压 ZIP 文件
     *
     * @param file     ZIP 文件
     * @param password ZIP 文件密码
     * @return 临时目录路径
     * @throws Exception 如果解压过程中发生异常
     */
    private String createAndExtractZip(File file, String password) throws Exception {
        String tempDirPath = System.getProperty("java.io.tmpdir") + File.separator + "pay-import";
        File tempDir = new File(tempDirPath);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }

        ZipFile zipFile = new ZipFile(file, password.toCharArray());
        zipFile.extractAll(tempDirPath);

        return tempDirPath;
    }

    /**
     * 递归查找目录及其子目录中的第一个 .csv 文件
     *
     * @param dir 目录对象
     * @return 第一个 .csv 文件，如果未找到则返回 null
     */
    private File findFirstCSVFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return null;
        }

        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    File result = findFirstCSVFile(file);
                    if (result != null) {
                        return result;
                    }
                } else if (file.getName().toLowerCase().endsWith(".csv")) {
                    return file;
                }
            }
        }

        return null;
    }

    @ApiOperation("导入支付宝对账单CSV")
    @PreAuthorize("@ss.hasAnyPermi('book:money:export')")
    @Log(title = "账本明细", businessType = BusinessType.EXPORT)
    @PostMapping("/import/ali/pay/csv")
    public R<List<AliPayImportDTO>> importAliPayCsv(@RequestPart @ApiParam(name = "file", value = "文件", required = true) MultipartFile file,
                                                    @RequestParam("bookId") Long bookId,
                                                    @RequestParam("userId") Long userId) {
        CSVUtils<AliPayImportDTO> util = new CSVUtils<>(AliPayImportDTO.class);
        List<AliPayImportDTO> importDTOList;
        try {
            importDTOList = util.importCsv(file.getInputStream(), 24, GBK);
        } catch (Exception e) {
            logger.error("导入支付宝对账单CSV异常：{}", e.getMessage());
            return R.fail("导入支付宝对账单异常，请稍后重试！");
        }
        if (CollectionUtils.isEmpty(importDTOList)) {
            return R.fail("数据为空！");
        }
        return R.ok(importDTOList);
    }

    @ApiOperation("导入微信对账单CSV")
    @PreAuthorize("@ss.hasAnyPermi('book:money:export')")
    @Log(title = "账本明细", businessType = BusinessType.EXPORT)
    @PostMapping("/import/wechat/pay/csv")
    public R<List<WechatPayImportDTO>> importWechatPayCsv(@RequestPart @ApiParam(name = "file", value = "文件", required = true) MultipartFile file,
                                                          @RequestParam("bookId") Long bookId,
                                                          @RequestParam("userId") Long userId) {
        CSVUtils<WechatPayImportDTO> util = new CSVUtils<>(WechatPayImportDTO.class);
        List<WechatPayImportDTO> importDTOList;
        try {
            importDTOList = util.importCsv(file.getInputStream(), 16, UTF8);
        } catch (Exception e) {
            logger.error("导入微信对账单CSV异常：{}", e.getMessage());
            return R.fail("导入微信对账单CSV异常，请稍后重试！");
        }
        if (CollectionUtils.isEmpty(importDTOList)) {
            return R.fail("数据为空！");
        }
        return R.ok(importDTOList);
    }

    @ApiOperation("导入支付宝对账单Json")
    @PreAuthorize("@ss.hasAnyPermi('book:money:export')")
    @Log(title = "账本明细", businessType = BusinessType.EXPORT)
    @PostMapping("/import/ali/pay/json")
    public R<Boolean> importAliPayJson(@RequestBody List<ImportJsonDTO> importJsonDTOList,
                                       @RequestParam("bookId") Long bookId,
                                       @RequestParam("userId") Long userId) {
        if (CollectionUtils.isEmpty(importJsonDTOList)) {
            return R.fail("数据为空！");
        }
        List<AliPayImportDTO> importDTOList = new ArrayList<>();
        importJsonDTOList.forEach(t -> {
            if (t != null) {
                AliPayImportDTO importDTO = new AliPayImportDTO();
                importDTO.setTradeTime(t.getDay() != null ? t.getDay() : null);
                importDTO.setTradeCategory(t.getType() != null ? t.getType() : null);
                importDTO.setTradeOpponent(t.getName() != null ? t.getName() : null);
                importDTO.setProductDescription(t.getDescription() != null ? t.getDescription() : null);
                importDTO.setIncomeExpense(t.getFlowType() != null ? t.getFlowType() : null);
                importDTO.setAmount(t.getMoney() != null ? t.getMoney() : null);
                importDTO.setPaymentMethod(t.getPayType() != null ? t.getPayType() : null);
                importDTOList.add(importDTO);
            }
        });
        return bookMoneyService.importAliPayExcel(importDTOList, bookId, userId);
    }

    @ApiOperation("导入微信对账单")
    @PreAuthorize("@ss.hasAnyPermi('book:money:export')")
    @Log(title = "账本明细", businessType = BusinessType.EXPORT)
    @PostMapping("/import/wechat/pay/json")
    public R<Boolean> importWechatPayJson(@RequestBody List<ImportJsonDTO> importJsonDTOList,
                                          @RequestParam("bookId") Long bookId,
                                          @RequestParam("userId") Long userId) {
        if (CollectionUtils.isEmpty(importJsonDTOList)) {
            return R.fail("数据为空！");
        }
        List<WechatPayImportDTO> importDTOList = new ArrayList<>();
        importJsonDTOList.forEach(t -> {
            if (t != null) {
                WechatPayImportDTO importDTO = new WechatPayImportDTO();
                importDTO.setTradeTime(t.getDay() != null ? t.getDay() : null);
                importDTO.setTradeCategory(t.getType() != null ? t.getType() : null);
                importDTO.setTradeOpponent(t.getName() != null ? t.getName() : null);
                importDTO.setProductDescription(t.getDescription() != null ? t.getDescription() : null);
                importDTO.setIncomeExpense(t.getFlowType() != null ? t.getFlowType() : null);
                importDTO.setAmount(t.getMoney() != null ? t.getMoney() : null);
                importDTO.setPaymentMethod(t.getPayType() != null ? t.getPayType() : null);
                importDTOList.add(importDTO);
            }
        });
        return bookMoneyService.importWechatPayExcel(importDTOList, bookId, userId);
    }


    @ApiOperation("基础统计账本报表")
    @PostMapping("/count/report")
    public R<BookMoneyReportVO> getCountReport(@RequestBody BookMoneyReportDTO reportDTO) {
        BizPreconditions.ifTrue(null == reportDTO.getBookId(), "请选择账本！");
        return R.ok(bookMoneyService.getCountReport(reportDTO));
    }

    @ApiOperation("基础统计报表趋势图")
    @PostMapping("/count/report/trend")
    public R<BookMoneyReportTrendVO> getCountReportTrend(@RequestBody BookMoneyReportDTO reportDTO) {
        BizPreconditions.ifTrue(null == reportDTO.getBookId(), "请选择账本！");
        return R.ok(bookMoneyService.getCountReportTrend(reportDTO));
    }

    @ApiOperation("分类报表统计")
    @PostMapping("/count/classify")
    public R<BookMoneyReportVO> getCountClassify(@RequestBody BookMoneyReportDTO reportDTO) {
        BizPreconditions.ifTrue(null == reportDTO.getBookId(), "请选择账本！");
        return R.ok(bookMoneyService.getCountClassify(reportDTO));
    }

    @ApiOperation("成员报表记账数据")
    @PostMapping("/count/member")
    public R<BookMemberResVO> getCountMember(@RequestBody BookMoneyReportDTO reportDTO) {
        BizPreconditions.ifTrue(null == reportDTO.getBookId(), "请选择账本！");
        return R.ok(bookMoneyService.getCountMember(reportDTO));
    }

    @ApiOperation("成员支出对比")
    @PostMapping("/count/member/compare")
    public R<BookMemberCompareResVO> getMemberCompare(@RequestBody BookMoneyReportDTO reportDTO) {
        BizPreconditions.ifTrue(null == reportDTO.getBookId(), "请选择账本！");
        return R.ok(bookMoneyService.getMemberCompare(reportDTO));
    }

    @ApiOperation("成员支出/收入统计")
    @PostMapping("/count/expenditure/income")
    public R<BookExpenditureIncomeResVO> expenditureIncome(@RequestBody BookMoneyReportDTO reportDTO) {
        BizPreconditions.ifTrue(null == reportDTO.getBookId(), "请选择账本！");
        BizPreconditions.ifTrue(null == reportDTO.getType(), "选择收入或开支！");
        return R.ok(bookMoneyService.expenditureIncome(reportDTO));
    }

    @ApiOperation("基础统计账本流水统计")
    @PostMapping("/count/flow/statistics")
    public R<BookLedgerFlowStatisticsVO> bookLedgerFlowStatistics(@RequestBody BookMoneyReportDTO reportDTO) {
        BizPreconditions.ifTrue(null == reportDTO.getBookId(), "请选择账本！");
        return R.ok(bookMoneyService.bookLedgerFlowStatistics(reportDTO));
    }

    @ApiOperation("账户统计")
    @PostMapping("/count/account/trend")
    public R<BookAccountTrendVO> getAccountTrend(@RequestBody BookMoneyReportDTO reportDTO) {
        BizPreconditions.ifTrue(null == reportDTO.getBookId(), "请选择账本！");
        return R.ok(bookMoneyService.getAccountTrend(reportDTO));
    }

    @ApiOperation("账户资产明细")
    @PostMapping("/count/account/detail")
    public R<BookAccountDetailRspVO> getAccountDetail(@RequestBody BookMoneyReportDTO reportDTO) {
        BizPreconditions.ifTrue(null == reportDTO.getBookId(), "请选择账本！");
        return R.ok(bookMoneyService.getAccountDetail(reportDTO));
    }


    @ApiOperation("账户资产折线图")
    @PostMapping("/count/account/line")
    @Anonymous
    public R<BookAccountLineVO> getAccountLine(@RequestBody BookMoneyReportDTO reportDTO) {
        BizPreconditions.ifTrue(null == reportDTO.getBookId(), "请选择账本！");
        return R.ok(bookMoneyService.getAccountLine(reportDTO));
    }
}
