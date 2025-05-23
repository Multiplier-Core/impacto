package com.multiplier.impacto.domain.repository

import com.multiplier.impacto.domain.model.ComponentEntity
import com.multiplier.impacto.domain.model.DomainEntity
import com.multiplier.impacto.domain.model.FeatureEntity

/**
 * Repository for domain entities.
 */
interface DomainRepository {
    /**
     * Get all domains.
     */
    fun getAllDomains(): List<DomainEntity>
    
    /**
     * Get a domain by ID.
     */
    fun getDomainById(id: String): DomainEntity?
    
    /**
     * Get domains by name pattern.
     */
    fun getDomainsByNamePattern(pattern: String): List<DomainEntity>
    
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
     * Get features by name pattern.
     */
    fun getFeaturesByNamePattern(pattern: String): List<FeatureEntity>
    
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
     * Get components by name pattern.
     */
    fun getComponentsByNamePattern(pattern: String): List<ComponentEntity>
    
    /**
     * Save a domain.
     */
    fun saveDomain(domain: DomainEntity): DomainEntity
    
    /**
     * Save a feature.
     */
    fun saveFeature(feature: FeatureEntity): FeatureEntity
    
    /**
     * Save a component.
     */
    fun saveComponent(component: ComponentEntity): ComponentEntity
    
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
}
