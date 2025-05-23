package com.multiplier.impacto.domain.repository.impl

import com.multiplier.impacto.domain.model.ComponentEntity
import com.multiplier.impacto.domain.model.DomainEntity
import com.multiplier.impacto.domain.model.FeatureEntity
import com.multiplier.impacto.domain.repository.DomainRepository
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

/**
 * In-memory implementation of the DomainRepository.
 */
@Repository
class InMemoryDomainRepository : DomainRepository {
    
    private val domains = ConcurrentHashMap<String, DomainEntity>()
    private val features = ConcurrentHashMap<String, FeatureEntity>()
    private val components = ConcurrentHashMap<String, ComponentEntity>()
    
    override fun getAllDomains(): List<DomainEntity> {
        return domains.values.toList()
    }
    
    override fun getDomainById(id: String): DomainEntity? {
        return domains[id]
    }
    
    override fun getDomainsByNamePattern(pattern: String): List<DomainEntity> {
        return domains.values.filter { it.name.contains(pattern, ignoreCase = true) }
    }
    
    override fun getAllFeatures(): List<FeatureEntity> {
        return features.values.toList()
    }
    
    override fun getFeatureById(id: String): FeatureEntity? {
        return features[id]
    }
    
    override fun getFeaturesByDomainId(domainId: String): List<FeatureEntity> {
        return features.values.filter { it.domain.id == domainId }
    }
    
    override fun getFeaturesByNamePattern(pattern: String): List<FeatureEntity> {
        return features.values.filter { it.name.contains(pattern, ignoreCase = true) }
    }
    
    override fun getAllComponents(): List<ComponentEntity> {
        return components.values.toList()
    }
    
    override fun getComponentById(id: String): ComponentEntity? {
        return components[id]
    }
    
    override fun getComponentsByFeatureId(featureId: String): List<ComponentEntity> {
        return components.values.filter { it.feature.id == featureId }
    }
    
    override fun getComponentsByNamePattern(pattern: String): List<ComponentEntity> {
        return components.values.filter { it.name.contains(pattern, ignoreCase = true) }
    }
    
    override fun saveDomain(domain: DomainEntity): DomainEntity {
        domains[domain.id] = domain
        return domain
    }
    
    override fun saveFeature(feature: FeatureEntity): FeatureEntity {
        features[feature.id] = feature
        return feature
    }
    
    override fun saveComponent(component: ComponentEntity): ComponentEntity {
        components[component.id] = component
        return component
    }
    
    override fun deleteDomain(id: String) {
        domains.remove(id)
        
        // Delete all features associated with this domain
        val featuresToDelete = features.values.filter { it.domain.id == id }
        featuresToDelete.forEach { feature ->
            deleteFeature(feature.id)
        }
    }
    
    override fun deleteFeature(id: String) {
        features.remove(id)
        
        // Delete all components associated with this feature
        val componentsToDelete = components.values.filter { it.feature.id == id }
        componentsToDelete.forEach { component ->
            deleteComponent(component.id)
        }
    }
    
    override fun deleteComponent(id: String) {
        components.remove(id)
    }
}
