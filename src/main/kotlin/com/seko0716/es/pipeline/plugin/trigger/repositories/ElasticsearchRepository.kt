package com.seko0716.es.pipeline.plugin.trigger.repositories

import com.seko0716.es.pipeline.plugin.trigger.constants.IndexConstants.Companion.INDEX
import com.seko0716.es.pipeline.plugin.trigger.constants.IndexConstants.Companion.SIZE
import org.elasticsearch.client.Client
import org.elasticsearch.common.unit.TimeValue
import org.elasticsearch.search.SearchHit
import org.elasticsearch.search.slice.SliceBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository


@Repository
class ElasticsearchRepository @Autowired constructor(
    val client: Client,
    @Value("\${spring.application.pipeline.source.fields}")
    val sourceFields: Array<String>
) {

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
        .setFetchSource(sourceFields, null)
        .get()
        .hits
        .hits
        .asSequence()
        .map { mapSearchHit2MapWithId.invoke(it) }


    fun findAllPipelineInfo(nodeId: Int, totalNodes: Int): List<Map<String, Any>> {
        val result = mutableListOf<Map<String, Any>>()

        var scrollResp = client
            .prepareSearch(INDEX)
            .setSize(SIZE)
            .setScroll(TimeValue(30000))
            .slice(SliceBuilder(nodeId, totalNodes))
            .setFetchSource(sourceFields, null)
            .get()

        do {
            result.addAll(scrollResp
                .hits
                .hits
                .map { mapSearchHit2MapWithId.invoke(it) })


            scrollResp =
                client.prepareSearchScroll(scrollResp.scrollId).setScroll(TimeValue(60000)).execute().actionGet()
        } while (scrollResp.hits.hits.isEmpty()) // Zero hits mark the end of the scroll and the while loop.
        return result.toList()
    }
}