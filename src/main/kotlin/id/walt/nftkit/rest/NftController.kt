package id.walt.nftkit.rest

import cc.vileda.openapi.dsl.schema
import id.walt.nftkit.services.*
import io.javalin.http.Context
import io.javalin.plugin.openapi.dsl.document
import io.swagger.annotations.ApiModelProperty
import kotlinx.serialization.Serializable
import java.math.BigInteger


@Serializable
data class MintRequest(
    val metadataUri : String?,
    val metadata: NftMetadata?,
    val recipientAddress: String,
    val metadataStorageType: MetadataStorageType,
    val offChainMetadataStorageType: OffChainMetadataStorageType?,
)

@Serializable
data class DeployRequest(
    val name: String,
    val symbol : String,
    val tokenStandard: TokenStandard
)

object NftController {

    fun deploy(ctx: Context){
        val deployReq = ctx.bodyAsClass(DeployRequest::class.java)
        val chain = ctx.pathParam("chain")
        val deploymentOptions = DeploymentOptions(tokenStandard = deployReq.tokenStandard)
        val deploymentParameter = DeploymentParameter(deployReq.name!!, deployReq.symbol!!,"","")
        val result = NftService.deploySmartContractToken(Chain.valueOf(chain?.uppercase()!!), deploymentParameter, deploymentOptions)
        ctx.json(
            result
        )
    }

    fun deployDocs() = document().operation {
        it.summary("Smart contract deployment")
            .operationId("deploySmartContract").addTagsItem("Blockchain: Non-fungible tokens(NFTs)")
    }.pathParam<String>("chain") {
        it.schema<Chain> {  }
    }.body<DeployRequest> {
        it.description("")
    }.json<DeploymentResponse>("200") { it.description("Transaction ID and smart contract address") }


    fun mint(ctx: Context) {
        val mintReq = ctx.bodyAsClass(MintRequest::class.java)
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val mintingParameter = MintingParameter(mintReq.metadataUri, mintReq.recipientAddress,mintReq.metadata)
        val mintingOptions = MintingOptions(mintReq.metadataStorageType, mintReq.offChainMetadataStorageType)
        val result = NftService.mintToken(Chain.valueOf(chain?.uppercase()!!),contractAddress, mintingParameter, mintingOptions)
        ctx.json(
            result
        )
    }

    fun mintDocs() = document().operation {
        it.summary("NFT minting")
            .operationId("mintNft").addTagsItem("Blockchain: Non-fungible tokens(NFTs)")
    }.pathParam<String>("chain") {
        it.schema<Chain> {  }
    }.pathParam<String>("contractAddress") {
    }.body<MintRequest> {
        it.description("")
    }.json<MintingResponse>("200") { it.description("Transaction ID and token ID") }


    fun getNftMetadatUri(ctx: Context){
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val tokenId = ctx.pathParam("tokenId").toLong()!!
        val result = NftService.getNftMetadataUri(Chain.valueOf(chain?.uppercase()!!), contractAddress!!, BigInteger.valueOf(tokenId))
        ctx.json(
            result!!
        )
    }

    fun getNftMetadatUriDocs() = document().operation {
        it.summary("Get NFT Token Metadata URI")
            .operationId("MetadataUri").addTagsItem("Blockchain: Non-fungible tokens(NFTs)")
    }.pathParam<String>("chain") {
        it.schema<Chain> {  }
    }.pathParam<String>("contractAddress") {

    }.pathParam<Int>("tokenId") {
    }.json<String>("200") { it.description("NFT Metadata URI") }


    fun getNftMetadata(ctx: Context){
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val tokenId = ctx.pathParam("tokenId").toLong()!!
        val result = NftService.getNftMetadata(Chain.valueOf(chain?.uppercase()!!), contractAddress!!, BigInteger.valueOf(tokenId))
        ctx.json(
            result!!
        )
    }

    fun getNftMetadataDocs() = document().operation {
        it.summary("Get NFT Token Metadata")
            .operationId("Metadata").addTagsItem("Blockchain: Non-fungible tokens(NFTs)")
    }.pathParam<String>("chain") {
        it.schema<Chain> {  }
    }.pathParam<String>("contractAddress") {
    }.pathParam<Int>("tokenId") {
    }.json<NftMetadata>("200") { it.description("NFT Metadata") }


    fun balance(ctx: Context){
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val ownerAdress = ctx.pathParam("ownerAddress")
        val result = NftService.balanceOf(Chain.valueOf(chain?.uppercase()!!), contractAddress!!, ownerAdress)
        ctx.json(
            result!!
        )
    }

    fun balanceDocs() = document().operation {
        it.summary("Get Account balance")
            .operationId("balance").addTagsItem("Blockchain: Non-fungible tokens(NFTs)")
    }.pathParam<String>("chain") {
        it.schema<Chain> {  }
    }.pathParam<String>("contractAddress") {
    }.pathParam<String>("ownerAddress") {
    }.json<BigInteger>("200") { it.description("Account balance") }


    fun owner(ctx: Context){
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val tokenId = ctx.pathParam("tokenId").toLong()!!
        val result = NftService.ownerOf(Chain.valueOf(chain?.uppercase()!!), contractAddress!!, tokenId)
        ctx.json(
            result!!
        )
    }

    fun ownerDocs() = document().operation {
        it.summary("Get Token owner")
            .operationId("owner").addTagsItem("Blockchain: Non-fungible tokens(NFTs)")
    }.pathParam<String>("chain") {
        it.schema<Chain> {  }
    }.pathParam<String>("contractAddress") {
    }.pathParam<Int>("tokenId") {
    }.json<String>("200") { it.description("Owner address") }


    fun tokenCollectionInfo(ctx: Context){
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val result = NftService.getTokenCollectionInfo(Chain.valueOf(chain?.uppercase()!!), contractAddress!!)
        ctx.json(
            result!!
        )
    }

    fun tokenCollectionInfoDocs() = document().operation {
        it.summary("Get Token info")
            .operationId("tokenInfo").addTagsItem("Blockchain: Non-fungible tokens(NFTs)")
    }.pathParam<String>("chain") {
        it.schema<Chain> {  }
    }.pathParam<String>("contractAddress") {
    }.json<TokenCollectionInfo>("200") { it.description("Token info") }


}