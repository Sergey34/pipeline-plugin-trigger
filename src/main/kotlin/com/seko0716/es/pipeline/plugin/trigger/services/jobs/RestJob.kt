package com.seko0716.es.pipeline.plugin.trigger.services.jobs

import org.elasticsearch.client.Request
import org.elasticsearch.client.RestClient
import org.quartz.JobExecutionContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class RestJob : PipelineJobBean() {
    @Autowired
    lateinit var restClient: RestClient

    override fun action(context: JobExecutionContext) {
        val jobDataMap = context.mergedJobDataMap
        val request = Request(
            "POST",
            "/_pipeline/execute${jobDataMap["id"]}"
        )
        restClient.performRequest(request)
    }
}