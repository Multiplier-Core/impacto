package com.multiplier.impacto.cli.command

import com.multiplier.impacto.domain.service.DomainService
import com.multiplier.impacto.domain.service.McpDomainService
import org.springframework.stereotype.Component
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.util.concurrent.Callable

/**
 * Command to analyze the domain context.
 */
@Component
@Command(
    name = "analyzeDomain",
    description = ["Analyze the domain context"],
    mixinStandardHelpOptions = true
)
class AnalyzeDomainCommand(
    private val domainService: DomainService,
    private val mcpDomainService: McpDomainService
) : Callable<Int> {
    
    @Option(names = ["--output-file"], description = ["Output file for the domain analysis report"])
    private var outputFile: String = "domain-analysis-report.md"
    
    @Option(names = ["--search-term"], description = ["Term to search for in the documentation"])
    private var searchTerm: String? = null
    
    override fun call(): Int {
        println("Analyzing domain context...")
        
        // Get all domains
        val domains = domainService.getAllDomains()
        println("Found ${domains.size} domains")
        
        // Get all features
        val features = domainService.getAllFeatures()
        println("Found ${features.size} features")
        
        // Get all components
        val components = domainService.getAllComponents()
        println("Found ${components.size} components")
        
        // Search for documentation if a search term is provided
        if (searchTerm != null) {
            println("Searching for documentation related to '$searchTerm'")
            val domainDocs = mcpDomainService.searchDomainDocumentation(searchTerm!!)
            val featureDocs = mcpDomainService.searchFeatureDocumentation(searchTerm!!)
            val componentDocs = mcpDomainService.searchComponentDocumentation(searchTerm!!)
            
            // Generate a report
            generateReport(domains, features, components, domainDocs, featureDocs, componentDocs)
        } else {
            // Generate a report without documentation
            generateReport(domains, features, components)
        }
        
        println("Domain analysis complete. Report saved to $outputFile")
        
        return 0
    }
    
    private fun generateReport(
        domains: List<com.multiplier.impacto.domain.model.DomainEntity>,
        features: List<com.multiplier.impacto.domain.model.FeatureEntity>,
        components: List<com.multiplier.impacto.domain.model.ComponentEntity>,
        domainDocs: String = "",
        featureDocs: String = "",
        componentDocs: String = ""
    ) {
        val report = StringBuilder()
        
        // Add header
        report.appendLine("# Domain Analysis Report")
        report.appendLine()
        report.appendLine("Generated on: ${java.time.LocalDateTime.now()}")
        report.appendLine()
        
        // Add domains section
        report.appendLine("## Domains")
        report.appendLine()
        
        if (domains.isEmpty()) {
            report.appendLine("No domains found.")
        } else {
            domains.forEach { domain ->
                report.appendLine("### ${domain.name}")
                report.appendLine()
                report.appendLine(domain.description)
                report.appendLine()
                
                // Add features for this domain
                val domainFeatures = features.filter { it.domain.id == domain.id }
                if (domainFeatures.isNotEmpty()) {
                    report.appendLine("#### Features")
                    report.appendLine()
                    domainFeatures.forEach { feature ->
                        report.appendLine("- **${feature.name}**: ${feature.description}")
                    }
                    report.appendLine()
                }
            }
        }
        
        // Add features section
        report.appendLine("## Features")
        report.appendLine()
        
        if (features.isEmpty()) {
            report.appendLine("No features found.")
        } else {
            features.forEach { feature ->
                report.appendLine("### ${feature.name}")
                report.appendLine()
                report.appendLine(feature.description)
                report.appendLine()
                report.appendLine("Domain: ${feature.domain.name}")
                report.appendLine()
                
                // Add components for this feature
                val featureComponents = components.filter { it.feature.id == feature.id }
                if (featureComponents.isNotEmpty()) {
                    report.appendLine("#### Components")
                    report.appendLine()
                    featureComponents.forEach { component ->
                        report.appendLine("- **${component.name}**: ${component.description}")
                    }
                    report.appendLine()
                }
            }
        }
        
        // Add components section
        report.appendLine("## Components")
        report.appendLine()
        
        if (components.isEmpty()) {
            report.appendLine("No components found.")
        } else {
            components.forEach { component ->
                report.appendLine("### ${component.name}")
                report.appendLine()
                report.appendLine(component.description)
                report.appendLine()
                report.appendLine("Feature: ${component.feature.name}")
                report.appendLine("Domain: ${component.feature.domain.name}")
                report.appendLine()
                
                // Add code entities for this component
                if (component.codeEntities.isNotEmpty()) {
                    report.appendLine("#### Code Entities")
                    report.appendLine()
                    component.codeEntities.forEach { codeEntity ->
                        report.appendLine("- **${codeEntity.entityId}** (${codeEntity.entityType}): Confidence ${codeEntity.confidence}")
                    }
                    report.appendLine()
                }
                
                // Add test entities for this component
                if (component.testEntities.isNotEmpty()) {
                    report.appendLine("#### Test Entities")
                    report.appendLine()
                    component.testEntities.forEach { testEntity ->
                        report.appendLine("- **${testEntity.entityId}** (${testEntity.entityType}): Confidence ${testEntity.confidence}")
                    }
                    report.appendLine()
                }
            }
        }
        
        // Add documentation sections if available
        if (domainDocs.isNotBlank()) {
            report.appendLine("## Domain Documentation")
            report.appendLine()
            report.appendLine(domainDocs)
            report.appendLine()
        }
        
        if (featureDocs.isNotBlank()) {
            report.appendLine("## Feature Documentation")
            report.appendLine()
            report.appendLine(featureDocs)
            report.appendLine()
        }
        
        if (componentDocs.isNotBlank()) {
            report.appendLine("## Component Documentation")
            report.appendLine()
            report.appendLine(componentDocs)
            report.appendLine()
        }
        
        // Write the report to the output file
        java.io.File(outputFile).writeText(report.toString())
    }
}
