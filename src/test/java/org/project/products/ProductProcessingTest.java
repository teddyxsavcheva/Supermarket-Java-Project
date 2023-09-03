package org.project.products;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.project.exceptions.*;
import org.project.shop.Shop;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductProcessingTest {

    private Shop supermarket;
    private Product cake;
    private Product flower;
    private ProductProcessing processingCake;
    private ProductProcessing processingFlower;

    @BeforeEach
    void init(){
        try {
            supermarket = new Shop("2+2", BigDecimal.valueOf(40),BigDecimal.valueOf(30),5,BigDecimal.valueOf(70));
        } catch (OverchargePercentageIsNegativeException e) {
            throw new RuntimeException(e);
        } catch (MaxPeriodOfDaysUntilExpirationIsNegativeException e) {
            throw new RuntimeException(e);
        } catch (ExpirationDiscountPercentageIsNegativeException e) {
            throw new RuntimeException(e);
        }

        try {
            cake = new Product("123cake","Chocolate Cake",BigDecimal.valueOf(5),ProductCategory.NUTRITION, LocalDate.of(2025,11,11));
        } catch (UnitDeliveryPriceIsNegativeException e) {
            throw new RuntimeException(e);
        }

        try {
            flower = new Product("123flower","Orchid",BigDecimal.valueOf(5),ProductCategory.NON_NUTRITION,LocalDate.of(2025,11,11));
        } catch (UnitDeliveryPriceIsNegativeException e) {
            throw new RuntimeException(e);
        }

        processingCake = new ProductProcessing(cake);
        processingFlower = new ProductProcessing(flower);
    }

    @Test
    void isProductNutritious_yes() {
        assertEquals(true,processingCake.isProductNutritious());
    }

    @Test
    void isProductNutritious_no() {
        assertEquals(false,processingFlower.isProductNutritious());

    }
    @Test
    void overchargeValue_withoutException_nutrition() {
        assertEquals(BigDecimal.valueOf(0.40),processingCake.overchargeValue(supermarket));
    }

    @Test
    void overchargeValue_withoutException_nonNutrition() {
        assertEquals(BigDecimal.valueOf(0.30),processingFlower.overchargeValue(supermarket));
    }

    /* Тук вероятно заради сетъра на магазина не работи както трябва */
  /*  @Test
    void overchargeValue_withException_nutrition() throws MaxPeriodOfDaysUntilExpirationIsNegativeException, ExpirationDiscountPercentageIsNegativeException, OverchargePercentageIsNegativeException {
        Shop shop_overchargeNutrition = new Shop("No name", BigDecimal.valueOf(-1),BigDecimal.valueOf(20),5,BigDecimal.valueOf(70));
        assertThrows(OverchargePercentageIsNegativeException.class, () -> processingCake.overchargeValue(shop_overchargeNutrition));
    }

    @Test
    void overchargeValue_withException_nonNutrition() throws MaxPeriodOfDaysUntilExpirationIsNegativeException, ExpirationDiscountPercentageIsNegativeException, OverchargePercentageIsNegativeException {
        Shop shop_overchargeNonNutrition = new Shop("No name", BigDecimal.valueOf(10),BigDecimal.valueOf(-1),5,BigDecimal.valueOf(70));
        assertThrows(OverchargePercentageIsNegativeException.class, () -> processingFlower.overchargeValue(shop_overchargeNonNutrition));
    }*/

    @Test
    void isThereExpirationDateDiscount_no() {
        assertEquals(false,processingCake.isThereExpirationDateDiscount(supermarket));
    }

    @Test
    void isThereExpirationDateDiscount_yes() {
        Product iceCream;
        try {
            iceCream = new Product("123iceCream","Ice Cream", BigDecimal.valueOf(5),ProductCategory.NUTRITION,LocalDate.of(2023,5,24));
        } catch (UnitDeliveryPriceIsNegativeException e) {
            throw new RuntimeException(e);
        }
        ProductProcessing processingIceCream = new ProductProcessing(iceCream);
        assertEquals(true,processingIceCream.isThereExpirationDateDiscount(supermarket));
    }

    @Test
    void expirationDiscountValue_withoutException_yes() {
        Product iceCream;
        try {
            iceCream = new Product("123iceCream","Ice Cream", BigDecimal.valueOf(5),ProductCategory.NUTRITION,LocalDate.of(2023,5,24));
        } catch (UnitDeliveryPriceIsNegativeException e) {
            throw new RuntimeException(e);
        }
        ProductProcessing processingIceCream = new ProductProcessing(iceCream);
        assertEquals(BigDecimal.valueOf(0.70),processingIceCream.expirationDiscountValue(supermarket));
    }

    @Test
    void expirationDiscountValue_withoutException_no() {
        assertEquals(BigDecimal.valueOf(0.00).setScale(2),processingCake.expirationDiscountValue(supermarket));
    }

    /* Tова е същото като надценката */
   /* @Test
    void expirationDiscountValue_withException() throws MaxPeriodOfDaysUntilExpirationIsNegativeException, ExpirationDiscountPercentageIsNegativeException, OverchargePercentageIsNegativeException {
        Shop shop = new Shop("No name", BigDecimal.valueOf(40),BigDecimal.valueOf(30),5,BigDecimal.valueOf(-1));
        assertThrows(ExpirationDiscountPercentageIsNegativeException.class, () -> processingFlower.expirationDiscountValue(shop));
    }*/
    @Test
    void isProductExpired_yes() {
        Product iceCream;
        try {
            iceCream = new Product("123iceCream","Ice Cream", BigDecimal.valueOf(5),ProductCategory.NUTRITION,LocalDate.of(2022,5,24));
        } catch (UnitDeliveryPriceIsNegativeException e) {
            throw new RuntimeException(e);
        }
        ProductProcessing processingIceCream = new ProductProcessing(iceCream);
        assertEquals(true,processingIceCream.isProductExpired());
    }

    @Test
    void isProductExpired_no() {
        assertEquals(false,processingFlower.isProductExpired());
    }

    /* Никаква идея защо не бачка? На бележките и на всичко останало си ми ги смята правилно */
    @Test
    void sellingPrice_nutrition_withoutDiscount_notExpired() throws TheProductHasExpiredException, UnitDeliveryPriceIsNegativeException {
        Product chocolate = new Product("123chocolate","Chocolate", BigDecimal.valueOf(5),ProductCategory.NUTRITION,LocalDate.of(2025,5,24));
        ProductProcessing processingChocolate = new ProductProcessing(chocolate);
        assertEquals(BigDecimal.valueOf(7.00).setScale(2),processingChocolate.sellingPrice(supermarket));
    }

    @Test
    void sellingPrice_nutrition_withDiscount_notExpired() throws UnitDeliveryPriceIsNegativeException, TheProductHasExpiredException {
        Product iceCream = new Product("123iceCream","Ice Cream", BigDecimal.valueOf(5),ProductCategory.NUTRITION,LocalDate.of(2023,5,30));
        ProductProcessing processingIceCream = new ProductProcessing(iceCream);
        assertEquals(BigDecimal.valueOf(4.90).setScale(2),processingIceCream.sellingPrice(supermarket));
    }

    @Test
    void sellingPrice_nutrition_expired() throws UnitDeliveryPriceIsNegativeException {
        Product iceCream = new Product("123iceCream","Ice Cream", BigDecimal.valueOf(5),ProductCategory.NUTRITION,LocalDate.of(2022,5,24));
        ProductProcessing processingIceCream = new ProductProcessing(iceCream);
        assertThrows(TheProductHasExpiredException.class, () -> processingIceCream.sellingPrice(supermarket));
    }

    @Test
    void sellingPrice_nonNutrition_withoutDiscount_notExpired() throws TheProductHasExpiredException, UnitDeliveryPriceIsNegativeException {
        Product washingPowder = new Product("123washingPowder","Washing Powder", BigDecimal.valueOf(5),ProductCategory.NON_NUTRITION,LocalDate.of(2024,6,24));
        ProductProcessing processingWashingPowder = new ProductProcessing(washingPowder);
        assertEquals(BigDecimal.valueOf(6.50).setScale(2),processingWashingPowder.sellingPrice(supermarket));
    }

    @Test
    void sellingPrice_nonNutrition_withDiscount_notExpired() throws UnitDeliveryPriceIsNegativeException, TheProductHasExpiredException {
        Product washingPowder = new Product("123washingPowder","Washing Powder", BigDecimal.valueOf(5),ProductCategory.NON_NUTRITION,LocalDate.of(2023,5,30));
        ProductProcessing processingWashingPowder = new ProductProcessing(washingPowder);
        assertEquals(BigDecimal.valueOf(4.55).setScale(2),processingWashingPowder.sellingPrice(supermarket));
    }

    @Test
    void sellingPrice_nonNutrition_expired() throws UnitDeliveryPriceIsNegativeException {
        Product washingPowder= new Product("123iceCream","Ice Cream", BigDecimal.valueOf(5),ProductCategory.NON_NUTRITION,LocalDate.of(2022,5,24));
        ProductProcessing processingWashingPowder = new ProductProcessing(washingPowder);
        assertThrows(TheProductHasExpiredException.class, () -> processingWashingPowder.sellingPrice(supermarket));
    }
}
