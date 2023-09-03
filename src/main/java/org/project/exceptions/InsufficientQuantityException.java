package org.project.exceptions;

public class InsufficientQuantityException extends Exception {
    private int additionalQuantityNeeded;

    public InsufficientQuantityException(String message, int additionalQuantityNeeded) {
        super(message);
        this.additionalQuantityNeeded = additionalQuantityNeeded;
    }

    public InsufficientQuantityException(String message) {
    }

    public int getAdditionalQuantityNeeded() {
        return additionalQuantityNeeded;
    }

    @Override
    public String toString() {
        return "InsufficientQuantityException{" +
                "additionalQuantityNeeded=" + additionalQuantityNeeded +
                "} " + super.toString();
    }
}
