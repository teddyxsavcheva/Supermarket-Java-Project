package org.project.cashRegister;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.project.client.Customer;
import org.project.distributor.Delivery;
import org.project.distributor.Distributor;
import org.project.exceptions.*;
import org.project.products.Product;
import org.project.products.ProductCategory;
import org.project.products.ProductProcessing;
import org.project.receipt.Receipt;
import org.project.shop.Shop;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CashRegisterTest {

    @Test
    void addCustomerToQueue_withoutException() throws TheCustomerIsAlreadyInTheQueueException {
        CashRegister cashRegister = new CashRegister("C1");
        Customer georgi = new Customer("order123", BigDecimal.valueOf(80));
        cashRegister.addCustomerToQueue(georgi);

        Queue<Customer> expectedQueue = new LinkedList<>();
        expectedQueue.add(georgi);

        Assertions.assertEquals(expectedQueue,cashRegister.getCustomerQueue());
    }

    @Test
    void addCustomerToQueue_withException() throws TheCustomerIsAlreadyInTheQueueException {
        CashRegister cashRegister = new CashRegister("C1");
        Customer georgi = new Customer("order123", BigDecimal.valueOf(80));
        cashRegister.addCustomerToQueue(georgi);

        Assertions.assertThrows(TheCustomerIsAlreadyInTheQueueException.class, () -> cashRegister.addCustomerToQueue(georgi));
    }

    @Test
    void addCustomersToQueue_withoutException() throws TheCustomerIsAlreadyInTheQueueException {
        CashRegister cashRegister = new CashRegister("C1");
        Customer georgi = new Customer("order123", BigDecimal.valueOf(80));

        Queue<Customer> expectedQueue = new LinkedList<>();
        expectedQueue.add(georgi);
        cashRegister.addCustomersToQueue(expectedQueue);

        Assertions.assertEquals(expectedQueue,cashRegister.getCustomerQueue());

    }

    @Test
    void addCustomersToQueue_withException() throws TheCustomerIsAlreadyInTheQueueException {
        CashRegister cashRegister = new CashRegister("C1");
        Customer georgi = new Customer("order123", BigDecimal.valueOf(80));

        Queue<Customer> expectedQueue = new LinkedList<>();
        expectedQueue.add(georgi);

        cashRegister.addCustomersToQueue(expectedQueue);

        Assertions.assertThrows(TheCustomerIsAlreadyInTheQueueException.class, () -> cashRegister.addCustomersToQueue(expectedQueue));
    }

    @Test
    void isMoneyInWalletEnough_yes() throws MaxPeriodOfDaysUntilExpirationIsNegativeException, ExpirationDiscountPercentageIsNegativeException, OverchargePercentageIsNegativeException, UnitDeliveryPriceIsNegativeException, TheProductHasExpiredException, InsufficientQuantityException {
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

        CashRegister cashRegister = new CashRegister("123CR");

        Assertions.assertEquals(true, cashRegister.isMoneyInWalletEnough(supermarket,georgi));
    }

    @Test
    void isMoneyInWalletEnough_no() throws MaxPeriodOfDaysUntilExpirationIsNegativeException, ExpirationDiscountPercentageIsNegativeException, OverchargePercentageIsNegativeException, UnitDeliveryPriceIsNegativeException, TheProductHasExpiredException, InsufficientQuantityException {
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
        georgi.addProductToBasket(supermarket, processingIceCream, BigDecimal.valueOf(12));

        CashRegister cashRegister = new CashRegister("123CR");

        Assertions.assertEquals(false, cashRegister.isMoneyInWalletEnough(supermarket,georgi));
    }

    @Test
    void sellingProcessingForCustomer() throws MaxPeriodOfDaysUntilExpirationIsNegativeException, ExpirationDiscountPercentageIsNegativeException, OverchargePercentageIsNegativeException, UnitDeliveryPriceIsNegativeException, TheProductHasExpiredException, InsufficientQuantityException, TheCustomerIsAlreadyInTheQueueException {
        Shop supermarket = new Shop("2+2",BigDecimal.valueOf(40),BigDecimal.valueOf(30),5,BigDecimal.valueOf(70));
        Product iceCream = new Product("123iceCream","Ice Cream", BigDecimal.valueOf(5), ProductCategory.NUTRITION, LocalDate.of(2025,5,24));
        ProductProcessing processingIceCream = new ProductProcessing(iceCream);

        Delivery deliveryOfIceCream = new Delivery();
        BigDecimal quantity = BigDecimal.valueOf(10);
        deliveryOfIceCream.addProductToDeliverToTheShop(iceCream,quantity);
        Distributor distributor = new Distributor("Firm Name","123order", deliveryOfIceCream);

        supermarket.addProductsToInventory(distributor);

        Customer georgi = new Customer("order123",BigDecimal.valueOf(80));
        georgi.addProductToBasket(supermarket, processingIceCream, BigDecimal.valueOf(10));
        CashRegister cashRegister = new CashRegister("123CR");
        cashRegister.addCustomerToQueue(georgi);

        Map<ProductProcessing, BigDecimal> expectedSoldProducts = new HashMap<>();
        expectedSoldProducts.put(processingIceCream,BigDecimal.valueOf(10));

        supermarket.addCashRegister(cashRegister);
        cashRegister.sellingProcessingForCustomer(supermarket,georgi);

        Assertions.assertEquals(BigDecimal.valueOf(0),supermarket.getInventory().get(iceCream));
        Assertions.assertEquals(expectedSoldProducts,cashRegister.getSoldProducts());
    }

    @Test
    void sellingProcessingForCustomer_withException() throws MaxPeriodOfDaysUntilExpirationIsNegativeException, ExpirationDiscountPercentageIsNegativeException, OverchargePercentageIsNegativeException, UnitDeliveryPriceIsNegativeException, TheProductHasExpiredException, InsufficientQuantityException, TheCustomerIsAlreadyInTheQueueException {
        Shop supermarket = new Shop("2+2",BigDecimal.valueOf(40),BigDecimal.valueOf(30),5,BigDecimal.valueOf(70));
        Product iceCream = new Product("123iceCream","Ice Cream", BigDecimal.valueOf(5), ProductCategory.NUTRITION, LocalDate.of(2025,5,24));
        ProductProcessing processingIceCream = new ProductProcessing(iceCream);

        Delivery deliveryOfIceCream = new Delivery();
        BigDecimal quantity = BigDecimal.valueOf(10);
        deliveryOfIceCream.addProductToDeliverToTheShop(iceCream,quantity);
        Distributor distributor = new Distributor("Firm Name","123order", deliveryOfIceCream);

        supermarket.addProductsToInventory(distributor);

        Customer georgi = new Customer("order123",BigDecimal.valueOf(80));

       Assertions.assertThrows(InsufficientQuantityException.class,() ->  georgi.addProductToBasket(supermarket, processingIceCream, BigDecimal.valueOf(11)));
    }

    @Test
    void sellingProcessingForQueue() throws MaxPeriodOfDaysUntilExpirationIsNegativeException, ExpirationDiscountPercentageIsNegativeException, OverchargePercentageIsNegativeException, UnitDeliveryPriceIsNegativeException, TheProductHasExpiredException, InsufficientQuantityException, TheCustomerIsAlreadyInTheQueueException {
        Shop supermarket = new Shop("2+2",BigDecimal.valueOf(40),BigDecimal.valueOf(30),5,BigDecimal.valueOf(70));
        Product iceCream = new Product("123iceCream","Ice Cream", BigDecimal.valueOf(5), ProductCategory.NUTRITION, LocalDate.of(2025,5,24));
        ProductProcessing processingIceCream = new ProductProcessing(iceCream);

        Delivery deliveryOfIceCream = new Delivery();
        BigDecimal quantity = BigDecimal.valueOf(10);
        deliveryOfIceCream.addProductToDeliverToTheShop(iceCream,quantity);
        Distributor distributor = new Distributor("Firm Name","123order", deliveryOfIceCream);

        supermarket.addProductsToInventory(distributor);

        Customer georgi = new Customer("order123",BigDecimal.valueOf(80));
        georgi.addProductToBasket(supermarket, processingIceCream, BigDecimal.valueOf(10));
        CashRegister cashRegister = new CashRegister("123CR");

        cashRegister.addCustomerToQueue(georgi);

        Map<ProductProcessing, BigDecimal> expectedSoldProducts = new HashMap<>();
        expectedSoldProducts.put(processingIceCream,BigDecimal.valueOf(10));

        supermarket.addCashRegister(cashRegister);
        cashRegister.sellingProcessingForQueue(supermarket);

        Assertions.assertEquals(BigDecimal.valueOf(0),supermarket.getInventory().get(iceCream));
        Assertions.assertEquals(expectedSoldProducts,cashRegister.getSoldProducts());
    }

    @Test
    void calculateChange() throws MaxPeriodOfDaysUntilExpirationIsNegativeException, ExpirationDiscountPercentageIsNegativeException, OverchargePercentageIsNegativeException, UnitDeliveryPriceIsNegativeException, TheProductHasExpiredException, InsufficientQuantityException, TheCustomerIsAlreadyInTheQueueException {
        Shop supermarket = new Shop("2+2",BigDecimal.valueOf(40),BigDecimal.valueOf(30),5,BigDecimal.valueOf(70));
        Product iceCream = new Product("123iceCream","Ice Cream", BigDecimal.valueOf(5), ProductCategory.NUTRITION, LocalDate.of(2025,5,24));
        ProductProcessing processingIceCream = new ProductProcessing(iceCream);

        Delivery deliveryOfIceCream = new Delivery();
        BigDecimal quantity = BigDecimal.valueOf(10);
        deliveryOfIceCream.addProductToDeliverToTheShop(iceCream,quantity);
        Distributor distributor = new Distributor("Firm Name","123order", deliveryOfIceCream);

        supermarket.addProductsToInventory(distributor);

        Customer georgi = new Customer("order123",BigDecimal.valueOf(80));
        georgi.addProductToBasket(supermarket, processingIceCream, BigDecimal.valueOf(10));
        CashRegister cashRegister = new CashRegister("123CR");
        cashRegister.addCustomerToQueue(georgi);

        BigDecimal expectedChange = BigDecimal.valueOf(10.00).setScale(2);
        Assertions.assertEquals(expectedChange,cashRegister.calculateChange(supermarket,georgi));
    }

    @Test
    void sellingToCustomer() throws MaxPeriodOfDaysUntilExpirationIsNegativeException, ExpirationDiscountPercentageIsNegativeException, OverchargePercentageIsNegativeException, UnitDeliveryPriceIsNegativeException, TheProductHasExpiredException, InsufficientQuantityException, TheCustomerIsAlreadyInTheQueueException, NotEnoughMoneyToBuyTheProductsException {
        Shop supermarket = new Shop("2+2",BigDecimal.valueOf(40),BigDecimal.valueOf(30),5,BigDecimal.valueOf(70));
        Product iceCream = new Product("123iceCream","Ice Cream", BigDecimal.valueOf(5), ProductCategory.NUTRITION, LocalDate.of(2025,5,24));
        ProductProcessing processingIceCream = new ProductProcessing(iceCream);

        Delivery deliveryOfIceCream = new Delivery();
        BigDecimal quantity = BigDecimal.valueOf(10);
        deliveryOfIceCream.addProductToDeliverToTheShop(iceCream,quantity);
        Distributor distributor = new Distributor("Firm Name","123order", deliveryOfIceCream);

        supermarket.addProductsToInventory(distributor);

        Customer georgi = new Customer("order123",BigDecimal.valueOf(80));
        georgi.addProductToBasket(supermarket, processingIceCream, BigDecimal.valueOf(10));
        CashRegister cashRegister = new CashRegister("123CR");

        cashRegister.addCustomerToQueue(georgi);
        cashRegister.sellingToCustomer(supermarket,georgi);

        BigDecimal expectedWallet = new BigDecimal(10.00).setScale(2);
        Map<ProductProcessing, BigDecimal> expectedSoldProducts = new HashMap<>();
        expectedSoldProducts.put(processingIceCream,BigDecimal.valueOf(10));

        Assertions.assertEquals(expectedWallet,georgi.getWallet());
        Assertions.assertEquals(expectedSoldProducts,cashRegister.getSoldProducts());
    }

    @Test
    void sellingToCustomer_withException() throws MaxPeriodOfDaysUntilExpirationIsNegativeException, ExpirationDiscountPercentageIsNegativeException, OverchargePercentageIsNegativeException, UnitDeliveryPriceIsNegativeException, TheProductHasExpiredException, InsufficientQuantityException, TheCustomerIsAlreadyInTheQueueException, NotEnoughMoneyToBuyTheProductsException {
        Shop supermarket = new Shop("2+2",BigDecimal.valueOf(40),BigDecimal.valueOf(30),5,BigDecimal.valueOf(70));
        Product iceCream = new Product("123iceCream","Ice Cream", BigDecimal.valueOf(5), ProductCategory.NUTRITION, LocalDate.of(2025,5,24));
        ProductProcessing processingIceCream = new ProductProcessing(iceCream);

        Delivery deliveryOfIceCream = new Delivery();
        BigDecimal quantity = BigDecimal.valueOf(10);
        deliveryOfIceCream.addProductToDeliverToTheShop(iceCream,quantity);
        Distributor distributor = new Distributor("Firm Name","123order", deliveryOfIceCream);

        supermarket.addProductsToInventory(distributor);

        Customer georgi = new Customer("order123",BigDecimal.valueOf(60));
        georgi.addProductToBasket(supermarket, processingIceCream, BigDecimal.valueOf(10));
        CashRegister cashRegister = new CashRegister("123CR");

        cashRegister.addCustomerToQueue(georgi);

        Assertions.assertThrows(NotEnoughMoneyToBuyTheProductsException.class, () ->  cashRegister.sellingToCustomer(supermarket,georgi));
    }

    @Test
    void sellingToCustomers() throws MaxPeriodOfDaysUntilExpirationIsNegativeException, ExpirationDiscountPercentageIsNegativeException, OverchargePercentageIsNegativeException, UnitDeliveryPriceIsNegativeException, TheProductHasExpiredException, InsufficientQuantityException, TheCustomerIsAlreadyInTheQueueException, NotEnoughMoneyToBuyTheProductsException {
        Shop supermarket = new Shop("2+2",BigDecimal.valueOf(40),BigDecimal.valueOf(30),5,BigDecimal.valueOf(70));
        Product iceCream = new Product("123iceCream","Ice Cream", BigDecimal.valueOf(5), ProductCategory.NUTRITION, LocalDate.of(2025,5,24));
        ProductProcessing processingIceCream = new ProductProcessing(iceCream);

        Delivery deliveryOfIceCream = new Delivery();
        BigDecimal quantity = BigDecimal.valueOf(10);
        deliveryOfIceCream.addProductToDeliverToTheShop(iceCream,quantity);
        Distributor distributor = new Distributor("Firm Name","123order", deliveryOfIceCream);

        supermarket.addProductsToInventory(distributor);

        Customer georgi = new Customer("order123",BigDecimal.valueOf(80));
        georgi.addProductToBasket(supermarket, processingIceCream, BigDecimal.valueOf(10));
        CashRegister cashRegister = new CashRegister("123CR");

        cashRegister.addCustomerToQueue(georgi);
        cashRegister.sellingToCustomers(supermarket);

        BigDecimal expectedWallet = new BigDecimal(10.00).setScale(2);
        Map<ProductProcessing, BigDecimal> expectedSoldProducts = new HashMap<>();
        expectedSoldProducts.put(processingIceCream,BigDecimal.valueOf(10));

        Assertions.assertEquals(expectedWallet,georgi.getWallet());
        Assertions.assertEquals(expectedSoldProducts,cashRegister.getSoldProducts());
    }

    @Test
    void sellingToCustomers_withException() throws MaxPeriodOfDaysUntilExpirationIsNegativeException, ExpirationDiscountPercentageIsNegativeException, OverchargePercentageIsNegativeException, UnitDeliveryPriceIsNegativeException, TheProductHasExpiredException, InsufficientQuantityException, TheCustomerIsAlreadyInTheQueueException {
        Shop supermarket = new Shop("2+2",BigDecimal.valueOf(40),BigDecimal.valueOf(30),5,BigDecimal.valueOf(70));
        Product iceCream = new Product("123iceCream","Ice Cream", BigDecimal.valueOf(5), ProductCategory.NUTRITION, LocalDate.of(2025,5,24));
        ProductProcessing processingIceCream = new ProductProcessing(iceCream);

        Delivery deliveryOfIceCream = new Delivery();
        BigDecimal quantity = BigDecimal.valueOf(10);
        deliveryOfIceCream.addProductToDeliverToTheShop(iceCream,quantity);
        Distributor distributor = new Distributor("Firm Name","123order", deliveryOfIceCream);

        supermarket.addProductsToInventory(distributor);

        Customer georgi = new Customer("order123",BigDecimal.valueOf(60));
        georgi.addProductToBasket(supermarket, processingIceCream, BigDecimal.valueOf(10));
        CashRegister cashRegister = new CashRegister("123CR");

        cashRegister.addCustomerToQueue(georgi);

        Assertions.assertThrows(NotEnoughMoneyToBuyTheProductsException.class, () ->  cashRegister.sellingToCustomers(supermarket));

    }
    @Test
    void saveSoldProducts() throws TheCustomerIsAlreadyInTheQueueException, NotEnoughMoneyToBuyTheProductsException, InsufficientQuantityException, TheProductHasExpiredException, MaxPeriodOfDaysUntilExpirationIsNegativeException, ExpirationDiscountPercentageIsNegativeException, OverchargePercentageIsNegativeException, UnitDeliveryPriceIsNegativeException {
        Shop supermarket = new Shop("2+2",BigDecimal.valueOf(40),BigDecimal.valueOf(30),5,BigDecimal.valueOf(70));
        Product iceCream = new Product("123iceCream","Ice Cream", BigDecimal.valueOf(5), ProductCategory.NUTRITION, LocalDate.of(2025,5,24));
        ProductProcessing processingIceCream = new ProductProcessing(iceCream);

        Delivery deliveryOfIceCream = new Delivery();
        BigDecimal quantity = BigDecimal.valueOf(10);
        deliveryOfIceCream.addProductToDeliverToTheShop(iceCream,quantity);
        Distributor distributor = new Distributor("Firm Name","123order", deliveryOfIceCream);

        supermarket.addProductsToInventory(distributor);

        Customer georgi = new Customer("order123",BigDecimal.valueOf(60));
        georgi.addProductToBasket(supermarket, processingIceCream, BigDecimal.valueOf(10));
        CashRegister cashRegister = new CashRegister("123CR");

        cashRegister.addCustomerToQueue(georgi);
        cashRegister.saveSoldProducts(processingIceCream);

        Map<ProductProcessing, BigDecimal> expectedSoldProducts = new HashMap<>();
        expectedSoldProducts.put(processingIceCream,BigDecimal.valueOf(10));

        Assertions.assertEquals(expectedSoldProducts,cashRegister.getSoldProducts());
    }

    @Test
    void addReceiptToSet() {
        Receipt receipt = new Receipt();
        CashRegister cashRegister = new CashRegister("123CR");
        cashRegister.addReceiptToSet(receipt);

        Set<Receipt> expextedSet = new HashSet<>();
        expextedSet.add(receipt);

        Assertions.assertEquals(expextedSet,cashRegister.getReceiptSet());
    }

    @Test
    void turnover() throws MaxPeriodOfDaysUntilExpirationIsNegativeException, ExpirationDiscountPercentageIsNegativeException, OverchargePercentageIsNegativeException, UnitDeliveryPriceIsNegativeException, TheProductHasExpiredException, InsufficientQuantityException, TheCustomerIsAlreadyInTheQueueException, NotEnoughMoneyToBuyTheProductsException {
        Shop supermarket = new Shop("2+2",BigDecimal.valueOf(40),BigDecimal.valueOf(30),5,BigDecimal.valueOf(70));
        Product iceCream = new Product("123iceCream","Ice Cream", BigDecimal.valueOf(5), ProductCategory.NUTRITION, LocalDate.of(2025,5,24));
        ProductProcessing processingIceCream = new ProductProcessing(iceCream);

        Delivery deliveryOfIceCream = new Delivery();
        BigDecimal quantity = BigDecimal.valueOf(10);
        deliveryOfIceCream.addProductToDeliverToTheShop(iceCream,quantity);
        Distributor distributor = new Distributor("Firm Name","123order", deliveryOfIceCream);

        supermarket.addProductsToInventory(distributor);

        Customer georgi = new Customer("order123",BigDecimal.valueOf(80));
        georgi.addProductToBasket(supermarket, processingIceCream, BigDecimal.valueOf(10));
        CashRegister cashRegister = new CashRegister("123CR");

        cashRegister.addCustomerToQueue(georgi);
        cashRegister.sellingToCustomers(supermarket);

        BigDecimal expectedTurnover = new BigDecimal(70.00).setScale(2);

        Assertions.assertEquals(expectedTurnover,cashRegister.turnover());
    }
}