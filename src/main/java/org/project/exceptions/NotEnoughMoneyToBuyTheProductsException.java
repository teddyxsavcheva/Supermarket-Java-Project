package org.project.exceptions;

import org.project.client.Customer;

public class NotEnoughMoneyToBuyTheProductsException extends Exception {
    private Customer customer;

    public NotEnoughMoneyToBuyTheProductsException(String s, Customer customer) {
        super(s);
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "NotEnoughMoneyToBuyTheProductsException{" +
                "customer=" + customer +
                "} " + super.toString();
    }
}
