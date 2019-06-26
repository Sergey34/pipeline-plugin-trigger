package com.seko0716.es.pipeline.plugin.trigger.services.jobs

import org.quartz.JobExecutionContext
import org.quartz.Scheduler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.quartz.QuartzJobBean

abstract class PipelineJobBean : QuartzJobBean() {
    @Autowired
    private lateinit var scheduler: Scheduler

    override fun executeInternal(context: JobExecutionContext) {
        scheduler.pauseJob(context.jobDetail.key)
        try {
            action(context)
        } finally {
            scheduler.resumeJob(context.jobDetail.key)
        }
    }

    protected abstract fun action(context: JobExecutionContext)
}