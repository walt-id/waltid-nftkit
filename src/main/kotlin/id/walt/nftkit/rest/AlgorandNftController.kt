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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement

object AlgorandNftController {
    private const val TAG = "Algorand Blockchain"
    fun fetchAssetMetadata(ctx: Context){
        val chain =ctx.pathParam("chain")
        val asset  = ctx.pathParam("asset")
        val response = AlgorandNftService.getAssetMeatadata(
                asset.toLong(),
                Common.getAlgorandChain(chain.uppercase())
        )
        ctx.json(response)
//        ctx.result(Json.encodeToString(nft))
    }
    fun fetchAssetMetadataDocs()= document().operation {
        it.summary("Fetching Asset on Algorand Network")
            .operationId("fetchAlgornadAssets")
            .addTagsItem(TAG)}
        .pathParam<String>("chain") {
            it.schema<AlgorandChain>{}}
        .pathParam<String>("asset"){}
        .json<TokenOwnersDataResponse>("200"){
            it.description("Fetched asset")
        }

    fun fetchAccountAssets(ctx: Context){
        val chain = ctx.pathParam("chain")
        val address = ctx.pathParam("address")
        val response = AlgorandNftService.getAccountAssets(address, Common.getAlgorandChain(chain.uppercase()))
        ctx.json(response)
    }
    fun fetchAccountAssetsDocs()= document().operation {
        it.summary("Fetching NFTs on Algorand Network")
            .operationId("fetchAccountAssets")
            .addTagsItem(TAG)}
        .pathParam<String>("chain") {
            it.schema<AlgorandChain>{}}
        .pathParam<String>("address"){}
        .json<TokenOwnersDataResponse>("200"){
            it.description("Fetched NFT")
        }
    fun fetchNftMetadata(ctx: Context){
        val chain =ctx.pathParam("chain")
        val asset  = ctx.pathParam("asset")
        val result = AlgorandNftService.getNftMetadata(asset.toLong(), Common.getAlgorandChain(chain.uppercase()) )
        ctx.result(Json.encodeToString(result))
    }
    fun fetchNftMetadataDocs()= document().operation {
        it.summary("Fetching NFTs on Algorand Network")
            .operationId("fetchAlgornadNfts")
            .addTagsItem(TAG)}
        .pathParam<String>("chain") {
            it.schema<AlgorandChain>{}}
        .pathParam<String>("asset"){}
        .json<TokenOwnersDataResponse>("200"){
            it.description("Fetched NFT")
    }
}
