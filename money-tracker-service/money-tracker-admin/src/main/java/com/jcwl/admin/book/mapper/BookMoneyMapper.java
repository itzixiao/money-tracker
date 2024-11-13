package com.jcwl.admin.book.mapper;

import com.jcwl.admin.book.domain.BookMoney;
import com.jcwl.admin.book.dto.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 账本明细Mapper接口
 *
 * @author jcwl
 * @date 2024-10-18
 */
public interface BookMoneyMapper {
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
    public int insert(BookMoney bookMoney);

    /**
     * 批量新增账本明细
     */
    public int insertBatch(@Param("list") List<BookMoney> bookMoneyList);

    /**
     * 修改账本明细
     */
    public int update(BookMoney bookMoney);

    /**
     * 修改用户昵称
     */
    void updateNickName(@Param("bookId") Long bookId, @Param("userId") Long userId, @Param("nickName") String nickName);

    /**
     * 批量修改账本明细
     */
    int updateBatch(@Param("list") List<BookMoney> bookMoneyList);

    /**
     * 删除账本明细
     */
    public int deleteById(Long id);

    /**
     * 批量删除账本明细
     */
    public int deleteByIds(Long[] ids);

    /**
     * 查询账本明细聚合列表
     */
    List<BookMoneyGroupVO> groupList(BookMoneyQueryDTO bookMoney);

    /**
     * 统计账本明细信息
     */
    List<BookMoneyCountVO> getCountInfo(BookMoneyQueryDTO bookMoney);

    /**
     * 删除账本明细
     */
    public int deleteByBookId(Long[] bookIds);

    /**
     *  统计账本明细报表
     */
    List<BookReportVO> countReport(@Param("dto") BookMoneyReportDTO reportDTO);

    /**
     *  统计账本明细报表趋势
     */
    List<BookMoneyTrendVO> selectCountReportTrend(@Param("dates") List<String> dates,
                                                  @Param("dto") BookMoneyReportDTO reportDTO,
                                                  @Param("type") int type);

    /**
     *  分类报表统计
     */
    List<BookReportVO> countClassify(@Param("dto") BookMoneyReportDTO reportDTO);

    /**
     *  成员报表统计
     */
    List<BookMemberVO> getCountMember(@Param("dto") BookMoneyReportDTO reportDTO);

    /**
     *  成员报表对比
     */
    List<BookMemberCompareVO> getMemberCompare(@Param("dto") BookMoneyReportDTO reportDTO);

    /**
     *  成员报表统计总数
     */
    Integer getMemberCount(@Param("dto") BookMoneyReportDTO reportDTO);

    /**
     *  成员支出/收入统计
     */
    List<BookExpenditureIncomeVO> expenditureIncome(@Param("dto") BookMoneyReportDTO reportDTO);

    /**
     *  账本流水统计
     */
    BookLedgerFlowStatisticsVO bookLedgerFlowStatistics(@Param("dto") BookMoneyReportDTO reportDTO);

    /**
     *  账户资产折线图
     */
    List<BookAccountVO> getAccountTrend(@Param("dto") BookMoneyReportDTO reportDTO,
                                        @Param("dates") List<String> dates);

    /**
     *  月结余额统计
     */
    List<MonthlyBalanceVO> selectMonthlyBalance(@Param("dto") BookMoneyReportDTO reportDTO,
                                                @Param("startTime") String startTime , @Param("endTime") String endTime);

    /**
     *  账单记账笔数
     */
    List<BookMoney> selectAccountingPen(@Param("dto") BookMoneyReportDTO reportDTO);
}
