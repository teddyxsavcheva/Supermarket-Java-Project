package org.project.distributor;

import org.project.exceptions.TheProductHasExpiredException;
import org.project.products.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

public class Delivery implements Deliverable{
    private Map<Product, BigDecimal> delivery;

    public Delivery() {
        this.delivery = new TreeMap<>(Product.ComparatorByProductCategory);
    }


    public Map<Product, BigDecimal> getDelivery() {
        return delivery;
    }

    @Override
    public void addProductToDeliverToTheShop(Product product, BigDecimal quantity) throws TheProductHasExpiredException {
        if (product.getExpirationDate().isBefore(LocalDate.now())) {
            throw new TheProductHasExpiredException("The product has expired so it can not be delivered to the shop!",product);
        }
        if (delivery.containsKey(product)) {
            delivery.put(product, delivery.get(product).add(quantity));
        } else {
            delivery.putIfAbsent(product,quantity);
        }
    }

    @Override
    public void addProductsToDeliverToTheShop(Map<Product,BigDecimal> products) throws TheProductHasExpiredException {
        for(Product key: products.keySet()){
            if(key.getExpirationDate().isBefore(LocalDate.now())) {
                throw new TheProductHasExpiredException("Some or all products have expired so they can not be delivered to the shop!",products);
            }
        }
        products.forEach((product, quantity) ->
                delivery.merge(product, quantity, (prev, curr) -> prev.add(curr)));
    }

    @Override
    public String toString() {
        return "Delivery: " + "delivery = " + delivery;
    }
}
