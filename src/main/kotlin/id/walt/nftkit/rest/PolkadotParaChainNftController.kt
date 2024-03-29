package id.walt.nftkit.rest

import id.walt.nftkit.services.PolkadotNftService
import id.walt.nftkit.services.PolkadotParachain

import cc.vileda.openapi.dsl.schema

import io.javalin.http.Context
import io.javalin.plugin.openapi.dsl.document


object PolkadotParaChainNftController {
    val TAG = "Polkadot Blockchain: Parachains"
    fun fetchParachainNFTs(ctx: Context) {
        val chain = ctx.pathParam("chain")
        val account = ctx.pathParam("account")


        val result = PolkadotNftService.fetchAccountTokensBySubscan(PolkadotParachain.valueOf(chain), account)
        ctx.json(result)
    }

    fun fetchParachainNFTsDocs() = document().operation {
        it.summary("Fetching Tokens by Subscan")
            .operationId("fetchparachainNFTS")
            .addTagsItem(TAG)
    }.pathParam<String>("chain") {
        it.schema<PolkadotParachain> {}
    }.pathParam<String>("account") {
        it.schema<String> {}

    }

    fun fetchEvmErc721CollectiblesBySubscan(ctx: Context) {
        val chain = ctx.pathParam("chain")
        val account = ctx.pathParam("account")
        val result = PolkadotNftService.fetchEvmErc721CollectiblesBySubscan(PolkadotParachain.valueOf(chain), account)
        ctx.json(result)
    }

    fun fetchEvmErc721CollectiblesBySubscanDocs() = document().operation {
        it.summary("Fetching EvmErc721 Collectibles by Subscan")
            .operationId("fetchEvmErc721CollectiblesBySubscan")
            .addTagsItem(TAG)
    }.pathParam<String>("chain") {
        it.schema<PolkadotParachain> {}
    }.pathParam<String>("account") {
        it.schema<String> {}

    }
}