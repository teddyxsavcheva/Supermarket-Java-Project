package org.project.products;
import org.project.exceptions.TheProductHasExpiredException;
import org.project.shop.Shop;
import java.math.BigDecimal;

public interface Sellable {
    BigDecimal sellingPrice(Shop shop) throws TheProductHasExpiredException;
}
