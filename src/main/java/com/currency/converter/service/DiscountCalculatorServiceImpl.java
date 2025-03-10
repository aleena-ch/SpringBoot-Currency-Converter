package com.currency.converter.service;

import com.currency.converter.model.Category;
import com.currency.converter.model.Item;
import com.currency.converter.model.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DiscountCalculatorServiceImpl implements DiscountCalculatorService {

    private static final Logger logger = LoggerFactory.getLogger(DiscountCalculatorServiceImpl.class);

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
