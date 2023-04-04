package id.walt.nftkit.rest

import cc.vileda.openapi.dsl.schema
import io.javalin.http.Context
import io.javalin.plugin.openapi.dsl.document

object FlowNftController {
    fun getAllNFTs(ctx: Context) {
       // TODO
    }

    fun getAllNFTsDocs() = document().operation {
        it.summary("Get NFT token")
            .operationId("getNftToken").addTagsItem("Flow Blockchain: Non-fungible tokens(NFTs)")
    }.pathParam<String>("chain") {
        it.schema<String> { }
    }
        .pathParam<String>("account_id") {
        }.pathParam<String>("contract_id") {
        }.json<String>("200") {
            it.description("NFT token")

        }
}