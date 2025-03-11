package com.currency.converter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Service implementation for retrieving exchange rates between currencies.
 *
 * <p>This class fetches real-time exchange rates from an external API using {@link RestTemplate}.</p>
 */
@Service
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyExchangeServiceImpl.class);

    @Autowired
    private final RestTemplate restTemplate;
    @Value("${currency.api.url}")
    private String apiUrl;

    public CurrencyExchangeServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Retrieves the exchange rate between two currencies by making an external API call.
     *
     * @param originalCurrency The currency from which to convert.
     * @param targetCurrency   The currency to which to convert.
     * @return The exchange rate as a {@link BigDecimal}.
     */
    @Cacheable("exchangeRates")
    public BigDecimal getExchangeRate(String originalCurrency, String targetCurrency) {
        String url = apiUrl.replace("{original_currency}", originalCurrency);
        logger.info("External URL accessed to get conversion rates:{}", url);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                }
        );

        Map<String, Object> responseBody = response.getBody();
        Map<String, Object> conversionRatesObj = (Map<String, Object>) responseBody.get("conversion_rates");
        logger.info("Response from external API");

        if (conversionRatesObj != null) {

            for (Map.Entry<String, Object> entry : conversionRatesObj.entrySet()) {
                String currency = entry.getKey();
                Object rateObject = entry.getValue();

                if (rateObject instanceof Double rate) {
                    BigDecimal rateBigDecimal = BigDecimal.valueOf(rate);  // Convert Double to BigDecimal
                    conversionRatesObj.put(currency, rateBigDecimal);  // Replace with BigDecimal
                }
            }
        }
        BigDecimal rate = (BigDecimal) conversionRatesObj.get(targetCurrency);
        logger.info("Exchange rate:{}", rate);
        return rate;
    }
}
