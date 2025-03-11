package com.currency.converter.controller;

import com.currency.converter.model.BillRequest;
import com.currency.converter.model.BillResponse;
import com.currency.converter.model.Item;
import com.currency.converter.model.UserType;
import com.currency.converter.service.CurrencyExchangeService;
import com.currency.converter.service.DiscountCalculatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controller implementation for processing bill requests, applying discounts,
 * and converting the total amount to a specified target currency.
 *
 * <p>This class implements the logic to handle bill requests, calculate discounts
 * using {@link DiscountCalculatorService}, fetch exchange rates using
 * {@link CurrencyExchangeService}, and return the final payable amount
 * in the specified target currency.</p>
 */
@RestController
public class CurrencyControllerImpl implements CurrencyController {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyControllerImpl.class);
    @Autowired
    private final CurrencyExchangeService currencyExchangeService;
    @Autowired
    private final DiscountCalculatorService discountCalculatorService;


    public CurrencyControllerImpl(CurrencyExchangeService currencyExchangeService, DiscountCalculatorService discountCalculatorService) {
        this.currencyExchangeService = currencyExchangeService;
        this.discountCalculatorService = discountCalculatorService;
    }

    /**
     * <p>This method orchestrates the business logic by calling the discount
     * service to calculate the discount and the currency exchange service
     * to retrieve the exchange rate for the specified currency.</p>
     *
     * @param billRequest The request containing the items, user type, and currency information.
     * @return A {@link ResponseEntity} containing the final payable amount,
     * exchange rate, and other relevant information.
     */
    public ResponseEntity<BillResponse> getTotalAmount(@RequestBody BillRequest billRequest) throws Exception {

        logger.info("Enter into controller method to get total amount");

        BillResponse response = new BillResponse();
        List<Item> itemsList = billRequest.getItems();
        UserType userType = billRequest.getUserType();
        String originalCurrency = billRequest.getOriginalCurrency();

        logger.info("Enter into discount calculate method to get total discount");
        BigDecimal totalAmount;
        try {
            totalAmount = discountCalculatorService.calculateDiscount(itemsList, userType);
        } catch (Exception exception) {
            throw new Exception(exception);
        }
        if (totalAmount == null) {
            return ResponseEntity.badRequest().build();
        }

        logger.info("Enter into currency exchange method to get exchange rate for:{}", originalCurrency);
        BigDecimal exchangeRate;
        try {
            exchangeRate = currencyExchangeService
                    .getExchangeRate(billRequest.getOriginalCurrency(), billRequest.getTargetCurrency());
        } catch (Exception exception) {
            throw new Exception(exception);
        }
        if (exchangeRate == null) {
            return ResponseEntity.badRequest().build();
        }

        BigDecimal payableAmount = totalAmount.multiply(exchangeRate);
        response.setExchangeRate(exchangeRate);
        response.setPayableAmount(payableAmount);
        response.setTargetCurrency(billRequest.getTargetCurrency());
        response.setOriginalCurrency(billRequest.getOriginalCurrency());
        response.setItems(billRequest.getItems());
        response.setUserType(billRequest.getUserType());
        logger.info("Total amount:{}", payableAmount);

        return ResponseEntity.ok(response);
    }
}
