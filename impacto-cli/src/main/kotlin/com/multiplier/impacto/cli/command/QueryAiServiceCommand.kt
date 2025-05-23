package com.multiplier.impacto.cli.command

import com.multiplier.impacto.domain.client.AiServiceClient
import org.springframework.stereotype.Component
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.util.concurrent.Callable

/**
 * Command to query the AI service for components and features.
 */
@Component
@Command(
    name = "queryAiService",
    description = ["Query the AI service for components and features"],
    mixinStandardHelpOptions = true
)
class QueryAiServiceCommand(
    private val aiServiceClient: AiServiceClient
) : Callable<Int> {
    
    @Option(names = ["--components"], description = ["List all components"])
    private var listComponents: Boolean = false
    
    @Option(names = ["--component-names"], description = ["List all component names"])
    private var listComponentNames: Boolean = false
    
    @Option(names = ["--component-id"], description = ["Get component by ID"])
    private var componentId: String? = null
    
    @Option(names = ["--features"], description = ["List all features"])
    private var listFeatures: Boolean = false
    
    @Option(names = ["--feature-names"], description = ["List all feature names"])
    private var listFeatureNames: Boolean = false
    
    @Option(names = ["--feature-id"], description = ["Get feature by ID"])
    private var featureId: String? = null
    
    override fun call(): Int {
        println("Querying AI service...")
        
        if (listComponents) {
            println("\n=== Components ===")
            val components = aiServiceClient.getAllComponents()
            if (components.isEmpty()) {
                println("No components found")
            } else {
                components.forEach { component ->
                    println("- ${component.name} (${component.id}): ${component.description}")
                    println("  Type: ${component.type}")
                }
            }
        }
        
        if (listComponentNames) {
            println("\n=== Component Names ===")
            val componentNames = aiServiceClient.getAllComponentNames()
            if (componentNames.isEmpty()) {
                println("No component names found")
            } else {
                componentNames.forEach { name ->
                    println("- $name")
                }
            }
        }
        
        if (componentId != null) {
            println("\n=== Component with ID: $componentId ===")
            val component = aiServiceClient.getComponentById(componentId!!)
            if (component == null) {
                println("No component found with ID: $componentId")
            } else {
                println("- ${component.name} (${component.id}): ${component.description}")
                println("  Type: ${component.type}")
            }
        }
        
        if (listFeatures) {
            println("\n=== Features ===")
            val features = aiServiceClient.getAllFeatures()
            if (features.isEmpty()) {
                println("No features found")
            } else {
                features.forEach { feature ->
                    println("- ${feature.name} (${feature.id}): ${feature.description}")
                    println("  Components: ${feature.components.joinToString(", ")}")
                }
            }
        }
        
        if (listFeatureNames) {
            println("\n=== Feature Names ===")
            val featureNames = aiServiceClient.getAllFeatureNames()
            if (featureNames.isEmpty()) {
                println("No feature names found")
            } else {
                featureNames.forEach { name ->
                    println("- $name")
                }
            }
        }
        
        if (featureId != null) {
            println("\n=== Feature with ID: $featureId ===")
            val feature = aiServiceClient.getFeatureById(featureId!!)
            if (feature == null) {
                println("No feature found with ID: $featureId")
            } else {
                println("- ${feature.name} (${feature.id}): ${feature.description}")
                println("  Components: ${feature.components.joinToString(", ")}")
            }
        }
        
        // If no options are specified, show all components and features
        if (!listComponents && !listComponentNames && componentId == null && 
            !listFeatures && !listFeatureNames && featureId == null) {
            println("\n=== Components ===")
            val components = aiServiceClient.getAllComponents()
            if (components.isEmpty()) {
                println("No components found")
            } else {
                components.forEach { component ->
                    println("- ${component.name} (${component.id}): ${component.description}")
                    println("  Type: ${component.type}")
                }
            }
            
            println("\n=== Features ===")
            val features = aiServiceClient.getAllFeatures()
            if (features.isEmpty()) {
                println("No features found")
            } else {
                features.forEach { feature ->
                    println("- ${feature.name} (${feature.id}): ${feature.description}")
                    println("  Components: ${feature.components.joinToString(", ")}")
                }
            }
        }
        
        return 0
    }
}
