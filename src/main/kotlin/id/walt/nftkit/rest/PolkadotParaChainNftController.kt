package id.walt.nftkit.rest

import cc.vileda.openapi.dsl.schema
import id.walt.nftkit.services.PolkadotNftService
import id.walt.nftkit.services.PolkadotParachain
import id.walt.nftkit.utilis.Common
import io.javalin.http.Context
import io.javalin.plugin.openapi.dsl.document

object PolkadotParaChainNftController {
    private const val TAG = "Polkadot Blockchain: Parachain network"

    fun fetchparachainNFTS(ctx: Context) {
        val chain = ctx.pathParam("chain")
        val account = ctx.pathParam("account")


        val result = PolkadotNftService.fetchAccountTokensBySubscan(PolkadotParachain.valueOf(chain), account)
        ctx.json(result)
    }

    fun fetchparachainNFTSDocs() = document().operation {
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