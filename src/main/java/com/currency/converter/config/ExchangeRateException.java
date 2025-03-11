package com.currency.converter.config;

public class ExchangeRateException extends RuntimeException{

    // Constructor with message
    public ExchangeRateException(String message) {
        super(message);
    }

    // Constructor with message and cause
    public ExchangeRateException(String message, Throwable cause) {
        super(message, cause);
    }
}
