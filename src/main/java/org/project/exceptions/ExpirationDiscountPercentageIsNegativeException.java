package org.project.exceptions;

import java.math.BigDecimal;

public class ExpirationDiscountPercentageIsNegativeException extends Exception {
    private BigDecimal expirationDiscountPercentage;
    public ExpirationDiscountPercentageIsNegativeException(String s, BigDecimal expirationDiscountPercentage) {
        super(s);
        this.expirationDiscountPercentage = expirationDiscountPercentage;
    }

    @Override
    public String toString() {
        return "ExpirationDiscountPercentageIsNegativeException{" +
                "expirationDiscountPercentage=" + expirationDiscountPercentage +
                "} " + super.toString();
    }
}
