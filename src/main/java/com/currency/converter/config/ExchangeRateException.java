package com.currency.converter.config;

/**
 * Custom exception class to handle errors related to exchange rate retrieval.
 * This exception is thrown when there are issues fetching or processing exchange rates
 * from the external currency exchange service.
 */
public class ExchangeRateException extends RuntimeException {

    /**
     * Constructs a new ExchangeRateException with the specified detail message.
     *
     * @param message the detail message, which provides specific information about the error
     */
    public ExchangeRateException(String message) {
        super(message);
    }
}
