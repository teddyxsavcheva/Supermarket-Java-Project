package org.project.exceptions;

import java.math.BigDecimal;

public class UnitDeliveryPriceIsNegativeException extends Exception{
    private BigDecimal unitDeliveryPrice;

    public UnitDeliveryPriceIsNegativeException(String message, BigDecimal unitDeliveryPrice){
        super(message);
        this.unitDeliveryPrice = unitDeliveryPrice;
    }

    @Override
    public String toString() {
        return "UnitDeliveryPriceIsNegativeException{" +
                "unitDeliveryPrice=" + unitDeliveryPrice +
                "} " + super.toString();
    }
}
