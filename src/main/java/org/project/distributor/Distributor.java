package org.project.distributor;
import org.project.exceptions.TheProductHasExpiredException;
import org.project.products.Product;
import java.time.LocalDate;
public class Distributor{
    private String nameOfDistributor;
    private String idOfDelivery;
    private Delivery delivery;

    public Distributor(String nameOfDistributor, String idOfDelivery, Delivery delivery) {
        this.nameOfDistributor = nameOfDistributor;
        this.idOfDelivery = idOfDelivery;
        this.delivery = delivery;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public boolean isNoExpiredProducts(Distributor delivery) throws TheProductHasExpiredException {
        for(Product key: delivery.getDelivery().getDelivery().keySet()){
            if(key.getExpirationDate().isBefore(LocalDate.now())) {
                throw new TheProductHasExpiredException("Some or all products have expired so they can not be delivered to the shop!",delivery);
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "Distributor{" +
                "nameOfDistributor='" + nameOfDistributor + '\'' +
                ", idOfDelivery='" + idOfDelivery + '\'' +
                ", delivery=" + delivery +
                '}';
    }
}
