package com.multiplier.impacto.rest

import com.multiplier.impacto.client.AiServiceClient
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(ComponentController::class)
class ComponentControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var aiServiceClient: AiServiceClient

    @Test
    fun `getAllComponents returns components`() {
        // Given
        val mockComponents = listOf(
            AiServiceClient.Component("auth", "Authentication", "Auth description", "Security"),
            AiServiceClient.Component("user-mgmt", "User Management", "User mgmt description", "Core")
        )
        
        `when`(aiServiceClient.getAllComponents()).thenReturn(mockComponents)

        // When/Then
        mockMvc.perform(get("/api/components")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value("auth"))
            .andExpect(jsonPath("$[0].name").value("Authentication"))
            .andExpect(jsonPath("$[1].id").value("user-mgmt"))
            .andExpect(jsonPath("$[1].name").value("User Management"))
    }

    @Test
    fun `getAllComponentNames returns component names`() {
        // Given
        val mockComponentNames = listOf("Authentication", "User Management")
        
        `when`(aiServiceClient.getAllComponentNames()).thenReturn(mockComponentNames)

        // When/Then
        mockMvc.perform(get("/api/components/names")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0]").value("Authentication"))
            .andExpect(jsonPath("$[1]").value("User Management"))
    }

    @Test
    fun `getComponentById returns component when found`() {
        // Given
        val mockComponent = AiServiceClient.Component("auth", "Authentication", "Auth description", "Security")
        
        `when`(aiServiceClient.getComponentById("auth")).thenReturn(mockComponent)

        // When/Then
        mockMvc.perform(get("/api/components/auth")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value("auth"))
            .andExpect(jsonPath("$.name").value("Authentication"))
            .andExpect(jsonPath("$.type").value("Security"))
    }

    @Test
    fun `getComponentById returns 404 when not found`() {
        // Given
        `when`(aiServiceClient.getComponentById("unknown")).thenReturn(null)

        // When/Then
        mockMvc.perform(get("/api/components/unknown")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `getAllFeatures returns features`() {
        // Given
        val mockFeatures = listOf(
            AiServiceClient.Feature("user-auth", "User Authentication", "User auth description", listOf("auth", "authz")),
            AiServiceClient.Feature("user-profile", "User Profile", "User profile description", listOf("user-mgmt"))
        )
        
        `when`(aiServiceClient.getAllFeatures()).thenReturn(mockFeatures)

        // When/Then
        mockMvc.perform(get("/api/components/features")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value("user-auth"))
            .andExpect(jsonPath("$[0].name").value("User Authentication"))
            .andExpect(jsonPath("$[1].id").value("user-profile"))
            .andExpect(jsonPath("$[1].name").value("User Profile"))
    }
}
