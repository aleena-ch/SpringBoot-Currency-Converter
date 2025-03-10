package com.currency.converter.service;

import com.currency.converter.model.Item;
import com.currency.converter.model.UserType;

import java.math.BigDecimal;
import java.util.List;


public interface DiscountCalculatorService {

    BigDecimal calculateDiscount(List<Item> items, UserType userType);
}
