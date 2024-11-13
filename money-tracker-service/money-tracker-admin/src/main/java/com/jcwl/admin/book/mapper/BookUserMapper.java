package com.jcwl.admin.book.mapper;

import com.jcwl.admin.book.domain.BookUser;
import com.jcwl.admin.book.dto.BookUserQueryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 账本用户Mapper接口
 *
 * @author jcwl
 * @date 2024-10-15
 */
public interface BookUserMapper {
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
    public int insert(BookUser bookUser);

    /**
     * 批量新增账本用户
     */
    public int insertBatch(@Param("list") List<BookUser> bookUserList);

    /**
     * 修改账本用户
     */
    public int update(BookUser bookUser);

    /**
     * 批量修改账本用户
     */
    int updateBatch(@Param("list") List<BookUser> bookUserList);

    /**
     * 删除账本用户
     */
    public int deleteById(Long id);

    /**
     * 批量删除账本用户
     */
    public int deleteByIds(Long[] ids);

    /**
     * 删除账本用户
     */
    public int deleteByBookId(Long[] bookIds);

    /**
     * 查询邀请用户
     */
    BookUser checkInviteUser(BookUser bookUser);

    /**
     * 根据用户id和账本id查询账本用户信息
     */
    BookUser selectByUserId(@Param("userId") Long userId, @Param("bookId") Long bookId);
}
