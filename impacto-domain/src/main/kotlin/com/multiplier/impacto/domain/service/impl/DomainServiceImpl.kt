package com.multiplier.impacto.domain.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.multiplier.impacto.domain.config.DomainMapping
import com.multiplier.impacto.domain.config.ImpactoConfig
import com.multiplier.impacto.domain.model.CodeEntity
import com.multiplier.impacto.domain.model.CodeEntityReference
import com.multiplier.impacto.domain.model.ComponentEntity
import com.multiplier.impacto.domain.model.DomainEntity
import com.multiplier.impacto.domain.model.FeatureEntity
import com.multiplier.impacto.domain.model.TestEntity
import com.multiplier.impacto.domain.model.TestEntityReference
import com.multiplier.impacto.domain.repository.DomainRepository
import com.multiplier.impacto.domain.service.DomainService
import com.multiplier.impacto.domain.service.McpDomainService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Paths

/**
 * Implementation of the DomainService that uses the DomainRepository and McpDomainService.
 */
@Service
class DomainServiceImpl(
    private val domainRepository: DomainRepository,
    private val mcpDomainService: McpDomainService
) : DomainService {
    
    private val logger = LoggerFactory.getLogger(DomainServiceImpl::class.java)
    private val objectMapper = ObjectMapper(YAMLFactory()).registerKotlinModule()
    
    override fun getAllDomains(): List<DomainEntity> {
        logger.info("Getting all domains")
        
        // First, try to get domains from the repository
        val domains = domainRepository.getAllDomains()
        
        // If no domains are found, try to get them from the MCP service
        if (domains.isEmpty()) {
            logger.info("No domains found in repository, fetching from MCP")
            val mcpDomains = mcpDomainService.getAllDomains()
            
            // Save the domains to the repository
            mcpDomains.forEach { domainRepository.saveDomain(it) }
            
            return mcpDomains
        }
        
        return domains
    }
    
    override fun getDomainById(id: String): DomainEntity? {
        logger.info("Getting domain with ID $id")
        
        // First, try to get the domain from the repository
        val domain = domainRepository.getDomainById(id)
        
        // If the domain is not found, try to get it from the MCP service
        if (domain == null) {
            logger.info("Domain not found in repository, fetching from MCP")
            val mcpDomain = mcpDomainService.getDomainById(id)
            
            // Save the domain to the repository if found
            if (mcpDomain != null) {
                domainRepository.saveDomain(mcpDomain)
            }
            
            return mcpDomain
        }
        
        return domain
    }
    
    override fun getAllFeatures(): List<FeatureEntity> {
        logger.info("Getting all features")
        
        // First, try to get features from the repository
        val features = domainRepository.getAllFeatures()
        
        // If no features are found, try to get them from the MCP service
        if (features.isEmpty()) {
            logger.info("No features found in repository, fetching from MCP")
            val mcpFeatures = mcpDomainService.getAllFeatures()
            
            // Save the features to the repository
            mcpFeatures.forEach { domainRepository.saveFeature(it) }
            
            return mcpFeatures
        }
        
        return features
    }
    
    override fun getFeatureById(id: String): FeatureEntity? {
        logger.info("Getting feature with ID $id")
        
        // First, try to get the feature from the repository
        val feature = domainRepository.getFeatureById(id)
        
        // If the feature is not found, try to get it from the MCP service
        if (feature == null) {
            logger.info("Feature not found in repository, fetching from MCP")
            val mcpFeature = mcpDomainService.getFeatureById(id)
            
            // Save the feature to the repository if found
            if (mcpFeature != null) {
                domainRepository.saveFeature(mcpFeature)
            }
            
            return mcpFeature
        }
        
        return feature
    }
    
    override fun getFeaturesByDomainId(domainId: String): List<FeatureEntity> {
        logger.info("Getting features for domain $domainId")
        
        // First, try to get features from the repository
        val features = domainRepository.getFeaturesByDomainId(domainId)
        
        // If no features are found, try to get them from the MCP service
        if (features.isEmpty()) {
            logger.info("No features found in repository for domain $domainId, fetching from MCP")
            val mcpFeatures = mcpDomainService.getFeaturesByDomainId(domainId)
            
            // Save the features to the repository
            mcpFeatures.forEach { domainRepository.saveFeature(it) }
            
            return mcpFeatures
        }
        
        return features
    }
    
    override fun getAllComponents(): List<ComponentEntity> {
        logger.info("Getting all components")
        
        // First, try to get components from the repository
        val components = domainRepository.getAllComponents()
        
        // If no components are found, try to get them from the MCP service
        if (components.isEmpty()) {
            logger.info("No components found in repository, fetching from MCP")
            val mcpComponents = mcpDomainService.getAllComponents()
            
            // Save the components to the repository
            mcpComponents.forEach { domainRepository.saveComponent(it) }
            
            return mcpComponents
        }
        
        return components
    }
    
    override fun getComponentById(id: String): ComponentEntity? {
        logger.info("Getting component with ID $id")
        
        // First, try to get the component from the repository
        val component = domainRepository.getComponentById(id)
        
        // If the component is not found, try to get it from the MCP service
        if (component == null) {
            logger.info("Component not found in repository, fetching from MCP")
            val mcpComponent = mcpDomainService.getComponentById(id)
            
            // Save the component to the repository if found
            if (mcpComponent != null) {
                domainRepository.saveComponent(mcpComponent)
            }
            
            return mcpComponent
        }
        
        return component
    }
    
    override fun getComponentsByFeatureId(featureId: String): List<ComponentEntity> {
        logger.info("Getting components for feature $featureId")
        
        // First, try to get components from the repository
        val components = domainRepository.getComponentsByFeatureId(featureId)
        
        // If no components are found, try to get them from the MCP service
        if (components.isEmpty()) {
            logger.info("No components found in repository for feature $featureId, fetching from MCP")
            val mcpComponents = mcpDomainService.getComponentsByFeatureId(featureId)
            
            // Save the components to the repository
            mcpComponents.forEach { domainRepository.saveComponent(it) }
            
            return mcpComponents
        }
        
        return components
    }
    
    override fun createDomain(domain: DomainEntity): DomainEntity {
        logger.info("Creating domain ${domain.name}")
        return domainRepository.saveDomain(domain)
    }
    
    override fun createFeature(feature: FeatureEntity): FeatureEntity {
        logger.info("Creating feature ${feature.name}")
        return domainRepository.saveFeature(feature)
    }
    
    override fun createComponent(component: ComponentEntity): ComponentEntity {
        logger.info("Creating component ${component.name}")
        return domainRepository.saveComponent(component)
    }
    
    override fun updateDomain(domain: DomainEntity): DomainEntity {
        logger.info("Updating domain ${domain.name}")
        return domainRepository.saveDomain(domain)
    }
    
    override fun updateFeature(feature: FeatureEntity): FeatureEntity {
        logger.info("Updating feature ${feature.name}")
        return domainRepository.saveFeature(feature)
    }
    
    override fun updateComponent(component: ComponentEntity): ComponentEntity {
        logger.info("Updating component ${component.name}")
        return domainRepository.saveComponent(component)
    }
    
    override fun deleteDomain(id: String) {
        logger.info("Deleting domain with ID $id")
        domainRepository.deleteDomain(id)
    }
    
    override fun deleteFeature(id: String) {
        logger.info("Deleting feature with ID $id")
        domainRepository.deleteFeature(id)
    }
    
    override fun deleteComponent(id: String) {
        logger.info("Deleting component with ID $id")
        domainRepository.deleteComponent(id)
    }
    
    override fun associateCodeEntityWithComponent(codeEntityId: String, componentId: String, confidence: Double) {
        logger.info("Associating code entity $codeEntityId with component $componentId")
        
        // Get the component
        val component = getComponentById(componentId) ?: throw IllegalArgumentException("Component not found: $componentId")
        
        // Add the code entity reference to the component
        val codeEntityReference = CodeEntityReference(
            entityId = codeEntityId,
            entityType = com.multiplier.impacto.domain.model.CodeEntityType.CLASS, // Default to CLASS, should be determined based on the actual entity
            confidence = confidence
        )
        
        // Create a new component with the updated code entities
        val updatedComponent = component.copy(
            codeEntities = component.codeEntities + codeEntityReference
        )
        
        // Save the updated component
        updateComponent(updatedComponent)
    }
    
    override fun associateTestEntityWithComponent(testEntityId: String, componentId: String, confidence: Double) {
        logger.info("Associating test entity $testEntityId with component $componentId")
        
        // Get the component
        val component = getComponentById(componentId) ?: throw IllegalArgumentException("Component not found: $componentId")
        
        // Add the test entity reference to the component
        val testEntityReference = TestEntityReference(
            entityId = testEntityId,
            entityType = com.multiplier.impacto.domain.model.TestEntityType.TEST_CLASS, // Default to TEST_CLASS, should be determined based on the actual entity
            confidence = confidence
        )
        
        // Create a new component with the updated test entities
        val updatedComponent = component.copy(
            testEntities = component.testEntities + testEntityReference
        )
        
        // Save the updated component
        updateComponent(updatedComponent)
    }
    
    override fun getDomainsForCodeEntity(codeEntity: CodeEntity): List<DomainEntity> {
        logger.info("Getting domains for code entity ${codeEntity.id}")
        
        // Get all components
        val components = getAllComponents()
        
        // Find components that contain the code entity
        val matchingComponents = components.filter { component ->
            component.codeEntities.any { it.entityId == codeEntity.id }
        }
        
        // Get the domains for the matching components
        return matchingComponents.map { it.feature.domain }.distinct()
    }
    
    override fun getFeaturesForCodeEntity(codeEntity: CodeEntity): List<FeatureEntity> {
        logger.info("Getting features for code entity ${codeEntity.id}")
        
        // Get all components
        val components = getAllComponents()
        
        // Find components that contain the code entity
        val matchingComponents = components.filter { component ->
            component.codeEntities.any { it.entityId == codeEntity.id }
        }
        
        // Get the features for the matching components
        return matchingComponents.map { it.feature }.distinct()
    }
    
    override fun getComponentsForCodeEntity(codeEntity: CodeEntity): List<ComponentEntity> {
        logger.info("Getting components for code entity ${codeEntity.id}")
        
        // Get all components
        val components = getAllComponents()
        
        // Find components that contain the code entity
        return components.filter { component ->
            component.codeEntities.any { it.entityId == codeEntity.id }
        }
    }
    
    override fun getDomainsForTestEntity(testEntity: TestEntity): List<DomainEntity> {
        logger.info("Getting domains for test entity ${testEntity.id}")
        
        // Get all components
        val components = getAllComponents()
        
        // Find components that contain the test entity
        val matchingComponents = components.filter { component ->
            component.testEntities.any { it.entityId == testEntity.id }
        }
        
        // Get the domains for the matching components
        return matchingComponents.map { it.feature.domain }.distinct()
    }
    
    override fun getFeaturesForTestEntity(testEntity: TestEntity): List<FeatureEntity> {
        logger.info("Getting features for test entity ${testEntity.id}")
        
        // Get all components
        val components = getAllComponents()
        
        // Find components that contain the test entity
        val matchingComponents = components.filter { component ->
            component.testEntities.any { it.entityId == testEntity.id }
        }
        
        // Get the features for the matching components
        return matchingComponents.map { it.feature }.distinct()
    }
    
    override fun getComponentsForTestEntity(testEntity: TestEntity): List<ComponentEntity> {
        logger.info("Getting components for test entity ${testEntity.id}")
        
        // Get all components
        val components = getAllComponents()
        
        // Find components that contain the test entity
        return components.filter { component ->
            component.testEntities.any { it.entityId == testEntity.id }
        }
    }
    
    override fun importFromConfig(configPath: String) {
        logger.info("Importing domains, features, and components from config file: $configPath")
        
        try {
            // Read the config file
            val configFile = File(configPath)
            val config = objectMapper.readValue(configFile, ImpactoConfig::class.java)
            
            // Process domain mappings
            processDomainMappings(config.domainMappings)
        } catch (e: Exception) {
            logger.error("Error importing from config file: ${e.message}", e)
            throw RuntimeException("Error importing from config file", e)
        }
    }
    
    override fun exportToConfig(outputPath: String) {
        logger.info("Exporting domains, features, and components to config file: $outputPath")
        
        try {
            // Get all domains, features, and components
            val domains = getAllDomains()
            val features = getAllFeatures()
            val components = getAllComponents()
            
            // Create domain mappings
            val domainMappings = domains.map { domain ->
                val domainFeatures = features.filter { it.domain.id == domain.id }
                
                DomainMapping(
                    domainId = domain.id,
                    pathPatterns = listOf("**/${domain.name.replace(" ", "-").lowercase()}/**"),
                    featureMappings = domainFeatures.map { feature ->
                        val featureComponents = components.filter { it.feature.id == feature.id }
                        
                        com.multiplier.impacto.domain.config.FeatureMapping(
                            featureId = feature.id,
                            pathPatterns = listOf("**/${feature.name.replace(" ", "-").lowercase()}/**"),
                            componentMappings = featureComponents.map { component ->
                                com.multiplier.impacto.domain.config.ComponentMapping(
                                    componentId = component.id,
                                    pathPatterns = listOf("**/${component.name.replace(" ", "-").lowercase()}/**")
                                )
                            }
                        )
                    }
                )
            }
            
            // Create the config
            val config = ImpactoConfig(
                domainMappings = domainMappings
            )
            
            // Write the config to the output file
            val outputFile = File(outputPath)
            objectMapper.writeValue(outputFile, config)
        } catch (e: Exception) {
            logger.error("Error exporting to config file: ${e.message}", e)
            throw RuntimeException("Error exporting to config file", e)
        }
    }
    
    private fun processDomainMappings(domainMappings: List<DomainMapping>) {
        // Process each domain mapping
        domainMappings.forEach { domainMapping ->
            // Get or create the domain
            val domain = getDomainById(domainMapping.domainId) ?: createDefaultDomain(domainMapping.domainId)
            
            // Process feature mappings
            domainMapping.featureMappings.forEach { featureMapping ->
                // Get or create the feature
                val feature = getFeatureById(featureMapping.featureId) ?: createDefaultFeature(featureMapping.featureId, domain)
                
                // Process component mappings
                featureMapping.componentMappings.forEach { componentMapping ->
                    // Get or create the component
                    val component = getComponentById(componentMapping.componentId) ?: createDefaultComponent(componentMapping.componentId, feature)
                    
                    // Save the component
                    updateComponent(component)
                }
                
                // Save the feature
                updateFeature(feature)
            }
            
            // Save the domain
            updateDomain(domain)
        }
    }
    
    private fun createDefaultDomain(id: String): DomainEntity {
        return DomainEntity(
            id = id,
            name = id.replace("-", " ").capitalize(),
            description = "Domain created from configuration"
        )
    }
    
    private fun createDefaultFeature(id: String, domain: DomainEntity): FeatureEntity {
        return FeatureEntity(
            id = id,
            name = id.replace("-", " ").capitalize(),
            description = "Feature created from configuration",
            domain = domain
        )
    }
    
    private fun createDefaultComponent(id: String, feature: FeatureEntity): ComponentEntity {
        return ComponentEntity(
            id = id,
            name = id.replace("-", " ").capitalize(),
            description = "Component created from configuration",
            feature = feature
        )
    }
    
    private fun String.capitalize(): String {
        return this.split(" ").joinToString(" ") { word ->
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
    }
}
