package com.jcwl.admin.account.mapper;

import com.jcwl.admin.account.domain.Account;
import com.jcwl.admin.account.dto.AccountQueryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 账户Mapper接口
 *
 * @author jcwl
 * @date 2024-10-24
 */
public interface AccountMapper {
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
    public int insert(Account account);

    /**
     * 批量新增账户
     */
    public int insertBatch(@Param("list") List<Account> accountList);

    /**
     * 修改账户
     */
    public int update(Account account);

    /**
     * 批量修改账户
     */
    int updateBatch(@Param("list") List<Account> accountList);

    /**
     * 删除账户
     */
    public int deleteById(Long id);

    /**
     * 批量删除账户
     */
    public int deleteByIds(Long[] ids);

    /**
     *  根据账本ID查询账户列表
     * @param bookId
     * @return
     */
    List<Account> selectListByBookId(@Param("bookId") Long bookId);

    /**
     * 根据账本ID和基础账户ID查询账户
     * @param bookId
     * @param accountBaseId
     * @return
     */
    Account selectByBook(@Param("bookId") Long bookId, @Param("accountBaseId") Long accountBaseId);
}
