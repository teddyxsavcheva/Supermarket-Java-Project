package org.project.exceptions;

import org.project.distributor.Distributor;
import org.project.products.Product;

import java.math.BigDecimal;
import java.util.Map;

public class TheProductHasExpiredException extends Throwable {
    private Distributor delivery;
    private Map<Product, BigDecimal> products;
    private Product product;

    public TheProductHasExpiredException(String s, Product product) {
        super(s);
        this.product = product;
    }

    public TheProductHasExpiredException(String s, Map<Product, BigDecimal> products) {
        super(s);
        this.products = products;
    }

    public TheProductHasExpiredException(String s, Distributor delivery) {
        super(s);
        this.delivery = delivery;
    }

    @Override
    public String toString() {
        return "TheProductHasExpiredException{" +
                "product=" + product +
                "} " + super.toString();
    }
}
