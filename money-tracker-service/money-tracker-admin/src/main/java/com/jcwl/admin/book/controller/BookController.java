package com.jcwl.admin.book.controller;

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
import com.jcwl.admin.book.domain.Book;
import com.jcwl.admin.book.dto.BookQueryDTO;
import com.jcwl.admin.book.service.IBookService;
import com.jcwl.common.utils.poi.ExcelUtil;
import org.springframework.web.multipart.MultipartFile;
import com.jcwl.common.core.page.TableDataInfo;

/**
 * 账本Controller
 *
 * @author jcwl
 * @date 2024-10-18
 */
@Api(tags = "账本管理")
@RestController
@RequestMapping("/book/book")
public class BookController extends BaseController {
    @Autowired
    private IBookService bookService;

    @ApiOperation("查询账本列表")
    @PreAuthorize("@ss.hasAnyPermi('book:book:list')")
    @PostMapping("/list")
    public TableDataInfo<Book> list(@RequestBody BookQueryDTO book) {
        // startPage(book);
        book.setUserId(this.getUserId());
        List<Book> list = bookService.selectList(book);
        return getDataTable(list);
    }

    @ApiOperation("获取账本详细信息")
    @GetMapping(value = "/{id}")
    public R<Book> getInfo(@PathVariable("id") Long id) {
        return R.ok(bookService.selectById(id));
    }

    @ApiOperation("新增账本")
    @PreAuthorize("@ss.hasAnyPermi('book:book:add')")
    @Log(title = "账本", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Boolean> add(@RequestBody Book book) {
        book.setUserId(this.getUserId());
        book.setCreateId(this.getUserId());
        book.setCreateBy(this.getUsername());
        book.setCreateTime(DateUtils.getNowDate());
        return bookService.insert(book);
    }

    @ApiOperation("修改账本")
    @PreAuthorize("@ss.hasAnyPermi('book:book:edit')")
    @Log(title = "账本", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Boolean> edit(@RequestBody Book book) {
        book.setUpdateId(this.getUserId());
        book.setUpdateBy(this.getUsername());
        book.setUpdateTime(DateUtils.getNowDate());
        return bookService.update(book);
    }

    @ApiOperation("删除账本")
    @PreAuthorize("@ss.hasAnyPermi('book:book:remove')")
    @Log(title = "账本", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public R<Boolean> remove(@PathVariable Long[] ids) {
        return bookService.deleteByIds(ids);
    }

    /**
     * 下载模板
     */
    @ApiOperation("下载模板")
    @Log(title = "下载模板", businessType = BusinessType.EXPORT)
    @GetMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response) {
        ExcelUtil<Book> util = new ExcelUtil<>(Book.class);
        util.exportExcel(response, new ArrayList<>(), "下载模板", "账本数据", "账本");
    }

    @ApiOperation("导出账本列表")
    @PreAuthorize("@ss.hasAnyPermi('book:book:export')")
    @Log(title = "账本", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response,@RequestBody BookQueryDTO book) {
        List<Book> list = bookService.selectList(book);
        ExcelUtil<Book> util = new ExcelUtil<>(Book.class);
        util.exportExcel(response, list, book.getExportFileName(), "账本数据", "账本");
    }

    @ApiOperation("导入账本列表")
    @PreAuthorize("@ss.hasAnyPermi('book:book:export')")
    @Log(title = "账本", businessType = BusinessType.EXPORT)
    @PostMapping("/importExcel")
    public R<Boolean> importExcel(@RequestPart @ApiParam(name = "file", value = "文件", required = true) MultipartFile file) {
        ExcelUtil<Book> util = new ExcelUtil<>(Book.class);
        List<Book> bookList = null;
        try {
            bookList = util.importExcel(file.getInputStream(), 1);
        } catch (Exception e) {
            logger.error("导入账本列表异常：{}", e.getMessage());
            return R.fail("导入账本列表异常，请稍后重试！");
        }
        if(CollectionUtils.isEmpty(bookList)){
            return R.fail("数据为空！");
        }
        bookList.forEach(t->{
            t.setCreateId(this.getUserId());
            t.setCreateBy(this.getUsername());
            t.setCreateTime(DateUtils.getNowDate());
            t.setDelFlag("0");
        });
        return bookService.importExcel(bookList);
    }

}
