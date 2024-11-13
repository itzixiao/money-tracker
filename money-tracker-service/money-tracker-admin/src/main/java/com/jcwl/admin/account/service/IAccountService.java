package com.jcwl.admin.account.service;

import com.jcwl.admin.account.domain.Account;
import com.jcwl.admin.account.dto.AccountQueryDTO;
import com.jcwl.admin.account.dto.AccountTreeSelectVO;
import com.jcwl.admin.book.domain.BookMoney;
import com.jcwl.common.core.domain.R;

import java.util.List;

/**
 * 账户Service接口
 *
 * @author jcwl
 * @date 2024-10-24
 */
public interface IAccountService {
    /**
     * 查询账户
     */
    public Account selectById(Long id);

    /**
     * 查询账户列表
     */
    public List<Account> selectList(AccountQueryDTO account);

    /**
     * 新增账户
     */
    public R<Boolean> insert(Account account);

    /**
     * 修改账户
     */
    public R<Boolean> update(Account account);

    /**
     * 更新账户余额
     */
    public R<Boolean> updateAccountBalance(BookMoney bookMoney);

    /**
     * 批量删除账户
     */
    public R<Boolean> deleteByIds(Long[] ids);

    /**
     * 删除账户信息
     */
    public R deleteById(Long id);

    /**
     * 导入账户列表
     */
    R<Boolean> importExcel(List<Account> accountList);

    /**
     * 查询账户树
     */
    List<AccountTreeSelectVO> getAccountTree(AccountQueryDTO category);

    /**
     *  根据账本id查询账户列表
     * @param bookId  账本id
     * @return
     */
    List<Account> selectListByBookId(Long bookId);

    /**
     *  根据账本id和基础账户id查询账户信息
     * @param bookId  账本id
     * @param accountBaseId  基础账户id
     * @return
     */
    Account selectByBook(Long bookId,Long accountBaseId);
}
