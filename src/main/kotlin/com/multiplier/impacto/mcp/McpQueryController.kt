package com.multiplier.impacto.mcp

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * REST controller for querying the Multiplier MCP server.
 * Provides endpoints to access documentation, features, and components.
 */
@RestController
@RequestMapping("/api/mcp")
class McpQueryController(private val mcpQueryService: McpQueryService) {

    /**
     * List all available MCP tools
     */
    @GetMapping("/tools")
    fun listTools(): ResponseEntity<List<McpTool>> {
        return ResponseEntity.ok(mcpQueryService.listAvailableTools())
    }

    /**
     * Get all components from the MCP server
     */
    @GetMapping("/components")
    fun getComponents(): ResponseEntity<List<McpComponent>> {
        return ResponseEntity.ok(mcpQueryService.getComponents())
    }

    /**
     * Get all features from the MCP server
     */
    @GetMapping("/features")
    fun getFeatures(): ResponseEntity<List<McpFeature>> {
        return ResponseEntity.ok(mcpQueryService.getFeatures())
    }

    /**
     * Get both features and components from the MCP server
     */
    @GetMapping("/features-and-components")
    fun getFeaturesAndComponents(): ResponseEntity<Map<String, Any>> {
        val (features, components) = mcpQueryService.getFeaturesAndComponents()
        return ResponseEntity.ok(
            mapOf(
                "features" to features,
                "components" to components
            )
        )
    }

    /**
     * Search documentation for a specific term
     */
    @GetMapping("/search")
    fun searchDocumentation(@RequestParam term: String): ResponseEntity<List<McpDocumentationEntry>> {
        return ResponseEntity.ok(mcpQueryService.searchDocumentation(term))
    }
}
