package org.project.distributor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.project.exceptions.TheProductHasExpiredException;
import org.project.exceptions.UnitDeliveryPriceIsNegativeException;
import org.project.products.Product;
import org.project.products.ProductCategory;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DistributorTest {

    @Test
    void isNoExpiredProducts_withoutException() throws UnitDeliveryPriceIsNegativeException, TheProductHasExpiredException {
        Delivery deliveryOfCakes = new Delivery();
        Product cake = new Product("123cake","Chocolate cake", BigDecimal.valueOf(5), ProductCategory.NUTRITION, LocalDate.of(2024,1,1));
        BigDecimal quantity = BigDecimal.valueOf(10);
        deliveryOfCakes.addProductToDeliverToTheShop(cake,quantity);

        Distributor distributor = new Distributor("Firm name", "123C",deliveryOfCakes);

        // Verify the products and quantities in the delivery
        Assertions.assertTrue(distributor.getDelivery().getDelivery().containsKey(cake));
        Assertions.assertEquals(quantity, distributor.getDelivery().getDelivery().get(cake));

        // Verify that none of the products have expired
        Assertions.assertTrue(distributor.getDelivery().getDelivery().keySet()
                .stream().noneMatch(product -> product.getExpirationDate().isBefore(LocalDate.now())));
    }

    /* Тук също не съм сигурна как да тествам */
    @Test
    void isNoExpiredProducts_withException() throws UnitDeliveryPriceIsNegativeException, TheProductHasExpiredException {
        Delivery deliveryOfCakes = new Delivery();
        Product cake = new Product("123cake","Chocolate cake", BigDecimal.valueOf(5), ProductCategory.NUTRITION, LocalDate.of(2022,1,1));
        BigDecimal quantity = BigDecimal.valueOf(10);
         try {
            deliveryOfCakes.addProductToDeliverToTheShop(cake, quantity);
            Assertions.fail("Expected TheProductHasExpiredException to be thrown");
        } catch (TheProductHasExpiredException e) {
            // Verify the products and quantities in the delivery
            Assertions.assertTrue(deliveryOfCakes.getDelivery().isEmpty());
            // Verify that none of the products have expired
            Assertions.assertThrows(TheProductHasExpiredException.class, () -> {
                throw e;
            });
        }
    }
}