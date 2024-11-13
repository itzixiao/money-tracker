package com.jcwl.admin.book.service.impl;

import com.jcwl.admin.book.domain.BookUser;
import com.jcwl.admin.book.dto.BookUserQueryDTO;
import com.jcwl.admin.book.enums.AuthType;
import com.jcwl.admin.book.mapper.BookUserMapper;
import com.jcwl.admin.book.service.IBookMoneyService;
import com.jcwl.admin.book.service.IBookUserService;
import com.jcwl.common.core.domain.R;
import com.jcwl.common.core.domain.entity.SysUser;
import com.jcwl.common.exception.BizPreconditions;
import com.jcwl.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 账本用户Service业务层处理
 *
 * @author jcwl
 * @date 2024-10-15
 */
@Service
public class BookUserServiceImpl implements IBookUserService {
    @Autowired
    private BookUserMapper bookUserMapper;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private IBookMoneyService bookMoneyService;

    /**
     * 查询账本用户
     */
    @Override
    public BookUser selectById(Long id) {
        return bookUserMapper.selectById(id);
    }

    @Override
    public BookUser selectByUserId(Long userId, Long bookId) {
        return bookUserMapper.selectByUserId(userId, bookId);
    }

    /**
     * 查询账本用户列表
     */
    @Override
    public List<BookUser> selectList(BookUserQueryDTO bookUser) {
        return bookUserMapper.selectList(bookUser);
    }

    /**
     * 新增账本用户
     */
    @Override
    public R<Boolean> insert(BookUser bookUser) {
        int insert = bookUserMapper.insert(bookUser);
        return R.ok(insert > 0);
    }

    @Override
    public R<Boolean> inviteUser(BookUser bookUser) {
        SysUser sysUser = userService.selectUserByUserName(bookUser.getUserName());
        BizPreconditions.ifNull(sysUser, "您邀请的用户不存在！");
        bookUser.setUserId(sysUser.getUserId());
        bookUser.setUserName(sysUser.getUserName());
        bookUser.setNickName(sysUser.getNickName());
        bookUser.setAvatar(sysUser.getAvatar());
        bookUser.setDefaultBook(0L);
        bookUser.setAuditStatus("1");
        bookUser.setStatus("0");
        bookUser.setAuth(AuthType.MEMBER.getValue());
        BookUser bookUserCheck =bookUserMapper.checkInviteUser(bookUser);
        BizPreconditions.ifNotNull(bookUserCheck, "您邀请的用户已加入到当前账本！");
        int insert = bookUserMapper.insert(bookUser);
        return R.ok(insert > 0);
    }

    /**
     * 修改账本用户
     */
    @Override
    public R<Boolean> update(BookUser bookUser) {
        BizPreconditions.ifFalse(canEditOrDelete(bookUser.getId()), "账本用户不可编辑！");
        BookUser oldBokUser = bookUserMapper.selectById(bookUser.getId());
        BizPreconditions.ifNull(oldBokUser, "账本用户不可编辑！");
        int update = bookUserMapper.update(bookUser);
        if (!oldBokUser.getNickName().equals(bookUser.getNickName())) {
            bookMoneyService.updateNickName(oldBokUser.getBookId(), oldBokUser.getUserId(), bookUser.getNickName());
        }
        return R.ok(update > 0);
    }

    /**
     * 批量删除账本用户
     */
    @Override
    public R<Boolean> deleteByIds(Long[] ids) {
        BizPreconditions.ifFalse(canEditOrDelete(ids), "账本用户不可删除！");
        int delete = bookUserMapper.deleteByIds(ids);
        return R.ok(delete > 0);
    }

    /**
     * 删除账本用户信息
     */
    @Override
    public R<Boolean> deleteById(Long id) {
        BizPreconditions.ifFalse(canEditOrDelete(id), "账本用户不可删除！");
        int delete = bookUserMapper.deleteById(id);
        return R.ok(delete > 0);
    }

    @Override
    public R<Boolean> deleteByBookId(Long[] bookIds) {
        BizPreconditions.ifFalse(canEditOrDelete(bookIds), "账本明细不可删除！");
        int delete = bookUserMapper.deleteByBookId(bookIds);
        return R.ok(delete > 0);
    }

    @Override
    public R<Boolean> importExcel(List<BookUser> bookUserList) {
        int insert = bookUserMapper.insertBatch(bookUserList);
        return R.ok(insert > 0);
    }

    /**
     * 判断账本用户是否可以编辑删除
     */
    private Boolean canEditOrDelete(Long... ids) {
        return true;
    }
}
