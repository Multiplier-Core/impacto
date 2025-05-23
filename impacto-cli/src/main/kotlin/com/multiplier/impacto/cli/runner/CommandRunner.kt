package com.multiplier.impacto.cli.runner

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import picocli.CommandLine

/**
 * Runner to execute the command.
 */
@Component
class CommandRunner(private val commandLine: CommandLine) : CommandLineRunner {
    
    override fun run(vararg args: String) {
        val exitCode = commandLine.execute(*args)
        if (exitCode != 0) {
            System.exit(exitCode)
        }
    }
}
