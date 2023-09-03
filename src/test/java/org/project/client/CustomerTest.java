package org.project.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.project.distributor.Delivery;
import org.project.distributor.Distributor;
import org.project.exceptions.*;
import org.project.products.Product;
import org.project.products.ProductCategory;
import org.project.products.ProductProcessing;
import org.project.shop.Shop;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void addProductToBasket() throws UnitDeliveryPriceIsNegativeException, InsufficientQuantityException, TheProductHasExpiredException, MaxPeriodOfDaysUntilExpirationIsNegativeException, ExpirationDiscountPercentageIsNegativeException, OverchargePercentageIsNegativeException {
        Shop supermarket = new Shop("2+2",BigDecimal.valueOf(40),BigDecimal.valueOf(30),5,BigDecimal.valueOf(70));
        Product iceCream = new Product("123iceCream","Ice Cream", BigDecimal.valueOf(5), ProductCategory.NUTRITION, LocalDate.of(2025,5,24));
        ProductProcessing processingIceCream = new ProductProcessing(iceCream);

        Delivery deliveryOfIceCream = new Delivery();
        BigDecimal quantity = BigDecimal.valueOf(10);
        deliveryOfIceCream.addProductToDeliverToTheShop(iceCream,quantity);
        Distributor distributor = new Distributor("Firm Name","123order", deliveryOfIceCream);

        supermarket.addProductsToInventory(distributor);
        supermarket.addProductsToInventory(distributor);

        Customer georgi = new Customer("order123",BigDecimal.valueOf(80));
        georgi.addProductToBasket(supermarket, processingIceCream, BigDecimal.valueOf(10));

        Map<ProductProcessing, BigDecimal> expextedMap = new HashMap<>();
        expextedMap.put(processingIceCream,BigDecimal.valueOf(10));

        Assertions.assertEquals(expextedMap, georgi.getShoppingCart());
    }

    @Test
    void addProductsToBasket() throws MaxPeriodOfDaysUntilExpirationIsNegativeException, ExpirationDiscountPercentageIsNegativeException, OverchargePercentageIsNegativeException, UnitDeliveryPriceIsNegativeException, TheProductHasExpiredException, InsufficientQuantityException {
        Shop supermarket = new Shop("2+2",BigDecimal.valueOf(40),BigDecimal.valueOf(30),5,BigDecimal.valueOf(70));
        Product iceCream = new Product("123iceCream","Ice Cream", BigDecimal.valueOf(5), ProductCategory.NUTRITION, LocalDate.of(2025,5,24));
        ProductProcessing processingIceCream = new ProductProcessing(iceCream);

        Delivery deliveryOfIceCream = new Delivery();
        BigDecimal quantity = BigDecimal.valueOf(10);
        deliveryOfIceCream.addProductToDeliverToTheShop(iceCream,quantity);
        Distributor distributor = new Distributor("Firm Name","123order", deliveryOfIceCream);

        supermarket.addProductsToInventory(distributor);
        supermarket.addProductsToInventory(distributor);

        Customer georgi = new Customer("order123",BigDecimal.valueOf(80));
        Map<ProductProcessing, BigDecimal> expextedMap = new HashMap<>();
        expextedMap.put(processingIceCream,BigDecimal.valueOf(10));
        georgi.addProductsToBasket(supermarket,expextedMap);

        Assertions.assertEquals(expextedMap, georgi.getShoppingCart());
    }

    @Test
    void shoppingCartPrice() throws MaxPeriodOfDaysUntilExpirationIsNegativeException, ExpirationDiscountPercentageIsNegativeException, OverchargePercentageIsNegativeException, UnitDeliveryPriceIsNegativeException, TheProductHasExpiredException, InsufficientQuantityException {
        Shop supermarket = new Shop("2+2",BigDecimal.valueOf(40),BigDecimal.valueOf(30),5,BigDecimal.valueOf(70));
        Product iceCream = new Product("123iceCream","Ice Cream", BigDecimal.valueOf(5), ProductCategory.NUTRITION, LocalDate.of(2025,5,24));
        ProductProcessing processingIceCream = new ProductProcessing(iceCream);

        Delivery deliveryOfIceCream = new Delivery();
        BigDecimal quantity = BigDecimal.valueOf(10);
        deliveryOfIceCream.addProductToDeliverToTheShop(iceCream,quantity);
        Distributor distributor = new Distributor("Firm Name","123order", deliveryOfIceCream);

        supermarket.addProductsToInventory(distributor);
        supermarket.addProductsToInventory(distributor);

        Customer georgi = new Customer("order123",BigDecimal.valueOf(80));
        georgi.addProductToBasket(supermarket, processingIceCream, BigDecimal.valueOf(10));

        BigDecimal expectedPrice = BigDecimal.valueOf(70.00).setScale(2);
        Assertions.assertEquals(expectedPrice,georgi.shoppingCartPrice(supermarket));
    }

    @Test
    void clearShoppingCart() throws MaxPeriodOfDaysUntilExpirationIsNegativeException, ExpirationDiscountPercentageIsNegativeException, OverchargePercentageIsNegativeException, UnitDeliveryPriceIsNegativeException, TheProductHasExpiredException, InsufficientQuantityException {
        Shop supermarket = new Shop("2+2",BigDecimal.valueOf(40),BigDecimal.valueOf(30),5,BigDecimal.valueOf(70));
        Product iceCream = new Product("123iceCream","Ice Cream", BigDecimal.valueOf(5), ProductCategory.NUTRITION, LocalDate.of(2025,5,24));
        ProductProcessing processingIceCream = new ProductProcessing(iceCream);

        Delivery deliveryOfIceCream = new Delivery();
        BigDecimal quantity = BigDecimal.valueOf(10);
        deliveryOfIceCream.addProductToDeliverToTheShop(iceCream,quantity);
        Distributor distributor = new Distributor("Firm Name","123order", deliveryOfIceCream);

        supermarket.addProductsToInventory(distributor);
        supermarket.addProductsToInventory(distributor);

        Customer georgi = new Customer("order123",BigDecimal.valueOf(80));
        georgi.addProductToBasket(supermarket, processingIceCream, BigDecimal.valueOf(10));
        georgi.clearShoppingCart();

        Map<ProductProcessing, BigDecimal> expextedMap = new HashMap<>();
        expextedMap.put(processingIceCream,BigDecimal.valueOf(10));
        expextedMap.clear();

        Assertions.assertEquals(expextedMap,georgi.getShoppingCart());
    }
}