package com.multiplier.impacto.client

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import org.assertj.core.api.Assertions.assertThat

@ExtendWith(MockitoExtension::class)
class AiServiceClientTest {

    @Mock
    private lateinit var restTemplate: RestTemplate

    private lateinit var aiServiceClient: AiServiceClient

    private val aiServiceUrl = "http://localhost:8122"

    @BeforeEach
    fun setUp() {
        aiServiceClient = AiServiceClient(restTemplate, aiServiceUrl)
    }

    @Test
    fun `getAllComponents returns components when API call is successful`() {
        // Given
        val mockComponents = arrayOf(
            AiServiceClient.Component("auth", "Authentication", "Auth description", "Security"),
            AiServiceClient.Component("user-mgmt", "User Management", "User mgmt description", "Core")
        )
        
        val responseEntity = ResponseEntity(mockComponents, HttpStatus.OK)
        
        `when`(restTemplate.getForEntity<Array<AiServiceClient.Component>>(
            "$aiServiceUrl/api/components",
            Array<AiServiceClient.Component>::class.java
        )).thenReturn(responseEntity)

        // When
        val result = aiServiceClient.getAllComponents()

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].name).isEqualTo("Authentication")
        assertThat(result[1].name).isEqualTo("User Management")
    }

    @Test
    fun `getAllComponents returns mock data when API call fails`() {
        // Given
        `when`(restTemplate.getForEntity<Array<AiServiceClient.Component>>(
            "$aiServiceUrl/api/components",
            Array<AiServiceClient.Component>::class.java
        )).thenThrow(RuntimeException("API error"))

        // When
        val result = aiServiceClient.getAllComponents()

        // Then
        assertThat(result).isNotEmpty()
        assertThat(result.any { it.name == "Authentication" }).isTrue()
    }

    @Test
    fun `getAllComponentNames returns component names when API call is successful`() {
        // Given
        val mockComponentNames = arrayOf("Authentication", "User Management")
        
        val responseEntity = ResponseEntity(mockComponentNames, HttpStatus.OK)
        
        `when`(restTemplate.getForEntity<Array<String>>(
            "$aiServiceUrl/api/components/names",
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
    fun `getComponentById returns component when API call is successful`() {
        // Given
        val mockComponent = AiServiceClient.Component("auth", "Authentication", "Auth description", "Security")
        
        val responseEntity = ResponseEntity(mockComponent, HttpStatus.OK)
        
        `when`(restTemplate.getForEntity<AiServiceClient.Component>(
            "$aiServiceUrl/api/components/auth",
            AiServiceClient.Component::class.java
        )).thenReturn(responseEntity)

        // When
        val result = aiServiceClient.getComponentById("auth")

        // Then
        assertThat(result).isNotNull
        assertThat(result?.name).isEqualTo("Authentication")
        assertThat(result?.type).isEqualTo("Security")
    }

    @Test
    fun `getAllFeatures returns features when API call is successful`() {
        // Given
        val mockFeatures = arrayOf(
            AiServiceClient.Feature("user-auth", "User Authentication", "User auth description", listOf("auth", "authz")),
            AiServiceClient.Feature("user-profile", "User Profile", "User profile description", listOf("user-mgmt"))
        )
        
        val responseEntity = ResponseEntity(mockFeatures, HttpStatus.OK)
        
        `when`(restTemplate.getForEntity<Array<AiServiceClient.Feature>>(
            "$aiServiceUrl/api/features",
            Array<AiServiceClient.Feature>::class.java
        )).thenReturn(responseEntity)

        // When
        val result = aiServiceClient.getAllFeatures()

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].name).isEqualTo("User Authentication")
        assertThat(result[1].name).isEqualTo("User Profile")
    }

    @Test
    fun `getFeatureById returns feature when API call is successful`() {
        // Given
        val mockFeature = AiServiceClient.Feature("user-auth", "User Authentication", "User auth description", listOf("auth", "authz"))
        
        val responseEntity = ResponseEntity(mockFeature, HttpStatus.OK)
        
        `when`(restTemplate.getForEntity<AiServiceClient.Feature>(
            "$aiServiceUrl/api/features/user-auth",
            AiServiceClient.Feature::class.java
        )).thenReturn(responseEntity)

        // When
        val result = aiServiceClient.getFeatureById("user-auth")

        // Then
        assertThat(result).isNotNull
        assertThat(result?.name).isEqualTo("User Authentication")
        assertThat(result?.components).contains("auth", "authz")
    }
}
