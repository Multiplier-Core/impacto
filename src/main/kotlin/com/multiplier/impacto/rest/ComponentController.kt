package com.multiplier.impacto.rest

import com.multiplier.impacto.client.AiServiceClient
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * REST controller for component-related operations.
 * This controller uses the AiServiceClient to fetch component information.
 */
@RestController
@RequestMapping("/api/components")
class ComponentController(
    private val aiServiceClient: AiServiceClient
) {
    private val logger = LoggerFactory.getLogger(ComponentController::class.java)

    /**
     * Get all components.
     *
     * @return A list of components
     */
    @GetMapping
    fun getAllComponents(): ResponseEntity<List<AiServiceClient.Component>> {
        logger.info("Fetching all components")
        
        val components = aiServiceClient.getAllComponents()
        return ResponseEntity.ok(components)
    }

    /**
     * Get all component names.
     *
     * @return A list of component names
     */
    @GetMapping("/names")
    fun getAllComponentNames(): ResponseEntity<List<String>> {
        logger.info("Fetching all component names")
        
        val componentNames = aiServiceClient.getAllComponentNames()
        return ResponseEntity.ok(componentNames)
    }

    /**
     * Get a component by ID.
     *
     * @param id The component ID
     * @return The component with the specified ID, or 404 if not found
     */
    @GetMapping("/{id}")
    fun getComponentById(@PathVariable id: String): ResponseEntity<AiServiceClient.Component> {
        logger.info("Fetching component with ID: $id")
        
        val component = aiServiceClient.getComponentById(id)
        
        return if (component != null) {
            ResponseEntity.ok(component)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    /**
     * Get all features.
     *
     * @return A list of features
     */
    @GetMapping("/features")
    fun getAllFeatures(): ResponseEntity<List<AiServiceClient.Feature>> {
        logger.info("Fetching all features")
        
        val features = aiServiceClient.getAllFeatures()
        return ResponseEntity.ok(features)
    }

    /**
     * Get all feature names.
     *
     * @return A list of feature names
     */
    @GetMapping("/features/names")
    fun getAllFeatureNames(): ResponseEntity<List<String>> {
        logger.info("Fetching all feature names")
        
        val featureNames = aiServiceClient.getAllFeatureNames()
        return ResponseEntity.ok(featureNames)
    }

    /**
     * Get a feature by ID.
     *
     * @param id The feature ID
     * @return The feature with the specified ID, or 404 if not found
     */
    @GetMapping("/features/{id}")
    fun getFeatureById(@PathVariable id: String): ResponseEntity<AiServiceClient.Feature> {
        logger.info("Fetching feature with ID: $id")
        
        val feature = aiServiceClient.getFeatureById(id)
        
        return if (feature != null) {
            ResponseEntity.ok(feature)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
