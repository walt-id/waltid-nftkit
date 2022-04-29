package id.walt.nftkit.rest

import cc.vileda.openapi.dsl.schema
import id.walt.nftkit.services.*
import io.javalin.http.Context
import io.javalin.plugin.openapi.dsl.document
import kotlinx.serialization.Serializable
import java.math.BigInteger

@Serializable
data class UpdateTokenURIRequest(
    val metadataUri: String?,
    val metadata: NftMetadata?,
)

data class TransferableRequest(val transferable: Boolean)
data class BurnableRequest(val burnable: Boolean)


object ExtensionsController {

    fun paused(ctx: Context) {
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val result = ExtensionsService.paused(Chain.valueOf(chain?.uppercase()!!), contractAddress)
        ctx.json(
            result
        )
    }

    fun pausedDocs() = document().operation {
        it.summary("Paused")
            .operationId("paused").addTagsItem("NFTs smart contract Extensions")
    }.pathParam<String>("chain") {
        it.schema<Chain> { }
    }.pathParam<String>("contractAddress") {
    }.json<Boolean>("200") { }

    fun pause(ctx: Context) {
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val result = ExtensionsService.pause(Chain.valueOf(chain?.uppercase()!!), contractAddress)
        ctx.json(
            result
        )
    }

    fun pauseDocs() = document().operation {
        it.summary("Pause")
            .operationId("pause").addTagsItem("NFTs smart contract Extensions")
    }.pathParam<String>("chain") {
        it.schema<Chain> { }
    }.pathParam<String>("contractAddress") {
    }.json<TransactionResponse>("200") { }

    fun unpause(ctx: Context) {
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val result = ExtensionsService.unpause(Chain.valueOf(chain?.uppercase()!!), contractAddress)
        ctx.json(
            result
        )
    }

    fun unpauseDocs() = document().operation {
        it.summary("Unpause")
            .operationId("unpause").addTagsItem("NFTs smart contract Extensions")
    }.pathParam<String>("chain") {
        it.schema<Chain> { }
    }.pathParam<String>("contractAddress") {
    }.json<TransactionResponse>("200") { }

    fun updateTokenURI(ctx: Context) {
        val updateTokenURIReq = ctx.bodyAsClass(UpdateTokenURIRequest::class.java)
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val tokenId = ctx.pathParam("tokenId")
        val result =
            ExtensionsService.setTokenURI(Chain.valueOf(chain?.uppercase()!!), contractAddress, tokenId, updateTokenURIReq)
        ctx.json(
            result
        )
    }

    fun updateTokenURIDocs() = document().operation {
        it.summary("Update Token URI")
            .operationId("setTokenURI").addTagsItem("NFTs smart contract Extensions")
    }.pathParam<String>("chain") {
        it.schema<Chain> { }
    }.pathParam<String>("contractAddress") {
    }.body<UpdateTokenURIRequest> {
        it.description("")
    }.json<TransactionResponse>("200") { }

    fun getTransferable(ctx: Context) {
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val result = ExtensionsService.getTransferable(Chain.valueOf(chain?.uppercase()!!), contractAddress)
        ctx.json(
            result
        )
    }

    fun getTransferableDocs() = document().operation {
        it.summary("Transferable option")
            .operationId("Transferable option").addTagsItem("NFTs smart contract Extensions")
    }.pathParam<String>("chain") {
        it.schema<Chain> { }
    }.pathParam<String>("contractAddress") {
    }.json<Boolean>("200") { }


    fun setTransferable(ctx: Context) {
        val transferableReq = ctx.bodyAsClass(TransferableRequest::class.java)
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val result = ExtensionsService.setTransferable(
            Chain.valueOf(chain?.uppercase()!!),
            contractAddress,
            transferableReq.transferable
        )
        ctx.json(
            result
        )
    }

    fun setTransferableDocs() = document().operation {
        it.summary("Activate/Deactivate transferable option")
            .operationId("Activate/Deactivate transferable option").addTagsItem("NFTs smart contract Extensions")
    }.pathParam<String>("chain") {
        it.schema<Chain> { }
    }.pathParam<String>("contractAddress") {
    }.body<TransferableRequest> {
        it.description("")
    }.json<TransactionResponse>("200") { }

    fun getBurnable(ctx: Context) {
        val burnableReq = ctx.bodyAsClass(BurnableRequest::class.java)
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val result = ExtensionsService.setBurnable(Chain.valueOf(chain?.uppercase()!!), contractAddress, burnableReq.burnable)
        ctx.json(
            result
        )
    }

    fun getBurnableDocs() = document().operation {
        it.summary("Burnable option")
            .operationId("Burnable option").addTagsItem("NFTs smart contract Extensions")
    }.pathParam<String>("chain") {
        it.schema<Chain> { }
    }.pathParam<String>("contractAddress") {
    }.json<Boolean>("200") { }


    fun setBurnable(ctx: Context) {
        val burnableReq = ctx.bodyAsClass(BurnableRequest::class.java)
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val result = ExtensionsService.setBurnable(Chain.valueOf(chain?.uppercase()!!), contractAddress, burnableReq.burnable)
        ctx.json(
            result
        )
    }

    fun setBurnableDocs() = document().operation {
        it.summary("Activate/Deactivate burnable option")
            .operationId("Activate/Deactivate burnable option").addTagsItem("NFTs smart contract Extensions")
    }.pathParam<String>("chain") {
        it.schema<Chain> { }
    }.pathParam<String>("contractAddress") {
    }.body<BurnableRequest> {
        it.description("")
    }.json<TransactionResponse>("200") { }


    fun burn(ctx: Context) {
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val tokenId = ctx.pathParam("tokenId")
        val result = ExtensionsService.burn(Chain.valueOf(chain?.uppercase()!!), contractAddress, BigInteger(tokenId))
        ctx.json(
            result
        )
    }

    fun burnDocs() = document().operation {
        it.summary("Burn token")
            .operationId("Burn token").addTagsItem("NFTs smart contract Extensions")
    }.pathParam<String>("chain") {
        it.schema<Chain> { }
    }.pathParam<String>("contractAddress") {
    }.json<TransactionResponse>("200") { }
}
