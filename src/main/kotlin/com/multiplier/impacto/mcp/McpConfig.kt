package com.multiplier.impacto.mcp

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configuration for the MCP query service.
 */
@Configuration
class McpConfig {
    
    /**
     * Creates an ObjectMapper configured for Kotlin.
     */
    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper().registerKotlinModule()
    }
}
