package com.seko0716.es.pipeline.plugin.trigger.configurations

import com.seko0716.es.pipeline.plugin.trigger.configurations.properties.ElasticsearchProperties
import org.elasticsearch.client.RestClient
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class ElasticsearchConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.data.elasticsearch")
    fun elasticsearchProperties(): ElasticsearchProperties {
        return ElasticsearchProperties()
    }

    @Bean
    fun client(elasticsearchProperties: ElasticsearchProperties): RestClient {
        return RestClient
            .builder(*elasticsearchProperties.getHttpHosts().toTypedArray())
            .build()
    }


}