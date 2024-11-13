package com.jcwl.admin.book.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.jcwl.admin.book.domain.Book;
import com.jcwl.admin.book.dto.BookQueryDTO;

/**
 * 账本Mapper接口
 *
 * @author jcwl
 * @date 2024-10-18
 */
public interface BookMapper
{
    /**
     * 查询账本
     */
    public Book selectById(Long id);

    /**
     * 查询账本列表
     */
    public List<Book> selectList(BookQueryDTO book);

    /**
     * 查询账本列表
     */
    List<Book> selectBookList(BookQueryDTO book);

    /**
     * 新增账本
     */
    public int insert(Book book);

    /**
     * 批量新增账本
     */
    public int insertBatch(@Param("list") List<Book> bookList);

    /**
     * 修改账本
     */
    public int update(Book book);

    /**
     * 批量修改账本
     */
    int updateBatch(@Param("list") List<Book> bookList);

    /**
     * 删除账本
     */
    public int deleteById(Long id);

    /**
     * 批量删除账本
     */
    public int deleteByIds(Long[] ids);
}
