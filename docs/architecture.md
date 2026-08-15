# CASHFLOWAI 架构

版权所有 © 2026 上海如静知华信息科技有限公司。

Vue 3 管理端和 H5 工作台通过 JWT 调用 Spring Boot REST API。领域服务 `CashflowForecastService` 组合期初余额、预计收付款、逾期应收、已承诺授信和预测置信度，输出期末余额、可用流动性、资金缺口与覆盖天数；JPA 与 Flyway 管理 MySQL 数据，Docker Compose 负责本地编排。

生产落地时应接入企业 SSO、银企直连、ERP、应收应付、预算与授信平台，并对付款调整和授信提款保留授权审批及完整审计证据。
