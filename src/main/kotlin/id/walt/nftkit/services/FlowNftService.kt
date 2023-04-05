package id.walt.nftkit.services

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.swagger.util.Json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable


enum class FlowChain {
    TESTNET, MAINNET
}

@Serializable
data class FlowNFTTraits(
    val name: String,
    val value: String,
    val displayType: String,
    val rarity: String,
)
@Serializable
data class FlowNFTMetadata(
    val id : String,
    val name: String,
    val description: String,
    val thumbnail: String,
   // val owner : String,
  //  val type : String,
    val externalURL: String?= null,
    val collectionName: String,
    val collectionDescription: String,
  //  val traits: List<FlowNFTTraits>,
)
object FlowNftService {

    fun getAllNFTs(account_id: String, chain: FlowChain): List<FlowNFTMetadata> {
        return runBlocking {
            val values = mapOf(
                "account_id" to account_id,
                "chain" to chain.toString()
            )
            val operationResult = NftService.client.post("${WaltIdServices.loadTezosConfig().tezosBackendServer}/flow/getAllNFTs"){
                contentType(ContentType.Application.Json)

                setBody(
                    values
                )
            }
                .body<List<FlowNFTMetadata>>()
           return@runBlocking operationResult
        }
    }
}