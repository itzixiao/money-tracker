package com.jcwl.admin.book.service.impl;

import com.jcwl.admin.account.domain.Account;
import com.jcwl.admin.account.domain.AccountBase;
import com.jcwl.admin.account.dto.AccountBaseQueryDTO;
import com.jcwl.admin.account.service.IAccountBaseService;
import com.jcwl.admin.account.service.IAccountService;
import com.jcwl.admin.book.domain.Book;
import com.jcwl.admin.book.domain.BookUser;
import com.jcwl.admin.book.dto.BookQueryDTO;
import com.jcwl.admin.book.enums.AuthType;
import com.jcwl.admin.book.mapper.BookMapper;
import com.jcwl.admin.book.service.IBookMoneyService;
import com.jcwl.admin.book.service.IBookService;
import com.jcwl.admin.book.service.IBookUserService;
import com.jcwl.admin.category.domain.Category;
import com.jcwl.admin.category.domain.CategoryBase;
import com.jcwl.admin.category.dto.CategoryBaseQueryDTO;
import com.jcwl.admin.category.service.ICategoryBaseService;
import com.jcwl.admin.category.service.ICategoryService;
import com.jcwl.common.core.domain.R;
import com.jcwl.common.core.domain.entity.SysUser;
import com.jcwl.common.exception.BizPreconditions;
import com.jcwl.common.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.jcwl.common.utils.SecurityUtils.getUserId;

/**
 * 账本Service业务层处理
 *
 * @author jcwl
 * @date 2024-10-18
 */
@Service
public class BookServiceImpl implements IBookService {
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private IBookUserService bookUserService;
    @Autowired
    private IBookMoneyService bookMoneyService;
    @Autowired
    private ICategoryBaseService categoryBaseService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IAccountBaseService accountBaseService;
    @Autowired
    private IAccountService accountService;

    /**
     * 查询账本
     */
    @Override
    public Book selectById(Long id) {
        return bookMapper.selectById(id);
    }

    /**
     * 查询账本列表
     */
    @Override
    public List<Book> selectList(BookQueryDTO book) {
        return bookMapper.selectBookList(book);
    }

    /**
     * 新增账本
     */
    @Override
    @Transactional
    public R<Boolean> insert(Book book) {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        BizPreconditions.ifNull(user, "请重新登录！");
        String bookName = book.getBookName();
        if (null == bookName || "".equals(bookName)) {
            book.setBookName(IBookService.DEFAULT_BOOK);
        }
        int insert = bookMapper.insert(book);
        BizPreconditions.ifFalse(insert > 0, "账本添加失败！");
        insertBookUser(book, user);
        insertCategory(book);
        insertAccount(book);

        return R.ok(Boolean.TRUE);
    }

    private void insertAccount(Book book) {
        List<AccountBase> accountBases = accountBaseService.selectList(new AccountBaseQueryDTO());
        if (CollectionUtils.isEmpty(accountBases)) {
            BizPreconditions.thrException("请先添加账户基础数据！");
        }
        List<Account> accountList = new ArrayList<>();
        for (AccountBase accountBase : accountBases) {
            Account account = new Account();
            BeanUtils.copyProperties(accountBase, account);
            account.setBookId(book.getId());
            accountList.add(account);
        }
        accountService.importExcel(accountList);
    }

    private void insertCategory(Book book) {
        List<CategoryBase> categoryBases = categoryBaseService.selectList(new CategoryBaseQueryDTO());
        if (CollectionUtils.isEmpty(categoryBases)) {
            BizPreconditions.thrException("请先添加分类基础数据！");
        }
        List<Category> categoryList = new ArrayList<>();
        for (CategoryBase categoryBase : categoryBases) {
            Category category = new Category();
            BeanUtils.copyProperties(categoryBase, category);
            category.setBookId(book.getId());
            categoryList.add(category);
        }
        categoryService.importExcel(categoryList);
    }

    private void insertBookUser(Book book, SysUser user) {
        BookUser bookUser = new BookUser();
        bookUser.setBookId(book.getId());
        bookUser.setUserId(user.getUserId());
        bookUser.setUserName(user.getUserName());
        bookUser.setNickName(user.getNickName());
        bookUser.setAvatar(user.getAvatar());
        bookUser.setDefaultBook(book.getDefaultBook());
        bookUser.setAuditStatus("1");
        bookUser.setStatus("0");
        bookUser.setAuth(AuthType.CREATOR.getValue());
        bookUser.setCreateId(user.getUserId());
        bookUser.setCreateBy(user.getUserName());
        bookUser.setCreateTime(book.getCreateTime());
        bookUserService.insert(bookUser);
    }

    /**
     * 修改账本
     */
    @Override
    public R<Boolean> update(Book book) {
        BizPreconditions.ifFalse(canEditOrDelete(book.getId()), "账本不可编辑！");
        int update = bookMapper.update(book);
        BookUser bookUser = bookUserService.selectByUserId(book.getUserId(), book.getId());
        bookUser.setDefaultBook(book.getDefaultBook());
        bookUserService.update(bookUser);
        return R.ok(update > 0);
    }

    /**
     * 批量删除账本
     */
    @Override
    public R<Boolean> deleteByIds(Long[] bookIds) {
        BizPreconditions.ifFalse(canDelete(bookIds), "账本不可删除！");
        int delete = bookMapper.deleteByIds(bookIds);
        bookMoneyService.deleteByBookId(bookIds);
        bookUserService.deleteByBookId(bookIds);
        categoryService.deleteByBookId(bookIds);
        return R.ok(delete > 0);
    }

    /**
     * 删除账本信息
     */
    @Override
    @Transactional
    public R<Boolean> deleteById(Long bookId) {
        BizPreconditions.ifFalse(canDelete(bookId), "账本不可删除！");
        int delete = bookMapper.deleteById(bookId);
        return R.ok(delete > 0);
    }

    @Override
    public R<Boolean> importExcel(List<Book> bookList) {
        int insert = bookMapper.insertBatch(bookList);
        return R.ok(insert > 0);
    }

    /**
     * 判断账本是否可以编辑删除
     */
    private Boolean canEditOrDelete(Long... ids) {
        return true;
    }

    /**
     * 判断账本是否可以编辑删除
     */
    private Boolean canDelete(Long... ids) {
        Long userId = getUserId();
        for (Long id : ids) {
            Book book = bookMapper.selectById(id);
            if (!book.getUserId().equals(userId)) {
                return false;
            }
        }
        return true;
    }
}
