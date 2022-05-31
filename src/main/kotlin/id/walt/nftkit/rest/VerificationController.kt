package id.walt.nftkit.rest

import cc.vileda.openapi.dsl.schema
import id.walt.nftkit.services.Chain
import id.walt.nftkit.services.VerificationService
import io.javalin.http.Context
import io.javalin.plugin.openapi.dsl.document
import kotlinx.serialization.Serializable
import java.math.BigInteger


@Serializable
data class VerifyCollectionRequest(
    val account: String,
    val tokenId: String
)

@Serializable
data class VerifyTraitRequest(
    val account: String,
    val tokenId: String,
    val traitType: String,
    val traitValue: String? = null,
)

@Serializable
data class OceanDaoVerificationRequest(
    val account: String,
    val factoryContractAddress: String,
    val propertyKey: String?= null,
    val propertyValue: String?= null,
)

object VerificationController {

    fun verifyCollection(ctx: Context) {
        val req = ctx.bodyAsClass(VerifyCollectionRequest::class.java)
        val chain = ctx.pathParam("chain").let {
            if (it.isEmpty()) {
                throw Exception("No chain defined")
            }
            Chain.valueOf(it.uppercase())
        }

        val contractAddress = ctx.pathParam("contractAddress")

        val result = VerificationService.verifyCollection(chain, contractAddress, req.account, req.tokenId)
        ctx.json(result)
    }

    fun verifyCollectionDocs() = document().operation {
        it.summary("Owner NFT verification")
            .operationId("NftVerification").addTagsItem("NFT verification")
    }.pathParam<String>("chain") {
        it.schema<Chain> { }
    }.pathParam<String>("contractAddress") {
    }.body<VerifyCollectionRequest> {
        it.description("")
    }.json<Boolean>("200") { }

    fun verifyCollectionWithTraits(ctx: Context) {
        val req = ctx.bodyAsClass(VerifyTraitRequest::class.java)
        val chain = ctx.pathParam("chain").let {
            if (it.isEmpty()) {
                throw Exception("No chain defined")
            }
            Chain.valueOf(it.uppercase())
        }

        val contractAddress = ctx.pathParam("contractAddress")


        val result = VerificationService.verifyTrait(chain, contractAddress, req.account, req.tokenId, req.traitType,req.traitValue)
        ctx.json(result)
    }


    fun verifyCollectionWithTraitsDocs() = document().operation {
        it.summary("Owner NFT verification with Traits")
            .operationId("NftAndTraitsVerification").addTagsItem("NFT verification")
    }.pathParam<String>("chain") {
        it.schema<Chain> { }
    }.pathParam<String>("contractAddress") {
    }.body<VerifyTraitRequest> {
        it.description("")
    }.json<Boolean>("200") { }

    fun oceanDaoVerification(ctx: Context) {
        val req = ctx.bodyAsClass(OceanDaoVerificationRequest::class.java)
        val chain = ctx.pathParam("chain").let {
            if (it.isEmpty()) {
                throw Exception("No chain defined")
            }
            Chain.valueOf(it.uppercase())
        }
        println(req)
        val contractAddress = ctx.pathParam("contractAddress")
        val result = VerificationService.oceanDaoVerification(chain, req.factoryContractAddress,contractAddress, req.account, req.propertyKey, req.propertyValue)
        ctx.json(result)
    }

    fun oceanDaoVerificationDocs() = document().operation {
        it.summary("OceanDao Verification")
            .operationId("OceanDaoVerification").addTagsItem("NFT verification")
    }.pathParam<String>("chain") {
        it.schema<Chain> { }
    }.pathParam<String>("contractAddress") {
    }.body<OceanDaoVerificationRequest> {
        it.description("")
    }.json<Boolean>("200") { }

}
