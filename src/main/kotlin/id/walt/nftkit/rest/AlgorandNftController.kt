package id.walt.nftkit.rest

import cc.vileda.openapi.dsl.schema
import id.walt.nftkit.services.*
import id.walt.nftkit.tokenownersquery.TokenOwnersDataResponse
import id.walt.nftkit.utilis.Common
import io.javalin.http.Context
import io.javalin.plugin.openapi.dsl.document
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement


@Serializable
data class Arc3Metadata(
    val name: String,
    val description: String,
    val image: String,
    val decimals: Int,
    val unitName: String,
    val image_integrity: String,
    val image_mimetype : String,
    val properties: Map<String, String>
)

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
        val chain =ctx.pathParam("chain")


        val properties = ctx.bodyAsClass(Arc3Metadata::class.java)

//        val result = AlgorandNftService.createAssetArc3(Common.getAlgorandChain(chain.uppercase()),ctx.pathParam("assetName"), ctx.pathParam("assetUnitName"), ctx.pathParam("url"))
//        ctx.json(result)

        val result = properties.let {
            AlgorandNftService.createAssetArc3(
                Common.getAlgorandChain(chain.uppercase()),
                it.name,
                it.unitName,
                it.image,
                it.description,
                it.decimals,
                it.properties
            )
        }
        ctx.json(result)
    }

    fun assetCreationDocs() = document().operation {
        it.summary("Create Algorand Asset").operationId("CreateAlgorandAsset").addTagsItem("Algorand Blockchain: Non-fungible tokens(NFTs)")

    }
        .pathParam<String>("chain") {
            it.schema<AlgorandChain>{}
        }
        .body<Arc3Metadata> {
            it.description("Algorand Asset")

        }
            .json<AlgodResponse>(200.toString()) {
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
        .json<String>(200.toString()) {
            it.description("Algorand Token")
        }



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
        .json<List<AlgorandToken>>("200"){
            it.description("Account tokens")
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
        .json<String>(200.toString()) {
            it.description("Algorand NFT Metadata")
        }

    ///////////////////////////////////////////////////////////////////////////

    fun verifyNftOwnerShipBasedOnCreator(ctx: Context){
        val chain =ctx.pathParam("chain")
        val address = ctx.pathParam("address")
        val creatorAddress = ctx.pathParam("creatorAddress")
        val result = AlgorandNftService.verifyOwnerShipBasedOnCreator(address, Common.getAlgorandChain(chain.uppercase()) , creatorAddress)
        ctx.result(Json.encodeToString(result))

    }

    fun verifyNftOwnerShipBasedOnCreatorDocs()= document().operation {
        it.summary("Verifying NFT ownership based on creator ")
            .operationId("verifyNftOwnerShipBasedOnCreator")
            .addTagsItem("NFT verification On Algorand")}
        .pathParam<String>("chain") {
            it.schema<AlgorandChain>{}}
        .pathParam<String>("address"){}
        .pathParam<String>("creatorAddress"){}
        .json<String>(200.toString()) {
            it.description("Algorand NFT Metadata")
        }


    fun verifyPolicyOnAlgorand(ctx: Context){
        val chain =ctx.pathParam("chain")
        val tokenid = ctx.pathParam("token id")
        val policyName = ctx.pathParam("policyName")

        val result = VerificationService.verifyPolicyAlgorand(Common.getChain(chain) ,tokenid , policyName)
        ctx.result(Json.encodeToString(result))

    }

    fun doVerifyPolicyOnAlgorandDocs()= document().operation {
        it.summary("Verify an NFT metadata against a dynamic policy")
            .operationId("verifyNftagainstPolicy")
            .addTagsItem("NFT verification On Algorand")}
        .pathParam<String>("chain") {
            it.schema<AlgorandChain>{}}
        .pathParam<String>("token id"){}
        .pathParam<String>("policyName"){}
        .json<String>(200.toString()) {
            it.description("Algorand NFT Metadata")
        }
}

