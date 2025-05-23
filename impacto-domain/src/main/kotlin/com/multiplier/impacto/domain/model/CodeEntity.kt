package com.multiplier.impacto.domain.model

/**
 * Represents a code entity in the codebase.
 * This could be a class, method, field, etc.
 */
sealed class CodeEntity {
    abstract val id: String
    abstract val name: String
    abstract val path: String
    abstract val type: CodeEntityType

    /**
     * Represents a class or interface in the codebase.
     */
    data class ClassEntity(
        override val id: String,
        override val name: String,
        override val path: String,
        val packageName: String,
        val imports: List<String> = emptyList(),
        val methods: List<MethodEntity> = emptyList(),
        val fields: List<FieldEntity> = emptyList(),
        val annotations: List<String> = emptyList(),
        val isInterface: Boolean = false,
        val isAbstract: Boolean = false,
        val superClass: String? = null,
        val interfaces: List<String> = emptyList()
    ) : CodeEntity() {
        override val type: CodeEntityType = CodeEntityType.CLASS
    }

    /**
     * Represents a method in a class.
     */
    data class MethodEntity(
        override val id: String,
        override val name: String,
        override val path: String,
        val signature: String,
        val annotations: List<String> = emptyList(),
        val parameters: List<ParameterEntity> = emptyList(),
        val returnType: String,
        val isPublic: Boolean = true,
        val isStatic: Boolean = false,
        val isAbstract: Boolean = false
    ) : CodeEntity() {
        override val type: CodeEntityType = CodeEntityType.METHOD
    }

    /**
     * Represents a field in a class.
     */
    data class FieldEntity(
        override val id: String,
        override val name: String,
        override val path: String,
        val fieldType: String,
        val annotations: List<String> = emptyList(),
        val isPublic: Boolean = false,
        val isStatic: Boolean = false,
        val isFinal: Boolean = false
    ) : CodeEntity() {
        override val type: CodeEntityType = CodeEntityType.FIELD
    }

    /**
     * Represents a parameter in a method.
     */
    data class ParameterEntity(
        val name: String,
        val type: String,
        val annotations: List<String> = emptyList()
    )
}

/**
 * Enum representing the type of code entity.
 */
enum class CodeEntityType {
    CLASS,
    METHOD,
    FIELD
}
