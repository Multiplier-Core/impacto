package com.multiplier.impacto.domain.service.impl

import com.multiplier.impacto.domain.client.AiServiceClient
import com.multiplier.impacto.domain.mcp.McpDocumentationEntry
import com.multiplier.impacto.domain.mcp.McpQueryService
import com.multiplier.impacto.domain.model.ComponentEntity
import com.multiplier.impacto.domain.model.DomainEntity
import com.multiplier.impacto.domain.model.FeatureEntity
import com.multiplier.impacto.domain.service.McpDomainService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

/**
 * Implementation of the McpDomainService that uses the McpQueryService and AiServiceClient to fetch domain information.
 */
@Service
class McpDomainServiceImpl(
    private val mcpQueryService: McpQueryService,
    private val aiServiceClient: AiServiceClient
) : McpDomainService {

    private val logger = LoggerFactory.getLogger(McpDomainServiceImpl::class.java)

    override fun getAllDomains(): List<DomainEntity> {
        logger.info("Fetching all domains from MCP")

        // Search for domains in documentation
        val domainDocs = mcpQueryService.searchDocumentation("domain")
        return extractDomainInfoFromDocumentation(domainDocs.joinToString("\n") { doc -> doc.content })
    }

    override fun getDomainById(id: String): DomainEntity? {
        logger.info("Fetching domain with ID $id from MCP")

        // Search for the specific domain in documentation
        val domainDocs = mcpQueryService.searchDocumentation("domain $id")
        val domains = extractDomainInfoFromDocumentation(domainDocs.joinToString("\n") { doc -> doc.content })
        return domains.find { domain -> domain.id == id }
    }

    override fun getDomainsByNamePattern(pattern: String): List<DomainEntity> {
        logger.info("Fetching domains matching pattern $pattern from MCP")

        // Search for domains matching the pattern in documentation
        val domainDocs = mcpQueryService.searchDocumentation("domain $pattern")
        val domains = extractDomainInfoFromDocumentation(domainDocs.joinToString("\n") { doc -> doc.content })
        return domains.filter { domain -> domain.name.contains(pattern, ignoreCase = true) }
    }

    override fun getAllFeatures(): List<FeatureEntity> {
        logger.info("Fetching all features from MCP")

        // Get features directly from the MCP service
        val mcpFeatures = mcpQueryService.getFeatures()

        // Convert MCP features to domain features
        val domains = getAllDomains()
        return mcpFeatures.map { mcpFeature ->
            val domain = domains.firstOrNull() ?: createDefaultDomain()
            FeatureEntity(
                id = mcpFeature.name.replace(" ", "-").lowercase(),
                name = mcpFeature.name,
                description = mcpFeature.description,
                domain = domain
            )
        }
    }

    override fun getFeatureById(id: String): FeatureEntity? {
        logger.info("Fetching feature with ID $id from MCP")

        // Search for the specific feature in documentation
        val featureDocs = mcpQueryService.searchDocumentation("feature $id")
        val features = extractFeatureInfoFromDocumentation(featureDocs.joinToString("\n") { doc -> doc.content })
        return features.find { feature -> feature.id == id }
    }

    override fun getFeaturesByDomainId(domainId: String): List<FeatureEntity> {
        logger.info("Fetching features for domain $domainId from MCP")

        // Get all features and filter by domain ID
        return getAllFeatures().filter { it.domain.id == domainId }
    }

    override fun getFeaturesByNamePattern(pattern: String): List<FeatureEntity> {
        logger.info("Fetching features matching pattern $pattern from MCP")

        // Search for features matching the pattern in documentation
        val featureDocs = mcpQueryService.searchDocumentation("feature $pattern")
        val features = extractFeatureInfoFromDocumentation(featureDocs.joinToString("\n") { doc -> doc.content })
        return features.filter { feature -> feature.name.contains(pattern, ignoreCase = true) }
    }

    override fun getAllComponents(): List<ComponentEntity> {
        logger.info("Fetching all components from MCP and AI service")

        // Get components from both MCP service and AI service
        val mcpComponents = mcpQueryService.getComponents()
        val aiComponents = aiServiceClient.getAllComponents()

        // Convert components to domain components
        val features = getAllFeatures()
        val defaultFeature = features.firstOrNull() ?: createDefaultFeature()

        // Combine components from both sources
        val combinedComponents = mutableListOf<ComponentEntity>()

        // Add MCP components
        combinedComponents.addAll(mcpComponents.map { mcpComponent ->
            ComponentEntity(
                id = mcpComponent.name.replace(" ", "-").lowercase(),
                name = mcpComponent.name,
                description = mcpComponent.description,
                feature = defaultFeature
            )
        })

        // Add AI service components (avoiding duplicates)
        val existingIds = combinedComponents.map { component -> component.id }.toSet()
        combinedComponents.addAll(aiComponents
            .filter { aiComponent -> !existingIds.contains(aiComponent.id.lowercase()) }
            .map { aiComponent ->
                ComponentEntity(
                    id = aiComponent.id.lowercase(),
                    name = aiComponent.name,
                    description = aiComponent.description,
                    feature = defaultFeature
                )
            }
        )

        return combinedComponents
    }

    override fun getComponentById(id: String): ComponentEntity? {
        logger.info("Fetching component with ID $id from MCP and AI service")

        // First try to get the component from the AI service
        val aiComponent = aiServiceClient.getComponentById(id)
        if (aiComponent != null) {
            val features = getAllFeatures()
            val defaultFeature = features.firstOrNull() ?: createDefaultFeature()

            return ComponentEntity(
                id = aiComponent.id.lowercase(),
                name = aiComponent.name,
                description = aiComponent.description,
                feature = defaultFeature
            )
        }

        // If not found in AI service, search in MCP documentation
        val componentDocs = mcpQueryService.searchDocumentation("component $id")
        val components = extractComponentInfoFromDocumentation(componentDocs.joinToString("\n") { it.content })
        return components.find { it.id == id }
    }

    override fun getComponentsByFeatureId(featureId: String): List<ComponentEntity> {
        logger.info("Fetching components for feature $featureId from MCP")

        // Get all components and filter by feature ID
        return getAllComponents().filter { it.feature.id == featureId }
    }

    override fun getComponentsByNamePattern(pattern: String): List<ComponentEntity> {
        logger.info("Fetching components matching pattern $pattern from MCP and AI service")

        // Get all components and filter by name pattern
        return getAllComponents().filter { it.name.contains(pattern, ignoreCase = true) }
    }

    override fun searchDomainDocumentation(domainName: String): String {
        logger.info("Searching for documentation related to domain $domainName")

        // Search for documentation related to the domain
        val domainDocs = mcpQueryService.searchDocumentation("domain $domainName")
        return domainDocs.joinToString("\n\n") { doc -> "## ${doc.toolName}\n\n${doc.content}" }
    }

    override fun searchFeatureDocumentation(featureName: String): String {
        logger.info("Searching for documentation related to feature $featureName")

        // Search for documentation related to the feature
        val featureDocs = mcpQueryService.searchDocumentation("feature $featureName")
        return featureDocs.joinToString("\n\n") { doc -> "## ${doc.toolName}\n\n${doc.content}" }
    }

    override fun searchComponentDocumentation(componentName: String): String {
        logger.info("Searching for documentation related to component $componentName")

        // Search for documentation related to the component
        val componentDocs = mcpQueryService.searchDocumentation("component $componentName")
        return componentDocs.joinToString("\n\n") { doc -> "## ${doc.toolName}\n\n${doc.content}" }
    }

    override fun extractDomainInfoFromDocumentation(documentation: String): List<DomainEntity> {
        logger.info("Extracting domain information from documentation")

        val domains = mutableListOf<DomainEntity>()
        val lines = documentation.split("\n")

        // Simple extraction logic - in a real implementation, this would be more sophisticated
        // Look for headings that might indicate domains
        var currentDomain: DomainEntity? = null

        for (line in lines) {
            // Look for headings that might indicate domains
            if (line.startsWith("# ") || line.startsWith("## ")) {
                val name = line.replace("#", "").trim()
                if (name.contains("domain", ignoreCase = true) ||
                    name.contains("module", ignoreCase = true) ||
                    name.contains("service", ignoreCase = true)) {
                    currentDomain = DomainEntity(
                        id = name.replace(" ", "-").lowercase(),
                        name = name,
                        description = ""
                    )
                    domains.add(currentDomain)
                }
            } else if (currentDomain != null && line.isNotBlank() && !line.startsWith("-") && !line.startsWith("*")) {
                // Add description to the current domain
                val updatedDomain = currentDomain.copy(
                    description = currentDomain.description + " " + line.trim()
                )
                domains[domains.indexOf(currentDomain)] = updatedDomain
                currentDomain = updatedDomain
            }
        }

        return domains
    }

    override fun extractFeatureInfoFromDocumentation(documentation: String): List<FeatureEntity> {
        logger.info("Extracting feature information from documentation")

        val features = mutableListOf<FeatureEntity>()
        val lines = documentation.split("\n")

        // Simple extraction logic - in a real implementation, this would be more sophisticated
        // Look for headings or bullet points that might indicate features
        var currentFeature: FeatureEntity? = null
        val defaultDomain = createDefaultDomain()

        for (line in lines) {
            // Look for headings that might indicate features
            if (line.startsWith("# ") || line.startsWith("## ") || line.startsWith("### ")) {
                val name = line.replace("#", "").trim()
                if (name.contains("feature", ignoreCase = true) ||
                    name.contains("functionality", ignoreCase = true)) {
                    currentFeature = FeatureEntity(
                        id = name.replace(" ", "-").lowercase(),
                        name = name,
                        description = "",
                        domain = defaultDomain
                    )
                    features.add(currentFeature)
                }
            } else if (line.trim().startsWith("- ") || line.trim().startsWith("* ")) {
                // Look for bullet points that might indicate features
                val name = line.replace("-", "").replace("*", "").trim()
                if (name.contains("feature", ignoreCase = true) ||
                    name.contains("functionality", ignoreCase = true)) {
                    val feature = FeatureEntity(
                        id = name.replace(" ", "-").lowercase(),
                        name = name,
                        description = "",
                        domain = defaultDomain
                    )
                    features.add(feature)
                    currentFeature = feature
                }
            } else if (currentFeature != null && line.isNotBlank() && !line.startsWith("-") && !line.startsWith("*")) {
                // Add description to the current feature
                val updatedFeature = currentFeature.copy(
                    description = currentFeature.description + " " + line.trim()
                )
                features[features.indexOf(currentFeature)] = updatedFeature
                currentFeature = updatedFeature
            }
        }

        return features
    }

    override fun extractComponentInfoFromDocumentation(documentation: String): List<ComponentEntity> {
        logger.info("Extracting component information from documentation")

        val components = mutableListOf<ComponentEntity>()
        val lines = documentation.split("\n")

        // Simple extraction logic - in a real implementation, this would be more sophisticated
        // Look for headings or bullet points that might indicate components
        var currentComponent: ComponentEntity? = null
        val defaultFeature = createDefaultFeature()

        for (line in lines) {
            // Look for headings that might indicate components
            if (line.startsWith("# ") || line.startsWith("## ") || line.startsWith("### ")) {
                val name = line.replace("#", "").trim()
                if (name.contains("component", ignoreCase = true) ||
                    name.contains("module", ignoreCase = true) ||
                    name.contains("service", ignoreCase = true)) {
                    currentComponent = ComponentEntity(
                        id = name.replace(" ", "-").lowercase(),
                        name = name,
                        description = "",
                        feature = defaultFeature
                    )
                    components.add(currentComponent)
                }
            } else if (line.trim().startsWith("- ") || line.trim().startsWith("* ")) {
                // Look for bullet points that might indicate components
                val name = line.replace("-", "").replace("*", "").trim()
                if (name.contains("component", ignoreCase = true) ||
                    name.contains("module", ignoreCase = true) ||
                    name.contains("service", ignoreCase = true)) {
                    val component = ComponentEntity(
                        id = name.replace(" ", "-").lowercase(),
                        name = name,
                        description = "",
                        feature = defaultFeature
                    )
                    components.add(component)
                    currentComponent = component
                }
            } else if (currentComponent != null && line.isNotBlank() && !line.startsWith("-") && !line.startsWith("*")) {
                // Add description to the current component
                val updatedComponent = currentComponent.copy(
                    description = currentComponent.description + " " + line.trim()
                )
                components[components.indexOf(currentComponent)] = updatedComponent
                currentComponent = updatedComponent
            }
        }

        return components
    }

    private fun createDefaultDomain(): DomainEntity {
        return DomainEntity(
            id = "default-domain",
            name = "Default Domain",
            description = "Default domain created when no domain information is available"
        )
    }

    private fun createDefaultFeature(): FeatureEntity {
        return FeatureEntity(
            id = "default-feature",
            name = "Default Feature",
            description = "Default feature created when no feature information is available",
            domain = createDefaultDomain()
        )
    }
}
