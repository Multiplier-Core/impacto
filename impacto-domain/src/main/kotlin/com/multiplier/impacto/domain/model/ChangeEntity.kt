package com.multiplier.impacto.domain.model

import java.time.Instant

/**
 * Represents a change to the codebase.
 */
data class ChangeEntity(
    val id: String,
    val type: ChangeType,
    val path: String,
    val oldPath: String? = null,
    val timestamp: Instant = Instant.now(),
    val author: String? = null,
    val commitId: String? = null,
    val commitMessage: String? = null,
    val diff: String? = null,
    val changedEntities: List<ChangedEntity> = emptyList()
)

/**
 * Enum representing the type of change.
 */
enum class ChangeType {
    ADD,
    MODIFY,
    DELETE,
    RENAME
}

/**
 * Represents a changed entity within a file.
 */
data class ChangedEntity(
    val entityId: String,
    val entityType: ChangedEntityType,
    val changeType: ChangeType,
    val lineStart: Int,
    val lineEnd: Int,
    val oldLineStart: Int? = null,
    val oldLineEnd: Int? = null,
    val content: String? = null,
    val oldContent: String? = null
)

/**
 * Enum representing the type of changed entity.
 */
enum class ChangedEntityType {
    CLASS,
    METHOD,
    FIELD,
    IMPORT,
    ANNOTATION,
    OTHER
}
