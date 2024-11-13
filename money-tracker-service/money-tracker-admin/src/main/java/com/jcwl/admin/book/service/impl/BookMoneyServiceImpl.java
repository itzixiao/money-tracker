package com.jcwl.admin.book.service.impl;

import com.jcwl.admin.account.domain.Account;
import com.jcwl.admin.account.dto.AccountQueryDTO;
import com.jcwl.admin.account.dto.AccountTreeSelectVO;
import com.jcwl.admin.account.service.IAccountService;
import com.jcwl.admin.book.domain.BookMoney;
import com.jcwl.admin.book.domain.BookUser;
import com.jcwl.admin.book.dto.*;
import com.jcwl.admin.book.enums.PaymentType;
import com.jcwl.admin.book.enums.QueryPeriodEnum;
import com.jcwl.admin.book.mapper.BookMoneyMapper;
import com.jcwl.admin.book.service.IBookMoneyService;
import com.jcwl.admin.book.service.IBookUserService;
import com.jcwl.admin.category.domain.Category;
import com.jcwl.admin.category.dto.CategoryQueryDTO;
import com.jcwl.admin.category.dto.CategoryTreeSelectVO;
import com.jcwl.admin.category.service.ICategoryService;
import com.jcwl.admin.common.constant.CacheConstants;
import com.jcwl.common.core.domain.R;
import com.jcwl.common.core.redis.RedisCache;
import com.jcwl.common.exception.BizPreconditions;
import com.jcwl.common.utils.DateUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 账本明细Service业务层处理
 *
 * @author jcwl
 * @date 2024-10-18
 */
@Service
public class BookMoneyServiceImpl implements IBookMoneyService {
    @Autowired
    private BookMoneyMapper bookMoneyMapper;

    @Autowired
    private IBookUserService bookUserService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private RedisCache redisCache;


    @PostConstruct
    private void init() {
        List<Category> categoryList = categoryService.selectList(new CategoryQueryDTO());
        categoryList.forEach(entity -> {
            String key = CacheConstants.BOOK_CATEGORY + entity.getBookId() + "_" + entity.getId();
            redisCache.setCacheObject(key, entity);
        });

        List<Account> accountList = accountService.selectList(new AccountQueryDTO());
        accountList.forEach(entity -> {
            String key = CacheConstants.BOOK_ACCOUNT + entity.getBookId() + "_" + entity.getId();
            redisCache.setCacheObject(key, entity);
        });
    }

    /**
     * 查询账本明细
     */
    @Override
    public BookMoney selectById(Long id) {
        return bookMoneyMapper.selectById(id);
    }

    /**
     * 查询账本明细列表
     */
    @Override
    public List<BookMoney> selectList(BookMoneyQueryDTO bookMoney) {
        return bookMoneyMapper.selectList(bookMoney);
    }

    @Override
    public List<BookMoneyGroupVO> groupList(BookMoneyQueryDTO bookMoney) {
        List<BookMoneyGroupVO> list = bookMoneyMapper.groupList(bookMoney);
        List<BookMoney> allBookMoneyList = bookMoneyMapper.selectList(bookMoney);

        // 将 BookMoney 数据按 bookTime 分组
        Map<Date, List<BookMoney>> bookMoneyMap = allBookMoneyList.stream()
                .collect(Collectors.groupingBy(BookMoney::getGroupBookTime));

        // 遍历 list 并设置每个 BookMoneyGroupVO 对象的 bookMoneyList
        for (BookMoneyGroupVO groupVO : list) {
            Date bookTime = groupVO.getBookTime();
            List<BookMoney> bookMoneyList = bookMoneyMap.getOrDefault(bookTime, new ArrayList<>());
            groupVO.setBookMoneyList(bookMoneyList);
        }

        return list;
    }

    @Override
    public List<BookMoneyGroupVO> calendarGroupList(BookMoneyQueryDTO bookMoney) {
        return bookMoneyMapper.groupList(bookMoney);
    }

    @Override
    public List<BookMoneyCountVO> getCountInfo(BookMoneyQueryDTO bookMoney) {
        return bookMoneyMapper.getCountInfo(bookMoney);
    }

    /**
     * 新增账本明细
     */
    @Override
    @Transactional
    public R<Boolean> insert(BookMoney bookMoney) {
        String categoryKey = CacheConstants.BOOK_CATEGORY + bookMoney.getBookId() + "_" + bookMoney.getCategoryId();
        Category category = redisCache.getCacheObject(categoryKey);
        String categoryCacheKey = CacheConstants.RECENT_USE_CATEGORY + bookMoney.getUserId() + "_" + bookMoney.getBookId() + "_" + bookMoney.getType();
        String accountKey = CacheConstants.BOOK_ACCOUNT + bookMoney.getBookId() + "_" + bookMoney.getAccountId();
        Account account = redisCache.getCacheObject(accountKey);
        String accountCacheKey = CacheConstants.RECENT_USE_ACCOUNT + bookMoney.getUserId() + "_" + bookMoney.getBookId();
        int insert = bookMoneyMapper.insert(bookMoney);
        accountService.updateAccountBalance(bookMoney);
        insertOrUpdateCategoryCache(categoryCacheKey, category, bookMoney);
        insertOrUpdateAccountCache(accountCacheKey, account, bookMoney);
        return R.ok(insert > 0);
    }

    private void insertOrUpdateAccountCache(String accountCacheKey, Account account, BookMoney bookMoney) {
        List<AccountTreeSelectVO> accountCacheList = redisCache.getCacheList(accountCacheKey);
        if (ObjectUtils.isNotEmpty(accountCacheList)) {
            updateExistingAccountCache(accountCacheList, account, bookMoney);
            redisCache.deleteObject(accountCacheKey);
        } else {
            createNewAccountCache(accountCacheList, account, bookMoney);
        }
        redisCache.setCacheList(accountCacheKey, accountCacheList);
    }


    private void updateExistingAccountCache(List<AccountTreeSelectVO> accountCacheList, Account account, BookMoney bookMoney) {
        List<AccountTreeSelectVO> subList = accountCacheList.get(0).getChildren();
        AccountTreeSelectVO subAccount = createSubAccount(account, bookMoney);
        subList.removeIf(cacheSubAccount -> Objects.equals(cacheSubAccount.getAccountBaseId(), subAccount.getAccountBaseId()));
        subList.add(0, subAccount);
    }

    private void createNewAccountCache(List<AccountTreeSelectVO> categoryCacheList, Account account, BookMoney bookMoney) {
        List<AccountTreeSelectVO> subList = new ArrayList<>();
        AccountTreeSelectVO sub = createSubAccount(account, bookMoney);
        subList.add(sub);
        AccountTreeSelectVO parent = createParentAccount(subList);
        categoryCacheList.add(parent);
    }

    private AccountTreeSelectVO createSubAccount(Account account, BookMoney bookMoney) {
        AccountTreeSelectVO sub = new AccountTreeSelectVO();
        sub.setId(bookMoney.getAccountId());
        sub.setAccountBaseId(account.getAccountBaseId());
        sub.setParentId(account.getParentId());
        sub.setName(account.getName());
        sub.setCode(account.getCode());
        sub.setAccountUrl(account.getAccountUrl());
        sub.setHasChild(account.getHasChild());
        return sub;
    }

    private AccountTreeSelectVO createParentAccount(List<AccountTreeSelectVO> children) {
        AccountTreeSelectVO parent = new AccountTreeSelectVO();
        parent.setId(100L);
        parent.setParentId(0L);
        parent.setName("最近使用");
        parent.setCode("RECENT_USE_ACCOUNT");
        parent.setAccountUrl("accounts/CsoXM2AFbFSEaCfvAAAAAFmgDS0338.png");
        parent.setHasChild("Y");
        parent.setChildren(children);
        return parent;
    }

    private void insertOrUpdateCategoryCache(String categoryCacheKey, Category category, BookMoney bookMoney) {
        List<CategoryTreeSelectVO> categoryCacheList = redisCache.getCacheList(categoryCacheKey);
        if (ObjectUtils.isNotEmpty(categoryCacheList)) {
            updateExistingCategoryCache(categoryCacheList, category, bookMoney);
            redisCache.deleteObject(categoryCacheKey);
        } else {
            createNewCategoryCache(categoryCacheList, category, bookMoney);
        }
        redisCache.setCacheList(categoryCacheKey, categoryCacheList);
    }

    private void updateExistingCategoryCache(List<CategoryTreeSelectVO> categoryCacheList, Category category, BookMoney bookMoney) {
        List<CategoryTreeSelectVO> subList = categoryCacheList.get(0).getChildren();
        CategoryTreeSelectVO subCategory = createSubCategory(category, bookMoney);
        subList.removeIf(cacheSubCategory -> Objects.equals(cacheSubCategory.getCategoryBaseId(), subCategory.getCategoryBaseId()));
        subList.add(0, subCategory);
    }

    private void createNewCategoryCache(List<CategoryTreeSelectVO> categoryCacheList, Category category, BookMoney bookMoney) {
        List<CategoryTreeSelectVO> subList = new ArrayList<>();
        CategoryTreeSelectVO sub = createSubCategory(category, bookMoney);
        subList.add(sub);
        CategoryTreeSelectVO parent = createParentCategory(bookMoney.getType(), subList);
        categoryCacheList.add(parent);
    }

    private CategoryTreeSelectVO createSubCategory(Category category, BookMoney bookMoney) {
        CategoryTreeSelectVO sub = new CategoryTreeSelectVO();
        sub.setId(bookMoney.getCategoryId());
        sub.setCategoryBaseId(category.getCategoryBaseId());
        sub.setParentId(category.getParentId());
        sub.setName(category.getName());
        sub.setCode(category.getCode());
        sub.setCategoryUrl(category.getCategoryUrl());
        sub.setHasChild(category.getHasChild());
        sub.setType(category.getType());
        return sub;
    }

    private CategoryTreeSelectVO createParentCategory(Integer type, List<CategoryTreeSelectVO> children) {
        CategoryTreeSelectVO parent = new CategoryTreeSelectVO();
        parent.setCategoryBaseId(100L);
        parent.setParentId(0L);
        parent.setName("最近使用");
        parent.setCode("RECENT_USE_CATEGORY");
        parent.setCategoryUrl("categories/CsoXM2AFbFSEaCfvAAAAAFmgDS0338.png");
        parent.setHasChild("Y");
        parent.setType(type);
        parent.setChildren(children);
        return parent;
    }

    /**
     * 修改账本明细
     */
    @Override
    public R<Boolean> update(BookMoney bookMoney) {
        BizPreconditions.ifFalse(canEditOrDelete(bookMoney.getId()), "账本明细不可编辑！");
        int update = bookMoneyMapper.update(bookMoney);
        return R.ok(update > 0);
    }

    @Override
    public void updateNickName(Long bookId, Long userId, String nickName) {
        bookMoneyMapper.updateNickName(bookId, userId, nickName);
    }

    /**
     * 批量删除账本明细
     */
    @Override
    public R<Boolean> deleteByIds(Long[] ids) {
        BizPreconditions.ifFalse(canEditOrDelete(ids), "账本明细不可删除！");
        int delete = bookMoneyMapper.deleteByIds(ids);
        return R.ok(delete > 0);
    }

    /**
     * 删除账本明细信息
     */
    @Override
    public R<Boolean> deleteById(Long id) {
        BizPreconditions.ifFalse(canEditOrDelete(id), "账本明细不可删除！");
        int delete = bookMoneyMapper.deleteById(id);
        return R.ok(delete > 0);
    }

    @Override
    public R<Boolean> deleteByBookId(Long[] bookIds) {
        BizPreconditions.ifFalse(canEditOrDelete(bookIds), "账本明细不可删除！");
        int delete = bookMoneyMapper.deleteByBookId(bookIds);
        return R.ok(delete > 0);
    }

    @Override
    public BookMoneyReportVO getCountReport(BookMoneyReportDTO reportDTO) {
        BizPreconditions.ifTrue(null == reportDTO.getBookTime(), "日期不能为空！");
        List<BookReportVO> bookMoneyList = bookMoneyMapper.countReport(reportDTO);
        if (!CollectionUtils.isEmpty(bookMoneyList)) {
            BookMoneyReportVO bookMoneyReportVO = new BookMoneyReportVO();
            bookMoneyReportVO.setRevenue(bookMoneyList.stream().filter(t -> t.getType() == 0).collect(Collectors.toList()));
            bookMoneyReportVO.setExpenditures(bookMoneyList.stream().filter(t -> t.getType() == 1).collect(Collectors.toList()));
            return bookMoneyReportVO;
        }
        return new BookMoneyReportVO();
    }

    public List<String> getDateByPeriod(BookMoneyReportDTO dto) {
        Integer queryPeriod = dto.getLogoType();
        String bookTime = dto.getBookTime();
        List<String> list = new ArrayList<>();
        if (queryPeriod.compareTo(QueryPeriodEnum.CURRENT_DAY.getValue()) == 0) {
            LocalTime currentTime = LocalTime.now();
            for (int i = 0; i < currentTime.getHour(); ++i) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(12, 0);
                calendar.set(11, calendar.get(11) - i);
                String hour = DateUtils.parseDateToStr("yyyy-MM-dd HH:mm", calendar.getTime());
                list.add(hour);
            }
            Collections.reverse(list);
        } else if (queryPeriod.compareTo(QueryPeriodEnum.CURRENT_MONTH.getValue()) == 0) {
            String[] split = bookTime.split("-");
            int year = Integer.parseInt(split[0]);
            int month = Integer.parseInt(split[1]);
            // 创建 LocalDate 对象表示该月的第一天
            LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
            // 确定循环次数
            int loopCount = firstDayOfMonth.lengthOfMonth();
            // 循环遍历该月的每一天，直到达到今天的日期
            for (LocalDate date = firstDayOfMonth; !date.isAfter(firstDayOfMonth.plusDays(loopCount - 1)); date = date.plusDays(1)) {
                String day = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                list.add(day);
            }
        } else if (queryPeriod.compareTo(QueryPeriodEnum.CURRENT_YEAR.getValue()) == 0) {
            String[] split = bookTime.split("-");
            int year = Integer.parseInt(split[0]);
            for (int month = 1; month <= 12; month++) {
                String yearMonth = String.format("%d-%02d", year, month);
                list.add(yearMonth);
            }
        } else {
            BizPreconditions.thrException("查询期限不支持！");
        }
        return list;
    }


    @Override
    public BookMoneyReportTrendVO getCountReportTrend(BookMoneyReportDTO reportDTO) {
        List<String> dateByPeriod = getDateByPeriod(reportDTO);
        BookMoneyReportTrendVO vo = new BookMoneyReportTrendVO();
        setExpenditures(reportDTO, dateByPeriod, 0, vo.getRevenue());
        setExpenditures(reportDTO, dateByPeriod, 1, vo.getExpenditures());
        vo.setX(dateByPeriod);
        return vo;
    }

    @Override
    public BookMoneyReportVO getCountClassify(BookMoneyReportDTO reportDTO) {
        List<BookReportVO> bookMoneyList = bookMoneyMapper.countClassify(reportDTO);
        List<BookMoney> bookMoneys = bookMoneyMapper.selectAccountingPen(reportDTO);
        if (!CollectionUtils.isEmpty(bookMoneyList)) {
            BookMoneyReportVO bookMoneyReportVO = new BookMoneyReportVO();
            List<BookReportVO> revenue = new ArrayList<>();
            List<BookReportVO> expenditures = new ArrayList<>();
            for (BookReportVO bookReportVO : bookMoneyList) {
                if (bookReportVO.getType() == 0) {
                    revenue.add(bookReportVO);
                } else if (bookReportVO.getType() == 1) {
                    expenditures.add(bookReportVO);
                }
            }
            int totalExpenditureCount = 0;
            int totalRevenueCount = 0;
            for (BookMoney bookMoney : bookMoneys) {
                if (bookMoney.getType() == 0) {
                    totalRevenueCount += 1;
                } else if (bookMoney.getType() == 1) {
                    totalExpenditureCount += 1;
                }
            }
            bookMoneyReportVO.setRevenue(revenue);
            expenditures.sort(Comparator.comparing(BookReportVO::getMoney));
            bookMoneyReportVO.setExpenditures(expenditures);
            bookMoneyReportVO.setTotalRevenue(revenue.stream().mapToLong(BookReportVO::getMoney).sum());
            bookMoneyReportVO.setTotalExpenditure(expenditures.stream().mapToLong(BookReportVO::getMoney).sum());
            bookMoneyReportVO.setTotalRevenueCount(totalRevenueCount);
            bookMoneyReportVO.setTotalExpenditureCount(totalExpenditureCount);
            return bookMoneyReportVO;
        }
        return new BookMoneyReportVO();
    }


    @Override
    public BookMemberResVO getCountMember(BookMoneyReportDTO reportDTO) {
        List<BookMemberVO> countMember = bookMoneyMapper.getCountMember(reportDTO);
        Integer total = bookMoneyMapper.getMemberCount(reportDTO);
        BookMemberResVO resVO = new BookMemberResVO();
        List<String> x = new ArrayList<>();
        List<Integer> y = new ArrayList<>();
        for (BookMemberVO bookMemberVO : countMember) {
            x.add(bookMemberVO.getName());
            y.add(bookMemberVO.getCount());
        }
        resVO.setX(x);
        resVO.setY(y);
        resVO.setTotal(total);
        return resVO;
    }

    @Override
    public BookMemberCompareResVO getMemberCompare(BookMoneyReportDTO reportDTO) {
        List<BookMemberCompareVO> memberCompare = bookMoneyMapper.getMemberCompare(reportDTO);
        BookMemberCompareResVO resVO = new BookMemberCompareResVO();
        Integer totalAllRevenue = 0;
        Integer totalAllSpending = 0;
        for (BookMemberCompareVO bookMemberCompareVO : memberCompare) {
            totalAllSpending += bookMemberCompareVO.getTotalSpending();
            totalAllRevenue += bookMemberCompareVO.getTotalRevenue();
        }
        resVO.setCompareVOS(memberCompare);
        resVO.setTotalAllSpending(totalAllSpending);
        resVO.setTotalAllRevenue(totalAllRevenue);
        return resVO;
    }

    @Override
    public BookExpenditureIncomeResVO expenditureIncome(BookMoneyReportDTO reportDTO) {
        List<BookExpenditureIncomeVO> vos = bookMoneyMapper.expenditureIncome(reportDTO);
        int sum = vos.stream().mapToInt(BookExpenditureIncomeVO::getMoney).sum();
        return new BookExpenditureIncomeResVO(vos, sum);
    }

    @Override
    public BookLedgerFlowStatisticsVO bookLedgerFlowStatistics(BookMoneyReportDTO reportDTO) {
        BookLedgerFlowStatisticsVO bookLedgerFlowStatisticsVO = bookMoneyMapper.bookLedgerFlowStatistics(reportDTO);
        if (null != bookLedgerFlowStatisticsVO) {
            bookLedgerFlowStatisticsVO.setBalance(bookLedgerFlowStatisticsVO.getTotalRevenue() + bookLedgerFlowStatisticsVO.getTotalSpending());
        }
        return bookLedgerFlowStatisticsVO;
    }

    @Override
    public BookAccountTrendVO getAccountTrend(BookMoneyReportDTO reportDTO) {
        List<String> dateByPeriod = this.getDateByPeriod(reportDTO);
        List<BookAccountVO> vos = bookMoneyMapper.getAccountTrend(reportDTO, dateByPeriod);
        List<String> x = new ArrayList<>();
        List<Long> y = new ArrayList<>();
        if (!CollectionUtils.isEmpty(vos)) {
            for (BookAccountVO vo : vos) {
                x.add(vo.getData());
                y.add(vo.getMoney());
            }
        }
        return new BookAccountTrendVO(x, y);
    }

    @Override
    public BookAccountDetailRspVO getAccountDetail(BookMoneyReportDTO reportDTO) {

        List<Account> accounts = accountService.selectListByBookId(reportDTO.getBookId());
        BookAccountDetailRspVO resVos = new BookAccountDetailRspVO();
        List<BookAccountDetailVO> assetDetails = new ArrayList<>();
        List<BookAccountDetailVO> liabilityDetails = new ArrayList<>();
        for (Account ac : accounts) {
            BookAccountDetailVO bookAccountDetailVO = new BookAccountDetailVO();
            BeanUtils.copyProperties(ac, bookAccountDetailVO);
            Long balance = ac.getBalance();
            if (balance.compareTo(0L) >= 0) {
                assetDetails.add(bookAccountDetailVO);
            } else {
                liabilityDetails.add(bookAccountDetailVO);
            }
        }
        resVos.setAssetDetails(assetDetails);
        resVos.setLiabilityDetails(liabilityDetails);
        return resVos;
    }

    private static Date parseDate(String dateStr) throws ParseException {
        SimpleDateFormat dateFormat;
        if (dateStr.length() == 7) { // 年月格式
            dateFormat = new SimpleDateFormat("yyyy-MM");
        } else if (dateStr.length() == 10) { // 年月日格式
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        } else {
            throw new IllegalArgumentException("不支持的日期格式");
        }
        return dateFormat.parse(dateStr);
    }

    @Override
    public BookAccountLineVO getAccountLine(BookMoneyReportDTO reportDTO) {
        List<String> dates = this.getDateByPeriod(reportDTO);
        List<MonthlyBalanceVO> vos = bookMoneyMapper.selectMonthlyBalance(reportDTO, dates.get(0), dates.get(dates.size() - 1));
        Map<String, Long> resultMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(vos)) {
            resultMap = vos.stream().collect(Collectors.toMap(MonthlyBalanceVO::getBookTime,
                    MonthlyBalanceVO::getBalance));
        }

        // 获取开始日期前一次的余额
        Double defaultAmount = 0D;
        for (MonthlyBalanceVO vo : vos) {
            try {
                Date bookTimeDate = parseDate(vo.getBookTime());
                Date startDateDate = parseDate(dates.get(0));
                if (bookTimeDate.after(startDateDate)) {
                    break;
                }
            } catch (ParseException e) {
                BizPreconditions.thrException("日期格式异常");
            }
            defaultAmount = vo.getBalance() / 100.0;
        }
        List<Double> y = new ArrayList<>();
        for (String date : dates) {
            Long amount = resultMap.get(date);
            if (null == amount) {
                y.add(defaultAmount);
            } else {
                double yAmount = amount / 100.0;
                y.add(yAmount);
                defaultAmount = yAmount;
            }
        }
        return new BookAccountLineVO(dates, y);
    }

    private void setExpenditures(BookMoneyReportDTO reportDTO, List<String> dateByPeriod, int type, List<Long> bookMoneyTrendVOS) {
        List<BookMoneyTrendVO> vos = bookMoneyMapper.selectCountReportTrend(dateByPeriod, reportDTO, type);
        // revenueList以日期为key转成map
        extracted(dateByPeriod, bookMoneyTrendVOS, vos);
    }

    private void extracted(List<String> dateByPeriod, List<Long> bookMoneyTrendVOS, List<BookMoneyTrendVO> vos) {
        Map<String, BookMoneyTrendVO> revenueMap = vos.stream().collect(Collectors.toMap(BookMoneyTrendVO::getBookTime, t -> t));
        for (String data : dateByPeriod) {
            BookMoneyTrendVO bookMoneyTrendVO = revenueMap.get(data);
            if (bookMoneyTrendVO == null) {
                bookMoneyTrendVOS.add(0L);
            } else {
                bookMoneyTrendVOS.add(Math.abs(bookMoneyTrendVO.getMoney()));
            }
        }
    }


    @Override
    public R<Boolean> importExcel(List<BookMoney> bookMoneyList) {
        int insert = bookMoneyMapper.insertBatch(bookMoneyList);
        return R.ok(insert > 0);
    }

    @Override
    public R<Boolean> importAliPayExcel(List<AliPayImportDTO> aliPayImportList, Long bookId, Long userId) {
        BookUser bookUser = bookUserService.selectByUserId(userId, bookId);
        BizPreconditions.ifNull(bookUser, "请重新选择账本用户！");
        initCategory(bookId);
        Account account = accountService.selectByBook(bookId, 7L);
        List<BookMoney> bookMoneyList = new ArrayList<>();
        aliPayImportList.forEach(t -> {
            BookMoney bookMoney = new BookMoney();

            bookMoney.setBookId(bookId);
            Category category = categoryMap.get(t.getTradeCategory());
            if (category != null) {
                bookMoney.setCategoryId(category.getId());
                bookMoney.setCategoryBaseId(category.getCategoryBaseId());
                bookMoney.setCategoryName(category.getName());
                bookMoney.setCategoryUrl(category.getCategoryUrl());
            } else {
                bookMoney.setCategoryBaseId(1001L);
                bookMoney.setCategoryId(1001L);
                bookMoney.setCategoryName("AliPay");
                bookMoney.setCategoryUrl("categories/AliPay.png");
            }

            if (account != null) {
                bookMoney.setAccountBaseId(account.getAccountBaseId());
                bookMoney.setAccountId(account.getId());
                bookMoney.setAccountName(account.getName());
                bookMoney.setAccountUrl(account.getAccountUrl());
            } else {
                bookMoney.setAccountBaseId(1001L);
                bookMoney.setAccountId(1001L);
                bookMoney.setAccountName("AliPay");
                bookMoney.setAccountUrl("categories/AliPay.png");
            }

            bookMoney.setUserId(bookUser.getUserId());
            bookMoney.setUserName(bookUser.getUserName());
            bookMoney.setNickName(bookUser.getNickName());
            bookMoney.setMoney((long) (t.getAmount() * 100));
            bookMoney.setBookTime(t.getTradeTime());
            bookMoney.setType(PaymentType.getByName(t.getIncomeExpense()).getValue());
            bookMoney.setDetailDesc("");
            bookMoney.setDetailDescFlag("");
            bookMoney.setDetailImg("");
            bookMoney.setRemark(t.getProductDescription());
            bookMoney.setCreateTime(DateUtils.getNowDate());
            bookMoney.setDelFlag("0");

            bookMoneyList.add(bookMoney);
        });
        int insert = bookMoneyMapper.insertBatch(bookMoneyList);
        bookMoneyList.forEach(bookMoney -> {
            accountService.updateAccountBalance(bookMoney);
        });
        return R.ok(insert > 0);
    }

    private Map<String, Category> categoryMap = new HashMap<>();

    private void initCategory(Long bookId) {
        categoryMap.clear();
        CategoryQueryDTO categoryQueryDTO = new CategoryQueryDTO();
        categoryQueryDTO.setBookId(bookId);
        List<Category> categoryList = categoryService.selectList(categoryQueryDTO);
        categoryMap = categoryList.stream()
                .collect(Collectors.toMap(
                        Category::getName,
                        category -> category,
                        (existing, replacement) -> existing // 选择现有值
                ));    }

    @Override
    public R<Boolean> importWechatPayExcel(List<WechatPayImportDTO> wechatPayImportList, Long bookId, Long userId) {
        BookUser bookUser = bookUserService.selectByUserId(userId, bookId);
        BizPreconditions.ifNull(bookUser, "请重新选择账本用户！");
        initCategory(bookId);
        Account account = accountService.selectByBook(bookId, 6L);
        List<BookMoney> bookMoneyList = new ArrayList<>();
        wechatPayImportList.forEach(t -> {
            BookMoney bookMoney = new BookMoney();
            bookMoney.setBookId(bookId);

            Category category = categoryMap.get(t.getTradeCategory());
            if (category != null) {
                bookMoney.setCategoryId(category.getId());
                bookMoney.setCategoryBaseId(category.getCategoryBaseId());
                bookMoney.setCategoryName(category.getName());
                bookMoney.setCategoryUrl(category.getCategoryUrl());
            } else {
                bookMoney.setCategoryBaseId(1001L);
                bookMoney.setCategoryId(1002L);
                bookMoney.setCategoryName("WechatPay");
                bookMoney.setCategoryUrl("categories/WechatPay.png");
            }
            if (account != null) {
                bookMoney.setAccountBaseId(account.getAccountBaseId());
                bookMoney.setAccountId(account.getId());
                bookMoney.setAccountName(account.getName());
                bookMoney.setAccountUrl(account.getAccountUrl());
            } else {
                bookMoney.setAccountBaseId(1001L);
                bookMoney.setAccountId(1001L);
                bookMoney.setAccountName("AliPay");
                bookMoney.setAccountUrl("categories/WechatPay.png");
            }
            bookMoney.setNickName(bookUser.getNickName());
            bookMoney.setUserName(bookUser.getUserName());
            bookMoney.setUserId(bookUser.getUserId());
            bookMoney.setMoney((long) (t.getAmount() * 100));
            bookMoney.setBookTime(t.getTradeTime());
            bookMoney.setType(PaymentType.getByName(t.getIncomeExpense()).getValue());
            bookMoney.setDetailDesc("");
            bookMoney.setDetailDescFlag("");
            bookMoney.setDetailImg("");
            bookMoney.setRemark(t.getProductDescription());
            bookMoney.setCreateTime(DateUtils.getNowDate());
            bookMoney.setDelFlag("0");
            bookMoneyList.add(bookMoney);
        });
        int insert = bookMoneyMapper.insertBatch(bookMoneyList);
        bookMoneyList.forEach(bookMoney -> {
            accountService.updateAccountBalance(bookMoney);
        });
        return R.ok(insert > 0);
    }

    /**
     * 判断账本明细是否可以编辑删除
     */
    private Boolean canEditOrDelete(Long... ids) {
        return true;
    }
}
