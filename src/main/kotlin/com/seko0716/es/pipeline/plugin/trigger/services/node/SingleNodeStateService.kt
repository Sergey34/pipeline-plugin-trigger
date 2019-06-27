package com.seko0716.es.pipeline.plugin.trigger.services.node

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.stereotype.Component

@Component
@ConditionalOnMissingBean(
    type = [
        "com.seko0716.es.pipeline.plugin.trigger.services.node.EurekaNodesStateService",
        "com.seko0716.es.pipeline.plugin.trigger.services.node.ZookeeperNodeStateService"]
)
class SingleNodeStateService : NodeStateService {
    override fun getTotalNodes() = 1

    override fun getCurrentNodeId() = 0
}