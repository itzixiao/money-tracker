package com.jcwl.admin.book.service;

import com.jcwl.admin.book.domain.BookUser;
import com.jcwl.admin.book.dto.BookUserQueryDTO;
import com.jcwl.common.core.domain.R;

import java.util.List;

/**
 * 账本用户Service接口
 *
 * @author jcwl
 * @date 2024-10-15
 */
public interface IBookUserService {
    /**
     * 查询账本用户
     */
    public BookUser selectById(Long id);

    /**
     * 查询账本用户列表
     */
    public List<BookUser> selectList(BookUserQueryDTO bookUser);

    /**
     * 新增账本用户
     */
    public R<Boolean> insert(BookUser bookUser);

    /**
     * 修改账本用户
     */
    public R<Boolean> update(BookUser bookUser);

    /**
     * 批量删除账本用户
     */
    public R<Boolean> deleteByIds(Long[] ids);

    /**
     * 删除账本用户信息
     */
    public R deleteById(Long id);

    /**
     * 导入账本用户列表
     */
    R<Boolean> importExcel(List<BookUser> bookUserList);

    /**
     * 删除账本用户信息
     */
    public R<Boolean> deleteByBookId(Long[] bookIds);

    /**
     * 邀请用户加入账本
     */
    R<Boolean> inviteUser(BookUser bookUser);

    /**
     * 根据用户id和账本id查询账本用户信息
     */
    BookUser selectByUserId(Long userId, Long bookId);
}
