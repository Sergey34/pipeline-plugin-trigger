package com.seko0716.es.pipeline.plugin.trigger.configurations.properties

import com.seko0716.es.pipeline.plugin.trigger.services.HostAndPort
import org.apache.http.HttpHost

class ElasticsearchProperties {
    lateinit var protocol: String
    lateinit var clusterName: String
    lateinit var clusterNodes: String

    fun getHttpHosts(): List<HttpHost> {
        return clusterNodes.split(", ?".toRegex())
            .asSequence()
            .map { HostAndPort.fromString(it) }
            .map { HttpHost(it.getHost(), it.getPort(), protocol) }
            .toList()

    }
}