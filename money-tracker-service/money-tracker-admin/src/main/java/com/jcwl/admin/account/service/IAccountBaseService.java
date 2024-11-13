package com.jcwl.admin.account.service;

import com.jcwl.admin.account.domain.AccountBase;
import com.jcwl.admin.account.dto.AccountBaseQueryDTO;
import com.jcwl.common.core.domain.R;

import java.util.List;

/**
 * 账户基础Service接口
 *
 * @author jcwl
 * @date 2024-10-24
 */
public interface IAccountBaseService {
    /**
     * 查询账户基础
     */
    public AccountBase selectByAccountBaseId(Long accountBaseId);

    /**
     * 查询账户基础列表
     */
    public List<AccountBase> selectList(AccountBaseQueryDTO accountBase);

    /**
     * 新增账户基础
     */
    public R<Boolean> insert(AccountBase accountBase);

    /**
     * 修改账户基础
     */
    public R<Boolean> update(AccountBase accountBase);

    /**
     * 批量删除账户基础
     */
    public R<Boolean> deleteByAccountBaseIds(Long[] accountBaseIds);

    /**
     * 删除账户基础信息
     */
    public R deleteByAccountBaseId(Long accountBaseId);

    /**
     * 导入账户基础列表
     */
    R<Boolean> importExcel(List<AccountBase> accountBaseList);
}
