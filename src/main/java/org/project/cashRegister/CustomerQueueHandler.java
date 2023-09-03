package org.project.cashRegister;
import org.project.client.Customer;
import org.project.exceptions.TheCustomerIsAlreadyInTheQueueException;
import java.util.Queue;

public interface CustomerQueueHandler {
    boolean addCustomerToQueue(Customer customer) throws TheCustomerIsAlreadyInTheQueueException;
    boolean addCustomersToQueue(Queue<Customer> customers) throws TheCustomerIsAlreadyInTheQueueException;
}
