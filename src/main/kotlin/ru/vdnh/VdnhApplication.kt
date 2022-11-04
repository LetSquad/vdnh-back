package ru.vdnh

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class VdnhApplication

fun main(args: Array<String>) {
    runApplication<VdnhApplication>(*args)
}
