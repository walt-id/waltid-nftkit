package id.walt.nftkit.rest

import id.walt.nftkit.services.*
import id.walt.nftkit.utilis.Common
import io.javalin.http.Context
import kotlinx.serialization.Serializable


@Serializable
data class NearMintRequest(
   val accountId: String,
    val tokenId: String,
    val recipientAddress: String,
    val metadata: NearTokenMetadata?,
)

object NearNftController {

    fun mint(ctx: Context) {
        val mintReq = ctx.bodyAsClass(NearMintRequest::class.java)

        val contractAddress = ctx.pathParam("contractAddress")
        val parameter = NearMintingParameter(mintReq.recipientAddress, mintReq.tokenId, mintReq.accountId,mintReq.metadata)
        val result= NearNftService.mintNftToken(contractAddress, parameter)
        ctx.json(result)
    }
}