/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.cashflowai.workbench;

import static cn.zhuatech.cashflowai.workbench.WorkbenchInput.*;

import java.math.*;
import java.util.*;
import org.springframework.stereotype.Service;

/** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
@Service
public class AiWorkbenchService {
  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  public Map<String, Object> execute(Map<String, Object> r) {
    validate(r);
    keys(r, "start", "days", "openingBalance", "minimumBalance", "movements");
    var start = date(text(r, "start", 10));
    int days = integer(r, "days", 1, 366);
    BigDecimal balance = decimal(r, "openingBalance", -1e12, 1e12),
        floor = money(r, "minimumBalance");
    Map<java.time.LocalDate, BigDecimal[]> movements = new TreeMap<>();
    Set<String> ids = new HashSet<>();
    for (var m : rows(r, "movements", 0, 500)) {
      keys(m, "id", "date", "direction", "amount");
      unique(ids, id(m, "id"));
      var d = date(text(m, "date", 10));
      if (d.isBefore(start) || !d.isBefore(start.plusDays(days)))
        throw new IllegalArgumentException("收支日期不在计算区间");
      String direction = text(m, "direction", 3);
      if (!Set.of("IN", "OUT").contains(direction)) throw new IllegalArgumentException("收支方向错误");
      var amounts =
          movements.computeIfAbsent(d, k -> new BigDecimal[] {BigDecimal.ZERO, BigDecimal.ZERO});
      int index = direction.equals("IN") ? 0 : 1;
      amounts[index] = amounts[index].add(money(m, "amount"));
    }
    boolean openingBelowMinimum = balance.compareTo(floor) < 0;
    List<Map<String, Object>> out = new ArrayList<>();
    BigDecimal lowest = balance;
    String firstShortfall = "";
    for (int i = 0; i < days; i++) {
      var d = start.plusDays(i);
      var amounts = movements.getOrDefault(d, new BigDecimal[] {BigDecimal.ZERO, BigDecimal.ZERO});
      var opening = balance;
      balance = balance.add(amounts[0]).subtract(amounts[1]);
      lowest = lowest.min(balance);
      boolean shortfall = balance.compareTo(floor) < 0;
      if (shortfall && firstShortfall.isEmpty()) firstShortfall = d.toString();
      out.add(
          Map.of(
              "date",
              d.toString(),
              "opening",
              opening,
              "receipts",
              amounts[0],
              "payments",
              amounts[1],
              "closing",
              balance,
              "belowMinimum",
              shortfall));
    }
    return Map.of(
        "openingBelowMinimum",
        openingBelowMinimum,
        "closingBalance",
        balance,
        "lowestBalance",
        lowest,
        "firstShortfallDate",
        firstShortfall,
        "days",
        out);
  }
}
