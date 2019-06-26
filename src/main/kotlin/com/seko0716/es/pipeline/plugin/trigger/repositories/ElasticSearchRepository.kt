package com.seko0716.es.pipeline.plugin.trigger.repositories

import com.seko0716.es.pipeline.plugin.trigger.constants.IndexConstants.Companion.INDEX
import com.seko0716.es.pipeline.plugin.trigger.constants.IndexConstants.Companion.SIZE
import org.elasticsearch.client.Client
import org.elasticsearch.search.SearchHit
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class ElasticSearchRepository @Autowired constructor(val client: Client) {

    val mapSearchHit2MapWithId =
        { searchHit: SearchHit -> HashMap(searchHit.sourceAsMap).apply { put("id", searchHit.id) } }

    fun findAll() = client
        .prepareSearch(INDEX)
        .setSize(SIZE)
        .get()
        .hits
        .hits
        .asSequence()
        .map { mapSearchHit2MapWithId }

    fun findAllPipelineInfo() = client
        .prepareSearch(INDEX)
        .setSize(SIZE)
        .setFetchSource(arrayOf("title", "description", "state", "trigger"), null)
        .get()
        .hits
        .hits
        .asSequence()
        .map { mapSearchHit2MapWithId.invoke(it) }
}