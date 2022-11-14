package id.walt.nftkit.services

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


enum class TezosChain {
    TEZOS,
    GHOSTNET
}

@Serializable
data class TezosNftMetadata(

    var name: String,
    val description: String?= null,
    var symbol: String,
    var image: String?= null,
    var creators: List<String>?= null,
    var decimals: String?= null,
    val displayUri: String?= null,
    val artifactUri: String?= null,
    val thumbnailUri: String?= null,
    val isTransferable:Boolean?= null,
    val isBooleanAmount:Boolean?= null,
    val shouldPreferSymbol:Boolean?= null
    )

@Serializable
data class TezosNFTsTzktResult(
    val id: Long,
    val account: Account,
    val token: Token,
    val balance: String? = null,
    val transfersCount: Long,
    val firstLevel: Long? = null,
    val firstTime: String? = null,
    val lastLevel: Long? = null,
    val lastTime: String? = null,

) {

    @Serializable
    data class Account(
        val address: String,
    )
    @Serializable
    data class Token(
        val id: Long,
        val contract: Contract,
        val tokenId: String,
        val standard: String,
        val totalSupply: String,
        val metadata: TezosNftMetadata?= null,
        // val timeLastUpdated: String
    ) {
        @Serializable
        data class Contract(
            val address: String,
        )
    }
}

@Serializable
data class TezosNftsSCDeploymentResponse(
    val contractAddress: String
)

enum class Fa2SmartContractType {
    SINGLE,
    MULTIPLE
}

object TezosNftService {

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

    fun deploySmartContract(chain: TezosChain, owner: String, type: Fa2SmartContractType): TezosNftsSCDeploymentResponse {
        return runBlocking {
            val values = mapOf("chain" to chain.toString(), "owner" to owner, "type" to type.toString())
            val deployment =
                NftService.client.post("${WaltIdServices.loadTezosConfig().tezosBackendServer}/tezos/contract/deployment") {
                    contentType(ContentType.Application.Json)
                    setBody(values)
                }
                    .body<TezosNftsSCDeploymentResponse>()
            return@runBlocking deployment
        }
    }
     fun fetchAccountNFTsByTzkt(
        chain: Chain,
        account: String,
        contractAddress: String?= null,
    ): List<TezosNFTsTzktResult> {
        return runBlocking {
            var contractAddressExp= ""
            var chainAPI= ""
            if(contractAddress != null)  contractAddressExp= "token.contract=$contractAddress&"
            if(Chain.GHOSTNET.equals(chain)) chainAPI= ".ghostnet"
            val nfts =
                NftService.client.get("https://api$chainAPI.tzkt.io/v1/tokens/balances?account=$account&$contractAddressExp select=id,account,token,balance,transfersCount&type=fa2&token.totalSupply=1") {
                    contentType(ContentType.Application.Json)
                }
                    .body<List<TezosNFTsTzktResult>>()

            return@runBlocking nfts
        }
    }
}