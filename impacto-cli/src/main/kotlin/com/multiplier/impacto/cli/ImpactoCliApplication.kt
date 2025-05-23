package com.multiplier.impacto.cli

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["com.multiplier.impacto"])
class ImpactoCliApplication

fun main(args: Array<String>) {
    SpringApplication.run(ImpactoCliApplication::class.java, *args)
}
