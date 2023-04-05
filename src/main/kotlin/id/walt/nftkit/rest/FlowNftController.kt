package id.walt.nftkit.rest

import cc.vileda.openapi.dsl.schema
import id.walt.nftkit.services.FlowNFTMetadata
import id.walt.nftkit.services.FlowNftService
import id.walt.nftkit.utilis.Common
import io.javalin.http.Context
import io.javalin.plugin.openapi.dsl.document

object FlowNftController {
    fun getAllNFTs(ctx: Context) {
        val account_id = ctx.pathParam("account_id")
        val chain = ctx.pathParam("chain")

        val result = FlowNftService.getAllNFTs(account_id, Common.getFlowChain(chain.lowercase() ))
        ctx.json(result)


    }

    fun getAllNFTsDocs() = document().operation {
        it.summary("Get NFT token")
            .operationId("GetAllNFT").addTagsItem("Flow Blockchain: Non-fungible tokens(NFTs)")
    }.pathParam<String>("chain") {
        it.schema<String> { }
    }
        .pathParam<String>("account_id") {
        }.json<FlowNFTMetadata>("200") {
            it.description("NFT token")

        }
}