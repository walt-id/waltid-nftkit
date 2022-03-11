package id.walt.nftkit.rest

import id.walt.nftkit.services.*
import io.javalin.http.Context
import kotlinx.serialization.Serializable
import java.math.BigInteger


@Serializable
data class mintRequest(
    val chain: Chain,
    val contractAddress: String,
    val metadataUri : String?,
    val metadata: NftMetadata?,
    val recipientAddress: String,
    val metadataStorageType: MetadataStorageType,
    val offChainMetadataStorageType: OffChainMetadataStorageType?,
)

@Serializable
data class deployRequest(
    val chain: Chain,
    val name: String?,
    val symbol : String?,
    val tokenStandard: TokenStandard
)

object NftController {

    fun deploy(ctx: Context){
        val deployReq = ctx.bodyAsClass(deployRequest::class.java)
        val deploymentOptions = DeploymentOptions(tokenStandard = deployReq.tokenStandard)
        val deploymentParameter = DeploymentParameter(deployReq.name!!, deployReq.symbol!!,"","")
        val result = NftService.deploySmartContractToken(deployReq.chain, deploymentParameter, deploymentOptions)
        ctx.json(
            result
        )
    }

    fun mint(ctx: Context) {
        val mintReq = ctx.bodyAsClass(mintRequest::class.java)
        val mintingParameter = MintingParameter(mintReq.metadataUri, mintReq.recipientAddress,mintReq.metadata)
        val mintingOptions = MintingOptions(mintReq.metadataStorageType, mintReq.offChainMetadataStorageType)
        val result = NftService.mintToken(mintReq.chain,mintReq.contractAddress, mintingParameter, mintingOptions)
        ctx.json(
            result
        )
    }

    fun getNftMetadatUri(ctx: Context){
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val tokenId = ctx.pathParam("tokenId").toLong()!!
        val result = NftService.getNftMetadataUri(Chain.valueOf(chain?.uppercase()!!), contractAddress!!, BigInteger.valueOf(tokenId))
        ctx.json(
            result!!
        )
    }


    fun getNftMetadata(ctx: Context){
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val tokenId = ctx.pathParam("tokenId").toLong()!!
        val result = NftService.getNftMetadata(Chain.valueOf(chain?.uppercase()!!), contractAddress!!, BigInteger.valueOf(tokenId))
        ctx.json(
            result!!
        )
    }

    fun balance(ctx: Context){
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val ownerAdress = ctx.pathParam("ownerAddress")
        val result = NftService.balanceOf(Chain.valueOf(chain?.uppercase()!!), contractAddress!!, ownerAdress)
        ctx.json(
            result!!
        )
    }

    fun owner(ctx: Context){
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val tokenId = ctx.pathParam("tokenId").toLong()!!
        val result = NftService.ownerOf(Chain.valueOf(chain?.uppercase()!!), contractAddress!!, tokenId)
        ctx.json(
            result!!
        )
    }

    fun tokenCollectionInfo(ctx: Context){
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val result = NftService.getTokenCollectionInfo(Chain.valueOf(chain?.uppercase()!!), contractAddress!!)
        ctx.json(
            result!!
        )
    }


}