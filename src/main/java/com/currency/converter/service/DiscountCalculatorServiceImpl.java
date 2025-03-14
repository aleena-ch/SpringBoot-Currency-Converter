package com.currency.converter.service;

import com.currency.converter.model.Category;
import com.currency.converter.model.Item;
import com.currency.converter.model.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service implementation for calculating discounts based on a list of items and user type.
 */
@Service
public class DiscountCalculatorServiceImpl implements DiscountCalculatorService {

    private static final Logger logger = LoggerFactory.getLogger(DiscountCalculatorServiceImpl.class);

    /**
     * Calculates the discount amount based on the items and user type.
     *
     * <p>This class implements the discount calculation logic, which includes both flat discounts
     * (e.g., $5 for every $100 spent) and percentage discounts based on the user's type (employee,
     * affiliate, or customer with tenure). The highest applicable discount is applied to the total amount.</p>
     *
     * @param items    The list of items in the bill.
     * @param userType The user type (e.g., employee, affiliate, or customer with tenure).
     * @return The total amount after applying discount as a {@link BigDecimal}..
     */
    @Override
    public BigDecimal calculateDiscount(List<Item> items, UserType userType) {

        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal flatDiscount;
        BigDecimal percentageDiscount = BigDecimal.ZERO;

        //calculate total amount
        for (Item item : items) {
            totalAmount = totalAmount.add(item.getPrice());
        }
        logger.info("Total amount before discount:{}", totalAmount);
        //calculate flat discount $5 for $100
        flatDiscount = BigDecimal.valueOf(totalAmount.intValue() / 100 * 5);
        logger.info("Flat discount:{}", flatDiscount);

        //calculate percentage discount
        for (Item item : items) {
            if (item.getCategory() != Category.GROCERY) {

                if (userType.isEmployee()) {
                    percentageDiscount = totalAmount.multiply(BigDecimal.valueOf(0.30));
                } else if (userType.isAffiliate()) {
                    percentageDiscount = totalAmount.multiply(BigDecimal.valueOf(0.10));
                } else if (userType.isCustomer() && userType.getTenure() > 2) {
                    percentageDiscount = totalAmount.multiply(BigDecimal.valueOf(0.05));
                }

            }
        }
        logger.info("Percentage discount:{}", percentageDiscount);
        //Apply the highest discount
        BigDecimal discount = percentageDiscount.max(flatDiscount);
        logger.info("Get the highest discount:{}", discount);
        //Calculate Final amount
        totalAmount = totalAmount.subtract(discount);
        logger.info("Final amount after discount:{}", totalAmount);
        return totalAmount;
    }
}
