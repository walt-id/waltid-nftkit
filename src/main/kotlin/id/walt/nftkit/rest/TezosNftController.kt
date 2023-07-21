package id.walt.nftkit.rest

import cc.vileda.openapi.dsl.schema
import id.walt.nftkit.models.NFTsInfos
import id.walt.nftkit.services.*
import id.walt.nftkit.utilis.Common
import io.javalin.http.Context
import io.javalin.plugin.openapi.dsl.document
import kotlinx.serialization.Serializable

@Serializable
data class TezosDeployRequest(
    val owner: String,
    val type: String,
)
@Serializable
data class TezosSCDeploymentResponse(
    val contractAddress: String,
    val contractExternalUrl: String
)

@Serializable
data class TezosMintRequest(
    val metadataUri: String?,
    val tokenId: String,
    val amount: String?,
    val recipientAddress: String,
    val metadata: TezosNftMetadata?,
)

@Serializable
data class TezosAddMinterRequest(
    val minterAddress: String
)

@Serializable
data class TezosOperationResponse(
    val operationHash: String,
    val contractExternalUrl: String
)

object TezosNftController {
    const val TAG1 = "Tezos Blockchain"
    fun deploy(ctx: Context) {
        val deployReq = ctx.bodyAsClass(TezosDeployRequest::class.java)
        val chain = ctx.pathParam("chain")
        val result =
            TezosNftService.deploySmartContract(Common.getTezosChain(chain.uppercase()), deployReq.owner, Common.getFa2SmartContractType(deployReq.type))

        ctx.json(result)
    }

    fun deployDocs() = document().operation {
        it.summary("Smart contract deployment")
            .operationId("deployTezosSmartContract").addTagsItem(TAG1)
    }.pathParam<String>("chain") {
        it.schema<TezosChain> { }
    }.body<TezosDeployRequest> {
        it.description("")
    }.json<TezosSCDeploymentResponse>("200") { it.description("Transaction ID and smart contract address") }

    fun mint(ctx: Context) {
        val mintReq = ctx.bodyAsClass(TezosMintRequest::class.java)
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val parameter = TezosMintingParameter(mintReq.metadataUri, mintReq.recipientAddress, mintReq.tokenId, mintReq.amount, mintReq.metadata)
        val result= TezosNftService.mintNftToken(Common.getTezosChain(chain.uppercase()), contractAddress, parameter)
        ctx.json(result)
    }

    fun mintDocs() = document().operation {
        it.summary("NFT minting")
            .operationId("mintTezosNft").addTagsItem(TAG1)
    }.pathParam<String>("chain") {
        it.schema<TezosChain> { }
    }.pathParam<String>("contractAddress") {
    }.body<TezosMintRequest> {
        it.description("")
    }.json<OperationResponse>("200") { it.description("Transaction ID and token ID") }

    fun addMinter(ctx: Context) {
        val minterReq = ctx.bodyAsClass(TezosAddMinterRequest::class.java)
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val result= TezosNftService.addMinter(Common.getTezosChain(chain.uppercase()), contractAddress, minterReq.minterAddress)
        ctx.json(result)
    }

    fun addMinterDocs() = document().operation {
        it.summary("Add minter")
            .operationId("AddMinter").addTagsItem(TAG1)
    }.pathParam<String>("chain") {
        it.schema<TezosChain> { }
    }.pathParam<String>("contractAddress") {
    }.body<TezosAddMinterRequest> {
        it.description("")
    }.json<OperationResponse>("200") { it.description("Transaction ID and token ID") }


    fun getNftMetadata(ctx: Context) {
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val tokenId = ctx.pathParam("tokenId")
        val result = TezosNftService.getNftTezosMetadata(Common.getTezosChain(chain.uppercase()), contractAddress, tokenId)
        result?.let {
            ctx.json(
                it
            )
        }
    }

    fun getNftMetadataDocs() = document().operation {
        it.summary("Get NFT Token Metadata")
            .operationId("GetNFTTokenTezosMetadata").addTagsItem(TAG1)
    }.pathParam<String>("chain") {
        it.schema<TezosChain> { }
    }.pathParam<String>("contractAddress") {
    }.pathParam<Int>("tokenId") {
    }.json<TezosNftMetadata>("200") { it.description("NFT Metadata") }

    fun getContractMetadata(ctx: Context) {
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val result =
            TezosNftService.getContractMetadata(Common.getTezosChain(chain.uppercase()), contractAddress)
        result?.let {
            ctx.json(
                it
            )
        }
    }

    fun getContractMetadataDocs() = document().operation {
        it.summary("Get Contract Metadata ")
            .operationId("GetContractMetadata").addTagsItem(TAG1)
    }.pathParam<String>("chain") {
        it.schema<TezosChain> { }
    }.pathParam<String>("contractAddress") {
    }


}
