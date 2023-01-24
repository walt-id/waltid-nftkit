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

@Serializable
data class NearSubAccountRequest(
        val  account_id: String,
        val newAccountId: String,
        val amount: String,

)

@Serializable
data class NearCustomDeployRequest(
        val spec: String,
        val name : String,
        val symbol : String,
        val icon : String?= null,
        val base_uri : String?= null,
        val reference : String?= null,
        val reference_hash : String?= null,
        val owner_id: String,
    )



object NearNftController {

    fun mint(ctx: Context) {

        val mintReq = ctx.bodyAsClass(NearMintRequest::class.java)
        val contract_id = ctx.pathParam("contract_id")
        val chain = ctx.pathParam("chain")

        val result = mintReq.media_hash?.let {
            NearNftService.mintNftToken(
                mintReq.account_id, contract_id, mintReq.token_id, mintReq.title, mintReq.description, mintReq.media,
                it, mintReq.reference.toString(), mintReq.reference_hash, mintReq.receiver_id,chain
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
        }.json<OperationResult>("200") { it.description("Transaction ID and smart contract address") }



    fun deployDefaultContract(ctx: Context) {
        val result = NearNftService.deployContractDefault(
            ctx.pathParam("account_id" ),
            ctx.pathParam("chain" )
        )
        ctx.json(result)
    }

    fun deployDefaultContractDocs() = document().operation {
        it.summary("Deploy default contract")
            .operationId("deployDefaultContract").addTagsItem("Near Blockchain: Non-fungible tokens(NFTs)")
    }
        .pathParam<String>("account_id") {
        }.json<OperationResult>("200") { it.description("Transaction ID") }

    fun deployCustomContract(ctx: Context) {
        val mintReq = ctx.bodyAsClass(NearCustomDeployRequest::class.java)
        val account_id = ctx.pathParam("account_id")
        val chain = ctx.pathParam("chain")

        val result = NearNftService.deployContractWithCustomMetadata(account_id,mintReq.owner_id, mintReq.spec, mintReq.name, mintReq.symbol, mintReq.icon.toString(), mintReq.base_uri.toString(), mintReq.reference.toString(), mintReq.reference_hash.toString(),chain)

        ctx.json(result)
    }

    fun deployCustomContractDocs() = document().operation {
        it.summary("Deploy custom contract")
            .operationId("deployCustomContract").addTagsItem("Near Blockchain: Non-fungible tokens(NFTs)")
    }
        .pathParam<String>("chain"){
        }.pathParam<String>("account_id") {
        }
        .body<NearCustomDeployRequest> {
            it.description("")
        }.json<OperationResult>("200") { it.description("Transaction ID") }




    fun getNftToken(ctx: Context) {
        val result = NearNftService.getNFTforAccount(
            ctx.pathParam("account_id"),
            ctx.pathParam("contract_id"),
            ctx.pathParam("chain")
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
            ctx.pathParam("contract_id"),
            ctx.pathParam("chain")
        )
        ctx.json(result)
    }

    fun getNFTContractMetadataDocs() = document().operation {
        it.summary("Get NFT contract metadata")
            .operationId("getNFTContractMetadata").addTagsItem("Near Blockchain: Non-fungible tokens(NFTs)")
    }
        .pathParam<String>("contract_id") {
        }.json<NFTMetadata>("200") {
            it.description("NFT contract metadata")

        }

    fun createSubAccount (ctx: Context) {

        val subAccountReq = ctx.bodyAsClass(NearSubAccountRequest::class.java)
        val chain = ctx.pathParam("chain")
        val result = NearNftService.createSubAccount(
            subAccountReq.account_id, subAccountReq.newAccountId, subAccountReq.amount, chain
        )
        ctx.json(result)
    }

    fun createSubAccountDocs() = document().operation {
        it.summary("Create sub account")
            .operationId("createSubAccount").addTagsItem("Near Blockchain: Non-fungible tokens(NFTs)")
    }
        .pathParam<String>("chain")
        .body<NearSubAccountRequest> {
            it.description("")
        }.json<OperationResult>("200") { it.description("Transaction ID and smart contract address") }


    fun getNFTTokenMetadata(ctx: Context) {
        val result = NearNftService.getTokenById(
            ctx.pathParam("contract_id"),
            ctx.pathParam("token_id"),
            ctx.pathParam("chain")
        )
        ctx.json(result)
    }

    fun getNFTTokenMetadataDocs() = document().operation {
        it.summary("Get NFT token metadata")
            .operationId("getNFTTokenMetadata").addTagsItem("Near Blockchain: Non-fungible tokens(NFTs)")
    }
        .pathParam<String>("contract_id") {
        }.pathParam<String>("token_id") {
        }.json<NearNftMetadata>("200") {
            it.description("NFT token metadata")
        }
}


