/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.cashflowai.controller;
import cn.zhuatech.cashflowai.common.ApiResponse;
import cn.zhuatech.cashflowai.service.LiquidityStressGovernanceService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/enterprise/cashflow")
public class LiquidityStressGovernanceController {
    private final LiquidityStressGovernanceService service;
    public LiquidityStressGovernanceController(LiquidityStressGovernanceService service) { this.service = service; }
    @PostMapping("/liquidity-stress")
    public ApiResponse<LiquidityStressGovernanceService.Result> evaluate(@Valid @RequestBody LiquidityStressGovernanceService.Request request) {
        return ApiResponse.ok(service.evaluate(request));
    }
}
