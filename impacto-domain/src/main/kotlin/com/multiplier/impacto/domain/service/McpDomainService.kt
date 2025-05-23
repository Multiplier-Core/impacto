package com.multiplier.impacto.domain.service

import com.multiplier.impacto.domain.model.ComponentEntity
import com.multiplier.impacto.domain.model.DomainEntity
import com.multiplier.impacto.domain.model.FeatureEntity

/**
 * Service for fetching domain information from the MCP server.
 */
interface McpDomainService {
    /**
     * Get all domains from the MCP server.
     */
    fun getAllDomains(): List<DomainEntity>
    
    /**
     * Get a domain by ID from the MCP server.
     */
    fun getDomainById(id: String): DomainEntity?
    
    /**
     * Get domains by name pattern from the MCP server.
     */
    fun getDomainsByNamePattern(pattern: String): List<DomainEntity>
    
    /**
     * Get all features from the MCP server.
     */
    fun getAllFeatures(): List<FeatureEntity>
    
    /**
     * Get a feature by ID from the MCP server.
     */
    fun getFeatureById(id: String): FeatureEntity?
    
    /**
     * Get features by domain ID from the MCP server.
     */
    fun getFeaturesByDomainId(domainId: String): List<FeatureEntity>
    
    /**
     * Get features by name pattern from the MCP server.
     */
    fun getFeaturesByNamePattern(pattern: String): List<FeatureEntity>
    
    /**
     * Get all components from the MCP server.
     */
    fun getAllComponents(): List<ComponentEntity>
    
    /**
     * Get a component by ID from the MCP server.
     */
    fun getComponentById(id: String): ComponentEntity?
    
    /**
     * Get components by feature ID from the MCP server.
     */
    fun getComponentsByFeatureId(featureId: String): List<ComponentEntity>
    
    /**
     * Get components by name pattern from the MCP server.
     */
    fun getComponentsByNamePattern(pattern: String): List<ComponentEntity>
    
    /**
     * Search for documentation related to a domain.
     */
    fun searchDomainDocumentation(domainName: String): String
    
    /**
     * Search for documentation related to a feature.
     */
    fun searchFeatureDocumentation(featureName: String): String
    
    /**
     * Search for documentation related to a component.
     */
    fun searchComponentDocumentation(componentName: String): String
    
    /**
     * Extract domain information from documentation.
     */
    fun extractDomainInfoFromDocumentation(documentation: String): List<DomainEntity>
    
    /**
     * Extract feature information from documentation.
     */
    fun extractFeatureInfoFromDocumentation(documentation: String): List<FeatureEntity>
    
    /**
     * Extract component information from documentation.
     */
    fun extractComponentInfoFromDocumentation(documentation: String): List<ComponentEntity>
}
