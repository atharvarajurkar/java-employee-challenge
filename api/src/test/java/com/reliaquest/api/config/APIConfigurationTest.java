package com.reliaquest.api.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

class APIConfigurationTest {

    @Test
    void init() {
        APIConfiguration apiConfiguration = new APIConfiguration();
        RestTemplate restTemplate = apiConfiguration.restTemplate();
        Assertions.assertNotNull(restTemplate);
        Assertions.assertSame(restTemplate.getClass(), RestTemplate.class);
    }
}