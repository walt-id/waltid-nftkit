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


    fun fetchToken(ctx: Context){
        val chain =ctx.pathParam("chain")
        val asset  = ctx.pathParam("assetId")
        val response = AlgorandNftService.getToken(
            asset.toLong(),
            Common.getAlgorandChain(chain.uppercase())
        )
        ctx.json(response)
    }
    fun fetchTokenDocs()= document().operation {
        it.summary("Fetching Token")
            .operationId("fetchToken")
            .addTagsItem(TAG)}
        .pathParam<String>("chain") {
            it.schema<AlgorandChain>{}}
        .pathParam<String>("assetId"){}
        .json<TokenOwnersDataResponse>("200"){
            it.description("Fetched token")
        }

    ///////////////////////////////////////////////////////////////////////////

    fun fetchAssetMetadata(ctx: Context){
        val chain =ctx.pathParam("chain")
        val asset  = ctx.pathParam("assetId")
        val response = AlgorandNftService.getAssetMeatadata(
                asset.toLong(),
                Common.getAlgorandChain(chain.uppercase())
        )
        ctx.json(response)
    }
    fun fetchAssetMetadataDocs()= document().operation {
        it.summary("Fetching token parametrs ")
            .operationId("fetchAlgornadAssets")
            .addTagsItem(TAG)}
        .pathParam<String>("chain") {
            it.schema<AlgorandChain>{}}
        .pathParam<String>("assetId"){}
        .json<TokenOwnersDataResponse>("200"){
            it.description("Fetched token parameteres")
        }

    ///////////////////////////////////////////////////////////////////////////

    fun fetchAccountAssets(ctx: Context){
        val chain = ctx.pathParam("chain")
        val address = ctx.pathParam("address")
        val response = AlgorandNftService.getAccountAssets(address, Common.getAlgorandChain(chain.uppercase()))
        ctx.json(response)
    }
    fun fetchAccountAssetsDocs()= document().operation {
        it.summary("Fetching account tokens ")
            .operationId("fetchAccountAssets")
            .addTagsItem(TAG)}
        .pathParam<String>("chain") {
            it.schema<AlgorandChain>{}}
        .pathParam<String>("address"){}
        .json<TokenOwnersDataResponse>("200"){
            it.description("Fetched Tokens")
        }

    ///////////////////////////////////////////////////////////////////////////

    fun fetchNftMetadata(ctx: Context){
        val chain =ctx.pathParam("chain")
        val asset  = ctx.pathParam("assetId")
        val result = AlgorandNftService.getNftMetadata(asset.toLong(), Common.getAlgorandChain(chain.uppercase()) )
        ctx.result(Json.encodeToString(result))
    }
    fun fetchNftMetadataDocs()= document().operation {
        it.summary("Fetching NFT metadata ")
            .operationId("fetchAlgornadNfts")
            .addTagsItem(TAG)}
        .pathParam<String>("chain") {
            it.schema<AlgorandChain>{}}
        .pathParam<String>("assetId"){}
        .json<TokenOwnersDataResponse>("200"){
            it.description("Fetched NFT metadata")
    }
}
