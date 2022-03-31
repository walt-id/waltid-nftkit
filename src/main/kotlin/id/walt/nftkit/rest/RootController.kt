package id.walt.nftkit.rest

import io.javalin.http.Context
import io.javalin.plugin.openapi.dsl.document

object RootController {

    fun root(ctx: Context) {
        ctx.html(
            " <!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<body>\n" +
                    "\n" +
                    "<h1>walt.id NFT Kit API</h1>\n" +
                    "<p><a href='/api-routes'>API Routes</a></p>\n" +
                    "<p><a href='/v1/swagger'>Swagger</a></p>\n" +
                    "<p><a href='/v1/redoc'>Redoc</a></p>\n" +
                    "\n" +
                    "</body>\n" +
                    "</html> "
        )
    }


    fun healthDocs() = document()
        .operation {
            it.summary("Returns HTTP 200 in case all services are up and running").operationId("health")
        }.json<String>("200")

    fun health(ctx: Context) {
        ctx.html("OK")
    }
}
