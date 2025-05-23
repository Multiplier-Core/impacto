package com.multiplier.impacto.domain.model

/**
 * Represents a test entity in the codebase.
 * This could be a test class or a test method.
 */
sealed class TestEntity {
    abstract val id: String
    abstract val name: String
    abstract val path: String
    abstract val type: TestEntityType
    
    /**
     * Represents a test class in the codebase.
     */
    data class TestClassEntity(
        override val id: String,
        override val name: String,
        override val path: String,
        val packageName: String,
        val imports: List<String> = emptyList(),
        val methods: List<TestMethodEntity> = emptyList(),
        val annotations: List<String> = emptyList(),
        val testFramework: TestFramework = TestFramework.JUNIT
    ) : TestEntity() {
        override val type: TestEntityType = TestEntityType.TEST_CLASS
    }
    
    /**
     * Represents a test method in a test class.
     */
    data class TestMethodEntity(
        override val id: String,
        override val name: String,
        override val path: String,
        val signature: String,
        val annotations: List<String> = emptyList(),
        val testType: TestType = TestType.UNIT,
        val testTags: List<String> = emptyList()
    ) : TestEntity() {
        override val type: TestEntityType = TestEntityType.TEST_METHOD
    }
}

/**
 * Enum representing the type of test entity.
 */
enum class TestEntityType {
    TEST_CLASS,
    TEST_METHOD
}

/**
 * Enum representing the test framework used.
 */
enum class TestFramework {
    JUNIT,
    TESTNG,
    SPOCK,
    CUCUMBER,
    OTHER
}

/**
 * Enum representing the type of test.
 */
enum class TestType {
    UNIT,
    INTEGRATION,
    E2E,
    PERFORMANCE,
    OTHER
}
