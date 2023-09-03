package org.project.client;
import org.project.exceptions.InsufficientQuantityException;
import org.project.exceptions.TheProductHasExpiredException;
import org.project.products.ProductProcessing;
import org.project.shop.Shop;
import java.math.BigDecimal;
import java.util.Map;

public interface ShoppingCartService {
    void addProductToBasket(Shop shop, ProductProcessing product, BigDecimal quantity)
            throws TheProductHasExpiredException, InsufficientQuantityException;

    void addProductsToBasket(Shop shop, Map<ProductProcessing, BigDecimal> products)
            throws TheProductHasExpiredException, InsufficientQuantityException;

    BigDecimal shoppingCartPrice(Shop shop) throws TheProductHasExpiredException;

    void clearShoppingCart();
}
