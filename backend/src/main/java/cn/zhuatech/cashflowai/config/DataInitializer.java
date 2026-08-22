/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.cashflowai.config;
import cn.zhuatech.cashflowai.model.*; import cn.zhuatech.cashflowai.repository.*; import org.springframework.boot.CommandLineRunner; import org.springframework.context.annotation.*; import org.springframework.security.crypto.password.PasswordEncoder; import java.time.LocalDate; import java.util.List;
@Configuration public class DataInitializer {
 @Bean CommandLineRunner seed(OperatingUnitRepository units,WorkRecordRepository orders,ResourceRegisterRepository resources,ReviewRecordRepository reviews,UserRepository users,PasswordEncoder encoder){return args->{if(units.count()>0)return;
 var u1=units.save(new OperatingUnit("TREASURY-GROUP","集团资金管理组","财务中心",180));var u2=units.save(new OperatingUnit("TREASURY-EAST","华东区域财资组","共享中心",120));var u3=units.save(new OperatingUnit("CREDIT-MANAGE","授信管理组","财务中心",96));
 var t1=orders.save(new WorkRecord("CF-260815-018","ENTITY-SH-01","上海制造主体三十日预测",u1,24,16,1,LocalDate.now().plusDays(1),WorkRecord.Status.RUNNING,"银行+ERP"));
 var t2=orders.save(new WorkRecord("CF-260815-021","ENTITY-HD-03","华东销售主体回款情景",u2,18,8,0,LocalDate.now().plusDays(2),WorkRecord.Status.RUNNING,"应收+承诺"));
 var t3=orders.save(new WorkRecord("CF-260815-026","ENTITY-SC-06","供应链集中付款预测",u1,12,0,0,LocalDate.now().plusDays(3),WorkRecord.Status.RELEASED,"应付+授信"));
 var t4=orders.save(new WorkRecord("CF-260814-015","ENTITY-SZ-02","深圳服务主体资金复盘",u3,20,20,1,LocalDate.now(),WorkRecord.Status.COMPLETED,"银行+预算"));
 resources.saveAll(List.of(new ResourceRegister("BANK-01","集团银企直连平台",u1,ResourceRegister.Status.RUNNING,99),new ResourceRegister("MODEL-02","现金流滚动预测模型",u2,ResourceRegister.Status.IDLE,94),new ResourceRegister("ERP-03","应收应付数据连接器",u2,ResourceRegister.Status.RUNNING,93),new ResourceRegister("CREDIT-04","授信额度服务",u3,ResourceRegister.Status.ALARM,89)));
 reviews.saveAll(List.of(new ReviewRecord("RV-260804-032",t1,"人工复核",6,0,ReviewRecord.Result.PASSED,"程越"),new ReviewRecord("RV-260804-011",t2,"质量检查",3,0,ReviewRecord.Result.PASSED,"许知"),new ReviewRecord("RV-260803-018",t4,"结果抽查",5,1,ReviewRecord.Result.FAILED,"程越"),new ReviewRecord("RV-260804-003",t3,"上线确认",4,0,ReviewRecord.Result.PENDING,"许知")));
 String demo=encoder.encode("Demo@2026");users.saveAll(List.of(new UserAccount("operator",demo,"许知",UserAccount.Role.DOMAIN_USER,"SEARCH-OPS"),new UserAccount("planner",demo,"程越",UserAccount.Role.DOMAIN_OPERATOR,null),new UserAccount("quality",demo,"顾清",UserAccount.Role.QUALITY,null),new UserAccount("admin",encoder.encode("ZhuaTech@2026"),"系统管理员",UserAccount.Role.ADMIN,null)));};}
}
