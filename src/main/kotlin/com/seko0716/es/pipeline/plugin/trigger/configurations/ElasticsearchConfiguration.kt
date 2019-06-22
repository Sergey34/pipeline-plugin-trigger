package com.seko0716.es.pipeline.plugin.trigger.configurations

import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class ElasticsearchConfiguration {

    /*@Bean
    fun client(): Client {
        return
    }

    @Bean
    fun elasticsearchTemplate(): ElasticsearchOperations {
        return ElasticsearchTemplate(client())
    }*/

    @Bean
    fun client(): RestClient {
        return RestClient.builder(
            HttpHost("localhost", 9200, "http"),
            HttpHost("localhost", 9201, "http")
        ).build()
    }


}