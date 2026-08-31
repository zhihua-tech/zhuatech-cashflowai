/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.cashflowai.service;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
@Service
public class LiquidityStressGovernanceService {
    public Result evaluate(Request request) {
        long stressedReceipts = request.expectedReceiptsCents() * (10_000L - request.receiptHaircutBps()) / 10_000L;
        long stressedClosing = request.openingCashCents() + stressedReceipts - request.committedPaymentsCents();
        List<String> actions = new ArrayList<>();
        if (stressedClosing < request.minimumLiquidityCents()) actions.add("压力情景流动性低于最低现金线");
        if (!request.covenantForecastComplete()) actions.add("融资契约预测未完成");
        if (!request.forecastOwnerApproved()) actions.add("13 周预测尚未由资金负责人批准");
        if (request.overdueReceivablesPercent() > 25) actions.add("逾期应收占比超过 25%");
        String decision = stressedClosing < 0 ? "CRITICAL" : actions.isEmpty() ? "HEALTHY" : "MITIGATE";
        return new Result(request.scenarioId(), decision, stressedClosing,
                stressedClosing - request.minimumLiquidityCents(), List.copyOf(actions), actions.isEmpty());
    }
    public record Request(@NotBlank String scenarioId, @Min(0) long openingCashCents,
                          @Min(0) long expectedReceiptsCents, @Min(0) long committedPaymentsCents,
                          @Min(0) long minimumLiquidityCents, @Min(0) int receiptHaircutBps,
                          @Min(0) int overdueReceivablesPercent, boolean covenantForecastComplete,
                          boolean forecastOwnerApproved) {
        public Request {
            if (scenarioId == null || scenarioId.isBlank()) throw new IllegalArgumentException("scenarioId is required");
            if (openingCashCents < 0 || expectedReceiptsCents < 0 || committedPaymentsCents < 0 || minimumLiquidityCents < 0)
                throw new IllegalArgumentException("cash values must be non-negative");
            if (receiptHaircutBps < 0 || receiptHaircutBps > 10_000 || overdueReceivablesPercent < 0 || overdueReceivablesPercent > 100)
                throw new IllegalArgumentException("invalid percentage");
        }
    }
    public record Result(String scenarioId, String decision, long stressedClosingCashCents,
                         long liquidityHeadroomCents, List<String> requiredActions, boolean fundingPlanApproved) {}
}
