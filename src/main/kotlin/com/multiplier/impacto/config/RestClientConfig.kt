package com.multiplier.impacto.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

/**
 * Configuration for REST clients.
 */
@Configuration
class RestClientConfig {
    
    /**
     * Creates a RestTemplate bean for making HTTP requests.
     */
    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}
