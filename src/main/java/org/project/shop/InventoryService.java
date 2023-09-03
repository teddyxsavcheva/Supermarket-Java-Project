package org.project.shop;

import org.project.distributor.Distributor;
import org.project.exceptions.InsufficientQuantityException;
import org.project.exceptions.TheProductHasExpiredException;
import org.project.products.Product;

import java.math.BigDecimal;

public interface InventoryService {
    void addProductsToInventory(Distributor delivery) throws TheProductHasExpiredException;
    void removeFromInventory(Product product, BigDecimal quantity) throws InsufficientQuantityException;
    BigDecimal getQuantityOfProduct(Product product);
    boolean isProductAvailable(Product product, BigDecimal quantity) throws InsufficientQuantityException;
}
