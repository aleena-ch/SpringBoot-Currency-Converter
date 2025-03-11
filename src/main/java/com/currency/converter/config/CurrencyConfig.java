package com.currency.converter.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for setting up application beans related to currency conversion.
 *
 * <p>This class is marked with {@link Configuration} and defines the necessary beans for the application,
 * such as the {@link RestTemplate} for making external API calls.</p>
 */
@Configuration
public class CurrencyConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
