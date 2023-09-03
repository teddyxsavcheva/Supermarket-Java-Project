package org.project.shop;
import org.project.cashRegister.CashRegister;
import org.project.cashier.Cashier;
import java.math.BigDecimal;

public interface CompanyService {
    void addCashier(Cashier cashier);
    void addCashRegister(CashRegister cashRegister);
    BigDecimal totalDeliveryExpenses();
    BigDecimal totalSalaryExpenses();
    BigDecimal amountSpent();
    BigDecimal amountEarned();
    BigDecimal profit();
}
