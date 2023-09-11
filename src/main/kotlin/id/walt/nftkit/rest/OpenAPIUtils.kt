package id.walt.nftkit.rest

import io.javalin.plugin.openapi.dsl.document

object OpenAPIUtils {
    fun documentedIgnored() = document().ignore(true)
}
