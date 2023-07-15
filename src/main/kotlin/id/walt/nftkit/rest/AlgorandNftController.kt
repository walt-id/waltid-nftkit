package id.walt.nftkit.rest

import cc.vileda.openapi.dsl.schema
import id.walt.nftkit.services.AlgodResponse
import id.walt.nftkit.services.AlgorandAccount
import id.walt.nftkit.services.AlgorandChain
import id.walt.nftkit.services.AlgorandNftService
import id.walt.nftkit.tokenownersquery.TokenOwnersDataResponse
import id.walt.nftkit.utilis.Common
import io.javalin.http.Context
import io.javalin.plugin.openapi.dsl.document
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object AlgorandNftController{
    fun accountCreation(ctx : Context){
        val result = AlgorandNftService.createAccount()
        ctx.json(result)
    }

    fun accountCreationDocs() = document().operation {
        it.summary("Create Algorand Account").operationId("CreateAlgorandAccount").addTagsItem("Algorand Blockchain: Non-fungible tokens(NFTs)")
    }.json<AlgorandAccount>(200.toString()) {
        it.description("Algorand Account")
    }


    fun assetCreation(ctx : Context){
        val result = AlgorandNftService.createAssetArc3(ctx.pathParam("assetName"), ctx.pathParam("assetUnitName"), ctx.pathParam("url"))
        ctx.json(result)
    }

    fun assetCreationDocs() = document().operation {
        it.summary("Create Algorand Asset").operationId("CreateAlgorandAsset").addTagsItem("Algorand Blockchain: Non-fungible tokens(NFTs)")
    }.json<AlgodResponse>(200.toString()) {
        it.description("Algorand Asset")
    }


    private const val TAG = "Algorand Blockchain"
    fun fetchAssetMetadata(ctx: Context){
        val chain =ctx.pathParam("chain")
        val asset  = ctx.pathParam("asset")
        val nft = AlgorandNftService.getAssetMeatadata(
            asset.toLong(),
            Common.getAlgorandChain(chain.uppercase())
        )

        ctx.result(Json.encodeToString(nft))
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