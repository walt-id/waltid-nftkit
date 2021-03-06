package id.walt.nftkit.rest

import cc.vileda.openapi.dsl.schema
import id.walt.nftkit.metadata.NFTStorageAddFileResult
import id.walt.nftkit.services.*
import id.walt.nftkit.utilis.Common
import io.javalin.http.Context
import io.javalin.plugin.openapi.dsl.document
import kotlinx.serialization.Serializable
import java.math.BigInteger


@Serializable
data class MintRequest(
    val metadataUri: String?,
    val metadata: NftMetadata?,
    val recipientAddress: String,
    val metadataStorageType: MetadataStorageType,
    //val offChainMetadataStorageType: OffChainMetadataStorageType?,
)

@Serializable
data class DeployRequest(
    val name: String,
    val symbol: String,
    val tokenStandard: TokenStandard,
    val accessControl: AccessControl,
    val options: DeploymentParameter.Options
)

@Serializable
data class TraitUpdateRequest(
    val key: String,
    val value: String,
)

object NftController {

    fun deploy(ctx: Context) {
        val deployReq = ctx.bodyAsClass(DeployRequest::class.java)
        val chain = ctx.pathParam("chain")
        val deploymentOptions = DeploymentOptions(deployReq.accessControl, deployReq.tokenStandard)
        val deploymentParameter = DeploymentParameter(deployReq.name, deployReq.symbol, deployReq.options)
        val result =
            NftService.deploySmartContractToken(Chain.valueOf(chain.uppercase()), deploymentParameter, deploymentOptions)
        ctx.json(
            result
        )
    }

    fun deployDocs() = document().operation {
        it.summary("Smart contract deployment")
            .operationId("deploySmartContract").addTagsItem("Blockchain: Non-fungible tokens(NFTs)")
    }.pathParam<String>("chain") {
        it.schema<Chain> { }
    }.body<DeployRequest> {
        it.description("")
    }.json<DeploymentResponse>("200") { it.description("Transaction ID and smart contract address") }


    fun mint(ctx: Context) {
        val mintReq = ctx.bodyAsClass(MintRequest::class.java)
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val mintingParameter = MintingParameter(mintReq.metadataUri, mintReq.recipientAddress, mintReq.metadata)
        val mintingOptions = MintingOptions(mintReq.metadataStorageType)
        val result =
            NftService.mintToken(Chain.valueOf(chain.uppercase()), contractAddress, mintingParameter, mintingOptions)
        ctx.json(
            result
        )
    }

    fun mintDocs() = document().operation {
        it.summary("NFT minting")
            .operationId("mintNft").addTagsItem("Blockchain: Non-fungible tokens(NFTs)")
    }.pathParam<String>("chain") {
        it.schema<Chain> { }
    }.pathParam<String>("contractAddress") {
    }.body<MintRequest> {
        it.description("")
    }.json<MintingResponse>("200") { it.description("Transaction ID and token ID") }


    fun getNftMetadatUri(ctx: Context) {
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val tokenId = ctx.pathParam("tokenId").toLong()
        val result =
            NftService.getNftMetadataUri(Chain.valueOf(chain.uppercase()), contractAddress, BigInteger.valueOf(tokenId))
        ctx.json(
            result
        )
    }

    fun getNftMetadatUriDocs() = document().operation {
        it.summary("Get NFT Token Metadata URI")
            .operationId("MetadataUri").addTagsItem("Blockchain: Non-fungible tokens(NFTs)")
    }.pathParam<String>("chain") {
        it.schema<Chain> { }
    }.pathParam<String>("contractAddress") {

    }.pathParam<Int>("tokenId") {
    }.json<String>("200") { it.description("NFT Metadata URI") }


    fun getNftMetadata(ctx: Context) {
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val tokenId = ctx.pathParam("tokenId").toLong()
        val result =
            NftService.getNftMetadata(Chain.valueOf(chain.uppercase()), contractAddress, BigInteger.valueOf(tokenId))
        ctx.json(
            result
        )
    }

    fun getNftMetadataDocs() = document().operation {
        it.summary("Get NFT Token Metadata")
            .operationId("Metadata").addTagsItem("Blockchain: Non-fungible tokens(NFTs)")
    }.pathParam<String>("chain") {
        it.schema<Chain> { }
    }.pathParam<String>("contractAddress") {
    }.pathParam<Int>("tokenId") {
    }.json<NftMetadata>("200") { it.description("NFT Metadata") }


    fun balance(ctx: Context) {
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val ownerAdress = ctx.pathParam("ownerAddress")
        val result = NftService.balanceOf(Chain.valueOf(chain.uppercase()), contractAddress, ownerAdress)
        ctx.json(
            result!!
        )
    }

    fun balanceDocs() = document().operation {
        it.summary("Get Account balance")
            .operationId("balance").addTagsItem("Blockchain: Non-fungible tokens(NFTs)")
    }.pathParam<String>("chain") {
        it.schema<Chain> { }
    }.pathParam<String>("contractAddress") {
    }.pathParam<String>("ownerAddress") {
    }.json<BigInteger>("200") { it.description("Account balance") }


    fun owner(ctx: Context) {
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val tokenId = ctx.pathParam("tokenId")
        val result = NftService.ownerOf(Chain.valueOf(chain.uppercase()), contractAddress, BigInteger(tokenId))
        ctx.json(
            result!!
        )
    }

    fun ownerDocs() = document().operation {
        it.summary("Get Token owner")
            .operationId("owner").addTagsItem("Blockchain: Non-fungible tokens(NFTs)")
    }.pathParam<String>("chain") {
        it.schema<Chain> { }
    }.pathParam<String>("contractAddress") {
    }.pathParam<Int>("tokenId") {
    }.json<String>("200") { it.description("Owner address") }


    fun tokenCollectionInfo(ctx: Context) {
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val result = NftService.getTokenCollectionInfo(Chain.valueOf(chain.uppercase()), contractAddress)
        ctx.json(
            result
        )
    }

    fun tokenCollectionInfoDocs() = document().operation {
        it.summary("Get Token info")
            .operationId("tokenInfo").addTagsItem("Blockchain: Non-fungible tokens(NFTs)")
    }.pathParam<String>("chain") {
        it.schema<Chain> { }
    }.pathParam<String>("contractAddress") {
    }.json<TokenCollectionInfo>("200") { it.description("Token info") }

    fun getAccountNFTs(ctx: Context) {
        val chain = ctx.pathParam("chain")
        val ownerAdress = ctx.pathParam("ownerAddress")
        val result = NftService.getAccountNFTsByAlchemy(Chain.valueOf(chain.uppercase()), ownerAdress)
        ctx.json(
            result
        )
    }

    fun getAccountNFTsDocs() = document().operation {
        it.summary("Get NFTs")
            .operationId("GetNFTs").addTagsItem("Blockchain: Non-fungible tokens(NFTs)")
    }.pathParam<String>("chain") {
        it.schema<Chain> { }
    }.pathParam<String>("ownerAddress") {
    }.json<Array<NFTsAlchemyResult.NftTokenByAlchemy>>("200") { it.description("NFTs list") }

    fun updateMetadata(ctx: Context) {
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val tokenId = ctx.pathParam("tokenId")
        val signedAccount = ctx.queryParam("signedAccount")
        val traitUpdateRequest = ctx.bodyAsClass(TraitUpdateRequest::class.java)
        val result = NftService.updateMetadata(Common.getChain(chain), contractAddress, tokenId,
            signedAccount,traitUpdateRequest.key, traitUpdateRequest.value)
        ctx.json(
            result
        )
    }

    fun updateMetadataDocs() = document().operation {
        it.summary("Metadata update")
            .operationId("updateMetadata").addTagsItem("Blockchain: Non-fungible tokens(NFTs)")
    }.pathParam<String>("chain") {
        it.schema<Chain> {  }
    }.pathParam<String>("contractAddress") {
    }.pathParam<String>("tokenId") {
    }.queryParam<String>("signedAccount") {
        it.required(false)
    }.body<TraitUpdateRequest> {
        it.description("")
    }.json<TransactionResponse>("200") {  }


    fun uploadFileToIpfs(ctx: Context) {
         ctx.uploadedFiles().forEach { (contentType, content, name, extension) ->
            //FileUtils.copyInputStreamToFile(content, File("upload/" + name))
            val fileData = contentType.readAllBytes()
            val result= NftService.addFileToIpfsUsingNFTStorage(fileData)
             ctx.json(
                 result
             )
        }
    }

    fun uploadFileToIpfsDocs() = document().operation {
        it.summary("Upload file to IPFS")
            .operationId("Upload file to IPFS").addTagsItem("Blockchain: Non-fungible tokens(NFTs)")
    }.uploadedFile("file") {
        // RequestBody, e.g.
        it.description = "File"
        it.required = true
    }.json<NFTStorageAddFileResult>("200") { }


}
