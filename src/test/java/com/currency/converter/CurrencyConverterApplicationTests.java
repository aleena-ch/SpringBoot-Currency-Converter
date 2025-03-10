package com.currency.converter;

import com.currency.converter.controller.CurrencyControllerImpl;
import com.currency.converter.model.BillRequest;
import com.currency.converter.model.BillResponse;
import com.currency.converter.model.Item;
import com.currency.converter.model.UserType;
import com.currency.converter.service.CurrencyExchangeService;
import com.currency.converter.service.DiscountCalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.currency.converter.model.Category.GROCERY;
import static com.currency.converter.model.Category.NON_GROCERY;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CurrencyConverterApplicationTests {

    @Mock
    private DiscountCalculatorService discountCalculatorService;
    @Mock
    private CurrencyExchangeService currencyExchangeService;

    @InjectMocks
    private CurrencyControllerImpl currencyController;

    private BillRequest billRequest;
    private BillResponse billResponse;
    private UserType userType;
    private BigDecimal mockTotalAmount;
    private BigDecimal mockExchangeRate;

    @BeforeEach
    public void setUp() {

        userType = new UserType();
        userType.setAffiliate(false);
        userType.setCustomer(false);
        userType.setEmployee(true);
        userType.setTenure(3);

        billRequest = new BillRequest();
        billRequest.setUserType(userType);
        billRequest.setOriginalCurrency("USD");
        billRequest.setTargetCurrency("AED");

        List<Item> items = new ArrayList<>();
        Item firstItem = new Item();
        firstItem.setName("Laptop");
        firstItem.setCategory(NON_GROCERY);
        firstItem.setPrice(BigDecimal.valueOf(1500));

        Item secondItem = new Item();
        secondItem.setName("Vegetables");
        secondItem.setCategory(GROCERY);
        secondItem.setPrice(BigDecimal.valueOf(50));
        items.add(firstItem);
        items.add(secondItem);
        billRequest.setItems(items);

        mockTotalAmount = BigDecimal.valueOf(1550);
        mockExchangeRate = BigDecimal.valueOf(3.6725);

        billResponse = new BillResponse();
        billResponse.setTargetCurrency("AED");
        billResponse.setOriginalCurrency("USD");
        billResponse.setItems(items);
        billResponse.setExchangeRate(mockExchangeRate);
        billResponse.setPayableAmount(mockTotalAmount);
    }

    @Test
    public void checkTotalAmount() throws Exception {

        BigDecimal expectedPayableAmount = mockTotalAmount.multiply(mockExchangeRate);

        when(discountCalculatorService.calculateDiscount(any(), any())).thenReturn(mockTotalAmount);

        when(currencyExchangeService.getExchangeRate(any(), any())).thenReturn((mockExchangeRate));

        BillResponse responseEntity = currencyController.getTotalAmount(billRequest).getBody();

        verify(discountCalculatorService, times(1)).calculateDiscount(any(), any());
        verify(currencyExchangeService, times(1)).getExchangeRate(any(), any());


        assertNotNull(responseEntity);
        BigDecimal actualResponse = responseEntity.getPayableAmount();
        assertNotNull(actualResponse);
        //  assertEquals(expectedPayableAmount, actualResponse.getPayableAmount());
        //  assertEquals("AED", actualResponse.getTargetCurrency());
        // assertEquals("USD", actualResponse.getOriginalCurrency());
        // assertEquals(2, actualResponse.getItems().size());
        verify(discountCalculatorService).calculateDiscount(any(), eq(userType));
        verify(currencyExchangeService).getExchangeRate("USD", "AED");
    }

}
