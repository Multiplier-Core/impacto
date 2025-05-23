package com.multiplier.impacto.client

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity

/**
 * Client for interacting with the AI service.
 * This client provides methods to fetch component and feature information from the AI service.
 */
@Component
class AiServiceClient(
    private val restTemplate: RestTemplate,
    @Value("\${ai.service.url}")
    private val aiServiceUrl: String
) {
    private val logger = LoggerFactory.getLogger(AiServiceClient::class.java)

    /**
     * Data class representing a component.
     */
    data class Component(
        val id: String,
        val name: String,
        val description: String,
        val type: String
    )

    /**
     * Data class representing a feature.
     */
    data class Feature(
        val id: String,
        val name: String,
        val description: String,
        val components: List<String> = emptyList()
    )

    /**
     * Get all components from the AI service.
     *
     * @return A list of components
     */
    fun getAllComponents(): List<Component> {
        logger.info("Fetching all components from AI service")
        
        return try {
            val response: ResponseEntity<Array<Component>> = restTemplate.getForEntity(
                "$aiServiceUrl/api/components"
            )
            
            response.body?.toList() ?: emptyList()
        } catch (e: Exception) {
            logger.error("Error fetching components from AI service", e)
            // Return mock data for now
            getMockComponents()
        }
    }

    /**
     * Get all component names from the AI service.
     *
     * @return A list of component names
     */
    fun getAllComponentNames(): List<String> {
        logger.info("Fetching all component names from AI service")
        
        return try {
            val response: ResponseEntity<Array<String>> = restTemplate.getForEntity(
                "$aiServiceUrl/api/components/names"
            )
            
            response.body?.toList() ?: emptyList()
        } catch (e: Exception) {
            logger.error("Error fetching component names from AI service", e)
            // Return component names from mock data
            getMockComponents().map { it.name }
        }
    }

    /**
     * Get a component by ID from the AI service.
     *
     * @param id The component ID
     * @return The component with the specified ID, or null if not found
     */
    fun getComponentById(id: String): Component? {
        logger.info("Fetching component with ID: $id from AI service")
        
        return try {
            val response: ResponseEntity<Component> = restTemplate.getForEntity(
                "$aiServiceUrl/api/components/$id"
            )
            
            response.body
        } catch (e: Exception) {
            logger.error("Error fetching component with ID: $id from AI service", e)
            // Return mock component if ID matches
            getMockComponents().find { it.id == id }
        }
    }

    /**
     * Get all features from the AI service.
     *
     * @return A list of features
     */
    fun getAllFeatures(): List<Feature> {
        logger.info("Fetching all features from AI service")
        
        return try {
            val response: ResponseEntity<Array<Feature>> = restTemplate.getForEntity(
                "$aiServiceUrl/api/features"
            )
            
            response.body?.toList() ?: emptyList()
        } catch (e: Exception) {
            logger.error("Error fetching features from AI service", e)
            // Return mock data for now
            getMockFeatures()
        }
    }

    /**
     * Get all feature names from the AI service.
     *
     * @return A list of feature names
     */
    fun getAllFeatureNames(): List<String> {
        logger.info("Fetching all feature names from AI service")
        
        return try {
            val response: ResponseEntity<Array<String>> = restTemplate.getForEntity(
                "$aiServiceUrl/api/features/names"
            )
            
            response.body?.toList() ?: emptyList()
        } catch (e: Exception) {
            logger.error("Error fetching feature names from AI service", e)
            // Return feature names from mock data
            getMockFeatures().map { it.name }
        }
    }

    /**
     * Get a feature by ID from the AI service.
     *
     * @param id The feature ID
     * @return The feature with the specified ID, or null if not found
     */
    fun getFeatureById(id: String): Feature? {
        logger.info("Fetching feature with ID: $id from AI service")
        
        return try {
            val response: ResponseEntity<Feature> = restTemplate.getForEntity(
                "$aiServiceUrl/api/features/$id"
            )
            
            response.body
        } catch (e: Exception) {
            logger.error("Error fetching feature with ID: $id from AI service", e)
            // Return mock feature if ID matches
            getMockFeatures().find { it.id == id }
        }
    }

    /**
     * Get mock components for testing or when the AI service is unavailable.
     *
     * @return A list of mock components
     */
    private fun getMockComponents(): List<Component> {
        return listOf(
            Component("auth", "Authentication", "Handles user authentication and session management", "Security"),
            Component("authz", "Authorization", "Manages user permissions and access control", "Security"),
            Component("user-mgmt", "User Management", "Manages user profiles and account settings", "Core"),
            Component("notif", "Notification Service", "Sends notifications to users via various channels", "Communication"),
            Component("payment", "Payment Processing", "Handles payment transactions and billing", "Finance"),
            Component("doc-gen", "Document Generation", "Creates and manages documents and templates", "Content"),
            Component("report", "Reporting", "Generates reports and analytics dashboards", "Analytics"),
            Component("data-export", "Data Export", "Exports data in various formats", "Data"),
            Component("search", "Search", "Provides search functionality across the platform", "Core"),
            Component("audit", "Audit Logging", "Tracks and logs system activities for compliance", "Security"),
            Component("email", "Email Service", "Sends and manages email communications", "Communication"),
            Component("sms", "SMS Service", "Sends SMS messages to users", "Communication"),
            Component("storage", "File Storage", "Manages file uploads and storage", "Infrastructure"),
            Component("analytics", "Analytics", "Collects and processes usage data", "Data"),
            Component("workflow", "Workflow Engine", "Manages business processes and workflows", "Core")
        )
    }

    /**
     * Get mock features for testing or when the AI service is unavailable.
     *
     * @return A list of mock features
     */
    private fun getMockFeatures(): List<Feature> {
        return listOf(
            Feature("user-auth", "User Authentication", "User authentication and authorization", listOf("auth", "authz")),
            Feature("user-profile", "User Profile Management", "User profile and account management", listOf("user-mgmt")),
            Feature("notifications", "Notifications", "User notifications via various channels", listOf("notif", "email", "sms")),
            Feature("billing", "Billing and Payments", "Billing and payment processing", listOf("payment")),
            Feature("document-management", "Document Management", "Document creation and management", listOf("doc-gen", "storage")),
            Feature("reporting", "Reporting and Analytics", "Reporting and data analytics", listOf("report", "analytics", "data-export")),
            Feature("search-functionality", "Search Functionality", "Search across the platform", listOf("search")),
            Feature("audit-logging", "Audit Logging", "System activity logging for compliance", listOf("audit")),
            Feature("workflow-management", "Workflow Management", "Business process and workflow management", listOf("workflow"))
        )
    }
}
