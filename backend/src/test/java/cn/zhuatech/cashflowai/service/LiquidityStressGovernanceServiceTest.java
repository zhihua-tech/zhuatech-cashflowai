/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.cashflowai.service;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class LiquidityStressGovernanceServiceTest {
    private final LiquidityStressGovernanceService service = new LiquidityStressGovernanceService();
    @Test void approvesHealthyStressScenario() {
        var r = service.evaluate(new LiquidityStressGovernanceService.Request("CF-001", 1_000_000, 2_000_000, 1_200_000, 500_000, 1000, 10, true, true));
        assertEquals("HEALTHY", r.decision()); assertTrue(r.fundingPlanApproved()); assertEquals(1_600_000, r.stressedClosingCashCents());
    }
    @Test void flagsCriticalLiquidityShortfall() {
        var r = service.evaluate(new LiquidityStressGovernanceService.Request("CF-002", 100_000, 400_000, 800_000, 300_000, 5000, 40, false, false));
        assertEquals("CRITICAL", r.decision()); assertEquals(4, r.requiredActions().size()); assertFalse(r.fundingPlanApproved());
    }
}
