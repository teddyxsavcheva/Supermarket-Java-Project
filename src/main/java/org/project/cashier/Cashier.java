package org.project.cashier;
import org.project.cashRegister.CashRegister;
import java.math.BigDecimal;
import java.util.Objects;

public class Cashier implements Employable{
    private String cashierName;
    private String idNumber;
    private BigDecimal monthlySalary;
    private CashRegister cashRegister;
    public Cashier(String cashierName, String idNumber, BigDecimal monthlySalary) {
        this.cashierName = cashierName;
        this.idNumber = idNumber;
        this.monthlySalary = monthlySalary;
        this.cashRegister = null;
    }

    public String getCashierName() {
        return cashierName;
    }

    public BigDecimal getMonthlySalary() {
        return monthlySalary;
    }

    public CashRegister getCashRegister() {
        return cashRegister;
    }

    public void setCashRegister(CashRegister cashRegister) {
        if(this.cashRegister==null){
            this.cashRegister = cashRegister;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cashier cashier = (Cashier) o;
        return Objects.equals(idNumber, cashier.idNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idNumber);
    }

    @Override
    public String toString() {
        return cashierName + '\'' +
                ", idNumber: " + idNumber + '\'' +
                ", on cash register " + (cashRegister != null ? cashRegister.getIdNumber() : "none");
    }
}
