package com.currency.converter.controller;

import com.currency.converter.model.BillRequest;
import com.currency.converter.model.BillResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public interface CurrencyController {

    @PostMapping(path = "/calculate", produces = "application/json")
    ResponseEntity<BillResponse> getTotalAmount(@RequestBody BillRequest billRequest) throws Exception;

}
