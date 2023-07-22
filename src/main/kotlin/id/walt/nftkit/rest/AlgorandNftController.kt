package id.walt.nftkit.rest

import cc.vileda.openapi.dsl.schema
import id.walt.nftkit.services.*
import id.walt.nftkit.tokenownersquery.TokenOwnersDataResponse
import id.walt.nftkit.utilis.Common
import io.javalin.http.Context
import io.javalin.plugin.openapi.dsl.document
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement


object AlgorandNftController{

    private const val TAG = "Algorand Blockchain: Non-fungible tokens(NFTs)"
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


      fun fetchToken(ctx: Context){
        val chain =ctx.pathParam("chain")
        val asset  = ctx.pathParam("assetId")
        val response = AlgorandNftService.getToken(
            asset,
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


    ///////////////////////////////////////////////////////////////////////////

    fun fetchAssetMetadata(ctx: Context){
        val chain =ctx.pathParam("chain")
        val asset  = ctx.pathParam("assetId")
        val response = AlgorandNftService.getAssetMeatadata(
                asset,
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
        .json<Asset>("200"){

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
        .json<AssetHoldingsResponse>("200"){
            it.description("Fetched Tokens")
        }


    ///////////////////////////////////////////////////////////////////////////

    fun fetchNftMetadata(ctx: Context){
        val chain =ctx.pathParam("chain")
        val asset  = ctx.pathParam("assetId")
        val result = AlgorandNftService.getNftMetadata(asset, Common.getAlgorandChain(chain.uppercase()) )
        ctx.result(Json.encodeToString(result))

    }
    fun fetchNftMetadataDocs()= document().operation {
        it.summary("Fetching NFT metadata ")
            .operationId("fetchAlgornadNfts")
            .addTagsItem(TAG)}
        .pathParam<String>("chain") {
            it.schema<AlgorandChain>{}}
        .pathParam<String>("assetId"){}

}

