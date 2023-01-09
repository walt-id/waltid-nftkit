package id.walt.nftkit.rest

import id.walt.nftkit.services.*
import io.javalin.http.Context
import io.javalin.plugin.openapi.dsl.document
import io.swagger.util.Json
import kotlinx.serialization.Serializable


@Serializable
data class NearMintRequest(

    val  account_id: String,
    val token_id: String,
    val title: String,
    val description: String,
    val media: String,
    val media_hash: String?= null,
    val reference: String?= null,
    val reference_hash: String?= null,
    val receiver_id: String,
    )

object NearNftController {

    fun mint(ctx: Context) {

        val mintReq = ctx.bodyAsClass(NearMintRequest::class.java)
        val contract_id = ctx.pathParam("contract_id")


        val result = mintReq.media_hash?.let {
            NearNftService.mintNftToken(
                mintReq.account_id, contract_id, mintReq.token_id, mintReq.title, mintReq.description, mintReq.media,
                it, mintReq.reference.toString(), mintReq.reference_hash, mintReq.receiver_id
            )
        }
        if (result != null) {
            ctx.json(result)
        }
    }

    fun mintDocs() = document().operation {
        it.summary("Near NFT minting")
            .operationId("mintNft").addTagsItem("Near Blockchain: Non-fungible tokens(NFTs)")
    }

        .body<NearMintRequest> {
            it.description("")
        }.json<String>("200") { it.description("Transaction ID and smart contract address") }



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


    fun getNftToken(ctx: Context) {
        val result = NearNftService.getNFTforAccount(
            ctx.pathParam("account_id"),
            ctx.pathParam("contract_id"),

        )
        ctx.json(result)

    }

    fun getNftTokenDocs() = document().operation {
        it.summary("Get NFT token")
            .operationId("getNftToken").addTagsItem("Near Blockchain: Non-fungible tokens(NFTs)")
    }
        .pathParam<String>("account_id") {
        }.pathParam<String>("contract_id") {
        }.json<NearNftMetadata>("200") {
            it.description("NFT token")

        }


    fun getNFTContractMetadata(ctx: Context) {
        val result = NearNftService.getNftNearMetadata(
            ctx.pathParam("contract_id")
        )
        ctx.json(result)
    }

    fun getNFTContractMetadataDocs() = document().operation {
        it.summary("Get NFT contract metadata")
            .operationId("getNFTContractMetadata").addTagsItem("Near Blockchain: Non-fungible tokens(NFTs)")
    }
        .pathParam<String>("contract_id") {
        }.json<NearNftMetadata>("200") {
            it.description("NFT contract metadata")

        }


}



