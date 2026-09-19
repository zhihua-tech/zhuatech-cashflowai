/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.cashflowai.controller;
import cn.zhuatech.cashflowai.common.ApiResponse;
import cn.zhuatech.cashflowai.service.LiquidityStressGovernanceService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@RestController @RequestMapping("/api/enterprise/cashflow")
public class LiquidityStressGovernanceController {
    private final LiquidityStressGovernanceService service;
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public LiquidityStressGovernanceController(LiquidityStressGovernanceService service) { this.service = service; }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/liquidity-stress")
    public ApiResponse<LiquidityStressGovernanceService.Result> evaluate(@Valid @RequestBody LiquidityStressGovernanceService.Request request) {
        return ApiResponse.ok(service.evaluate(request));
    }
}
