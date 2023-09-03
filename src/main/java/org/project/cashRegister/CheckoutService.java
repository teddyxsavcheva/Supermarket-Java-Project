package org.project.cashRegister;

import org.project.products.ProductProcessing;
import org.project.receipt.Receipt;

import java.math.BigDecimal;

public interface CheckoutService {
    void saveSoldProducts(ProductProcessing product);
    void addReceiptToSet(Receipt receipt);
    BigDecimal turnover();
}
