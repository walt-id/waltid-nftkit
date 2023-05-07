package id.walt.nftkit.rest

import cc.vileda.openapi.dsl.schema
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import id.walt.nftkit.opa.DynamicPolicyArg
import id.walt.nftkit.opa.PolicyRegistry
import id.walt.nftkit.services.Chain
import id.walt.nftkit.services.EVMChain
import id.walt.nftkit.services.VerificationService
import id.walt.nftkit.utilis.Common
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.http.HttpCode
import io.javalin.plugin.openapi.dsl.document
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable


object VerificationController {


    fun verifyNftOwnershipWithinCollection(ctx: Context) {
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val account = ctx.queryParam("account") ?: throw  BadRequestResponse("Account not specified")
        val result = VerificationService.verifyNftOwnershipWithinCollection(Common.getChain(chain.uppercase()), contractAddress, account!!)
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
         val chain = ctx.pathParam("chain")
         val contractAddress = ctx.pathParam("contractAddress")
         val account = ctx.queryParam("account") ?: throw  BadRequestResponse("Account not specified")
         val tokenId = ctx.queryParam("tokenId") ?: throw  BadRequestResponse("Token Id not specified")
         runBlocking {
             val result = VerificationService.verifyNftOwnership(Common.getChain(chain.uppercase()), contractAddress, account, tokenId)
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
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val account = ctx.queryParam("account") ?: throw  BadRequestResponse("Account not specified")
        val tokenId = ctx.queryParam("tokenId") ?: throw  BadRequestResponse("Token Id not specified")
        val traitType = ctx.queryParam("traitType") ?: throw  BadRequestResponse("Trait type  not specified")
        val traitValue = ctx.queryParam("traitValue") ?: null

        val result = VerificationService.verifyNftOwnershipWithTraits(Common.getChain(chain.uppercase()), contractAddress, account, tokenId, traitType,traitValue)
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
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val account = ctx.queryParam("account") ?: throw  BadRequestResponse("Account not specified")
        val factoryContractAddress = ctx.queryParam("factoryContractAddress") ?: throw  BadRequestResponse("Factory contract address not specified")
        val propertyKey = ctx.queryParam("propertyKey") ?: throw  BadRequestResponse("Property key not specified")
        val propertyValue = ctx.queryParam("propertyValue") ?: null

        val result = VerificationService.dataNftVerification(Common.getEVMChain(chain.uppercase()), factoryContractAddress,contractAddress, account, propertyKey, propertyValue)
        ctx.json(result)
    }

    fun oceanDaoVerificationDocs() = document().operation {
        it.summary("Data NFT Verification")
            .operationId("OceanDaoVerification").addTagsItem("NFT verification")
    }.pathParam<String>("chain") {
        it.schema<EVMChain> { }
    }.pathParam<String>("contractAddress") {
    }.queryParam<String>("account") {
        it.required(true)
    }.queryParam<String>("factoryContractAddress") {
        it.required(true)
    }.queryParam<String>("propertyKey") {
        it.required(true)
    }.queryParam<String>("propertyValue") {
    }.json<Boolean>("200") { }

    fun createDynamicPolicy(ctx: Context) {
        val dynArg = Klaxon().parse<DynamicPolicyArg>(ctx.body()) ?: throw BadRequestResponse("Could not parse dynamic policy argument")
        val success = PolicyRegistry.createSavedPolicy(dynArg.name, dynArg)
        if(!success)
            ctx.status(HttpCode.BAD_REQUEST).result("Failed to create dynamic policy")
    }

    fun createDynamicPolicyDocs() = document().operation {
        it.summary("Create dynamic policy verification ").operationId("createDynamicPolicy").addTagsItem("NFT verification")
    }
        .body<DynamicPolicyArg>()
        .json<String>("200")

    fun listPolicies(ctx: Context) {
        ctx.json(PolicyRegistry.listPolicies())
    }

    fun listPoliciesDocs() = document().operation {
        it.summary("List verification policies").operationId("listPolicies").addTagsItem("NFT verification")
    }.json<Pair<String, DynamicPolicyArg>>("200")

    fun verifyNftPolicy(ctx: Context) {
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val tokenId = ctx.pathParam("tokenId")
        val policyName = ctx.pathParam("policyName")
        val result= policyName?.let { VerificationService.verifyPolicy(Common.getChain(chain), contractAddress, tokenId, it) }
        if (result != null) {
            ctx.json(result)
        }
    }

    fun verifyNftPolicyDocs() = document()
        .operation { it.summary("Verify an NFT metadata against a dynamic policy").operationId("verifyNftPolicy").addTagsItem("NFT verification") }
        .pathParam<String>("chain") {
            it.schema<Chain> { }
        }.pathParam<String>("contractAddress") {
        }.pathParam<String>("tokenId") {
        }.pathParam<String>("policyName") {
            it.required(true)
        }.jsonArray<Boolean>("200") { it.description("Request processed successfully (NFT metadata might not be valid)") }


    fun verifyNftOwnershipOnFlow(ctx: Context) {
        val chain = ctx.pathParam("chain")
        val contractAddress = ctx.pathParam("contractAddress")
        val account = ctx.queryParam("account") ?: throw  BadRequestResponse("Account not specified")
        val tokenId = ctx.queryParam("tokenId") ?: throw  BadRequestResponse("Token Id not specified")
        val collectionPath = ctx.queryParam("collectionPath") ?: throw  BadRequestResponse("Collection path not specified")
        val result = VerificationService.verifyNftOwnershipOnFlow(Common.getChain(chain.uppercase()), contractAddress, account, tokenId , collectionPath)
        ctx.json(result)
    }

    fun verifyNftOwnershipOnFlowDocs() = document().operation {
        it.summary("NFT ownership verification on Flow")
            .operationId("NFTOwnershipVerificationOnFlow").addTagsItem("NFT verification")
    }.pathParam<String>("chain") {
        it.schema<Chain> { }
    }.pathParam<String>("contractAddress") {
    }.queryParam<String>("account") {
        it.required(true)
    }.queryParam<String>("tokenId") {
        it.required(true)
    }.queryParam<String>("collectionPath") {
    }.json<Boolean>("200") {

    }
}
