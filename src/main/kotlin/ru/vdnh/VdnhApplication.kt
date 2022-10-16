package ru.vdnh

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VdnhApplication

fun main(args: Array<String>) {
    runApplication<VdnhApplication>(*args)
}
