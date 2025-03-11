package com.currency.converter.service;

import com.currency.converter.model.Item;
import com.currency.converter.model.UserType;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for calculating discounts on a list of items based on user type.
 */
public interface DiscountCalculatorService {

    /**
     * Calculates the discount amount for a given list of items based on the user's type.
     *
     * @param items    The list of items in the bill.
     * @param userType The type of user (e.g., employee, affiliate, or customer).
     * @return The total discount amount as a {@link BigDecimal}.
     */
    BigDecimal calculateDiscount(List<Item> items, UserType userType);
}
