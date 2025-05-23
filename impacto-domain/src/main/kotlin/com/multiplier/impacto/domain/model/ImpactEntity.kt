package com.multiplier.impacto.domain.model

/**
 * Represents the impact of a change on the codebase.
 */
data class ImpactEntity(
    val id: String,
    val change: ChangeEntity,
    val impactedEntities: List<ImpactedEntity> = emptyList(),
    val impactedDomains: List<ImpactedDomain> = emptyList(),
    val impactedFeatures: List<ImpactedFeature> = emptyList(),
    val impactedComponents: List<ImpactedComponent> = emptyList()
)

/**
 * Represents an entity impacted by a change.
 */
data class ImpactedEntity(
    val entityId: String,
    val entityType: ImpactedEntityType,
    val impactType: ImpactType,
    val confidence: Double,
    val reasons: List<ImpactReason> = emptyList()
)

/**
 * Enum representing the type of impacted entity.
 */
enum class ImpactedEntityType {
    CODE,
    TEST
}

/**
 * Enum representing the type of impact.
 */
enum class ImpactType {
    DIRECT,      // The entity was directly changed
    DEPENDENCY,  // The entity depends on a changed entity
    REFERENCE,   // The entity is referenced by a changed entity
    SEMANTIC     // The entity is semantically related to a changed entity
}

/**
 * Represents a reason for an impact.
 */
data class ImpactReason(
    val type: ImpactReasonType,
    val description: String,
    val confidence: Double
)

/**
 * Enum representing the type of impact reason.
 */
enum class ImpactReasonType {
    IMPORT_DEPENDENCY,
    METHOD_CALL,
    INHERITANCE,
    ANNOTATION,
    PACKAGE_PROXIMITY,
    SEMANTIC_SIMILARITY,
    HISTORICAL_CORRELATION
}

/**
 * Represents a domain impacted by a change.
 */
data class ImpactedDomain(
    val domain: DomainEntity,
    val confidence: Double,
    val impactedEntities: List<ImpactedEntity> = emptyList()
)

/**
 * Represents a feature impacted by a change.
 */
data class ImpactedFeature(
    val feature: FeatureEntity,
    val confidence: Double,
    val impactedEntities: List<ImpactedEntity> = emptyList()
)

/**
 * Represents a component impacted by a change.
 */
data class ImpactedComponent(
    val component: ComponentEntity,
    val confidence: Double,
    val impactedEntities: List<ImpactedEntity> = emptyList()
)
