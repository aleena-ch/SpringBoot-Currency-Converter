package com.currency.converter.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class BillRequest {
    private String originalCurrency;
    private String targetCurrency;
    private List<Item> items;
    private UserType userType;
}
