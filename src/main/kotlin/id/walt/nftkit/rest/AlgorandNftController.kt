package id.walt.nftkit.rest

import id.walt.nftkit.services.AlgodResponse
import id.walt.nftkit.services.AlgorandAccount
import id.walt.nftkit.services.AlgorandNftService
import io.javalin.http.Context
import io.javalin.plugin.openapi.dsl.document

object AlgorandNftController{
    fun accountCreation(ctx : Context){
        val result = AlgorandNftService.createAccount()
        ctx.json(result)
    }

    fun accountCreationDocs() = document().operation {
        it.summary("Create Algorand Account").operationId("CreateAlgorandAccount").addTagsItem("Algorand Blockchain: Non-fungible tokens(NFTs)")
    }.json<AlgorandAccount>(200.toString()) {
        it.description("Algorand Account")
    }


    fun assetCreation(ctx : Context){
        val result = AlgorandNftService.createAssetArc3(ctx.pathParam("assetName"), ctx.pathParam("assetUnitName"), ctx.pathParam("url"))
        ctx.json(result)
    }

    fun assetCreationDocs() = document().operation {
        it.summary("Create Algorand Asset").operationId("CreateAlgorandAsset").addTagsItem("Algorand Blockchain: Non-fungible tokens(NFTs)")
    }.json<AlgodResponse>(200.toString()) {
        it.description("Algorand Asset")
    }


}