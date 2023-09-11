package id.walt.nftkit.rest

import cc.vileda.openapi.dsl.schema
import id.walt.nftkit.services.EVMChain
import id.walt.nftkit.services.ExtensionsService
import id.walt.nftkit.services.NftMetadata
import id.walt.nftkit.services.TransactionResponse
import id.walt.nftkit.utilis.Common
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
    val TAG1 = "EVM based Blockchain"
    fun paused(ctx: Context) {
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val result = ExtensionsService.paused(Common.getEVMChain(chain.uppercase()), contractAddress)
        ctx.json(
            result
        )
    }

    fun pausedDocs() = document().operation {
        it.summary("Paused")
            .operationId("paused").addTagsItem(TAG1)
    }.pathParam<String>("chain") {
        it.schema<EVMChain> { }
    }.pathParam<String>("contractAddress") {
    }.json<Boolean>("200") { }

    fun pause(ctx: Context) {
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val result = ExtensionsService.pause(Common.getEVMChain(chain.uppercase()), contractAddress)
        ctx.json(
            result
        )
    }

    fun pauseDocs() = document().operation {
        it.summary("Pause")
            .operationId("pause").addTagsItem(TAG1)
    }.pathParam<String>("chain") {
        it.schema<EVMChain> { }
    }.pathParam<String>("contractAddress") {
    }.json<TransactionResponse>("200") { }

    fun unpause(ctx: Context) {
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val result = ExtensionsService.unpause(Common.getEVMChain(chain.uppercase()), contractAddress)
        ctx.json(
            result
        )
    }

    fun unpauseDocs() = document().operation {
        it.summary("Unpause")
            .operationId("unpause").addTagsItem(TAG1)
    }.pathParam<String>("chain") {
        it.schema<EVMChain> { }
    }.pathParam<String>("contractAddress") {
    }.json<TransactionResponse>("200") { }

    fun updateTokenURI(ctx: Context) {
        val updateTokenURIReq = ctx.bodyAsClass(UpdateTokenURIRequest::class.java)
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val tokenId = ctx.pathParam("tokenId")
        val signedAccount = ctx.queryParam("signedAccount")
        val result =
            ExtensionsService.setTokenURI(Common.getEVMChain(chain.uppercase()), contractAddress, tokenId, signedAccount, updateTokenURIReq)
        ctx.json(
            result
        )
    }

    fun updateTokenURIDocs() = document().operation {
        it.summary("Update Token URI")
            .operationId("setTokenURI").addTagsItem(TAG1)
    }.pathParam<String>("chain") {
        it.schema<EVMChain> { }
    }.pathParam<String>("contractAddress") {
    }.body<UpdateTokenURIRequest> {
        it.description("")
    }.queryParam<String>("signedAccount") {
        it.required(false)
    }.json<TransactionResponse>("200") { }

    fun getTransferable(ctx: Context) {
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val result = ExtensionsService.getTransferable(Common.getEVMChain(chain.uppercase()), contractAddress)
        ctx.json(
            result
        )
    }

    fun getTransferableDocs() = document().operation {
        it.summary("Transferable option")
            .operationId("Transferable option").addTagsItem(TAG1)
    }.pathParam<String>("chain") {
        it.schema<EVMChain> { }
    }.pathParam<String>("contractAddress") {
    }.json<Boolean>("200") { }


    fun setTransferable(ctx: Context) {
        val transferableReq = ctx.bodyAsClass(TransferableRequest::class.java)
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val result = ExtensionsService.setTransferable(
            Common.getEVMChain(chain.uppercase()),
            contractAddress,
            transferableReq.transferable
        )
        ctx.json(
            result
        )
    }

    fun setTransferableDocs() = document().operation {
        it.summary("Activate/Deactivate transferable option")
            .operationId("Activate/Deactivate transferable option").addTagsItem(TAG1)
    }.pathParam<String>("chain") {
        it.schema<EVMChain> { }
    }.pathParam<String>("contractAddress") {
    }.body<TransferableRequest> {
        it.description("")
    }.json<TransactionResponse>("200") { }

    fun getBurnable(ctx: Context) {
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val result = ExtensionsService.getBurnable(Common.getEVMChain(chain.uppercase()), contractAddress)
        ctx.json(
            result
        )
    }

    fun getBurnableDocs() = document().operation {
        it.summary("Burnable option")
            .operationId("Burnable option").addTagsItem(TAG1)
    }.pathParam<String>("chain") {
        it.schema<EVMChain> { }
    }.pathParam<String>("contractAddress") {
    }.json<Boolean>("200") { }


    fun setBurnable(ctx: Context) {
        val burnableReq = ctx.bodyAsClass(BurnableRequest::class.java)
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val result = ExtensionsService.setBurnable(Common.getEVMChain(chain.uppercase()), contractAddress, burnableReq.burnable)
        ctx.json(
            result
        )
    }

    fun setBurnableDocs() = document().operation {
        it.summary("Activate/Deactivate burnable option")
            .operationId("Activate/Deactivate burnable option").addTagsItem(TAG1)
    }.pathParam<String>("chain") {
        it.schema<EVMChain> { }
    }.pathParam<String>("contractAddress") {
    }.body<BurnableRequest> {
        it.description("")
    }.json<TransactionResponse>("200") { }


    fun burn(ctx: Context) {
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val tokenId = ctx.pathParam("tokenId")
        val result = ExtensionsService.burn(Common.getEVMChain(chain.uppercase()), contractAddress, BigInteger(tokenId))
        ctx.json(
            result
        )
    }

    fun burnDocs() = document().operation {
        it.summary("Burn token")
            .operationId("Burn token").addTagsItem(TAG1)
    }.pathParam<String>("chain") {
        it.schema<EVMChain> { }
    }.pathParam<String>("contractAddress") {
    }.json<TransactionResponse>("200") { }
}
