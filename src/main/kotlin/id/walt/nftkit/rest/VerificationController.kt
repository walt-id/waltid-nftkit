package id.walt.nftkit.rest

import cc.vileda.openapi.dsl.schema
import id.walt.nftkit.services.*
import io.javalin.http.Context
import io.javalin.plugin.openapi.dsl.document
import kotlinx.serialization.Serializable
import java.math.BigInteger



@Serializable
data class VerifyCollectionRequest(
    val accountAddress: String,
)

@Serializable
data class VerifyTraitRequest(
    val accountAddress: String,
    val traitType: String,
    val traitValue: String? = null,
)

object VerificationController {

    fun verifyCollection(ctx: Context) {
        val req = ctx.bodyAsClass(VerifyCollectionRequest::class.java)
        val chain = ctx.pathParam("chain").let {
            if (it.isEmpty()){
                throw Exception("No chain defined")
            }
            Chain.valueOf(it.uppercase())
        }

        val contractAddress = ctx.pathParam("contractAddress")

        val result = VerificationService.verifyCollection(chain, contractAddress, req.accountAddress)
        ctx.json(result)
    }

    fun verifyCollectionDocs() = document().operation {
        it.summary("Owner NFT verification")
            .operationId("NftVerification").addTagsItem("NFT verification")
    }.pathParam<String>("chain") {
        it.schema<Chain> {  }
    }.pathParam<String>("contractAddress") {
    }.body<VerifyCollectionRequest> {
        it.description("")
    }.json<Boolean>("200") {  }

    fun verifyCollectionWithTraits(ctx: Context) {
        val req = ctx.bodyAsClass(VerifyTraitRequest::class.java)
        val chain = ctx.pathParam("chain").let {
            if (it.isEmpty()){
                throw Exception("No chain defined")
            }
            Chain.valueOf(it.uppercase())
        }

        val contractAddress = ctx.pathParam("contractAddress")

        val result = VerificationService.verifyTrait(chain, contractAddress, req.accountAddress, req.traitType,req.traitValue)
        ctx.json(result)
    }


    fun verifyCollectionWithTraitsDocs() = document().operation {
        it.summary("Owner NFT verification with Traits")
            .operationId("NftAndTraitsVerification").addTagsItem("NFT verification")
    }.pathParam<String>("chain") {
        it.schema<Chain> {  }
    }.pathParam<String>("contractAddress") {
    }.body<VerifyTraitRequest> {
        it.description("")
    }.json<Boolean>("200") {  }

}