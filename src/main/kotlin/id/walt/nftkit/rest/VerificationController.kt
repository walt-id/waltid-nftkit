package id.walt.nftkit.rest

import cc.vileda.openapi.dsl.schema
import id.walt.nftkit.services.Chain
import id.walt.nftkit.services.VerificationService
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.plugin.openapi.dsl.document
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable


object VerificationController {


    fun verifyNftOwnershipWithinCollection(ctx: Context) {
        val chain = ctx.pathParam("chain").let {
            if (it.isEmpty()) {
                throw Exception("No chain defined")
            }
            Chain.valueOf(it.uppercase())
        }

        val contractAddress = ctx.pathParam("contractAddress")
        val account = ctx.queryParam("account") ?: throw  BadRequestResponse("Account not specified")
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
        it.required(true)
    }.json<Boolean>("200") { }

     fun verifyNftOwnership(ctx: Context) {
        val chain = ctx.pathParam("chain").let {
            if (it.isEmpty()) {
                throw Exception("No chain defined")
            }
            Chain.valueOf(it.uppercase())
        }

        val contractAddress = ctx.pathParam("contractAddress")
         val account = ctx.queryParam("account") ?: throw  BadRequestResponse("Account not specified")
         val tokenId = ctx.queryParam("tokenId") ?: throw  BadRequestResponse("Token Id not specified")
         runBlocking {
             val result = VerificationService.verifyNftOwnership(chain, contractAddress, account, tokenId)
             ctx.json(result)
         }
    }

    fun verifyNftOwnershipDocs() = document().operation {
        it.summary("NFT ownership verification")
            .operationId("NFTOwnershipVerification").addTagsItem("NFT verification")
    }.pathParam<String>("chain") {
        it.schema<Chain> { }
    }.pathParam<String>("contractAddress") {
    }.queryParam<String>("account") {
        it.required(true)
    }.queryParam<String>("tokenId") {
        it.required(true)
    }.json<Boolean>("200") { }

    fun verifyNftOwnershipWithTraits(ctx: Context) {
        val chain = ctx.pathParam("chain").let {
            if (it.isEmpty()) {
                throw Exception("No chain defined")
            }
            Chain.valueOf(it.uppercase())
        }

        val contractAddress = ctx.pathParam("contractAddress")
        val account = ctx.queryParam("account") ?: throw  BadRequestResponse("Account not specified")
        val tokenId = ctx.queryParam("tokenId") ?: throw  BadRequestResponse("Token Id not specified")
        val traitType = ctx.queryParam("traitType") ?: throw  BadRequestResponse("Trait type  not specified")
        val traitValue = ctx.queryParam("traitValue") ?: null

        val result = VerificationService.verifyNftOwnershipWithTraits(chain, contractAddress, account, tokenId, traitType,traitValue)
        ctx.json(result)
    }


    fun verifyNftOwnershipWithTraitsDocs() = document().operation {
        it.summary("NFT ownership verification with traits")
            .operationId("NFTOwnershipVerificationWithTraits").addTagsItem("NFT verification")
    }.pathParam<String>("chain") {
        it.schema<Chain> { }
    }.pathParam<String>("contractAddress") {
    }.queryParam<String>("account") {
        it.required(true)
    }.queryParam<String>("tokenId") {
        it.required(true)
    }.queryParam<String>("traitType") {
        it.required(true)
    }.queryParam<String>("traitValue") {
    }.json<Boolean>("200") { }

    fun oceanDaoVerification(ctx: Context) {
        val chain = ctx.pathParam("chain").let {
            if (it.isEmpty()) {
                throw Exception("No chain defined")
            }
            Chain.valueOf(it.uppercase())
        }
        val contractAddress = ctx.pathParam("contractAddress")
        val account = ctx.queryParam("account") ?: throw  BadRequestResponse("Account not specified")
        val factoryContractAddress = ctx.queryParam("factoryContractAddress") ?: throw  BadRequestResponse("Factory contract address not specified")
        val propertyKey = ctx.queryParam("propertyKey") ?: throw  BadRequestResponse("Property key not specified")
        val propertyValue = ctx.queryParam("propertyValue") ?: null

        val result = VerificationService.dataNftVerification(chain, factoryContractAddress,contractAddress, account, propertyKey, propertyValue)
        ctx.json(result)
    }

    fun oceanDaoVerificationDocs() = document().operation {
        it.summary("Data NFT Verification")
            .operationId("OceanDaoVerification").addTagsItem("NFT verification")
    }.pathParam<String>("chain") {
        it.schema<Chain> { }
    }.pathParam<String>("contractAddress") {
    }.queryParam<String>("account") {
        it.required(true)
    }.queryParam<String>("factoryContractAddress") {
        it.required(true)
    }.queryParam<String>("propertyKey") {
        it.required(true)
    }.queryParam<String>("propertyValue") {
    }.json<Boolean>("200") { }

}
