package com.jcwl.admin.book.service;

import com.jcwl.admin.book.domain.BookMoney;
import com.jcwl.admin.book.dto.*;
import com.jcwl.common.core.domain.R;

import java.util.List;

/**
 * 账本明细Service接口
 *
 * @author jcwl
 * @date 2024-10-18
 */
public interface IBookMoneyService {
    /**
     * 查询账本明细
     */
    public BookMoney selectById(Long id);

    /**
     * 查询账本明细列表
     */
    public List<BookMoney> selectList(BookMoneyQueryDTO bookMoney);

    /**
     * 新增账本明细
     */
    public R<Boolean> insert(BookMoney bookMoney);

    /**
     * 修改账本明细
     */
    public R<Boolean> update(BookMoney bookMoney);

    /**
     * 更新用户昵称
     */
    void updateNickName(Long bookId, Long userId, String nickName);

    /**
     * 批量删除账本明细
     */
    public R<Boolean> deleteByIds(Long[] ids);

    /**
     * 删除账本明细信息
     */
    public R deleteById(Long id);

    /**
     * 导入账本明细列表
     */
    R<Boolean> importExcel(List<BookMoney> bookMoneyList);

    /**
     * 获取账本明细聚合列表
     */
    List<BookMoneyGroupVO> groupList(BookMoneyQueryDTO bookMoney);

    /**
     * 获取账本明细日历聚合列表
     */
    List<BookMoneyGroupVO> calendarGroupList(BookMoneyQueryDTO bookMoney);

    /**
     * 统计账本明细信息
     */
    List<BookMoneyCountVO> getCountInfo(BookMoneyQueryDTO bookMoney);

    /**
     * 导入支付宝对账单
     */
    R<Boolean> importAliPayExcel(List<AliPayImportDTO> bookMoneyList, Long bookId, Long userId);

    /**
     * 导入微信对账单
     */
    R<Boolean> importWechatPayExcel(List<WechatPayImportDTO> bookMoneyList, Long bookId, Long userId);

    /**
     * 删除账单明细信息
     */
    public R<Boolean> deleteByBookId(Long[] bookIds);

    /**
     * 统计账本报表
     *
     * @param reportDTO
     * @return
     */
    BookMoneyReportVO getCountReport(BookMoneyReportDTO reportDTO);

    /**
     * 统计报表趋势图
     *
     * @param reportDTO
     * @return
     */
    BookMoneyReportTrendVO getCountReportTrend(BookMoneyReportDTO reportDTO);

    /**
     * 分类报表统计
     *
     * @param reportDTO
     * @return
     */
    BookMoneyReportVO getCountClassify(BookMoneyReportDTO reportDTO);

    /**
     * 成员报表记账数据
     *
     * @param reportDTO
     * @return
     */
    BookMemberResVO getCountMember(BookMoneyReportDTO reportDTO);

    /**
     * 成员报表对比
     *
     * @param reportDTO
     * @return
     */
    BookMemberCompareResVO getMemberCompare(BookMoneyReportDTO reportDTO);

    /**
     * 成员支出收入统计
     *
     * @param reportDTO
     * @return
     */
    BookExpenditureIncomeResVO expenditureIncome(BookMoneyReportDTO reportDTO);

    /**
     * 基础统计账本流水统计
     *
     * @param reportDTO
     * @return
     */
    BookLedgerFlowStatisticsVO bookLedgerFlowStatistics(BookMoneyReportDTO reportDTO);

    /**
     * 账户趋势图
     *
     * @param reportDTO
     * @return
     */
    BookAccountTrendVO getAccountTrend(BookMoneyReportDTO reportDTO);

    /**
     * 账户资产明细
     *
     * @param reportDTO
     * @return
     */
    BookAccountDetailRspVO getAccountDetail(BookMoneyReportDTO reportDTO);

    /**
     * 账户资产趋势图
     *
     * @param reportDTO
     * @return
     */
    BookAccountLineVO getAccountLine(BookMoneyReportDTO reportDTO);
}
