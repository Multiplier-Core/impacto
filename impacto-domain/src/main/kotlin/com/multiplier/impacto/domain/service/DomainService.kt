package com.multiplier.impacto.domain.service

import com.multiplier.impacto.domain.model.CodeEntity
import com.multiplier.impacto.domain.model.ComponentEntity
import com.multiplier.impacto.domain.model.DomainEntity
import com.multiplier.impacto.domain.model.FeatureEntity
import com.multiplier.impacto.domain.model.TestEntity

/**
 * Service for managing domain entities and their relationships.
 */
interface DomainService {
    /**
     * Get all domains.
     */
    fun getAllDomains(): List<DomainEntity>
    
    /**
     * Get a domain by ID.
     */
    fun getDomainById(id: String): DomainEntity?
    
    /**
     * Get all features.
     */
    fun getAllFeatures(): List<FeatureEntity>
    
    /**
     * Get a feature by ID.
     */
    fun getFeatureById(id: String): FeatureEntity?
    
    /**
     * Get features by domain ID.
     */
    fun getFeaturesByDomainId(domainId: String): List<FeatureEntity>
    
    /**
     * Get all components.
     */
    fun getAllComponents(): List<ComponentEntity>
    
    /**
     * Get a component by ID.
     */
    fun getComponentById(id: String): ComponentEntity?
    
    /**
     * Get components by feature ID.
     */
    fun getComponentsByFeatureId(featureId: String): List<ComponentEntity>
    
    /**
     * Create a new domain.
     */
    fun createDomain(domain: DomainEntity): DomainEntity
    
    /**
     * Create a new feature.
     */
    fun createFeature(feature: FeatureEntity): FeatureEntity
    
    /**
     * Create a new component.
     */
    fun createComponent(component: ComponentEntity): ComponentEntity
    
    /**
     * Update a domain.
     */
    fun updateDomain(domain: DomainEntity): DomainEntity
    
    /**
     * Update a feature.
     */
    fun updateFeature(feature: FeatureEntity): FeatureEntity
    
    /**
     * Update a component.
     */
    fun updateComponent(component: ComponentEntity): ComponentEntity
    
    /**
     * Delete a domain.
     */
    fun deleteDomain(id: String)
    
    /**
     * Delete a feature.
     */
    fun deleteFeature(id: String)
    
    /**
     * Delete a component.
     */
    fun deleteComponent(id: String)
    
    /**
     * Associate a code entity with a component.
     */
    fun associateCodeEntityWithComponent(codeEntityId: String, componentId: String, confidence: Double = 1.0)
    
    /**
     * Associate a test entity with a component.
     */
    fun associateTestEntityWithComponent(testEntityId: String, componentId: String, confidence: Double = 1.0)
    
    /**
     * Get domains for a code entity.
     */
    fun getDomainsForCodeEntity(codeEntity: CodeEntity): List<DomainEntity>
    
    /**
     * Get features for a code entity.
     */
    fun getFeaturesForCodeEntity(codeEntity: CodeEntity): List<FeatureEntity>
    
    /**
     * Get components for a code entity.
     */
    fun getComponentsForCodeEntity(codeEntity: CodeEntity): List<ComponentEntity>
    
    /**
     * Get domains for a test entity.
     */
    fun getDomainsForTestEntity(testEntity: TestEntity): List<DomainEntity>
    
    /**
     * Get features for a test entity.
     */
    fun getFeaturesForTestEntity(testEntity: TestEntity): List<FeatureEntity>
    
    /**
     * Get components for a test entity.
     */
    fun getComponentsForTestEntity(testEntity: TestEntity): List<ComponentEntity>
    
    /**
     * Import domains, features, and components from a configuration file.
     */
    fun importFromConfig(configPath: String)
    
    /**
     * Export domains, features, and components to a configuration file.
     */
    fun exportToConfig(outputPath: String)
}
