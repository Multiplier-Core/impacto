package com.multiplier.impacto.domain.model

/**
 * Represents a business domain entity.
 * This is used to categorize code and tests into business domains.
 */
data class DomainEntity(
    val id: String,
    val name: String,
    val description: String,
    val parentDomain: DomainEntity? = null,
    val subDomains: List<DomainEntity> = emptyList(),
    val features: List<FeatureEntity> = emptyList(),
    val tags: List<String> = emptyList()
)

/**
 * Represents a feature within a business domain.
 */
data class FeatureEntity(
    val id: String,
    val name: String,
    val description: String,
    val domain: DomainEntity,
    val components: List<ComponentEntity> = emptyList(),
    val tags: List<String> = emptyList()
)

/**
 * Represents a component within a feature.
 */
data class ComponentEntity(
    val id: String,
    val name: String,
    val description: String,
    val feature: FeatureEntity,
    val codeEntities: List<CodeEntityReference> = emptyList(),
    val testEntities: List<TestEntityReference> = emptyList(),
    val tags: List<String> = emptyList()
)

/**
 * Reference to a code entity.
 */
data class CodeEntityReference(
    val entityId: String,
    val entityType: CodeEntityType,
    val confidence: Double = 1.0
)

/**
 * Reference to a test entity.
 */
data class TestEntityReference(
    val entityId: String,
    val entityType: TestEntityType,
    val confidence: Double = 1.0
)
