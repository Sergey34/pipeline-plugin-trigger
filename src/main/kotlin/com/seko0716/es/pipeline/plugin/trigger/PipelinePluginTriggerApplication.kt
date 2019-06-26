package com.seko0716.es.pipeline.plugin.trigger

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties
class PipelinePluginTriggerApplication

fun main(args: Array<String>) {
    runApplication<PipelinePluginTriggerApplication>(*args)
}
