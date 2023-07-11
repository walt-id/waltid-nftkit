package id.walt.nftkit.services


import com.algorand.algosdk.crypto.Address
import com.algorand.algosdk.v2.client.common.AlgodClient
import com.algorand.algosdk.v2.client.model.Asset
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.annotations.SerializedName
import id.walt.nftkit.metadata.IPFSMetadata
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import io.ktor.util.Encoder
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.*
import java.util.*


enum class AlgorandChain{
    MAINNET, TESTNET, BETANET
}


@Serializable
data class AlgoNftMetadata (
    var name: String? = null,
    var description: String? = null,
    var image: String? = null,
    var decimals: Int? = null,
    var unitName: String? = null,
    val properties: JsonObject
)
object AlgorandNftService {
    val client = HttpClient(CIO.create{requestTimeout = 0}) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
//            logger = Logger.SIMPLE
//            level = LogLevel.ALL
            level = LogLevel.BODY
        }
        expectSuccess = false
    }
    const val ALGOD_PORT = 443
    const val ALGOD_API_TOKEN_KEY = "X-API-Key"
    const val ALGOD_API_TOKEN = ""

    fun getAssetMeatadata(assetId: Long, chain: AlgorandChain):Asset{
//        return runBlocking {
        val ALGOD_API_ADDR = when(chain){
            AlgorandChain.MAINNET -> "https://mainnet-api.algonode.cloud"
            AlgorandChain.TESTNET -> "https://testnet-api.algonode.cloud"
            AlgorandChain.BETANET -> "https://betanet-api.algonode.cloud"
        }
        var client1 = AlgodClient(ALGOD_API_ADDR, ALGOD_PORT, ALGOD_API_TOKEN)
        val asset: Asset = client1.GetAssetByID(assetId).execute().body()

        return asset
//            client.get(ALGOD_API_ADDR+"/v2/assets/"+assetId){
//                contentType(ContentType.Application.Json)
//            }.body<id.walt.nftkit.services.Asset>()
        }
    fun getNftMetadata(assetId: Long, chain: AlgorandChain):AlgoNftMetadata{
        return runBlocking {
            val asset: Asset = getAssetMeatadata(assetId, chain)
            var cid = (asset.params.url).substringAfter("ipfs://")
            val nft = IPFSMetadata.client.get("https://ipfs.algonode.xyz/ipfs/$cid") {}.body<AlgoNftMetadata>()
            println("--------")
            println(nft)
            return@runBlocking nft;
        }
    }

    fun getAccountAssets(address:String, chain: AlgorandChain):Any{
        var ALGOD_API_ADDR = when(chain){
            AlgorandChain.MAINNET -> "https://mainnet-algorand.api.purestake.io/idx2"
            AlgorandChain.TESTNET -> "https://testnet-algorand.api.purestake.io/idx2"
            AlgorandChain.BETANET -> "https://betanet-algorand.api.purestake.io/idx2"
        }
        var client = AlgodClient(ALGOD_API_ADDR, ALGOD_PORT, ALGOD_API_TOKEN, ALGOD_API_TOKEN_KEY)
        val add = Address(address);

        return 0;
    }

}

