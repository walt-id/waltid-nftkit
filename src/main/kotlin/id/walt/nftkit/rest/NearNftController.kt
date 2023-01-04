package id.walt.nftkit.rest

import id.walt.nftkit.services.*
import io.javalin.http.Context
import io.javalin.plugin.openapi.dsl.document
import kotlinx.serialization.Serializable



data class NearMintRequest(
    val receiver_id: String,
    val token_id: String,
    val title: String,
    val description: String,
    val media: String,
    val media_hash: String?= null,
    val reference: String?= null,
    val reference_hash: String?= null,

    )

object NearNftController {

    fun mint(ctx: Context) {

        val contract_id = ctx.pathParam("contract_id")
        val account_id = ctx.pathParam("account_id")
        val  token_id = ctx.pathParam("token_id")
        val  title = ctx.pathParam("title")
        val  description = ctx.pathParam("description")
        val  media = ctx.pathParam("media")
        val  media_hash = ctx.pathParam("media_hash")
        val  reference = ctx.pathParam("reference")
        val  reference_hash = ctx.pathParam("reference_hash")
        val  receiver_id = ctx.pathParam("receiver_id")

        val result= NearNftService.mintNftToken(account_id,contract_id,token_id,title,description,media,media_hash,reference,reference_hash,receiver_id)
        ctx.json(result)
    }

    fun mintDocs() = document().operation {
        it.summary("Near NFT minting")
            .operationId("mintNft").addTagsItem("Near Blockchain: Non-fungible tokens(NFTs)")
    }
    .pathParam<String>("contractAddress") {
    }.body<NearMintRequest> {
        it.description("")
    }.json<OperationResponse>("200") { it.description("Transaction ID and token ID") }

    fun deployDefaultContract(ctx: Context) {
        val result = NearNftService.deployContractDefault(
            ctx.pathParam("account_id")
        )
        ctx.json(result)
    }

    fun deployDefaultContractDocs() = document().operation {
        it.summary("Deploy default contract")
            .operationId("deployDefaultContract").addTagsItem("Near Blockchain: Non-fungible tokens(NFTs)")
    }
    .pathParam<String>("account_id") {
    }.json<OperationResponse>("200") { it.description("Transaction ID") }

    fun deployCustomContract(ctx: Context) {
        val result = NearNftService.deployContractWithCustomMetadata(
            ctx.pathParam("account_id"),
            ctx.pathParam("owner_id"),
            ctx.pathParam("spec"),
            ctx.pathParam("name"),
            ctx.pathParam("symbol"),
            ctx.pathParam("icon"),
            ctx.pathParam("base_uri"),
            ctx.pathParam("reference"),
            ctx.pathParam("reference_hash")
        )
        ctx.json(result)
    }

    fun deployCustomContractDocs() = document().operation {
        it.summary("Deploy custom contract")
            .operationId("deployCustomContract").addTagsItem("Near Blockchain: Non-fungible tokens(NFTs)")
    }
    .pathParam<String>("account_id") {
    }.pathParam<String>("owner_id") {
    }.pathParam<String>("spec") {
    }.pathParam<String>("name") {
    }.pathParam<String>("symbol") {
    }.pathParam<String>("icon") {
    }.pathParam<String>("base_uri") {
    }.pathParam<String>("reference") {
    }.pathParam<String>("reference_hash") {
    }.json<OperationResponse>("200") { it.description("Transaction ID") }
}