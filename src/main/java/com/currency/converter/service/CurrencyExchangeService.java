package com.currency.converter.service;


import java.math.BigDecimal;

public interface CurrencyExchangeService {
    BigDecimal getExchangeRate(String baseCurrency, String targetCurrency);
}
