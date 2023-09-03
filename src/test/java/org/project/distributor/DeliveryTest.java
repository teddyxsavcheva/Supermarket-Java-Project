package org.project.distributor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.project.exceptions.TheProductHasExpiredException;
import org.project.exceptions.UnitDeliveryPriceIsNegativeException;
import org.project.products.Product;
import org.project.products.ProductCategory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryTest {
    /* може да имам отделни тестови класове */
    @Test
    void addProductToDeliverToTheShop_withoutException() throws TheProductHasExpiredException, UnitDeliveryPriceIsNegativeException {
        Delivery deliveryOfCakes = new Delivery();
        Product cake = new Product("123cake","Chocolate cake",BigDecimal.valueOf(5), ProductCategory.NUTRITION, LocalDate.of(2024,1,1));
        BigDecimal quantity = BigDecimal.valueOf(10);
        deliveryOfCakes.addProductToDeliverToTheShop(cake,quantity);

        Map<Product,BigDecimal> expectedDelivery = new TreeMap<>();
        expectedDelivery.put(cake,quantity);
        Assertions.assertEquals(expectedDelivery,deliveryOfCakes.getDelivery());
    }
    @Test
    void addProductToDeliverToTheShop_withoutException_withCake() throws TheProductHasExpiredException, UnitDeliveryPriceIsNegativeException {
        Delivery deliveryOfCakes = new Delivery();
        Product cake = new Product("123cake","Chocolate cake",BigDecimal.valueOf(5), ProductCategory.NUTRITION, LocalDate.of(2024,1,1));
        BigDecimal quantity = BigDecimal.valueOf(10);
        deliveryOfCakes.addProductToDeliverToTheShop(cake,quantity);
        deliveryOfCakes.addProductToDeliverToTheShop(cake,quantity);

        Map<Product,BigDecimal> expectedDelivery = new TreeMap<>();
        expectedDelivery.put(cake,BigDecimal.valueOf(20));
        Assertions.assertEquals(expectedDelivery,deliveryOfCakes.getDelivery());
    }

    @Test
    void addProductToDeliverToTheShop_withException() throws UnitDeliveryPriceIsNegativeException, TheProductHasExpiredException {
        Delivery deliveryOfCakes = new Delivery();
        Product expiredCake = new Product("123cake","Chocolate cake",BigDecimal.valueOf(5), ProductCategory.NUTRITION, LocalDate.of(2022,1,1));
        BigDecimal quantity = BigDecimal.valueOf(10);

        Assertions.assertThrows(TheProductHasExpiredException.class, () -> deliveryOfCakes.addProductToDeliverToTheShop(expiredCake,quantity));
    }

    @Test
    void addMapProductsToDeliverToTheShop_withoutException() throws TheProductHasExpiredException, UnitDeliveryPriceIsNegativeException {
        Delivery deliveryOfCakes = new Delivery();
        Product cake = new Product("123cake","Chocolate cake",BigDecimal.valueOf(5), ProductCategory.NUTRITION, LocalDate.of(2024,1,1));
        BigDecimal quantity = BigDecimal.valueOf(10);

        Map<Product,BigDecimal> deliveryMap = new TreeMap<>();
        deliveryMap.put(cake,quantity);
        deliveryMap.put(cake,quantity);

        deliveryOfCakes.addProductsToDeliverToTheShop(deliveryMap);
        Map<Product,BigDecimal> expectedDelivery = new TreeMap<>();
        expectedDelivery.putAll(deliveryMap);

        Assertions.assertEquals(expectedDelivery,deliveryOfCakes.getDelivery());
    }

    @Test
    void addMapProductsToDeliverToTheShop_withException() throws UnitDeliveryPriceIsNegativeException, TheProductHasExpiredException {
        Delivery deliveryOfCakes = new Delivery();
        Product cake = new Product("123cake","Chocolate cake",BigDecimal.valueOf(5), ProductCategory.NUTRITION, LocalDate.of(2022,1,1));
        BigDecimal quantity = BigDecimal.valueOf(10);

        Map<Product,BigDecimal> deliveryMap = new TreeMap<>();
        deliveryMap.put(cake,quantity);
        deliveryMap.put(cake,quantity);

        Assertions.assertThrows(TheProductHasExpiredException.class, () -> deliveryOfCakes.addProductsToDeliverToTheShop(deliveryMap));
    }
}