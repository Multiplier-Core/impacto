package com.multiplier.impacto.domain.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import org.assertj.core.api.Assertions.assertThat

class AiServiceClientTest {
    
    private lateinit var restTemplate: RestTemplate
    private lateinit var objectMapper: ObjectMapper
    private lateinit var aiServiceClient: AiServiceClient
    
    @BeforeEach
    fun setUp() {
        restTemplate = mock(RestTemplate::class.java)
        objectMapper = ObjectMapper().registerKotlinModule()
        aiServiceClient = AiServiceClient(restTemplate, objectMapper, "http://localhost:8122")
    }
    
    @Test
    fun `getAllComponents should return components from AI service`() {
        // Given
        val components = arrayOf(
            AiServiceClient.Component("auth", "Authentication", "Authentication service", "Security"),
            AiServiceClient.Component("user", "User Management", "User management service", "Core")
        )
        val responseEntity = ResponseEntity(components, HttpStatus.OK)
        
        `when`(restTemplate.getForEntity<Array<AiServiceClient.Component>>(
            "http://localhost:8122/api/components",
            Array<AiServiceClient.Component>::class.java
        )).thenReturn(responseEntity)
        
        // When
        val result = aiServiceClient.getAllComponents()
        
        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].id).isEqualTo("auth")
        assertThat(result[0].name).isEqualTo("Authentication")
        assertThat(result[1].id).isEqualTo("user")
        assertThat(result[1].name).isEqualTo("User Management")
    }
    
    @Test
    fun `getAllComponentNames should return component names from AI service`() {
        // Given
        val componentNames = arrayOf("Authentication", "User Management")
        val responseEntity = ResponseEntity(componentNames, HttpStatus.OK)
        
        `when`(restTemplate.getForEntity<Array<String>>(
            "http://localhost:8122/api/components/names",
            Array<String>::class.java
        )).thenReturn(responseEntity)
        
        // When
        val result = aiServiceClient.getAllComponentNames()
        
        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0]).isEqualTo("Authentication")
        assertThat(result[1]).isEqualTo("User Management")
    }
    
    @Test
    fun `getComponentById should return component from AI service`() {
        // Given
        val component = AiServiceClient.Component("auth", "Authentication", "Authentication service", "Security")
        val responseEntity = ResponseEntity(component, HttpStatus.OK)
        
        `when`(restTemplate.getForEntity<AiServiceClient.Component>(
            "http://localhost:8122/api/components/auth",
            AiServiceClient.Component::class.java
        )).thenReturn(responseEntity)
        
        // When
        val result = aiServiceClient.getComponentById("auth")
        
        // Then
        assertThat(result).isNotNull
        assertThat(result?.id).isEqualTo("auth")
        assertThat(result?.name).isEqualTo("Authentication")
    }
    
    @Test
    fun `getAllFeatures should return features from AI service`() {
        // Given
        val features = arrayOf(
            AiServiceClient.Feature("auth-feature", "Authentication Feature", "Authentication feature", listOf("auth")),
            AiServiceClient.Feature("user-feature", "User Management Feature", "User management feature", listOf("user"))
        )
        val responseEntity = ResponseEntity(features, HttpStatus.OK)
        
        `when`(restTemplate.getForEntity<Array<AiServiceClient.Feature>>(
            "http://localhost:8122/api/features",
            Array<AiServiceClient.Feature>::class.java
        )).thenReturn(responseEntity)
        
        // When
        val result = aiServiceClient.getAllFeatures()
        
        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].id).isEqualTo("auth-feature")
        assertThat(result[0].name).isEqualTo("Authentication Feature")
        assertThat(result[1].id).isEqualTo("user-feature")
        assertThat(result[1].name).isEqualTo("User Management Feature")
    }
}
