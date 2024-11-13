package com.jcwl.admin.book.service;

import com.jcwl.common.core.domain.R;
import java.util.List;
import com.jcwl.admin.book.domain.Book;
import com.jcwl.admin.book.dto.BookQueryDTO;

/**
 * 账本Service接口
 *
 * @author jcwl
 * @date 2024-10-18
 */
public interface IBookService {
    public  static  final String DEFAULT_BOOK = "默认账本";
    /**
     * 查询账本
     */
    public Book selectById(Long id);

    /**
     * 查询账本列表
     */
    public List<Book> selectList(BookQueryDTO book);

    /**
     * 新增账本
     */
    public R<Boolean> insert(Book book);

    /**
     * 修改账本
     */
    public R<Boolean> update(Book book);

    /**
     * 批量删除账本
     */
    public R<Boolean> deleteByIds(Long[] ids);

    /**
     * 删除账本信息
     */
    public R deleteById(Long id);

    /**
     * 导入账本列表
     */
    R<Boolean> importExcel(List<Book> bookList);

}
