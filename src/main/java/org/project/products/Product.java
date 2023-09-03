package org.project.products;

import org.project.exceptions.UnitDeliveryPriceIsNegativeException;
import org.project.shop.Shop;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;

public class Product implements Comparable<Product>{
    private String inventoryNumber;
    private String name;
    private BigDecimal unitDeliveryPrice; //единична цена за доставка
    private ProductCategory productCategory;
    private LocalDate expirationDate;

    public Product() {
    }

    public Product(String inventoryNumber, String name, BigDecimal unitDeliveryPrice, ProductCategory productCategory, LocalDate expirationDate) throws UnitDeliveryPriceIsNegativeException {
        this.inventoryNumber = inventoryNumber;
        this.name = name;
        setUnitDeliveryPrice(unitDeliveryPrice);
        this.productCategory = productCategory;
        this.expirationDate = expirationDate;
    }

    public String getInventoryNumber() {
        return inventoryNumber;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getUnitDeliveryPrice() {
        return unitDeliveryPrice;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setUnitDeliveryPrice(BigDecimal unitDeliveryPrice) throws UnitDeliveryPriceIsNegativeException {
        if(unitDeliveryPrice.compareTo(BigDecimal.valueOf(0))==-1){
            throw new UnitDeliveryPriceIsNegativeException("The unit delivery price must be of a non negative value!",unitDeliveryPrice);
        }
        this.unitDeliveryPrice = unitDeliveryPrice;
    }

    @Override
    public int compareTo(Product product) {
        return Integer.compare(this.productCategory.getCodeForPriority(), product.productCategory.getCodeForPriority());
    }

    public static Comparator<Product> ComparatorByProductCategory = new Comparator<Product>() {
        @Override
        public int compare(Product product1, Product product2) {
            return product1.productCategory.compareTo(product2.productCategory);
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(inventoryNumber, product.inventoryNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inventoryNumber);
    }

    @Override
    public String toString() {
        return "\nProduct{ " //+ shop
        +
                ", inventoryNumber = '" + inventoryNumber + '\'' +
                ", name = '" + name + '\'' +
                ", unitDeliveryPrice = " + unitDeliveryPrice +
                ", productCategory = " + productCategory +
                ", expirationDate = " + expirationDate + "} ";
    }
}
