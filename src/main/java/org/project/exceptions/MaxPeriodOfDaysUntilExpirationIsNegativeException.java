package org.project.exceptions;

public class MaxPeriodOfDaysUntilExpirationIsNegativeException extends Exception {
    private long maxPeriodOfDaysUntilExpiration;
    public MaxPeriodOfDaysUntilExpirationIsNegativeException(String s, long maxPeriodOfDaysUntilExpiration) {
        super(s);
        this.maxPeriodOfDaysUntilExpiration = maxPeriodOfDaysUntilExpiration;
    }

    @Override
    public String toString() {
        return "MaxPeriodOfDaysUntilExpirationIsNegativeException{" +
                "maxPeriodOfDaysUntilExpiration=" + maxPeriodOfDaysUntilExpiration +
                "} " + super.toString();
    }
}
