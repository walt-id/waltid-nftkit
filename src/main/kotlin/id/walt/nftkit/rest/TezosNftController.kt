package id.walt.nftkit.rest

import cc.vileda.openapi.dsl.schema
import id.walt.nftkit.Values
import id.walt.nftkit.services.*
import id.walt.nftkit.utilis.Common
import io.javalin.http.Context
import io.javalin.plugin.openapi.dsl.document
import kotlinx.serialization.Serializable

@Serializable
data class TezosDeployRequest(
    val owner: String,
    val type: String,
)
@Serializable
data class TezosSCDeploymentResponse(
    val contractAddress: String,
    val contractExternalUrl: String
)

object TezosNftController {
    fun deploy(ctx: Context) {
        val deployReq = ctx.bodyAsClass(TezosDeployRequest::class.java)
        val chain = ctx.pathParam("chain")
        val result =
            TezosNftService.deploySmartContract(Common.getTezosChain(chain), deployReq.owner, Common.getFa2SmartContractType(deployReq.type))
        val contractExternalUrl= when(Common.getTezosChain(chain)){
            TezosChain.TEZOS -> Values.TEZOS_MAINNET_BETTER_CALL_DEV
            TezosChain.GHOSTNET -> Values.TEZOS_GHOSTNET_BETTER_CALL_DEV
        }
        ctx.json(
            TezosSCDeploymentResponse(result.contractAddress, "$contractExternalUrl/${result.contractAddress}")
        )
    }

    fun deployDocs() = document().operation {
        it.summary("Smart contract deployment")
            .operationId("deploySmartContract").addTagsItem("Tezos Blockchain: Non-fungible tokens(NFTs)")
    }.pathParam<String>("chain") {
        it.schema<TezosChain> { }
    }.body<TezosDeployRequest> {
        it.description("")
    }.json<DeploymentResponse>("200") { it.description("Transaction ID and smart contract address") }
}