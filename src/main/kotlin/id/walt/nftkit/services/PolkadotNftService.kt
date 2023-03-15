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

    fun fetchAccountNFTsBySubscan(parachain: PolkadotParachain, account: String): PolkadotNFTsSubscanResult{//https://moonbeam.api.subscan.io/api/scan/account/tokens
        return runBlocking {
            val values = mapOf("address" to account)
            var parachainAPI= when(parachain){
                PolkadotParachain.ASTAR -> "astar"
                PolkadotParachain.MOONBEAM -> "moonbeam"
            }
            client.post("https://$parachainAPI.api.subscan.io/api/scan/account/tokens") {
                contentType(ContentType.Application.Json)
                header("X-API-Key", "a316376503ab4bceb3c38c601de91ca7")
                setBody(values)
            }.body<PolkadotNFTsSubscanResult>()
        }
    }
}