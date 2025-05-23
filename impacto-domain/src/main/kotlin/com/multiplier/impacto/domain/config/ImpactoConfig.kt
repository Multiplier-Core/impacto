package com.multiplier.impacto.domain.config

/**
 * Configuration for the Impacto tool.
 */
data class ImpactoConfig(
    val repositories: List<RepositoryConfig> = emptyList(),
    val domainMappings: List<DomainMapping> = emptyList(),
    val scoringConfig: ScoringConfig = ScoringConfig(),
    val botConfig: BotConfig = BotConfig(),
    val outputConfig: OutputConfig = OutputConfig()
)

/**
 * Configuration for a repository.
 */
data class RepositoryConfig(
    val name: String,
    val path: String,
    val type: RepositoryType = RepositoryType.GIT,
    val branch: String = "main",
    val testPaths: List<String> = emptyList(),
    val excludePaths: List<String> = emptyList()
)

/**
 * Enum representing the type of repository.
 */
enum class RepositoryType {
    GIT,
    SVN,
    MERCURIAL,
    LOCAL
}

/**
 * Mapping between code paths and domains.
 */
data class DomainMapping(
    val domainId: String,
    val pathPatterns: List<String> = emptyList(),
    val packagePatterns: List<String> = emptyList(),
    val featureMappings: List<FeatureMapping> = emptyList()
)

/**
 * Mapping between code paths and features.
 */
data class FeatureMapping(
    val featureId: String,
    val pathPatterns: List<String> = emptyList(),
    val packagePatterns: List<String> = emptyList(),
    val componentMappings: List<ComponentMapping> = emptyList()
)

/**
 * Mapping between code paths and components.
 */
data class ComponentMapping(
    val componentId: String,
    val pathPatterns: List<String> = emptyList(),
    val packagePatterns: List<String> = emptyList()
)

/**
 * Configuration for the scoring engine.
 */
data class ScoringConfig(
    val weights: Map<ScoringFactor, Double> = mapOf(
        ScoringFactor.IMPORT_OVERLAP to 0.3,
        ScoringFactor.PACKAGE_SIMILARITY to 0.2,
        ScoringFactor.ANNOTATION_MATCH to 0.2,
        ScoringFactor.HISTORICAL_DATA to 0.3
    ),
    val thresholds: Map<ConfidenceLevel, Double> = mapOf(
        ConfidenceLevel.HIGH to 0.8,
        ConfidenceLevel.MEDIUM to 0.5,
        ConfidenceLevel.LOW to 0.2
    )
)

/**
 * Enum representing a scoring factor.
 */
enum class ScoringFactor {
    IMPORT_OVERLAP,
    PACKAGE_SIMILARITY,
    ANNOTATION_MATCH,
    HISTORICAL_DATA
}

/**
 * Enum representing a confidence level.
 */
enum class ConfidenceLevel {
    HIGH,
    MEDIUM,
    LOW,
    NONE
}

/**
 * Configuration for the bot.
 */
data class BotConfig(
    val name: String = "Impacto",
    val language: String = "en",
    val templates: Map<String, String> = emptyMap()
)

/**
 * Configuration for the output.
 */
data class OutputConfig(
    val format: OutputFormat = OutputFormat.MARKDOWN,
    val outputPath: String = "impacto-report",
    val includeDetails: Boolean = true,
    val groupBy: GroupBy = GroupBy.DOMAIN
)

/**
 * Enum representing the output format.
 */
enum class OutputFormat {
    MARKDOWN,
    JSON,
    HTML,
    CONSOLE
}

/**
 * Enum representing how to group the output.
 */
enum class GroupBy {
    DOMAIN,
    FEATURE,
    COMPONENT,
    TEST
}
