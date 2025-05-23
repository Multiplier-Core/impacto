package com.multiplier.impacto.cli.config

import com.multiplier.impacto.cli.command.AnalyzeDomainCommand
import com.multiplier.impacto.cli.command.QueryAiServiceCommand
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import picocli.CommandLine
import picocli.CommandLine.IFactory

/**
 * Configuration for the CLI.
 */
@Configuration
class CliConfig {

    /**
     * Create the root command.
     */
    @Bean
    fun commandLine(
        analyzeDomainCommand: AnalyzeDomainCommand,
        queryAiServiceCommand: QueryAiServiceCommand,
        factory: IFactory
    ): CommandLine {
        val rootCommand = CommandLine(analyzeDomainCommand, factory)
        rootCommand.addSubcommand("query-ai", CommandLine(queryAiServiceCommand, factory))
        return rootCommand
    }
}
