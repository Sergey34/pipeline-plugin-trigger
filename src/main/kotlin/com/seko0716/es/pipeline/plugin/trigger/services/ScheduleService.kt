package com.seko0716.es.pipeline.plugin.trigger.services

import com.seko0716.es.pipeline.plugin.trigger.repositories.ElasticsearchRepository
import com.seko0716.es.pipeline.plugin.trigger.services.jobs.RestJob
import com.seko0716.es.pipeline.plugin.trigger.services.node.NodeStateService
import org.quartz.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service


@Service
class ScheduleService @Autowired constructor(
    val repository: ElasticsearchRepository,
    @Value("\${spring.application.schedule.group}") val group: String,
    val scheduler: Scheduler,
    val nodeStateService: NodeStateService
) {

    @EventListener(ContextRefreshedEvent::class)
    fun initScheduling() {
        repository.findAllPipelineInfo(nodeStateService.getCurrentNodeId(), nodeStateService.getTotalNodes())
            .forEach { scheduler.scheduleJob(buildJobDetail(it), buildTrigger(it)) }

    }

    private fun buildTrigger(it: Map<String, Any>): CronTrigger {
        return TriggerBuilder
            .newTrigger()
            .withIdentity(it["title"] as String, group)
            .withSchedule(
                CronScheduleBuilder.cronSchedule(it["trigger"] as String)
            )
            .build()
    }

    private fun buildJobDetail(pipelineInfo: Map<String, Any>): JobDetail {
        val jobDataMap = JobDataMap()
        jobDataMap["id"] = pipelineInfo["id"] as String

        return JobBuilder.newJob(RestJob::class.java)
            .withIdentity(pipelineInfo["title"] as String, group)
            .withDescription(pipelineInfo["description"] as String)
            .usingJobData(jobDataMap)
            .storeDurably()
            .build()
    }

}