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

    fun fetchUniqueNftMetadata(ctx: Context) {
        val network: UniqueNetwork = Common.getUniqueChain(ctx.pathParam("chain").uppercase())
        val collectionId: String = ctx.pathParam("collectionId")
        val tokenId: String = ctx.pathParam("tokenId")

        val tokenDataResponse = PolkadotNftService.fetchUniqueNFTsMetadata(network, collectionId, tokenId)
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

    fun fetchparachainNFTS(ctx: Context) {
        val chain = ctx.pathParam("chain")
        val account = ctx.pathParam("account")


        val result = PolkadotNftService.fetchAccountTokensBySubscan(PolkadotParachain.valueOf(chain), account)
        ctx.json(result)
    }

    fun fetchparachainNFTSDocs() = document().operation {
        it.summary("Fetching Tokens by Subscan")
            .operationId("fetchparachainNFTS")
            .addTagsItem(TAG)
    }.pathParam<String>("chain") {
        it.schema<PolkadotParachain> {}
    }.pathParam<String>("account") {
        it.schema<String> {}

    }

    fun fetchEvmErc721CollectiblesBySubscan(ctx: Context) {
        val chain = ctx.pathParam("chain")
        val account = ctx.pathParam("account")
        val result = PolkadotNftService.fetchEvmErc721CollectiblesBySubscan(PolkadotParachain.valueOf(chain), account)
        ctx.json(result)
    }

    fun fetchEvmErc721CollectiblesBySubscanDocs() = document().operation {
        it.summary("Fetching EvmErc721 Collectibles by Subscan")
            .operationId("fetchEvmErc721CollectiblesBySubscan")
            .addTagsItem(TAG)
    }.pathParam<String>("chain") {
        it.schema<PolkadotParachain> {}
    }.pathParam<String>("account") {
        it.schema<String> {}

    }
}