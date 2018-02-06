package org.aefimov.barrier.exception;

public class BarrierNotFoundException extends Exception {

    public BarrierNotFoundException(String message) {
        super(message);
    }

    public BarrierNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
