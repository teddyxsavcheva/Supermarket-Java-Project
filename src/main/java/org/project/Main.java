package org.project;

import org.project.cashRegister.CashRegister;
import org.project.cashier.Cashier;
import org.project.client.Customer;
import org.project.distributor.Delivery;
import org.project.distributor.Distributor;
import org.project.exceptions.*;
import org.project.products.Product;
import org.project.products.ProductCategory;
import org.project.products.ProductProcessing;
import org.project.shop.Shop;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
public class Main {
    public static void main(String[] args) {
        /* Creating a Shop */
        Shop supermarket;
        try {
            supermarket = new Shop("2+2",BigDecimal.valueOf(40),BigDecimal.valueOf(30),5,BigDecimal.valueOf(70));
        } catch (OverchargePercentageIsNegativeException e) {
            throw new RuntimeException(e);
        } catch (MaxPeriodOfDaysUntilExpirationIsNegativeException e) {
            throw new RuntimeException(e);
        } catch (ExpirationDiscountPercentageIsNegativeException e) {
            throw new RuntimeException(e);
        }

        /* Creating product cake */
        Product cake;
        try {
            cake = new Product("12345C", "Cake", BigDecimal.valueOf(5),ProductCategory.NUTRITION,LocalDate.of(2025,5,17));
        } catch (UnitDeliveryPriceIsNegativeException e) {
            throw new RuntimeException(e);
        }

        /* Creating product flowers */
        Product flowers;
        try {
            flowers = new Product("12345F","Flowers", BigDecimal.valueOf(5),ProductCategory.NON_NUTRITION,LocalDate.of(2025,2,2));
        } catch (UnitDeliveryPriceIsNegativeException e) {
            throw new RuntimeException(e);
        }

        /* Creating delivery so that we can deliver cakes and flowers to the supermarket */
        Delivery delivery = new Delivery();
        try {
            /* We are adding 3 flowers to the delivery */
            delivery.addProductToDeliverToTheShop(flowers,BigDecimal.valueOf(3));
        } catch (TheProductHasExpiredException e) {
            throw new RuntimeException(e);
        }

        /* We are adding 5 flowers to the delivery */
        try {
            delivery.addProductToDeliverToTheShop(flowers,BigDecimal.valueOf(5));
        } catch (TheProductHasExpiredException e) {
            throw new RuntimeException(e);
        }

        /* We are adding 6 cakes to the delivery */
        try {
            delivery.addProductToDeliverToTheShop(cake,BigDecimal.valueOf(6));
        } catch (TheProductHasExpiredException e) {
            throw new RuntimeException(e);
        }

        /* We are creating a map of products that we can add to the delivery */
        Map<Product,BigDecimal> products = new TreeMap<>();
        /* Adding two cakes to the map */
        products.put(cake,BigDecimal.valueOf(2));
        /* Adding two cakes to the map again */
        products.merge(cake,BigDecimal.valueOf(2),BigDecimal::add);
        /* Adding one flower to the map */
        products.put(flowers,BigDecimal.valueOf(1));
        /* Adding one more flower to the map */
        products.merge(flowers,BigDecimal.valueOf(1),BigDecimal::add);

        /* Let's add the products to the delivery as well */
        try {
            delivery.addProductsToDeliverToTheShop(products);
        } catch (TheProductHasExpiredException e) {
            throw new RuntimeException(e);
        }
        /* Now we have a total of 10 flowers and 10 cakes. Let's add them to the
        * supermarket's inventory */

        /* Creating distributor */
        Distributor distributor = new Distributor("FirmName","Order12345", delivery);
        /* Creating inventory */

        /* Making the delivery */
        try {
            supermarket.addProductsToInventory(distributor);
        } catch (TheProductHasExpiredException e) {
            throw new RuntimeException(e);
        }

        /* Now the shop with its inventory looks like this: */
        System.out.println("---------------------------------------------------------");
        System.out.println("The shop with it's inventory looks like this: \n");
        System.out.println(supermarket);

        System.out.println("\n---------------------------------------------------------");
        System.out.println("The pairs of cashiers and cash registers: \n");
        Cashier cashierG = new Cashier("Georgi","123G",BigDecimal.valueOf(800));
        Cashier cashierT = new Cashier("Teddy","234T",BigDecimal.valueOf(800));
        Cashier cashierB = new Cashier("Boki","345B",BigDecimal.valueOf(800));

        /* Hiring the cashiers in the supermarket */
        supermarket.addCashier(cashierG);
        supermarket.addCashier(cashierT);
        supermarket.addCashier(cashierB);

        CashRegister cashRegister1 = new CashRegister("1C");
        CashRegister cashRegister2 = new CashRegister("2C");
        CashRegister cashRegister3 = new CashRegister("3C");

        /* Adding the cash registers to the supermarket */
        supermarket.addCashRegister(cashRegister1);
        supermarket.addCashRegister(cashRegister2);
        supermarket.addCashRegister(cashRegister3);

        cashierG.setCashRegister(cashRegister1);
        cashierT.setCashRegister(cashRegister2);
        cashierB.setCashRegister(cashRegister3);

        cashRegister1.setCashier(cashierG);
        cashRegister2.setCashier(cashierT);
        cashRegister3.setCashier(cashierB);

        System.out.println(cashierG);
        System.out.println(cashierT);
        System.out.println(cashierB);
        System.out.println();
        System.out.println(cashRegister1);
        System.out.println(cashRegister2);
        System.out.println(cashRegister3);

        /* Creating a customer */
        Customer customer = new Customer("OrderCustomer123",BigDecimal.valueOf(80));
        try {
            //Adding 5 cakes to the basket of the customer
            customer.addProductToBasket(supermarket,new ProductProcessing(cake),BigDecimal.valueOf(2));
        } catch (TheProductHasExpiredException e) {
            throw new RuntimeException(e);
        } catch (InsufficientQuantityException e) {
            throw new RuntimeException(e);
        }

        System.out.println("\n--------------------------------------------");
        System.out.println("The customer and their shopping cart: " + customer.getShoppingCart() +", " + customer.getOrderId());
        ProductProcessing product = new ProductProcessing(cake);
        /* The selling price method */

        System.out.println("\n--------------------------------------------\nThe selling price of the cake: \n");

        try {
            System.out.println(product.sellingPrice(supermarket));
        } catch (TheProductHasExpiredException e) {
            throw new RuntimeException(e);
        }

        try {
             /*Adding the customer to the queue*/
            cashRegister1.addCustomerToQueue(customer);
        } catch (TheCustomerIsAlreadyInTheQueueException e) {
            throw new RuntimeException(e);
        }
         /*Creating another customer*/
        Customer customer2 = new Customer("OrderCustomer2234",BigDecimal.valueOf(40));
        try {
             /*Adding 5 flowers to the basket of the customer*/
            customer2.addProductToBasket(supermarket,new ProductProcessing(flowers),BigDecimal.valueOf(5));
        } catch (TheProductHasExpiredException e) {
            throw new RuntimeException(e);
        } catch (InsufficientQuantityException e) {
            throw new RuntimeException(e);
        }

        try {
            /* Adding another customer to the queue */
            cashRegister1.addCustomerToQueue(customer2);
        } catch (TheCustomerIsAlreadyInTheQueueException e) {
            throw new RuntimeException(e);
        }

        System.out.println("-------------------------------------------------");
        System.out.println("And the two customers look like this: " + customer + customer2);

        /* Selling the products to the first customer */
        try {
            cashRegister1.sellingToCustomer(supermarket,customer);
        } catch (TheProductHasExpiredException e) {
            throw new RuntimeException(e);
        } catch (InsufficientQuantityException e) {
            throw new RuntimeException(e);
        } catch (NotEnoughMoneyToBuyTheProductsException e) {
            throw new RuntimeException(e);
        }
        /* Selling the products to the second customer */
        try {
            cashRegister1.sellingToCustomer(supermarket, customer2);
        } catch (TheProductHasExpiredException e) {
            throw new RuntimeException(e);
        } catch (InsufficientQuantityException e) {
            throw new RuntimeException(e);
        } catch (NotEnoughMoneyToBuyTheProductsException e) {
            throw new RuntimeException(e);
        }
        System.out.println("------------------------------------------------");
        System.out.println("After selling the products, the shop looks like: \n\n" + supermarket);

        System.out.println("------------------------------------------------");
        System.out.println("Now the sold products in cash register look like: \n\n" + cashRegister1.getSoldProducts());

        System.out.println("------------------------------------------------");
        /* Now let's add the clients to a new queue
        * Creating the first client */
        Customer strahil = new Customer("Order123Strahil", BigDecimal.valueOf(40));
        Map<ProductProcessing,BigDecimal> order = new TreeMap<>(Comparator.comparing(ProductProcessing::getProduct));
        /* Creating a map with products */
        order.put(new ProductProcessing(cake),BigDecimal.valueOf(2));
        order.put(new ProductProcessing(flowers),BigDecimal.valueOf(2));
        try {
            /* Adding the products to the client's basket */
            strahil.addProductsToBasket(supermarket,order);
        } catch (TheProductHasExpiredException e) {
            throw new RuntimeException(e);
        } catch (InsufficientQuantityException e) {
            throw new RuntimeException(e);
        }

        /* Creating a second customer and adding the same products to their basket*/
        Customer vera = new Customer("Order234Vera", BigDecimal.valueOf(40));
        try {
            vera.addProductsToBasket(supermarket,order);
        } catch (TheProductHasExpiredException e) {
            throw new RuntimeException(e);
        } catch (InsufficientQuantityException e) {
            throw new RuntimeException(e);
        }
        /* Putting the two customers in a queue */
        Queue<Customer> customerQueue = new LinkedList<>();
        customerQueue.add(strahil);
        customerQueue.add(vera);
        /* Assigning the queue to the supermarket's queue in cashRegister1*/
        try {
            cashRegister1.addCustomersToQueue(customerQueue);
        } catch (TheCustomerIsAlreadyInTheQueueException e) {
            throw new RuntimeException(e);
        }
        System.out.println("The basket price of strahil is: \n");
        try {
            System.out.println(strahil.shoppingCartPrice(supermarket));
        } catch (TheProductHasExpiredException e) {
            throw new RuntimeException(e);
        }
        System.out.println("------------------------------------------------");
        /* Selling the products to all the customers in the queue */
        try {
            cashRegister1.sellingToCustomers(supermarket);
        } catch (TheProductHasExpiredException e) {
            throw new RuntimeException(e);
        } catch (InsufficientQuantityException e) {
            throw new RuntimeException(e);
        } catch (NotEnoughMoneyToBuyTheProductsException e) {
            throw new RuntimeException(e);
        }
        System.out.println("After selling products to two more customers the sold products in cash register 1 look like: \n\n" + cashRegister1.getSoldProducts());
        System.out.println("------------------------------------------------");
        System.out.println("And the queue in cash register 1 looks like: \n\n" + cashRegister1.getCustomerQueue());
        System.out.println("------------------------------------------------");
        System.out.println("The inventory in the supermarket looks like: \n\n" + supermarket.getInventory());
        System.out.println("------------------------------------------------");
        System.out.print("And the customers with empty shopping carts look like:" + customer);
        System.out.print(customer2);
        System.out.print(strahil);
        System.out.print(vera);
        System.out.println("\n------------------------------------------------");
        System.out.println("Cash Register 1 looks like: ");
        System.out.println(cashRegister1);
        System.out.println("\n------------------------------------------------");
        System.out.println("Cash Register 2 looks like: ");
        System.out.println(cashRegister2);
        System.out.println("\n------------------------------------------------");
        System.out.println("Supermarket delivery expenses: ");
        System.out.println(supermarket.totalDeliveryExpenses());
        System.out.println("\n------------------------------------------------");
        System.out.println("Supermarket salary expenses: ");
        System.out.println(supermarket.totalSalaryExpenses());
        System.out.println("\n------------------------------------------------");
        System.out.println("Supermarket amount spent: ");
        System.out.println(supermarket.amountSpent());
        System.out.println("\n------------------------------------------------");
        System.out.println("Supermarket amount earned: ");
        System.out.println(supermarket.amountEarned());
        System.out.println("\n------------------------------------------------");
        System.out.println("Supermarket profit: ");
        System.out.println(supermarket.profit());

        /* To read one of the receipts*/
        System.out.println("\n------------------------------------------------");
        System.out.println("Let's try to read one of the receipts:\n");
        System.out.println(cashRegister1.readFromFile("files/receipt_1.txt"));
    }
}