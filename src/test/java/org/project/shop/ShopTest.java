package org.project.shop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.project.cashRegister.CashRegister;
import org.project.cashier.Cashier;
import org.project.client.Customer;
import org.project.distributor.Delivery;
import org.project.distributor.Distributor;
import org.project.exceptions.*;
import org.project.products.Product;
import org.project.products.ProductCategory;
import org.project.products.ProductProcessing;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class ShopTest {

    @Test
    void addCashier() throws MaxPeriodOfDaysUntilExpirationIsNegativeException, ExpirationDiscountPercentageIsNegativeException, OverchargePercentageIsNegativeException {
        Shop supermarket = new Shop("2+2", BigDecimal.valueOf(40),BigDecimal.valueOf(30),5,BigDecimal.valueOf(70));
        Set<Cashier> expectedSet = new HashSet<>();

        Cashier georgi = new Cashier("Georgi","123GG",BigDecimal.valueOf(800));
        supermarket.addCashier(georgi);
        expectedSet.add(georgi);
        Assertions.assertEquals(expectedSet,supermarket.getCashiers());
    }

    @Test
    void addCashRegister() throws MaxPeriodOfDaysUntilExpirationIsNegativeException, ExpirationDiscountPercentageIsNegativeException, OverchargePercentageIsNegativeException {
        Shop supermarket = new Shop("2+2", BigDecimal.valueOf(40),BigDecimal.valueOf(30),5,BigDecimal.valueOf(70));
        Set<CashRegister> expectedSet = new HashSet<>();

        CashRegister cashRegister = new CashRegister("123CR");
        supermarket.addCashRegister(cashRegister);
        expectedSet.add(cashRegister);
        Assertions.assertEquals(expectedSet,supermarket.getCashRegisters());
    }

    @Test
    void totalDeliveryExpenses() throws UnitDeliveryPriceIsNegativeException, TheProductHasExpiredException, MaxPeriodOfDaysUntilExpirationIsNegativeException, ExpirationDiscountPercentageIsNegativeException, OverchargePercentageIsNegativeException {
        Delivery deliveryOfCakes = new Delivery();
        Product cake = new Product("123cake","Chocolate cake", BigDecimal.valueOf(5), ProductCategory.NUTRITION, LocalDate.of(2024,1,1));
        BigDecimal quantity = BigDecimal.valueOf(10);
        deliveryOfCakes.addProductToDeliverToTheShop(cake,quantity);

        Distributor distributor = new Distributor("Firm name", "123C",deliveryOfCakes);
        Shop supermarket = new Shop("2+2", BigDecimal.valueOf(40),BigDecimal.valueOf(30),5,BigDecimal.valueOf(70));
        supermarket.addProductsToInventory(distributor);

        BigDecimal expectedExpenses = BigDecimal.valueOf(50.00).setScale(2);
        Assertions.assertEquals(expectedExpenses,supermarket.totalDeliveryExpenses());
    }

    @Test
    void totalSalaryExpenses() throws MaxPeriodOfDaysUntilExpirationIsNegativeException, ExpirationDiscountPercentageIsNegativeException, OverchargePercentageIsNegativeException {
        Shop supermarket = new Shop("2+2", BigDecimal.valueOf(40),BigDecimal.valueOf(30),5,BigDecimal.valueOf(70));

        Cashier georgi = new Cashier("Georgi","123GG",BigDecimal.valueOf(800));
        Cashier teddy = new Cashier("Teddy","123TT",BigDecimal.valueOf(800));
        supermarket.addCashier(georgi);
        supermarket.addCashier(teddy);

        BigDecimal expectedExpenses = BigDecimal.valueOf(1600.00).setScale(2);
        Assertions.assertEquals(expectedExpenses,supermarket.totalSalaryExpenses());
    }

    @Test
    void amountSpent() throws MaxPeriodOfDaysUntilExpirationIsNegativeException, ExpirationDiscountPercentageIsNegativeException, OverchargePercentageIsNegativeException, UnitDeliveryPriceIsNegativeException, TheProductHasExpiredException {
        Shop supermarket = new Shop("2+2", BigDecimal.valueOf(40),BigDecimal.valueOf(30),5,BigDecimal.valueOf(70));

        Cashier georgi = new Cashier("Georgi","123GG",BigDecimal.valueOf(800));
        Cashier teddy = new Cashier("Teddy","123TT",BigDecimal.valueOf(800));
        supermarket.addCashier(georgi);
        supermarket.addCashier(teddy);

        Delivery deliveryOfCakes = new Delivery();
        Product cake = new Product("123cake","Chocolate cake", BigDecimal.valueOf(5), ProductCategory.NUTRITION, LocalDate.of(2024,1,1));
        BigDecimal quantity = BigDecimal.valueOf(10);
        deliveryOfCakes.addProductToDeliverToTheShop(cake,quantity);

        Distributor distributor = new Distributor("Firm name", "123C",deliveryOfCakes);
        supermarket.addProductsToInventory(distributor);

        BigDecimal expectedAmount = BigDecimal.valueOf(1650.00).setScale(2);
        Assertions.assertEquals(expectedAmount,supermarket.amountSpent());
    }

    @Test
    void amountEarned() throws MaxPeriodOfDaysUntilExpirationIsNegativeException, ExpirationDiscountPercentageIsNegativeException, OverchargePercentageIsNegativeException, UnitDeliveryPriceIsNegativeException, InsufficientQuantityException, TheProductHasExpiredException, TheCustomerIsAlreadyInTheQueueException, NotEnoughMoneyToBuyTheProductsException {
        Shop supermarket = new Shop("2+2", BigDecimal.valueOf(40),BigDecimal.valueOf(30),5,BigDecimal.valueOf(70));
        CashRegister cashRegister = new CashRegister("1CR");
        supermarket.addCashRegister(cashRegister);

        Product cake = new Product("123C", "Cake", BigDecimal.valueOf(5),ProductCategory.NUTRITION,LocalDate.of(2024,5,5));
        ProductProcessing cakeProcessing = new ProductProcessing(cake);

        Delivery delivery = new Delivery();
        delivery.addProductToDeliverToTheShop(cake,BigDecimal.valueOf(10));
        Distributor distributor = new Distributor("Firm Name","123order", delivery);
        supermarket.addProductsToInventory(distributor);

        Customer georgi = new Customer("Order123",BigDecimal.valueOf(80));
        georgi.addProductToBasket(supermarket,cakeProcessing,BigDecimal.valueOf(10));
        cashRegister.addCustomerToQueue(georgi);
        cashRegister.sellingToCustomer(supermarket,georgi);

        BigDecimal expectedAmount = BigDecimal.valueOf(70.00).setScale(2);
        Assertions.assertEquals(expectedAmount,supermarket.amountEarned());
    }

    @Test
    void profit() throws UnitDeliveryPriceIsNegativeException, MaxPeriodOfDaysUntilExpirationIsNegativeException, ExpirationDiscountPercentageIsNegativeException, OverchargePercentageIsNegativeException, TheProductHasExpiredException, InsufficientQuantityException, TheCustomerIsAlreadyInTheQueueException, NotEnoughMoneyToBuyTheProductsException {
        Shop supermarket = new Shop("2+2", BigDecimal.valueOf(40),BigDecimal.valueOf(30),5,BigDecimal.valueOf(70));
        CashRegister cashRegister = new CashRegister("1CR");
        supermarket.addCashRegister(cashRegister);

        Product cake = new Product("123C", "Cake", BigDecimal.valueOf(5),ProductCategory.NUTRITION,LocalDate.of(2024,5,5));
        ProductProcessing cakeProcessing = new ProductProcessing(cake);

        Delivery delivery = new Delivery();
        delivery.addProductToDeliverToTheShop(cake,BigDecimal.valueOf(10));
        Distributor distributor = new Distributor("Firm Name","123order", delivery);
        supermarket.addProductsToInventory(distributor);

        Customer georgi = new Customer("Order123",BigDecimal.valueOf(80));
        georgi.addProductToBasket(supermarket,cakeProcessing,BigDecimal.valueOf(10));
        cashRegister.addCustomerToQueue(georgi);
        cashRegister.sellingToCustomer(supermarket,georgi);

        BigDecimal expectedAmountEarned = BigDecimal.valueOf(70.00).setScale(2);
        BigDecimal expectedAmountSpent = BigDecimal.valueOf(50.00).setScale(2);
        BigDecimal profit = expectedAmountEarned.subtract(expectedAmountSpent).setScale(2);

        Assertions.assertEquals(profit,supermarket.profit());
    }

    @Test
    void addProductsToInventory() throws UnitDeliveryPriceIsNegativeException, TheProductHasExpiredException, MaxPeriodOfDaysUntilExpirationIsNegativeException, ExpirationDiscountPercentageIsNegativeException, OverchargePercentageIsNegativeException {
        Shop supermarket = new Shop("2+2", BigDecimal.valueOf(40),BigDecimal.valueOf(30),5,BigDecimal.valueOf(70));

        Delivery deliveryOfCakes = new Delivery();
        Product cake = new Product("123cake","Chocolate cake",BigDecimal.valueOf(5), ProductCategory.NUTRITION, LocalDate.of(2024,1,1));
        BigDecimal quantity = BigDecimal.valueOf(10);
        deliveryOfCakes.addProductToDeliverToTheShop(cake,quantity);
        Distributor distributor = new Distributor("Firm Name","123order", deliveryOfCakes);

        supermarket.addProductsToInventory(distributor);

        Map<Product,BigDecimal> expectedInventory = new TreeMap<>();
        expectedInventory.put(cake,quantity);
        Assertions.assertEquals(expectedInventory,deliveryOfCakes.getDelivery());
    }

    @Test
    void getQuantityOfProduct() throws MaxPeriodOfDaysUntilExpirationIsNegativeException, ExpirationDiscountPercentageIsNegativeException, OverchargePercentageIsNegativeException, UnitDeliveryPriceIsNegativeException, TheProductHasExpiredException {
        Shop supermarket = new Shop("2+2", BigDecimal.valueOf(40),BigDecimal.valueOf(30),5,BigDecimal.valueOf(70));

        Delivery deliveryOfCakes = new Delivery();
        Product cake = new Product("123cake","Chocolate cake",BigDecimal.valueOf(5), ProductCategory.NUTRITION, LocalDate.of(2024,1,1));
        BigDecimal quantity = BigDecimal.valueOf(10);
        deliveryOfCakes.addProductToDeliverToTheShop(cake,quantity);
        Distributor distributor = new Distributor("Firm Name","123order", deliveryOfCakes);

        supermarket.addProductsToInventory(distributor);

        Assertions.assertEquals(quantity,supermarket.getQuantityOfProduct(cake));
    }

    @Test
    void isProductAvailable_true() throws MaxPeriodOfDaysUntilExpirationIsNegativeException, ExpirationDiscountPercentageIsNegativeException, OverchargePercentageIsNegativeException, UnitDeliveryPriceIsNegativeException, TheProductHasExpiredException, InsufficientQuantityException {
        Shop supermarket = new Shop("2+2", BigDecimal.valueOf(40),BigDecimal.valueOf(30),5,BigDecimal.valueOf(70));

        Delivery deliveryOfCakes = new Delivery();
        Product cake = new Product("123cake","Chocolate cake",BigDecimal.valueOf(5), ProductCategory.NUTRITION, LocalDate.of(2024,1,1));
        BigDecimal quantity = BigDecimal.valueOf(10);
        deliveryOfCakes.addProductToDeliverToTheShop(cake,quantity);
        Distributor distributor = new Distributor("Firm Name","123order", deliveryOfCakes);

        supermarket.addProductsToInventory(distributor);

        Assertions.assertEquals(true,supermarket.isProductAvailable(cake,quantity));
    }

    @Test
    void isProductAvailable_false() throws MaxPeriodOfDaysUntilExpirationIsNegativeException, ExpirationDiscountPercentageIsNegativeException, OverchargePercentageIsNegativeException, UnitDeliveryPriceIsNegativeException, TheProductHasExpiredException, InsufficientQuantityException {
        Shop supermarket = new Shop("2+2", BigDecimal.valueOf(40),BigDecimal.valueOf(30),5,BigDecimal.valueOf(70));

        Delivery deliveryOfCakes = new Delivery();
        Product cake = new Product("123cake","Chocolate cake",BigDecimal.valueOf(5), ProductCategory.NUTRITION, LocalDate.of(2024,1,1));
        BigDecimal quantity = BigDecimal.valueOf(10);
        deliveryOfCakes.addProductToDeliverToTheShop(cake,quantity);
        Distributor distributor = new Distributor("Firm Name","123order", deliveryOfCakes);

        supermarket.addProductsToInventory(distributor);
        BigDecimal moreQuantity = BigDecimal.valueOf(15);

        Assertions.assertThrows(InsufficientQuantityException.class,() ->supermarket.isProductAvailable(cake,moreQuantity));
    }
    @Test
    void removeFromInventory() throws MaxPeriodOfDaysUntilExpirationIsNegativeException, ExpirationDiscountPercentageIsNegativeException, OverchargePercentageIsNegativeException, UnitDeliveryPriceIsNegativeException, TheProductHasExpiredException, InsufficientQuantityException {
        Shop supermarket = new Shop("2+2", BigDecimal.valueOf(40),BigDecimal.valueOf(30),5,BigDecimal.valueOf(70));

        Delivery deliveryOfCakes = new Delivery();
        Product cake = new Product("123cake","Chocolate cake",BigDecimal.valueOf(5), ProductCategory.NUTRITION, LocalDate.of(2024,1,1));
        BigDecimal quantity = BigDecimal.valueOf(10);
        deliveryOfCakes.addProductToDeliverToTheShop(cake,quantity);
        Distributor distributor = new Distributor("Firm Name","123order", deliveryOfCakes);

        supermarket.addProductsToInventory(distributor);
        supermarket.removeFromInventory(cake,BigDecimal.valueOf(5));

        Map<Product, BigDecimal> expectedMap = new TreeMap<>();
        expectedMap.put(cake,quantity.subtract(BigDecimal.valueOf(5)));

        Assertions.assertEquals(expectedMap,supermarket.getInventory());
    }

    @Test
    void removeFromInventory_withException() throws MaxPeriodOfDaysUntilExpirationIsNegativeException, ExpirationDiscountPercentageIsNegativeException, OverchargePercentageIsNegativeException, UnitDeliveryPriceIsNegativeException, TheProductHasExpiredException, InsufficientQuantityException {
        Shop supermarket = new Shop("2+2", BigDecimal.valueOf(40),BigDecimal.valueOf(30),5,BigDecimal.valueOf(70));

        Delivery deliveryOfCakes = new Delivery();
        Product cake = new Product("123cake","Chocolate cake",BigDecimal.valueOf(5), ProductCategory.NUTRITION, LocalDate.of(2024,1,1));
        BigDecimal quantity = BigDecimal.valueOf(10);
        deliveryOfCakes.addProductToDeliverToTheShop(cake,quantity);
        Distributor distributor = new Distributor("Firm Name","123order", deliveryOfCakes);

        supermarket.addProductsToInventory(distributor);
        /* друг ексепшън */
       Assertions.assertThrows(ArithmeticException.class, () -> supermarket.removeFromInventory(cake,BigDecimal.valueOf(11)));
    }
}