package org.project.receipt;

import org.project.cashier.Cashier;
import org.project.exceptions.TheProductHasExpiredException;
import org.project.products.ProductProcessing;
import org.project.shop.Shop;

import java.awt.print.Printable;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

public class Receipt {
    private Shop shop;
    private final long id;
    private static long numberOfInstances = 0;
    private Cashier cashier;
    private LocalDateTime dateOfPurchase;
    private Map<ProductProcessing,BigDecimal> shoppingCart;
    private BigDecimal orderPrice;

    public Receipt(Shop shop, Cashier cashier, Map<ProductProcessing, BigDecimal> basket, BigDecimal orderPrice) {
        this.shop = shop;
        this.id = ++numberOfInstances;
        this.dateOfPurchase = LocalDateTime.now();
        this.cashier = cashier;
        this.shoppingCart = basket;
        this.orderPrice = orderPrice;
    }

    public Receipt() {
        this.id = ++numberOfInstances;
    }

    public long getId() {
        return id;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice.setScale(2);
    }

    public Shop getShop() {
        return shop;
    }

    public String formattedDateTime(){
        String pattern = "dd/MM/yyyy HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String formattedDateTime = dateOfPurchase.format(formatter);
        return formattedDateTime;
    }

    public String getShoppingCartDetails() throws TheProductHasExpiredException {
        StringBuilder sb = new StringBuilder();
        for (ProductProcessing product : shoppingCart.keySet()) {
            BigDecimal sellingPrice = product.sellingPrice(shop);
            BigDecimal quantity = shoppingCart.get(product);
            sb.append("\n" + product + "\t x" + quantity + "\t\t" + sellingPrice + "\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Receipt receipt = (Receipt) o;
        return id == receipt.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getDateAndIdSection() {
        return "\t\t\t\t\t\tReceipt from Shop '" + getShop().getName() + "':" +
                "\n\t\t\t\tDate of purchase: " + formattedDateTime();
    }

    public String getRestOfPurchaseInfo(){
        try {
            return "\nID of receipt: " + id +
                    "\nCashier that took care of your purchase:\n" + cashier +
                    "\n\nShoppingCart: " + getShoppingCartDetails() +
                    "\nTotal price: " + orderPrice.setScale(2);
        } catch (TheProductHasExpiredException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        try {
            return "\t\t\t\t\t\tReceipt from Shop '" + getShop().getName() + "':" +
                    "\n\t\t\t\tDate of purchase: " + formattedDateTime() +
                    "\n\nID of receipt: " + id +
                    "\nCashier that took care of your purchase:\n" + cashier +
                    "\n\nShoppingCart: " + getShoppingCartDetails() +
                    "\nTotal price: " + orderPrice.setScale(2);
        } catch (TheProductHasExpiredException e) {
            throw new RuntimeException(e);
        }
    }
}
