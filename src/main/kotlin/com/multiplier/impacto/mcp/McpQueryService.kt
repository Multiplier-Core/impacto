package com.multiplier.impacto.mcp

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.UUID
import java.util.concurrent.TimeUnit

/**
 * Service for querying the Multiplier MCP server for documentation, features, and components.
 * This service interacts with the MCP server via stdio protocol.
 */
@Service
class McpQueryService {
    private val logger = LoggerFactory.getLogger(McpQueryService::class.java)
    private val objectMapper = ObjectMapper()
    
    // Path to the MCP server executable
    private val mcpServerPath = "/Users/parijatkalita/git/knowledge-base/stdio-mcp-server/src/index.js"
    
    /**
     * Query for all available documentation tools in the MCP server
     * 
     * @return List of available documentation tools
     */
    fun listAvailableTools(): List<McpTool> {
        logger.info("Listing available MCP tools")
        
        val request = McpRequest(
            id = UUID.randomUUID().toString(),
            method = "tools/list",
            params = emptyMap()
        )
        
        val response = executeRequest(request)
        return parseToolsResponse(response)
    }
    
    /**
     * Query for all components from the MCP server
     * 
     * @return List of components or empty list if none found
     */
    fun getComponents(): List<McpComponent> {
        logger.info("Querying MCP for components")
        
        // First check if there's a specific tool for components
        val tools = listAvailableTools()
        val componentTool = tools.find { 
            it.name.contains("component", ignoreCase = true) || 
            it.description.contains("component", ignoreCase = true) 
        }
        
        if (componentTool != null) {
            logger.info("Found component tool: ${componentTool.name}")
            return queryComponentsWithTool(componentTool.name)
        }
        
        // If no specific tool, try to find components in documentation
        logger.info("No specific component tool found, searching in documentation")
        return searchComponentsInDocumentation()
    }
    
    /**
     * Query for all features from the MCP server
     * 
     * @return List of features or empty list if none found
     */
    fun getFeatures(): List<McpFeature> {
        logger.info("Querying MCP for features")
        
        // First check if there's a specific tool for features
        val tools = listAvailableTools()
        val featureTool = tools.find { 
            it.name.contains("feature", ignoreCase = true) || 
            it.description.contains("feature", ignoreCase = true) 
        }
        
        if (featureTool != null) {
            logger.info("Found feature tool: ${featureTool.name}")
            return queryFeaturesWithTool(featureTool.name)
        }
        
        // If no specific tool, try to find features in documentation
        logger.info("No specific feature tool found, searching in documentation")
        return searchFeaturesInDocumentation()
    }
    
    /**
     * Query for both features and components from the MCP server
     * 
     * @return Pair of features and components lists
     */
    fun getFeaturesAndComponents(): Pair<List<McpFeature>, List<McpComponent>> {
        return Pair(getFeatures(), getComponents())
    }
    
    /**
     * Search for a specific term in all documentation
     * 
     * @param searchTerm The term to search for
     * @return List of documentation entries containing the search term
     */
    fun searchDocumentation(searchTerm: String): List<McpDocumentationEntry> {
        logger.info("Searching documentation for: $searchTerm")
        
        val tools = listAvailableTools()
        val results = mutableListOf<McpDocumentationEntry>()
        
        // Try to use each documentation tool to find the search term
        for (tool in tools) {
            try {
                val request = McpRequest(
                    id = UUID.randomUUID().toString(),
                    method = "tools/call",
                    params = mapOf(
                        "name" to tool.name,
                        "arguments" to mapOf("query" to searchTerm)
                    )
                )
                
                val response = executeRequest(request)
                val content = extractContentFromResponse(response)
                
                if (content.isNotBlank() && !content.contains("Error:")) {
                    results.add(
                        McpDocumentationEntry(
                            toolName = tool.name,
                            content = content
                        )
                    )
                }
            } catch (e: Exception) {
                logger.warn("Error searching with tool ${tool.name}: ${e.message}")
            }
        }
        
        return results
    }
    
    // Private helper methods
    
    private fun queryComponentsWithTool(toolName: String): List<McpComponent> {
        val request = McpRequest(
            id = UUID.randomUUID().toString(),
            method = "tools/call",
            params = mapOf(
                "name" to toolName,
                "arguments" to emptyMap<String, Any>()
            )
        )
        
        val response = executeRequest(request)
        return parseComponentsResponse(response)
    }
    
    private fun queryFeaturesWithTool(toolName: String): List<McpFeature> {
        val request = McpRequest(
            id = UUID.randomUUID().toString(),
            method = "tools/call",
            params = mapOf(
                "name" to toolName,
                "arguments" to emptyMap<String, Any>()
            )
        )
        
        val response = executeRequest(request)
        return parseFeaturesResponse(response)
    }
    
    private fun searchComponentsInDocumentation(): List<McpComponent> {
        val docEntries = searchDocumentation("component")
        val components = mutableListOf<McpComponent>()
        
        // Extract component information from documentation
        for (entry in docEntries) {
            // Simple extraction logic - in a real implementation, this would be more sophisticated
            val lines = entry.content.split("\n")
            for (line in lines) {
                if (line.contains("component", ignoreCase = true) && 
                    !line.startsWith("#") && !line.startsWith("-")) {
                    components.add(
                        McpComponent(
                            name = extractName(line),
                            description = line,
                            source = entry.toolName
                        )
                    )
                }
            }
        }
        
        return components
    }
    
    private fun searchFeaturesInDocumentation(): List<McpFeature> {
        val docEntries = searchDocumentation("feature")
        val features = mutableListOf<McpFeature>()
        
        // Extract feature information from documentation
        for (entry in docEntries) {
            // Simple extraction logic - in a real implementation, this would be more sophisticated
            val lines = entry.content.split("\n")
            for (line in lines) {
                if (line.contains("feature", ignoreCase = true) && 
                    !line.startsWith("#") && !line.startsWith("-")) {
                    features.add(
                        McpFeature(
                            name = extractName(line),
                            description = line,
                            source = entry.toolName
                        )
                    )
                }
            }
        }
        
        return features
    }
    
    private fun extractName(line: String): String {
        // Simple extraction logic - in a real implementation, this would be more sophisticated
        val words = line.split(" ", ":", "-", ".")
        for (i in 0 until words.size - 1) {
            if (words[i].equals("component", ignoreCase = true) || 
                words[i].equals("feature", ignoreCase = true)) {
                return words.getOrNull(i + 1)?.trim() ?: "Unknown"
            }
        }
        return "Unknown"
    }
    
    private fun executeRequest(request: McpRequest): String {
        logger.debug("Executing MCP request: $request")
        
        val process = ProcessBuilder("node", mcpServerPath)
            .redirectErrorStream(true)
            .start()
        
        val writer = BufferedWriter(OutputStreamWriter(process.outputStream))
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        
        // Write the request to the process
        val requestJson = objectMapper.writeValueAsString(request)
        writer.write(requestJson)
        writer.newLine()
        writer.flush()
        
        // Read the response
        val response = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            response.append(line).append("\n")
        }
        
        // Wait for the process to complete
        if (!process.waitFor(30, TimeUnit.SECONDS)) {
            process.destroy()
            throw RuntimeException("MCP request timed out")
        }
        
        if (process.exitValue() != 0) {
            throw RuntimeException("MCP process exited with code ${process.exitValue()}")
        }
        
        return response.toString()
    }
    
    private fun parseToolsResponse(response: String): List<McpTool> {
        try {
            val jsonNode = objectMapper.readTree(response)
            val toolsNode = jsonNode.path("result").path("tools")
            
            if (toolsNode.isArray) {
                return objectMapper.readValue(toolsNode.toString())
            }
        } catch (e: Exception) {
            logger.error("Error parsing tools response: ${e.message}")
        }
        
        return emptyList()
    }
    
    private fun parseComponentsResponse(response: String): List<McpComponent> {
        try {
            val jsonNode = objectMapper.readTree(response)
            val resultNode = jsonNode.path("result")
            
            // The structure depends on the actual response format
            // This is a simplified example
            if (resultNode.isArray) {
                return objectMapper.readValue(resultNode.toString())
            } else {
                val content = extractContentFromResponse(response)
                return parseComponentsFromText(content)
            }
        } catch (e: Exception) {
            logger.error("Error parsing components response: ${e.message}")
        }
        
        return emptyList()
    }
    
    private fun parseFeaturesResponse(response: String): List<McpFeature> {
        try {
            val jsonNode = objectMapper.readTree(response)
            val resultNode = jsonNode.path("result")
            
            // The structure depends on the actual response format
            // This is a simplified example
            if (resultNode.isArray) {
                return objectMapper.readValue(resultNode.toString())
            } else {
                val content = extractContentFromResponse(response)
                return parseFeaturesFromText(content)
            }
        } catch (e: Exception) {
            logger.error("Error parsing features response: ${e.message}")
        }
        
        return emptyList()
    }
    
    private fun extractContentFromResponse(response: String): String {
        try {
            val jsonNode = objectMapper.readTree(response)
            return jsonNode.path("result").asText("")
        } catch (e: Exception) {
            logger.error("Error extracting content from response: ${e.message}")
        }
        
        return ""
    }
    
    private fun parseComponentsFromText(text: String): List<McpComponent> {
        // Simple parsing logic - in a real implementation, this would be more sophisticated
        val components = mutableListOf<McpComponent>()
        val lines = text.split("\n")
        
        for (line in lines) {
            if (line.contains("component", ignoreCase = true)) {
                components.add(
                    McpComponent(
                        name = extractName(line),
                        description = line,
                        source = "text-extraction"
                    )
                )
            }
        }
        
        return components
    }
    
    private fun parseFeaturesFromText(text: String): List<McpFeature> {
        // Simple parsing logic - in a real implementation, this would be more sophisticated
        val features = mutableListOf<McpFeature>()
        val lines = text.split("\n")
        
        for (line in lines) {
            if (line.contains("feature", ignoreCase = true)) {
                features.add(
                    McpFeature(
                        name = extractName(line),
                        description = line,
                        source = "text-extraction"
                    )
                )
            }
        }
        
        return features
    }
}

// Data classes

data class McpRequest(
    val id: String,
    val method: String,
    val params: Map<String, Any>
)

data class McpTool(
    val name: String,
    val description: String,
    val inputSchema: JsonNode? = null
)

data class McpComponent(
    val name: String,
    val description: String,
    val source: String
)

data class McpFeature(
    val name: String,
    val description: String,
    val source: String
)

data class McpDocumentationEntry(
    val toolName: String,
    val content: String
)
