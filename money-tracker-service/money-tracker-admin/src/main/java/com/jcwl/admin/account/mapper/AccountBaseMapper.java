package com.jcwl.admin.account.mapper;

import com.jcwl.admin.account.domain.AccountBase;
import com.jcwl.admin.account.dto.AccountBaseQueryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 账户基础Mapper接口
 *
 * @author jcwl
 * @date 2024-10-24
 */
public interface AccountBaseMapper {
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
    public int insert(AccountBase accountBase);

    /**
     * 批量新增账户基础
     */
    public int insertBatch(@Param("list") List<AccountBase> accountBaseList);

    /**
     * 修改账户基础
     */
    public int update(AccountBase accountBase);

    /**
     * 批量修改账户基础
     */
    int updateBatch(@Param("list") List<AccountBase> accountBaseList);

    /**
     * 删除账户基础
     */
    public int deleteByAccountBaseId(Long accountBaseId);

    /**
     * 批量删除账户基础
     */
    public int deleteByAccountBaseIds(Long[] accountBaseIds);
}
