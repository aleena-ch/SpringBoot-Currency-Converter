package com.currency.converter.controller;

import com.currency.converter.model.BillRequest;
import com.currency.converter.model.BillResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller interface for processing bill requests, applying discounts,
 * and converting the total amount to a specified target currency.
 */
@RestController
@RequestMapping("/api")
public interface CurrencyController {

    /**
     * Calculates the total payable amount after applying discounts and converting to the target currency.
     *
     * @param billRequest The request containing items, user type, and currency info.
     * @return A {@link ResponseEntity} with the final payable amount and exchange rate.
     */
    @PostMapping(path = "/calculate", produces = "application/json")
    ResponseEntity<BillResponse> getTotalAmount(@RequestBody BillRequest billRequest) throws Exception;

}
