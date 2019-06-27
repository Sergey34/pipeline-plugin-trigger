package com.seko0716.es.pipeline.plugin.trigger.services.node

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(name = ["spring.cloud.zookeeper.connect-string"])
class ZookeeperNodeStateService : NodeStateService {
    override fun getCurrentNodeId(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTotalNodes(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}