package org.project.shop;
import org.project.cashRegister.CashRegister;
import org.project.cashier.Cashier;
import org.project.distributor.Distributor;
import org.project.exceptions.*;
import org.project.products.Product;
import org.project.products.ProductCategory;
import java.math.BigDecimal;
import java.util.*;

/* Data - service */
public class Shop implements CompanyService, InventoryService{
    private String name;
    private Map<ProductCategory,BigDecimal> categoryOverchargeMap;
    private long maxDaysUntilExpiration;
    private BigDecimal expirationDiscount;
    private Map<Product,BigDecimal> inventory;
    private Map<Product,BigDecimal> deliveredProducts;
    private Set<CashRegister> cashRegisters;
    private Set<Cashier> cashiers;

    public Shop(String name, BigDecimal overchargeNutrition,BigDecimal overchargeNonNutrition,
                long maxDaysUntilExpiration, BigDecimal expirationDiscount) throws OverchargePercentageIsNegativeException, MaxPeriodOfDaysUntilExpirationIsNegativeException, ExpirationDiscountPercentageIsNegativeException {
        this.name = name;
        this.categoryOverchargeMap = new EnumMap<ProductCategory, BigDecimal>(ProductCategory.class);
        setCategoriesOverchargesMap(overchargeNutrition,overchargeNonNutrition);
        setMaxDaysUntilExpiration(maxDaysUntilExpiration,0);
        setExpirationDiscount(expirationDiscount);
        this.inventory = new TreeMap<>(Product.ComparatorByProductCategory);
        this.deliveredProducts = new TreeMap<>(Product.ComparatorByProductCategory);
        /* Calculating the delivery expenses */
        this.cashiers = new HashSet<>();
        this.cashRegisters = new HashSet<>();
    }

    public void setMaxDaysUntilExpiration(long maxDaysUntilExpiration, int upperBound) throws MaxPeriodOfDaysUntilExpirationIsNegativeException {
        if(maxDaysUntilExpiration < upperBound){
            throw new MaxPeriodOfDaysUntilExpirationIsNegativeException("The max period of days until expiration date must be of a non negative value!",maxDaysUntilExpiration);
        }
        this.maxDaysUntilExpiration = maxDaysUntilExpiration;
    }

    public void setExpirationDiscount(BigDecimal expirationDiscount) throws ExpirationDiscountPercentageIsNegativeException {
        if(expirationDiscount.compareTo(BigDecimal.valueOf(0))==-1){
            throw new ExpirationDiscountPercentageIsNegativeException("The expiration discount percentage must be of a non negative value!",expirationDiscount);
        }
        this.expirationDiscount = expirationDiscount;
    }

    public void setCategoryOvercharge(ProductCategory productCategory, BigDecimal overchargePercentage) throws OverchargePercentageIsNegativeException {
        if(overchargePercentage.compareTo(BigDecimal.valueOf(0)) == -1){
            throw new OverchargePercentageIsNegativeException
                    ("The overcharge percentage must be of a non negative value! You have entered incorrect percentage for product category: ", productCategory);
        }
        categoryOverchargeMap.put(productCategory,overchargePercentage);
    }

    public void setCategoriesOverchargesMap(BigDecimal overchargeNutrition, BigDecimal overchargeNonNutrition) throws OverchargePercentageIsNegativeException {
        setCategoryOvercharge(ProductCategory.NUTRITION,overchargeNutrition);
        setCategoryOvercharge(ProductCategory.NON_NUTRITION,overchargeNonNutrition);
    }

    public String getName() {
        return name;
    }

    public Map<ProductCategory, BigDecimal> getCategoryOverchargeMap() {
        return categoryOverchargeMap;
    }

    public long getMaxDaysUntilExpiration() {
        return maxDaysUntilExpiration;
    }

    public BigDecimal getExpirationDiscount() {
        return expirationDiscount;
    }

    public Map<Product, BigDecimal> getInventory() {
        return inventory;
    }

    public Map<Product, BigDecimal> getDeliveredProducts() {
        return deliveredProducts;
    }

    public Set<CashRegister> getCashRegisters() {
        return cashRegisters;
    }

    public Set<Cashier> getCashiers() {
        return cashiers;
    }

    @Override
    public void addCashier(Cashier cashier){
        cashiers.add(cashier);
    }

    @Override
    public void addCashRegister(CashRegister cashRegister){
        cashRegisters.add(cashRegister);
    }

    @Override
    public BigDecimal totalDeliveryExpenses(){
        BigDecimal deliveryExpenses = BigDecimal.valueOf(0);
        for (Product key: deliveredProducts.keySet()) {
            BigDecimal quantity = deliveredProducts.get(key);
            BigDecimal productDeliveryExpenses = key.getUnitDeliveryPrice().multiply(quantity);
            deliveryExpenses = deliveryExpenses.add(productDeliveryExpenses);
        }
        return deliveryExpenses.setScale(2);
    }

    @Override
    public BigDecimal totalSalaryExpenses(){
        BigDecimal salaryExpenses = BigDecimal.ZERO;
        for (Cashier cashier: cashiers) {
            salaryExpenses = salaryExpenses.add(cashier.getMonthlySalary());
        }
        return salaryExpenses.setScale(2);
    }

    @Override
    public BigDecimal amountSpent(){
        BigDecimal amountSpent = totalSalaryExpenses().add(totalDeliveryExpenses());
        return amountSpent.setScale(2);
    }

    @Override
    public BigDecimal amountEarned(){
        BigDecimal amountEarned = BigDecimal.ZERO;
        for (CashRegister register: cashRegisters) {
            amountEarned = amountEarned.add(register.turnover());
        }
        return amountEarned.setScale(2);
    }

    @Override
    public BigDecimal profit(){
        BigDecimal profit = amountEarned().subtract(amountSpent());
        return profit.setScale(2);
    }

    @Override
    public void addProductsToInventory(Distributor delivery) throws TheProductHasExpiredException {
        if(delivery.isNoExpiredProducts(delivery)){
            delivery.getDelivery().getDelivery().forEach((product, quantity) -> inventory.merge(product,quantity,(prev,curr) -> prev.add(curr)));
        }
        inventory.forEach((key, value) ->
                deliveredProducts.merge(key, value, (v1, v2) -> v1.add(v2)));
        totalDeliveryExpenses();
    }

    @Override
    public BigDecimal getQuantityOfProduct(Product product){
        BigDecimal quantity = BigDecimal.valueOf(0);
        for (Product key: inventory.keySet()) {
            if(key == product){
                quantity = quantity.add(inventory.get(key));
            }
        }
        return quantity;
    }

    @Override
    public boolean isProductAvailable(Product product, BigDecimal quantity) throws InsufficientQuantityException {
        BigDecimal availableQuantity = getQuantityOfProduct(product);
        if(availableQuantity.compareTo(quantity)==-1){
            BigDecimal insufficientQuantity = quantity.subtract(availableQuantity);
            throw new InsufficientQuantityException
                    ("Not enough" + product + "in stock! Available quantity is: " + availableQuantity + ", and the insufficient quantity is: " + insufficientQuantity);
        }
        return  true;
    }

    @Override
    public void removeFromInventory(Product product, BigDecimal quantity) throws InsufficientQuantityException {
        if(inventory.get(product).compareTo(quantity)>=0)
        {
            inventory.computeIfPresent(product,(prod,quan) -> quan.subtract(quantity));
        }
        else {
            throw new ArithmeticException();
        }
    }

    @Override
    public String toString() {
        return "Shop{" +
                "name='" + name + '\'' +
                ", categoryOverchargeMap=" + categoryOverchargeMap +
                ", maxDaysUntilExpiration=" + maxDaysUntilExpiration +
                ", expirationDiscount=" + expirationDiscount +
                ", inventory=" + inventory +
                '}';
    }
}