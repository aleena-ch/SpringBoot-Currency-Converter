package com.currency.converter.service;


import java.math.BigDecimal;

/**
 * Service interface for retrieving exchange rates between currencies.
 */
public interface CurrencyExchangeService {
    /**
     * Retrieves the exchange rate between two currencies.
     *
     * @param originalCurrency The currency from which to convert.
     * @param targetCurrency   The currency to which to convert.
     * @return The exchange rate as a {@link BigDecimal}.
     */
    BigDecimal getExchangeRate(String originalCurrency, String targetCurrency) throws Exception;
}
