package org.project.cashRegister;

import org.project.cashier.Cashier;
import org.project.client.Customer;
import org.project.exceptions.InsufficientQuantityException;
import org.project.exceptions.NotEnoughMoneyToBuyTheProductsException;
import org.project.exceptions.TheCustomerIsAlreadyInTheQueueException;
import org.project.exceptions.TheProductHasExpiredException;
import org.project.products.ProductProcessing;
import org.project.receipt.Receipt;
import org.project.shop.Shop;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class CashRegister implements CheckoutProcess, CustomerQueueHandler, ReceiptRenderer, CheckoutService{
    private final String idNumber;
    private Cashier cashier;
    private Queue<Customer> customerQueue;
    private Map<ProductProcessing, BigDecimal> soldProducts;
    private Set<Receipt> receiptSet;
    private long totalReceipts = 0;

    public CashRegister(String idNumber) {
        this.idNumber = idNumber;
        this.cashier = null;
        this.customerQueue = new LinkedList<>();
        this.soldProducts = new TreeMap<>(Comparator.comparing(ProductProcessing::getProduct));
        this.receiptSet = new HashSet<>();
    }

    public void setCashier(Cashier cashier) {
        if(this.cashier==null){
            this.cashier = cashier;
        }
    }

    @Override
    public boolean addCustomerToQueue(Customer customer) throws TheCustomerIsAlreadyInTheQueueException {
        if(customerQueue.contains(customer)){
            throw new TheCustomerIsAlreadyInTheQueueException("The customer is already in the queue waiting to be served!",customer);
        }
        customerQueue.offer(customer);
        return true;
    }

    @Override
    public boolean addCustomersToQueue(Queue<Customer> customers) throws TheCustomerIsAlreadyInTheQueueException {
        for (Customer customer : customers) {
            if(customerQueue.contains(customer)){
                throw new TheCustomerIsAlreadyInTheQueueException("The customer is already in the queue waiting to be served!",customer);
            }
            else{
                customerQueue.offer(customer);
            }
        }
        return true;
    }

    @Override
    public boolean isMoneyInWalletEnough(Shop shop, Customer customer) throws TheProductHasExpiredException {
        BigDecimal basketPrice = customer.shoppingCartPrice(shop);
        BigDecimal paidAmount = customer.getWallet();
        if(paidAmount.compareTo(basketPrice) >= 0){
            return true;
        }
        else return false;
    }

    @Override
    public void sellingProcessingForCustomer(Shop shop, Customer customer) throws InsufficientQuantityException {
        for (ProductProcessing product : customer.getShoppingCart().keySet()) {
            /* Checks once again if the product is available from the inventory in the shop */
            if (shop.isProductAvailable(product.getProduct(), customer.getShoppingCart().get(product))) {
                /* Removes the product from the inventory in the shop */
                shop.removeFromInventory(product.getProduct(),customer.getShoppingCart().get(product));
                /* Adds the product to the map of sold products */
                saveSoldProducts(product);
            }
        }
    }

    @Override
    public void sellingProcessingForQueue(Shop shop) throws InsufficientQuantityException {
        for (ProductProcessing product : customerQueue.peek().getShoppingCart().keySet()) {
            /* Checks once again if the product is available from the inventory in the shop */
            if (shop.isProductAvailable(product.getProduct(), customerQueue.peek().getShoppingCart().get(product))) {
                /* Removes the product from the inventory in the shop */
                shop.removeFromInventory(product.getProduct(), customerQueue.peek().getShoppingCart().get(product));
                /* Adds the product to the map of sold products - to make it into a method */
                saveSoldProducts(product);
            }
        }
    }

    @Override
    public BigDecimal calculateChange(Shop shop, Customer customer) throws TheProductHasExpiredException {
        return customer.getWallet().subtract(customer.shoppingCartPrice(shop));
    }

    @Override
    public void sellingToCustomer(Shop shop, Customer customer)
            throws TheProductHasExpiredException, InsufficientQuantityException, NotEnoughMoneyToBuyTheProductsException {
        if (isMoneyInWalletEnough(shop,customer)) {
            sellingProcessingForCustomer(shop,customer);
        }
        else throw new NotEnoughMoneyToBuyTheProductsException("The customer does not have enough money to purchase the products!",customer);
        /* Creating receipt */
        createReceipt(shop,customer.shoppingCartPrice(shop));
        /* The change of the customer */
        customer.setWallet(calculateChange(shop,customer));
        customer.clearShoppingCart();
        customerQueue.remove();
    }

    @Override
    public void sellingToCustomers(Shop shop)
            throws TheProductHasExpiredException, InsufficientQuantityException, NotEnoughMoneyToBuyTheProductsException {
        while (!customerQueue.isEmpty()) {
            if (isMoneyInWalletEnough(shop,customerQueue.peek())) {
                sellingProcessingForQueue(shop);
            } else throw new NotEnoughMoneyToBuyTheProductsException("The customer does not have enough money to purchase the products!", customerQueue.peek());
            /* Creating receipt */
            createReceipt(shop,customerQueue.peek().shoppingCartPrice(shop));
            /* The change for the customer */
            customerQueue.peek().setWallet(calculateChange(shop,customerQueue.peek()));
            customerQueue.peek().clearShoppingCart();
            customerQueue.remove();
        }
    }

    @Override
    public String addOrderIdToReceipt(Receipt receipt){
        StringBuilder sb = new StringBuilder();
        sb.append(receipt.getDateAndIdSection() + "\n\nOrder ID: " + customerQueue.peek().getOrderId() + receipt.getRestOfPurchaseInfo());
        return sb.toString();
    }
    @Override
    public void createReceipt(Shop shop, BigDecimal basketPrice){
        Receipt receipt = new Receipt(shop,cashier,customerQueue.peek().getShoppingCart(),basketPrice);
        String receiptFileName = "files/receipt_" + receipt.getId() + ".txt";
        saveReceiptToFile(receiptFileName,addOrderIdToReceipt(receipt));
        addReceiptToSet(receipt);
        ++totalReceipts;
    }
    @Override
    public String readFromFile(String fileName) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    @Override
    public void saveReceiptToFile(String fileName, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveSoldProducts(ProductProcessing product){
        BigDecimal currentQuantity = soldProducts.getOrDefault(product, BigDecimal.ZERO);
        soldProducts.put(product, currentQuantity.add(customerQueue.peek().getShoppingCart().get(product)));
    }

    @Override
    public void addReceiptToSet(Receipt receipt){
        receiptSet.add(receipt);
    }

    public String idOfReceipts(){
        StringBuilder sb = new StringBuilder();
        int count = 0;
        int size = receiptSet.size();
        for (Receipt receipt : receiptSet) {
            sb.append("ID: ").append(receipt.getId());
            count++;
            if (count != size) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    @Override
    public BigDecimal turnover(){
        BigDecimal turnover = BigDecimal.ZERO;
        for (Receipt receipt: receiptSet) {
            turnover = turnover.add(receipt.getOrderPrice());
        }
        return turnover.setScale(2);
    }

    public Queue<Customer> getCustomerQueue() {
        return customerQueue;
    }

    public Map<ProductProcessing, BigDecimal> getSoldProducts() {
        return soldProducts;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public Cashier getCashier() {
        return cashier;
    }

    public Set<Receipt> getReceiptSet() {
        return receiptSet;
    }

    public long getTotalReceipts() {
        return totalReceipts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CashRegister that = (CashRegister) o;
        return Objects.equals(idNumber, that.idNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idNumber);
    }

    @Override
    public String toString() {
        return "CashRegister{" +
                "idNumber='" + idNumber + '\'' +
                ", cashier=" + (cashier != null ? cashier.getCashierName() : "none") +
                ", customerQueue=" + customerQueue +
                ", receipts = [" + idOfReceipts() +
                "], total receipts = " + totalReceipts +
                ", turnover = " + turnover() +
                '}';
    }
}
