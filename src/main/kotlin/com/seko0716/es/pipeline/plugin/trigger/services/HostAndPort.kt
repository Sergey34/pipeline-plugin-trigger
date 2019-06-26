package com.seko0716.es.pipeline.plugin.trigger.services

import org.springframework.util.StringUtils
import java.io.Serializable
import java.util.*

class HostAndPort private constructor(
    private val host: String,
    private val port: Int,
    private val hasBracketlessColons: Boolean
) : Serializable {

    fun hasPort(): Boolean {
        return this.port >= 0
    }

    fun getPort(): Int {
        checkState(this.hasPort())
        return this.port
    }

    fun getHost(): String {
        return this.host
    }

    fun getPortOrDefault(defaultPort: Int): Int {
        return if (this.hasPort()) this.port else defaultPort
    }

    fun withDefaultPort(defaultPort: Int): HostAndPort {
        checkState(isValidPort(defaultPort))
        return if (this.hasPort()) this else HostAndPort(this.host, defaultPort, this.hasBracketlessColons)
    }

    fun requireBracketsForIPv6(): HostAndPort {
        checkArgument(!this.hasBracketlessColons, "Possible bracketless IPv6 literal: %s", this.host)
        return this
    }

    override fun equals(other: Any?): Boolean {
        return if (this === other) {
            true
        } else if (other != null && this.javaClass == other.javaClass) {
            val that = other as HostAndPort?
            this.port == that!!.port && this.host == that.host
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return Objects.hash(this.host, this.port, this.hasBracketlessColons)
    }

    override fun toString(): String {
        val builder = StringBuilder(this.host.length + 8)
        if (this.host.indexOf(':') >= 0) {
            builder.append('[').append(this.host).append(']')
        } else {
            builder.append(this.host)
        }

        if (this.hasPort()) {
            builder.append(':').append(this.port)
        }

        return builder.toString()
    }

    companion object {
        private const val serialVersionUID = 0L

        private fun checkState(state: Boolean) {
            if (!state) {
                throw IllegalStateException()
            }
        }

        fun fromParts(host: String, port: Int): HostAndPort {
            checkArgument(isValidPort(port), "Port out of range: %s", port)
            val parsedHost = fromString(host)
            checkArgument(!parsedHost.hasPort(), "Host has a port: %s", host)
            return HostAndPort(parsedHost.host, port, parsedHost.hasBracketlessColons)
        }

        private fun checkArgument(state: Boolean, message: String, value: Any) {
            if (!state) {
                throw IllegalStateException(String.format(message, value))
            }
        }

        fun fromHost(host: String): HostAndPort {
            val parsedHost = fromString(host)
            checkArgument(!parsedHost.hasPort(), "Host has a port: %s", host)
            return parsedHost
        }

        fun fromString(hostPortString: String): HostAndPort {
            checkNotNull(hostPortString)
            var portString: String? = null
            var hasBracketlessColons = false
            val host: String
            var port: Int
            if (hostPortString.startsWith("[")) {
                val hostAndPort = getHostAndPortFromBracketedHost(hostPortString)
                host = hostAndPort[0]
                portString = hostAndPort[1]
            } else {
                port = hostPortString.indexOf(':')
                if (port >= 0 && hostPortString.indexOf(':', port + 1) == -1) {
                    host = hostPortString.substring(0, port)
                    portString = hostPortString.substring(port + 1)
                } else {
                    host = hostPortString
                    hasBracketlessColons = port >= 0
                }
            }

            port = -1
            if (!StringUtils.isEmpty(portString)) {
                checkArgument(!portString!!.startsWith("+"), "Unparseable port number: %s", hostPortString)

                try {
                    port = Integer.parseInt(portString)
                } catch (var6: NumberFormatException) {
                    throw IllegalArgumentException("Unparseable port number: $hostPortString")
                }

                checkArgument(isValidPort(port), "Port number out of range: %s", hostPortString)
            }

            return HostAndPort(host, port, hasBracketlessColons)
        }

        private fun checkNotNull(value: String?) {
            checkState(value != null)
        }

        private fun getHostAndPortFromBracketedHost(hostPortString: String): Array<String> {
            val colonIndex: Int = hostPortString.indexOf(':')
            val closeBracketIndex: Int = hostPortString.lastIndexOf(':')
            checkArgument(
                hostPortString[0] == '[',
                "Bracketed host-port string must start with a bracket: %s",
                hostPortString
            )
            checkArgument(
                colonIndex > -1 && closeBracketIndex > colonIndex,
                "Invalid bracketed host/port: %s",
                hostPortString
            )
            val host = hostPortString.substring(1, closeBracketIndex)
            return if (closeBracketIndex + 1 == hostPortString.length) {
                arrayOf(host, "")
            } else {
                checkArgument(
                    hostPortString[closeBracketIndex + 1] == ':',
                    "Only a colon may follow a close bracket: %s",
                    hostPortString
                )

                for (i in closeBracketIndex + 2 until hostPortString.length) {
                    checkArgument(Character.isDigit(hostPortString[i]), "Port must be numeric: %s", hostPortString)
                }

                arrayOf(host, hostPortString.substring(closeBracketIndex + 2))
            }
        }

        private fun isValidPort(port: Int): Boolean {
            return port in 0..65535
        }
    }
}
