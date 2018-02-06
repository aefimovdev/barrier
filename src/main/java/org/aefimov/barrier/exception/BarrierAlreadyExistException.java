package org.aefimov.barrier.exception;

public class BarrierAlreadyExistException extends Exception {

    public BarrierAlreadyExistException(String message) {
        super(message);
    }

    public BarrierAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
