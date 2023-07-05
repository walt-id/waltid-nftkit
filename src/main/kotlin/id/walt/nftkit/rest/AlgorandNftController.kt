package id.walt.nftkit.rest

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
}