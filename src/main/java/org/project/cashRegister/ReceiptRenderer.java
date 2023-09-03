package org.project.cashRegister;

import org.project.receipt.Receipt;
import org.project.shop.Shop;

import java.io.*;
import java.math.BigDecimal;

public interface ReceiptRenderer {
    String addOrderIdToReceipt(Receipt receipt);
    public void createReceipt(Shop shop, BigDecimal basketPrice);
    String readFromFile(String fileName);
    void saveReceiptToFile(String fileName, String content);

}
