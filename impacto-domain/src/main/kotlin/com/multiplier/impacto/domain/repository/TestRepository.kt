package com.multiplier.impacto.domain.repository

import com.multiplier.impacto.domain.model.TestEntity
import com.multiplier.impacto.domain.model.TestEntityType
import com.multiplier.impacto.domain.model.TestFramework
import com.multiplier.impacto.domain.model.TestType

/**
 * Repository for test entities.
 */
interface TestRepository {
    /**
     * Get all test entities.
     */
    fun getAllTestEntities(): List<TestEntity>
    
    /**
     * Get a test entity by ID.
     */
    fun getTestEntityById(id: String): TestEntity?
    
    /**
     * Get test entities by type.
     */
    fun getTestEntitiesByType(type: TestEntityType): List<TestEntity>
    
    /**
     * Get test entities by path pattern.
     */
    fun getTestEntitiesByPathPattern(pattern: String): List<TestEntity>
    
    /**
     * Get test entities by name pattern.
     */
    fun getTestEntitiesByNamePattern(pattern: String): List<TestEntity>
    
    /**
     * Get test entities by package name.
     */
    fun getTestEntitiesByPackage(packageName: String): List<TestEntity>
    
    /**
     * Get test entities by test framework.
     */
    fun getTestEntitiesByFramework(framework: TestFramework): List<TestEntity>
    
    /**
     * Get test entities by test type.
     */
    fun getTestEntitiesByTestType(testType: TestType): List<TestEntity>
    
    /**
     * Get test entities by annotation.
     */
    fun getTestEntitiesByAnnotation(annotation: String): List<TestEntity>
    
    /**
     * Get test entities by tag.
     */
    fun getTestEntitiesByTag(tag: String): List<TestEntity>
    
    /**
     * Save a test entity.
     */
    fun saveTestEntity(entity: TestEntity): TestEntity
    
    /**
     * Save multiple test entities.
     */
    fun saveTestEntities(entities: List<TestEntity>): List<TestEntity>
    
    /**
     * Delete a test entity.
     */
    fun deleteTestEntity(id: String)
    
    /**
     * Delete all test entities.
     */
    fun deleteAllTestEntities()
}
