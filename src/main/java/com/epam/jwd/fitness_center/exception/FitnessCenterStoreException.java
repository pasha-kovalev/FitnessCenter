package com.epam.jwd.fitness_center.exception;

public class FitnessCenterStoreException extends Exception{
    public FitnessCenterStoreException(String message) {
        super(message);
    }

    public FitnessCenterStoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
