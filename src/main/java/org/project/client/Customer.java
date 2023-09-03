package org.project.client;
import org.project.exceptions.InsufficientQuantityException;
import org.project.exceptions.TheProductHasExpiredException;
import org.project.products.ProductProcessing;
import org.project.shop.Shop;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class Customer implements ShoppingCartService{
    private String orderId;
    private BigDecimal wallet;
    private Map<ProductProcessing,BigDecimal> shoppingCart;

    public Customer(String orderId, BigDecimal wallet) {
        this.orderId = orderId;
        this.wallet = wallet;
        this.shoppingCart = new TreeMap<>(Comparator.comparing(ProductProcessing::getProduct));
    }

    public void addProductToBasket(Shop shop, ProductProcessing product, BigDecimal quantity) throws TheProductHasExpiredException, InsufficientQuantityException {
        if(product.isProductExpired()){
            throw new TheProductHasExpiredException("The product you are trying to buy has already expired!",product.getProduct());
        }
        if (shop.isProductAvailable(product.getProduct(),quantity)) {
            BigDecimal currentQuantity = shoppingCart.getOrDefault(product,BigDecimal.ZERO);
            shoppingCart.put(product,currentQuantity.add(quantity));
        }
        else{
            BigDecimal availableQuantity = shop.getQuantityOfProduct(product.getProduct());
            BigDecimal insufficientQuantity = quantity.subtract(availableQuantity);
            throw new InsufficientQuantityException ("Not enough" + product + "in stock! Available quantity is: " + availableQuantity + ", and the insufficient quantity is: " + insufficientQuantity);
        }
    }

    public void addProductsToBasket(Shop shop, Map<ProductProcessing,BigDecimal> products) throws TheProductHasExpiredException, InsufficientQuantityException {
        for (ProductProcessing product : products.keySet()){
            if (product.isProductExpired()){
                throw new TheProductHasExpiredException("The product you are trying to buy has already expired!",product.getProduct());
            }
           if (shop.isProductAvailable(product.getProduct(),products.get(product))){
               BigDecimal currentQuantity = shoppingCart.getOrDefault(product,BigDecimal.ZERO);
               shoppingCart.put(product,currentQuantity.add(products.get(product)));
           }
           else{
               BigDecimal availableQuantity = shop.getQuantityOfProduct(product.getProduct());
               BigDecimal insufficientQuantity = products.get(product).subtract(availableQuantity);
               throw new InsufficientQuantityException ("Not enough" + product + "in stock! Available quantity is: " + availableQuantity + ", and the insufficient quantity is: " + insufficientQuantity);
           }
        }
    }

    public BigDecimal shoppingCartPrice(Shop shop) throws TheProductHasExpiredException {
        BigDecimal price = BigDecimal.valueOf(0);
        for (ProductProcessing product: shoppingCart.keySet()) {
            BigDecimal quantity = shoppingCart.get(product);
            BigDecimal productPrice = product.sellingPrice(shop).multiply(quantity);
            price = price.add(productPrice);
        }
        return price;
    }
    public void clearShoppingCart(){
        shoppingCart.clear();
    }

    public String getOrderId() {
        return orderId;
    }

    public BigDecimal getWallet() {
        return wallet;
    }

    public Map<ProductProcessing, BigDecimal> getShoppingCart() {
        return shoppingCart;
    }

    public void setWallet(BigDecimal wallet) {
        this.wallet = wallet;
    }

    @Override
    public String toString() {
        return "\n\nCustomer{" +
                "orderId='" + orderId + '\'' +
                ", wallet=" + wallet +
                ", Shopping cart: " + shoppingCart;
    }
}
