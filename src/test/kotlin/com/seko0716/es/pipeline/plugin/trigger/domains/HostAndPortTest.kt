package com.seko0716.es.pipeline.plugin.trigger.domains

import org.junit.Assert
import org.junit.Test

internal class HostAndPortTest {
    @Test
    fun testIpV4() {
        val hosts = "localhost:9201"
        val fromString = HostAndPort.fromString(hosts)
        Assert.assertEquals("localhost", fromString.getHost())
        Assert.assertEquals(9201, fromString.getPort())
    }

    @Test
    fun testIpV6() {
        val hosts = "[2001:0db8:11a3:09d7:1f34:8a2e:07a0:765d]:9201"
        val fromString = HostAndPort.fromString(hosts)
        Assert.assertEquals("2001:0db8:11a3:09d7:1f34:8a2e:07a0:765d", fromString.getHost())
        Assert.assertEquals(9201, fromString.getPort())
    }
}