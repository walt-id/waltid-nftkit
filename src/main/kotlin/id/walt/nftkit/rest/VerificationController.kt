package id.walt.nftkit.rest

import cc.vileda.openapi.dsl.schema
import id.walt.nftkit.services.Chain
import id.walt.nftkit.services.VerificationService
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.plugin.openapi.dsl.document
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable


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


    fun verifyNftOwnershipWithinCollection(ctx: Context) {
        val chain = ctx.pathParam("chain").let {
            if (it.isEmpty()) {
                throw Exception("No chain defined")
            }
            Chain.valueOf(it.uppercase())
        }

        val contractAddress = ctx.pathParam("contractAddress")
        val account = ctx.queryParam("account") ?: throw  BadRequestResponse("account not specified")
        val result = VerificationService.verifyNftOwnershipWithinCollection(chain, contractAddress, account!!)
        ctx.json(result)
    }

    fun verifyNftOwnershipWithinCollectionDocs() = document().operation {
        it.summary("NFT ownership verification within a collection")
            .operationId("NFTOwnershipVerificationWithinCollection").addTagsItem("NFT verification")
    }.pathParam<String>("chain") {
        it.schema<Chain> { }
    }.pathParam<String>("contractAddress") {
    }.queryParam<String>("account") {
    }.json<Boolean>("200") { }

     fun verifyNftOwnership(ctx: Context) {
        val req = ctx.bodyAsClass(VerifyCollectionRequest::class.java)
        val chain = ctx.pathParam("chain").let {
            if (it.isEmpty()) {
                throw Exception("No chain defined")
            }
            Chain.valueOf(it.uppercase())
        }

        val contractAddress = ctx.pathParam("contractAddress")
         runBlocking {
             val result = VerificationService.verifyNftOwnership(chain, contractAddress, req.account, req.tokenId)
             ctx.json(result)
         }
    }

    fun verifyNftOwnershipDocs() = document().operation {
        it.summary("NFT ownership verification")
            .operationId("NFTOwnershipVerification").addTagsItem("NFT verification")
    }.pathParam<String>("chain") {
        it.schema<Chain> { }
    }.pathParam<String>("contractAddress") {
    }.body<VerifyCollectionRequest> {
        it.description("")
    }.json<Boolean>("200") { }

    fun verifyNftOwnershipWithTraits(ctx: Context) {
        val req = ctx.bodyAsClass(VerifyTraitRequest::class.java)
        val chain = ctx.pathParam("chain").let {
            if (it.isEmpty()) {
                throw Exception("No chain defined")
            }
            Chain.valueOf(it.uppercase())
        }

        val contractAddress = ctx.pathParam("contractAddress")


        val result = VerificationService.verifyNftOwnershipWithTraits(chain, contractAddress, req.account, req.tokenId, req.traitType,req.traitValue)
        ctx.json(result)
    }


    fun verifyNftOwnershipWithTraitsDocs() = document().operation {
        it.summary("NFT ownership verification with traits")
            .operationId("NFTOwnershipVerificationWithTraits").addTagsItem("NFT verification")
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
        val result = VerificationService.dataNftVerification(chain, req.factoryContractAddress,contractAddress, req.account, req.propertyKey, req.propertyValue)
        ctx.json(result)
    }

    fun oceanDaoVerificationDocs() = document().operation {
        it.summary("Data NFT Verification")
            .operationId("OceanDaoVerification").addTagsItem("NFT verification")
    }.pathParam<String>("chain") {
        it.schema<Chain> { }
    }.pathParam<String>("contractAddress") {
    }.body<OceanDaoVerificationRequest> {
        it.description("")
    }.json<Boolean>("200") { }

}
