package id.walt.nftkit.rest

import cc.vileda.openapi.dsl.schema
import id.walt.nftkit.services.*
import id.walt.nftkit.tokenownersquery.TokenOwnersDataResponse
import id.walt.nftkit.utilis.Common
import io.javalin.http.Context
import io.javalin.plugin.openapi.dsl.document
import kotlinx.serialization.Serializable

@Serializable
data class PolkadotUniqueDeployRequest(
    val owner: String,
    val type: String,
)

@Serializable
data class PolkadotUniqueSCDeploymentResponse(
    val contractAddress: String,
    val contractExternalUrl: String
)

@Serializable
data class PolkadotUniqueMintRequest(
    val metadataUri: String?,
    val tokenId: String,
    val amount: String?,
    val recipientAddress: String,
    val metadata: UniqueNftMetadata?,
)

@Serializable
data class PolkadotUniqueAddMinterRequest(
    val minterAddress: String
)

@Serializable
data class PolkadotUniqueOperationResponse(
    val operationHash: String,
    val contractExternalUrl: String
)

object PolkadotUniqueNftController {
    private const val TAG = "Polkadot Blockchain: Unique network"

    fun fetchUniqueNfts(ctx: Context) {
        val chain = ctx.pathParam("network")
        val account = ctx.pathParam("account")
        val network: UniqueNetwork = Common.getUniqueChain(chain.uppercase())

        val result = PolkadotNftService.fetchUniqueNFTs(network, account)
        ctx.json(result)
    }

    fun fetchUniqueNftsDocs() = document().operation {
        it.summary("Fetching NFTs on Unique Network")
            .operationId("fetchUniqueNfts")
            .addTagsItem(TAG)
        }.pathParam<String>("network") {
        it.schema<UniqueNetwork> {}
    }.pathParam<String>("account"){
    }.json<TokenOwnersDataResponse>("200"){
        it.description("Fetched NFTs")
    }

    fun fetchUniqueNftsMetadata(ctx: Context) {
        val chain = ctx.pathParam("network")
        val account = ctx.pathParam("account")
        val network: UniqueNetwork = Common.getUniqueChain(chain.uppercase())
        val result = PolkadotNftService.fetchUniqueNFTsMetadata(network, account)

        println("Result: $result")

        ctx.json(result)
    }

    fun fetchUniqueNftsMetadataDocs() = document().operation {
        it.summary("Fetching NFTs on Unique Network")
            .operationId("fetchUniqueNftsMetadata")
            .addTagsItem(TAG)
    }.pathParam<String>("network") {
        it.schema<UniqueNetwork> {}
    }.pathParam<String>("account"){
    }.json<UniqueNftMetadata>("200"){
        it.description("Fetched NFTs")
    }



    fun fetchUniqueNftMetadata(ctx: Context) {
        val network: UniqueNetwork = Common.getUniqueChain(ctx.pathParam("chain").uppercase())
        val collectionId: String = ctx.pathParam("collectionId")
        val tokenId: String = ctx.pathParam("tokenId")

        val tokenDataResponse = PolkadotNftService.fetchUniqueNFTMetadata(network, collectionId, tokenId)
        val result = PolkadotNftService.parseNftMetadataUniqueResponse(tokenDataResponse!!)
        ctx.json(result)
    }

    fun fetchUniqueNftMetadataDocs() = document().operation {
        it.summary("Fetching NFTs metadata on Unique Network")
            .operationId("fetchUniqueNftMetadata")
            .addTagsItem(TAG)
    }.pathParam<String>("chain"){
        it.schema<UniqueNetwork> {}
    }.pathParam<String>("collectionId"){
    }.pathParam<String>("tokenId"){
    }.json<UniqueNftMetadata>("200"){
        it.description("Fetched NFT metadata")
    }

}
