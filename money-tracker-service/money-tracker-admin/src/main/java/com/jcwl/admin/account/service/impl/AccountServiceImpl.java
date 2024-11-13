package com.jcwl.admin.account.service.impl;

import com.jcwl.admin.account.domain.Account;
import com.jcwl.admin.account.dto.AccountQueryDTO;
import com.jcwl.admin.account.dto.AccountTreeSelectVO;
import com.jcwl.admin.account.mapper.AccountMapper;
import com.jcwl.admin.account.service.IAccountService;
import com.jcwl.admin.book.domain.BookMoney;
import com.jcwl.admin.book.enums.PaymentType;
import com.jcwl.admin.common.constant.CacheConstants;
import com.jcwl.common.core.domain.R;
import com.jcwl.common.core.redis.RedisCache;
import com.jcwl.common.exception.BizPreconditions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 账户Service业务层处理
 *
 * @author jcwl
 * @date 2024-10-24
 */
@Service
public class AccountServiceImpl implements IAccountService {
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private RedisCache redisCache;

    /**
     * 查询账户
     */
    @Override
    public Account selectById(Long id) {
        return accountMapper.selectById(id);
    }

    /**
     * 查询账户列表
     */
    @Override
    public List<Account> selectList(AccountQueryDTO account) {
        return accountMapper.selectList(account);
    }

    @Override
    public List<AccountTreeSelectVO> getAccountTree(AccountQueryDTO account) {
        String categoryCacheKey = CacheConstants.RECENT_USE_ACCOUNT + account.getUserId() + "_" + account.getBookId();
        List<AccountTreeSelectVO> accountCacheList = redisCache.getCacheList(categoryCacheKey);
        List<Account> accountList = accountMapper.selectList(account);

        // 创建一个 map 来存储所有 account，key 为 accountBaseId
        Map<Long, AccountTreeSelectVO> accountMap = new HashMap<>();
        for (Account entity : accountList) {
            AccountTreeSelectVO treeSelectVO = new AccountTreeSelectVO();
            BeanUtils.copyProperties(entity, treeSelectVO);
            accountMap.put(treeSelectVO.getAccountBaseId(), treeSelectVO);
        }

        // 构建树结构
        List<AccountTreeSelectVO> tree = accountList.stream()
                .filter(entity -> entity.getParentId() == 0)
                .map(entity -> accountMap.get(entity.getAccountBaseId()))
                .peek(entity -> entity.setChildren(getChildren(entity, accountMap)))
                .collect(Collectors.toList());

        // 将 accountCacheList 插入到 tree 的第一个位置
        tree.addAll(0, accountCacheList);
        return tree;
    }

    @Override
    public List<Account> selectListByBookId(Long bookId) {
        return accountMapper.selectListByBookId(bookId);
    }

    @Override
    public Account selectByBook(Long bookId, Long accountBaseId) {
        return accountMapper.selectByBook(bookId, accountBaseId);
    }

    private List<AccountTreeSelectVO> getChildren(AccountTreeSelectVO parent, Map<Long, AccountTreeSelectVO> accountMap) {
        return accountMap.values().stream()
                .filter(entity -> entity.getParentId().equals(parent.getAccountBaseId()))
                .peek(entity -> entity.setChildren(getChildren(entity, accountMap)))
                .collect(Collectors.toList());
    }

    /**
     * 新增账户
     */
    @Override
    public R<Boolean> insert(Account account) {
        int insert = accountMapper.insert(account);
        return R.ok(insert > 0);
    }

    /**
     * 修改账户
     */
    @Override
    public R<Boolean> update(Account account) {
        BizPreconditions.ifFalse(canEditOrDelete(account.getId()), "账户不可编辑！");
        int update = accountMapper.update(account);
        return R.ok(update > 0);
    }

    @Override
    @Transactional
    public R<Boolean> updateAccountBalance(BookMoney bookMoney) {
        Long id = bookMoney.getAccountId();
        Long bookId = bookMoney.getBookId();
        Integer type = bookMoney.getType();
        Long money = bookMoney.getMoney();

        // 查询账户信息
        Account existingAccount = accountMapper.selectById(id);
        BizPreconditions.ifNull(existingAccount, "账户不存在！");
        // 校验数据一致性
        BizPreconditions.ifFalse(existingAccount.getBookId().equals(bookId), "数据错误！");
        // 处理不计收支的情况
        if (PaymentType.EXCEPT.getValue().equals(type)) {
            return R.ok(true);
        }
        // 更新余额
        if (!type.equals(PaymentType.INCOME.getValue()) && !type.equals(PaymentType.EXPENDITURE.getValue())) {
            BizPreconditions.thrException("无效的交易类型！");
        }
        existingAccount.setBalance(existingAccount.getBalance() + money);
        // 更新数据库
        int updateCount = accountMapper.update(existingAccount);
        return R.ok(updateCount > 0);
    }

    /**
     * 批量删除账户
     */
    @Override
    public R<Boolean> deleteByIds(Long[] ids) {
        BizPreconditions.ifFalse(canEditOrDelete(ids), "账户不可删除！");
        int delete = accountMapper.deleteByIds(ids);
        return R.ok(delete > 0);
    }

    /**
     * 删除账户信息
     */
    @Override
    public R<Boolean> deleteById(Long id) {
        BizPreconditions.ifFalse(canEditOrDelete(id), "账户不可删除！");
        int delete = accountMapper.deleteById(id);
        return R.ok(delete > 0);
    }

    @Override
    public R<Boolean> importExcel(List<Account> accountList) {
        int insert = accountMapper.insertBatch(accountList);
        return R.ok(insert > 0);
    }

    /**
     * 判断账户是否可以编辑删除
     */
    private Boolean canEditOrDelete(Long... ids) {
        return true;
    }
}
