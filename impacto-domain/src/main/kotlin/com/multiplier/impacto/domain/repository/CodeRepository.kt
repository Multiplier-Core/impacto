package com.multiplier.impacto.domain.repository

import com.multiplier.impacto.domain.model.CodeEntity
import com.multiplier.impacto.domain.model.CodeEntityType

/**
 * Repository for code entities.
 */
interface CodeRepository {
    /**
     * Get all code entities.
     */
    fun getAllCodeEntities(): List<CodeEntity>
    
    /**
     * Get a code entity by ID.
     */
    fun getCodeEntityById(id: String): CodeEntity?
    
    /**
     * Get code entities by type.
     */
    fun getCodeEntitiesByType(type: CodeEntityType): List<CodeEntity>
    
    /**
     * Get code entities by path pattern.
     */
    fun getCodeEntitiesByPathPattern(pattern: String): List<CodeEntity>
    
    /**
     * Get code entities by name pattern.
     */
    fun getCodeEntitiesByNamePattern(pattern: String): List<CodeEntity>
    
    /**
     * Get code entities by package name.
     */
    fun getCodeEntitiesByPackage(packageName: String): List<CodeEntity>
    
    /**
     * Get code entities by import.
     */
    fun getCodeEntitiesByImport(importName: String): List<CodeEntity>
    
    /**
     * Get code entities by annotation.
     */
    fun getCodeEntitiesByAnnotation(annotation: String): List<CodeEntity>
    
    /**
     * Save a code entity.
     */
    fun saveCodeEntity(entity: CodeEntity): CodeEntity
    
    /**
     * Save multiple code entities.
     */
    fun saveCodeEntities(entities: List<CodeEntity>): List<CodeEntity>
    
    /**
     * Delete a code entity.
     */
    fun deleteCodeEntity(id: String)
    
    /**
     * Delete all code entities.
     */
    fun deleteAllCodeEntities()
}
