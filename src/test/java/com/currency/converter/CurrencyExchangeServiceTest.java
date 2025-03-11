package com.currency.converter;

import com.currency.converter.service.CurrencyExchangeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class CurrencyExchangeServiceTest {

    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private CurrencyExchangeServiceImpl currencyExchangeService;
    @Value("${currency.api.url}")
    private String apiUrl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        currencyExchangeService = new CurrencyExchangeServiceImpl(restTemplate);
        ReflectionTestUtils.setField(currencyExchangeService, "apiUrl",
                "https://v6.exchangerate-api.com/v6/8bf4925c3f14c7d1cc45d011/latest/{original_currency}");
    }

    @Test
    void testGetExchangeRate() {
        String originalCurrency = "USD";
        String targetCurrency = "EUR";

        Map<String, Object> mockConversionRates = Map.of("EUR", 0.85, "GBP", 0.75);

        Map<String, Object> mockApiResponse = Map.of("conversion_rates", mockConversionRates);

        ResponseEntity<Map<String, Object>> mockResponse = ResponseEntity.ok(mockApiResponse);

        when(restTemplate.exchange(
                eq(apiUrl),
                eq(HttpMethod.GET),
                eq(HttpEntity.EMPTY),
                any(ParameterizedTypeReference.class)
        )).thenReturn(mockResponse);

        BigDecimal exchangeRate = currencyExchangeService.getExchangeRate(originalCurrency, targetCurrency);

        assertNotNull(exchangeRate);
        assertEquals(BigDecimal.valueOf(0.85), exchangeRate);

        verify(restTemplate, times(1)).exchange(
                eq(apiUrl),
                eq(HttpMethod.GET),
                eq(HttpEntity.EMPTY),
                any(ParameterizedTypeReference.class)
        );
    }
}
