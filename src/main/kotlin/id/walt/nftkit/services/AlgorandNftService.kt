package id.walt.nftkit.services
import com.algorand.algosdk.crypto.Address
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
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.*
import java.util.*

enum class AlgorandChain{
    MAINNET, TESTNET, BETANET
}

@Serializable
data class AlgorandToken
    (
    var TokenParams: Asset?= null,
    var Metadata : AlgoNftMetadata? = null
)


@Serializable
data class Asset (
    var index: Long? = null,
    @SerialName("created-at-round")
    var createdAtRound: Long? = null,
    var deleted: Boolean? = null,
    @SerialName("destroyed-at-round")
    var destroyedAtRound: Long? = null,
    @SerialName("params")
    var params: AssetParams? = null
){
    @Serializable
    data class AssetParams(
        var clawback: String? = null,
        var creator: String? = null,
        var decimals: Long? = null,
        @SerialName("default-frozen")
        var defaultFrozen: Boolean? = null,
        var freeze: String? = null,
        var manager: String? = null,
        var name: String? = null,
        var reserve: String? = null,
        var total: Long? = null,
        @SerialName("unit-name")
        var unitName: String? = null,
        var url: String? = null
    )
}

@Serializable
data class AlgoNftMetadata (
    var name: String? = null,
    var description: String? = null,
    var image: String? = null,
    var decimals: Int? = null,
    var unitName: String? = null,
    val properties:  Map<String, JsonElement> ? = null,
)
@Serializable
data class AssetHoldingsResponse (
var assets: List<AssetHolding> = ArrayList())
{
    @Serializable
    data class AssetHolding(
        var amount: Int? = null,
        @SerialName("asset-id")
        var assetId: Long? = null,
        @SerialName("deleted")
        var deleted: Boolean? = null,
        @SerialName("is-frozen")
        var isFrozen: Boolean? = null,
    )
}
object AlgorandNftService {
    val client = HttpClient(CIO.create { requestTimeout = 0 }) {
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

    fun getToken(assetId: Long, chain: AlgorandChain): AlgorandToken {
        return runBlocking {
            val ALGOD_API_ADDR = when (chain) {
                AlgorandChain.MAINNET -> "https://mainnet-api.algonode.cloud"
                AlgorandChain.TESTNET -> "https://testnet-api.algonode.cloud"
                AlgorandChain.BETANET -> "https://betanet-api.algonode.cloud"
            }
            val tokenParams = client.get(ALGOD_API_ADDR + "/v2/assets/" + assetId){
                contentType(ContentType.Application.Json)
            }.body<Asset>()

            var cid = (tokenParams.params?.url)?.substringAfter("ipfs://")
            val nft =
                IPFSMetadata.client.get("https://ipfs.io/ipfs/$cid")
                { contentType(ContentType.Application.Json)}.body<AlgoNftMetadata>()

            var result = AlgorandToken()

            result.TokenParams = tokenParams
            result.Metadata = nft
            return@runBlocking result
        }
    }
    fun getAssetMeatadata(assetId: Long, chain: AlgorandChain): Asset {
        return runBlocking {
            val ALGOD_API_ADDR = when (chain) {
                AlgorandChain.MAINNET -> "https://mainnet-api.algonode.cloud"
                AlgorandChain.TESTNET -> "https://testnet-api.algonode.cloud"
                AlgorandChain.BETANET -> "https://betanet-api.algonode.cloud"
            }
            val asset = client.get(ALGOD_API_ADDR + "/v2/assets/" + assetId){
                    contentType(ContentType.Application.Json)
                }.body<Asset>()

            return@runBlocking asset
        }
    }

    fun getNftMetadata(assetId: Long, chain: AlgorandChain): AlgoNftMetadata {
        return runBlocking {
            val asset: Asset = getAssetMeatadata(assetId, chain)
            var cid = (asset.params?.url)?.substringAfter("ipfs://")
            val nft =
                IPFSMetadata.client.get("https://ipfs.algonode.xyz/ipfs/$cid")
                { contentType(ContentType.Application.Json)}.body<AlgoNftMetadata>()
            return@runBlocking nft;
        }
    }

    fun getAccountAssets(address: String, chain: AlgorandChain): AssetHoldingsResponse {
        return runBlocking {
            var ALGOD_API_ADDR = when (chain) {
                AlgorandChain.MAINNET -> "https://mainnet-idx.algonode.cloud"
                AlgorandChain.TESTNET -> "https://testnet-idx.algonode.cloud"
                AlgorandChain.BETANET -> "https://betanet-idx.algonode.cloud"
            }
            val add = Address(address);
            val result =
                client.get(ALGOD_API_ADDR + "/v2/accounts/" + add + "/assets") {
                contentType(ContentType.Application.Json)
            }.body<AssetHoldingsResponse>()
            return@runBlocking result;
        }
    }
}

