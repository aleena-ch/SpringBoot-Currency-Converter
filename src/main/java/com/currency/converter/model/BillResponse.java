package com.currency.converter.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
public class BillResponse {

    private BigDecimal payableAmount;
    private BigDecimal exchangeRate;
    private String originalCurrency;
    private String targetCurrency;
    private List<Item> items;
    private UserType userType;
}
