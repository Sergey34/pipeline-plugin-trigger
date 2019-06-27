package com.seko0716.es.pipeline.plugin.trigger

import com.seko0716.es.pipeline.plugin.trigger.services.ScheduleService
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class PipelinePluginTriggerApplicationTests {

    @MockBean
    lateinit var scheduleService: ScheduleService

    @Test
    fun contextLoads() {
    }

}
