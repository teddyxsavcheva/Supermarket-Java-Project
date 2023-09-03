package org.project.cashRegister;

import org.project.client.Customer;
import org.project.exceptions.InsufficientQuantityException;
import org.project.exceptions.NotEnoughMoneyToBuyTheProductsException;
import org.project.exceptions.TheProductHasExpiredException;
import org.project.products.ProductProcessing;
import org.project.shop.Shop;

import java.math.BigDecimal;

public interface CheckoutProcess {
    boolean isMoneyInWalletEnough(Shop shop, Customer customer) throws TheProductHasExpiredException;
    void sellingProcessingForCustomer(Shop shop, Customer customer) throws InsufficientQuantityException;
    void sellingProcessingForQueue(Shop shop) throws InsufficientQuantityException;
    BigDecimal calculateChange(Shop shop, Customer customer) throws TheProductHasExpiredException;
    void sellingToCustomer(Shop shop, Customer customer) throws TheProductHasExpiredException,
          InsufficientQuantityException, NotEnoughMoneyToBuyTheProductsException;

    void sellingToCustomers(Shop shop) throws TheProductHasExpiredException,
            InsufficientQuantityException, NotEnoughMoneyToBuyTheProductsException;

}
