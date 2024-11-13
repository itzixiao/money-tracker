package com.jcwl.admin.account.service.impl;

import com.jcwl.admin.account.domain.AccountBase;
import com.jcwl.admin.account.dto.AccountBaseQueryDTO;
import com.jcwl.admin.account.mapper.AccountBaseMapper;
import com.jcwl.admin.account.service.IAccountBaseService;
import com.jcwl.common.core.domain.R;
import com.jcwl.common.exception.BizPreconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 账户基础Service业务层处理
 *
 * @author jcwl
 * @date 2024-10-24
 */
@Service
public class AccountBaseServiceImpl implements IAccountBaseService {
    @Autowired
    private AccountBaseMapper accountBaseMapper;

    /**
     * 查询账户基础
     */
    @Override
    public AccountBase selectByAccountBaseId(Long accountBaseId) {
        return accountBaseMapper.selectByAccountBaseId(accountBaseId);
    }

    /**
     * 查询账户基础列表
     */
    @Override
    public List<AccountBase> selectList(AccountBaseQueryDTO accountBase) {
        return accountBaseMapper.selectList(accountBase);
    }

    /**
     * 新增账户基础
     */
    @Override
    public R<Boolean> insert(AccountBase accountBase) {
        int insert = accountBaseMapper.insert(accountBase);
        return R.ok(insert > 0);
    }

    /**
     * 修改账户基础
     */
    @Override
    public R<Boolean> update(AccountBase accountBase) {
        BizPreconditions.ifFalse(canEditOrDelete(accountBase.getAccountBaseId()), "账户基础不可编辑！");
        int update = accountBaseMapper.update(accountBase);
        return R.ok(update > 0);
    }

    /**
     * 批量删除账户基础
     */
    @Override
    public R<Boolean> deleteByAccountBaseIds(Long[] accountBaseIds) {
        BizPreconditions.ifFalse(canEditOrDelete(accountBaseIds), "账户基础不可删除！");
        int delete = accountBaseMapper.deleteByAccountBaseIds(accountBaseIds);
        return R.ok(delete > 0);
    }

    /**
     * 删除账户基础信息
     */
    @Override
    public R<Boolean> deleteByAccountBaseId(Long accountBaseId) {
        BizPreconditions.ifFalse(canEditOrDelete(accountBaseId), "账户基础不可删除！");
        int delete = accountBaseMapper.deleteByAccountBaseId(accountBaseId);
        return R.ok(delete > 0);
    }

    @Override
    public R<Boolean> importExcel(List<AccountBase> accountBaseList) {
        int insert = accountBaseMapper.insertBatch(accountBaseList);
        return R.ok(insert > 0);
    }

    /**
     * 判断账户基础是否可以编辑删除
     */
    private Boolean canEditOrDelete(Long... accountBaseIds) {
        return true;
    }
}
