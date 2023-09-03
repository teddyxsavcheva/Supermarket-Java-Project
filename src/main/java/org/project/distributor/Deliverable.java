package org.project.distributor;

import org.project.exceptions.TheProductHasExpiredException;
import org.project.products.Product;

import java.math.BigDecimal;
import java.util.Map;

public interface Deliverable {
    void addProductToDeliverToTheShop(Product product, BigDecimal quantity) throws TheProductHasExpiredException;
    void addProductsToDeliverToTheShop(Map<Product,BigDecimal> products) throws TheProductHasExpiredException;

}
