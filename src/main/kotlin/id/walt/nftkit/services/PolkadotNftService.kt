package id.walt.nftkit.services

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

enum class PolkadotParachain {
    ASTAR,
    MOONBEAM
}

@Serializable
data class PolkadotNFTsSubscanResult(
    val data: Data?=null,
){
    @Serializable
    data class Data(
        val ERC721: List<Erc721>?=null,
    ){
        @Serializable
        data class Erc721(
            val symbol: String,
            val balance: String,
            val contract: String,
            )
    }
}
@Serializable
data class SubscanEvmErc721CollectiblesBody(
    val address: String,
    val row: Int
)

@Serializable
data class SubscanEvmErc721CollectiblesResult(
    val data: Data?=null,
){
    @Serializable
    data class Data(
        val count: Int,
        val list: List<TokenHolder>?=null,
    ){
        @Serializable
        data class TokenHolder(
            val contract: String,
            val holder: String,
            val token_id: String,
            val storage_url: String
        )
    }
}


object PolkadotNftService {

    val client = HttpClient(CIO.create{requestTimeout = 0}) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
            // LogLevel.BODY
        }
        expectSuccess = false
    }

    fun fetchAccountTokensBySubscan(parachain: PolkadotParachain, account: String): PolkadotNFTsSubscanResult{
        return runBlocking {
            val values = mapOf("address" to account)
            var parachainAPI= when(parachain){
                PolkadotParachain.ASTAR -> "astar"
                PolkadotParachain.MOONBEAM -> "moonbeam"
            }
            client.post("https://$parachainAPI.api.subscan.io/api/scan/account/tokens") {
                contentType(ContentType.Application.Json)
                header("X-API-Key", "")
                setBody(values)
            }.body<PolkadotNFTsSubscanResult>()
        }
    }


    fun fetchEvmErc721CollectiblesBySubscan(parachain: PolkadotParachain, account: String):SubscanEvmErc721CollectiblesResult{
        return runBlocking {
            val v = SubscanEvmErc721CollectiblesBody(account, 100)
            var parachainAPI= when(parachain){
                PolkadotParachain.ASTAR -> "astar"
                PolkadotParachain.MOONBEAM -> "moonbeam"
            }
            client.post("https://$parachainAPI.api.subscan.io/api/scan/evm/erc721/collectibles") {
                contentType(ContentType.Application.Json)
                header("X-API-Key", "")
                setBody(v)
            }.body<SubscanEvmErc721CollectiblesResult>()
        }
    }
}