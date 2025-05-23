package com.multiplier.impacto.mcp

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

/**
 * Command-line client for demonstrating the McpQueryService.
 * This component will run when the application starts and demonstrate
 * how to query the MCP server for features and components.
 */
@Component
class McpQueryClient(private val mcpQueryService: McpQueryService) : CommandLineRunner {
    
    private val objectMapper = ObjectMapper().registerKotlinModule()
    
    override fun run(vararg args: String) {
        println("=== MCP Query Client Demo ===")
        
        // List available tools
        println("\n=== Available MCP Tools ===")
        val tools = mcpQueryService.listAvailableTools()
        tools.forEach { tool ->
            println("- ${tool.name}: ${tool.description}")
        }
        
        // Query for components
        println("\n=== Components ===")
        val components = mcpQueryService.getComponents()
        if (components.isEmpty()) {
            println("No components found")
        } else {
            components.forEach { component ->
                println("- ${component.name}: ${component.description}")
                println("  Source: ${component.source}")
            }
        }
        
        // Query for features
        println("\n=== Features ===")
        val features = mcpQueryService.getFeatures()
        if (features.isEmpty()) {
            println("No features found")
        } else {
            features.forEach { feature ->
                println("- ${feature.name}: ${feature.description}")
                println("  Source: ${feature.source}")
            }
        }
        
        // Search for a specific term
        val searchTerm = "GraphQL"
        println("\n=== Documentation Search for '$searchTerm' ===")
        val searchResults = mcpQueryService.searchDocumentation(searchTerm)
        if (searchResults.isEmpty()) {
            println("No results found for '$searchTerm'")
        } else {
            searchResults.forEach { result ->
                println("=== From ${result.toolName} ===")
                println(result.content.take(200) + "...")
                println()
            }
        }
        
        println("\n=== Demo Complete ===")
    }
}
