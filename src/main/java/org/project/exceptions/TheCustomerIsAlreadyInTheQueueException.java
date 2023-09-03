package org.project.exceptions;

import org.project.client.Customer;

public class TheCustomerIsAlreadyInTheQueueException extends Throwable {
    private Customer customer;

    public TheCustomerIsAlreadyInTheQueueException(String s, Customer customer) {
        super(s);
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "TheCustomerIsAlreadyInTheQueueException{" +
                "customer=" + customer +
                "} " + super.toString();
    }
}
