package com.seko0716.es.pipeline.plugin.trigger.configurations.properties

import org.apache.http.HttpHost
import org.junit.Assert

internal class ElasticsearchPropertiesTest {

    @org.junit.Test
    fun testSingleIpV4() {
        val hosts = "localhost:9201"
        val properties = ElasticsearchProperties().apply {
            protocol = "http"
            clusterNodes = hosts
        }
        val actualResult = properties.getHttpHosts()
        val expectedResult = listOf(HttpHost("localhost", 9201, "http"))
        Assert.assertEquals(expectedResult, actualResult)

    }

    @org.junit.Test
    fun testSingleIpV6() {
        val hosts = "[2001:0db8:11a3:09d7:1f34:8a2e:07a0:765d]:9201"
        val properties = ElasticsearchProperties().apply {
            protocol = "http"
            clusterNodes = hosts
        }
        val actualResult = properties.getHttpHosts()
        val expectedResult = listOf(HttpHost("2001:0db8:11a3:09d7:1f34:8a2e:07a0:765d", 9201, "http"))
        Assert.assertEquals(expectedResult, actualResult)

    }

    @org.junit.Test
    fun testMultipleIpV4() {
        val hosts = "localhost:9201,localhost:9202, localhost:9203"
        val properties = ElasticsearchProperties().apply {
            protocol = "http"
            clusterNodes = hosts
        }
        val actualResult = properties.getHttpHosts()
        val expectedResult = listOf(
            HttpHost("localhost", 9201, "http"),
            HttpHost("localhost", 9202, "http"),
            HttpHost("localhost", 9203, "http")
        )
        Assert.assertEquals(expectedResult, actualResult)

    }

    @org.junit.Test
    fun testMultipleIpV6() {
        val hosts =
            "[2001:0db8:11a3:09d7:1f34:8a2e:07a0:765d]:9201,[2001:0db8:11a3:09d7:1f34:8a2e:07a0:765d]:9202, [2001:0db8:11a3:09d7:1f34:8a2e:07a0:765d]:9203"
        val properties = ElasticsearchProperties().apply {
            protocol = "http"
            clusterNodes = hosts
        }
        val actualResult = properties.getHttpHosts()
        val expectedResult = listOf(
            HttpHost("2001:0db8:11a3:09d7:1f34:8a2e:07a0:765d", 9201, "http"),
            HttpHost("2001:0db8:11a3:09d7:1f34:8a2e:07a0:765d", 9202, "http"),
            HttpHost("2001:0db8:11a3:09d7:1f34:8a2e:07a0:765d", 9203, "http")
        )
        Assert.assertEquals(expectedResult, actualResult)

    }
}