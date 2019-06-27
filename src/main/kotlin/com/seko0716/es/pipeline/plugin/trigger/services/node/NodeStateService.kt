package com.seko0716.es.pipeline.plugin.trigger.services.node

interface NodeStateService {
    fun getTotalNodes(): Int
    fun getCurrentNodeId(): Int
}