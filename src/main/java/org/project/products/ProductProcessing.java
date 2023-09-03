package org.project.products;
import org.project.exceptions.TheProductHasExpiredException;
import org.project.shop.Shop;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ProductProcessing implements Sellable{
    private Product product;
    public ProductProcessing(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public boolean isProductNutritious(){
        return (product.getProductCategory()==ProductCategory.NUTRITION);
    }

    public BigDecimal overchargeValue(Shop shop) {
        if (isProductNutritious()) {
            return shop.getCategoryOverchargeMap().get(ProductCategory.NUTRITION).divide(BigDecimal.valueOf(100).setScale(2));
        }
        else return shop.getCategoryOverchargeMap().get(ProductCategory.NON_NUTRITION).divide(BigDecimal.valueOf(100).setScale(2));
    }

    public boolean isThereExpirationDateDiscount(Shop shop){
        if(ChronoUnit.DAYS.between(LocalDate.now(),product.getExpirationDate()) <= shop.getMaxDaysUntilExpiration()){
            return true;
        }
        else{
            return false;
        }
    }

    public BigDecimal expirationDiscountValue(Shop shop){
        if(isThereExpirationDateDiscount(shop)){
            return shop.getExpirationDiscount().divide(BigDecimal.valueOf(100).setScale(2));
        }
        else{
            return BigDecimal.valueOf(0).setScale(2);
        }
    }

    public boolean isProductExpired(){
        if(ChronoUnit.DAYS.between(LocalDate.now(),product.getExpirationDate())<=0){
            return true;
        }
        else {
            return false;
        }
    }
    @Override
    public BigDecimal sellingPrice(Shop shop) throws TheProductHasExpiredException {
        if(isProductExpired()){
            throw new TheProductHasExpiredException("The selling price can not be estimated, because the product has already expired and can not be sold!", product);
        }
        else{
            BigDecimal overchargeValue = overchargeValue(shop); // overchargePercentage / 100
            BigDecimal basePrice = product.getUnitDeliveryPrice(); // the base price
            BigDecimal markup = basePrice.multiply(overchargeValue); // the calculated overcharge
            BigDecimal sellingPrice = basePrice.add(markup); // the selling price of the product

            BigDecimal expirationDiscountValue = expirationDiscountValue(shop);
            BigDecimal discount = BigDecimal.ZERO;
            if (expirationDiscountValue.compareTo(discount)==1) { // if there is an expiration discount
                BigDecimal discountedAmount = sellingPrice.multiply(expirationDiscountValue);
                discount = sellingPrice.subtract(discountedAmount);
            }
            return sellingPrice.subtract(discount).setScale(2);
        }
    }

    @Override
    public String toString() {
        return product.getName();
    }
}