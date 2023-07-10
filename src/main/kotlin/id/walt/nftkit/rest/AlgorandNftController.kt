package id.walt.nftkit.rest

import cc.vileda.openapi.dsl.schema
import id.walt.nftkit.services.AlgorandChain
import id.walt.nftkit.services.AlgorandNftService
import id.walt.nftkit.services.UniqueNetwork
import id.walt.nftkit.tokenownersquery.TokenOwnersDataResponse
import id.walt.nftkit.utilis.Common
import io.javalin.http.Context
import io.javalin.plugin.openapi.dsl.document
import io.ktor.util.*

object AlgorandNftController {
    private const val TAG = "Algorand Blockchain"


    fun fetchNftMetadata(ctx: Context){
        val chain =ctx.pathParam("chain")
        val asset  = ctx.pathParam("asset")
        val result = AlgorandNftService.getNftMetadata(asset.toLong(), Common.getAlgorandChain(chain.uppercase()) )
        ctx.json(result)
    }

    fun fetchNftMetadataDocs()= document().operation {
        it.summary("Fetching NFTs on Algorand Network")
            .operationId("fetchAlgornadNfts")
            .addTagsItem(TAG)}
        .pathParam<String>("chain") {
            it.schema<AlgorandChain>{}}
        .pathParam<String>("asset"){}
        .json<TokenOwnersDataResponse>("200"){
            it.description("Fetched NFTs")
    }
}
