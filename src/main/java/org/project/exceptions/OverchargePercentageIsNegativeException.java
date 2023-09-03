package org.project.exceptions;

import org.project.products.ProductCategory;

public class OverchargePercentageIsNegativeException extends Exception {
    private ProductCategory productCategory;
    public OverchargePercentageIsNegativeException(String s, ProductCategory productCategory) {
        super(s);
        this.productCategory = productCategory;
    }

    @Override
    public String toString() {
        return "OverchargePercentageIsNegativeException{" +
                "productCategory=" + productCategory +
                "} " + super.toString();
    }
}
