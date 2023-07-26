package id.walt.nftkit.services

import com.algorand.algosdk.account.Account
import com.algorand.algosdk.transaction.Transaction
import com.algorand.algosdk.util.Encoder
import com.algorand.algosdk.v2.client.common.AlgodClient
import com.beust.klaxon.JsonObject
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import id.walt.nftkit.Values.ALGORAND_BETANET_EXPLORER
import id.walt.nftkit.Values.ALGORAND_MAINNET_EXPLORER
import id.walt.nftkit.Values.ALGORAND_TESTNET_EXPLORER
import id.walt.nftkit.metadata.IPFSMetadata
import id.walt.nftkit.services.WaltIdServices.loadAlgorand
import id.walt.nftkit.utilis.Common
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

enum class AlgorandChain{
    ALGORAND_MAINNET, ALGORAND_TESTNET, ALGORAND_BETANET
}

@Serializable
data class AccountAssetResponse
    (
    @SerialName("asset-holding")
    val assetHolding: AssetHoldingsResponse.AssetHolding?=null
    )

@Serializable
data class AlgorandAccount(val address: String, val mnemonic: String)

@Serializable
data class AlgodResponse(val txId: String , val explorerUrl: String)
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



    val ALGOD_API_ADDR = "https://testnet-algorand.api.purestake.io/ps2"
    val ALGOD_PORT = 443
    val ALGOD_API_TOKEN_KEY = "X-API-Key"
    val ALGOD_API_TOKEN = "B3SU4KcVKi94Jap2VXkK83xx38bsv95K5UZm2lab"
    val client1 = AlgodClient(ALGOD_API_ADDR, ALGOD_PORT, ALGOD_API_TOKEN, ALGOD_API_TOKEN_KEY)

    fun createAccount(): AlgorandAccount {
        val account = com.algorand.algosdk.account.Account()
        return AlgorandAccount(account.address.toString(), account.toMnemonic())
    }

    fun createAssetArc3(chain : AlgorandChain ,assetName : String , assetUnitName : String ,  url : String) : AlgodResponse{

        val client_algod = when(chain) {
            AlgorandChain.ALGORAND_MAINNET -> AlgodClient("https://mainnet-algorand.api.purestake.io/ps2", ALGOD_PORT, ALGOD_API_TOKEN, ALGOD_API_TOKEN_KEY)
            AlgorandChain.ALGORAND_TESTNET -> AlgodClient("https://testnet-algorand.api.purestake.io/ps2", ALGOD_PORT, ALGOD_API_TOKEN, ALGOD_API_TOKEN_KEY)
            AlgorandChain.ALGORAND_BETANET -> AlgodClient("https://betanet-algorand.api.purestake.io/ps2", ALGOD_PORT, ALGOD_API_TOKEN, ALGOD_API_TOKEN_KEY)
        }
        val SRC_ACCOUNT = loadAlgorand().algorandConfig.algorand_seed_Mnemonic
        val src = Account(SRC_ACCOUNT)
        val params = client_algod.TransactionParams().execute().body()

        val tx = Transaction.AssetCreateTransactionBuilder()
            .suggestedParams(params)
            .sender(src.address)
            .manager(src.address)
            .reserve(src.address)
            .freeze(src.address)
            .clawback(src.address)
            .assetTotal(1)
            .assetDecimals(0)
            .defaultFrozen(false)
            .assetUnitName(assetUnitName)
            .assetName(assetName)
            .url("$url#arc3")
            .build()
        val signedTx = src.signTransaction(tx)
        // send the transaction to the network
        try {
            val txHeaders = arrayOf("Content-Type")
            val txValues = arrayOf("application/x-binary")
            val encodedTxBytes = Encoder.encodeToMsgPack(signedTx)
            val txResponse = client_algod.RawTransaction().rawtxn(encodedTxBytes).execute(txHeaders, txValues).body()
            return when(chain) {
                AlgorandChain.ALGORAND_MAINNET -> AlgodResponse(txResponse.txId, "$ALGORAND_MAINNET_EXPLORER"+"tx/${txResponse.txId}")
                AlgorandChain.ALGORAND_TESTNET -> AlgodResponse(txResponse.txId, "$ALGORAND_TESTNET_EXPLORER"+"tx/${txResponse.txId}")
                AlgorandChain.ALGORAND_BETANET -> AlgodResponse(txResponse.txId, "$ALGORAND_BETANET_EXPLORER"+"tx/${txResponse.txId}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return AlgodResponse("Null" , "Null")
    }


    fun getToken(assetId: String, chain: AlgorandChain): AlgorandToken {
        return runBlocking {
            val API_ADDR = when (chain) {
                AlgorandChain.ALGORAND_MAINNET -> "https://mainnet-api.algonode.cloud"
                AlgorandChain.ALGORAND_TESTNET -> "https://testnet-api.algonode.cloud"
                AlgorandChain.ALGORAND_BETANET -> "https://betanet-api.algonode.cloud"
            }
            val tokenParams = client.get(API_ADDR + "/v2/assets/" + assetId){
                contentType(ContentType.Application.Json)
            }.body<Asset>()
            var result = AlgorandToken()

            if (tokenParams.params?.url != null) {
                var cid = (tokenParams.params?.url)?.substringAfter("ipfs://")
                val nft =
                    IPFSMetadata.client.get("https://ipfs.io/ipfs/$cid")
                    { contentType(ContentType.Application.Json)}.body<AlgoNftMetadata>()
                result.TokenParams = tokenParams
                result.Metadata = nft
                }

            else
                result.TokenParams = tokenParams

            return@runBlocking result
        }
    }
    fun getAssetMeatadata(assetId: String, chain: AlgorandChain): Asset {
        return runBlocking {
            val API_ADDR = when (chain) {
                AlgorandChain.ALGORAND_MAINNET -> "https://mainnet-api.algonode.cloud"
                AlgorandChain.ALGORAND_TESTNET -> "https://testnet-api.algonode.cloud"
                AlgorandChain.ALGORAND_BETANET -> "https://betanet-api.algonode.cloud"
            }
            val asset = client.get(API_ADDR + "/v2/assets/" + assetId){
                    contentType(ContentType.Application.Json)
                }.body<Asset>()

            return@runBlocking asset
        }
    }

    fun getNftMetadata(assetId: String, chain: AlgorandChain): AlgoNftMetadata {
        return runBlocking {
            val asset: Asset = getAssetMeatadata(assetId, chain)
            var cid = (asset.params?.url)?.substringAfter("ipfs://")
            val nft =
                IPFSMetadata.client.get("https://ipfs.algonode.xyz/ipfs/$cid")
                { contentType(ContentType.Application.Json)}.body<AlgoNftMetadata>()
            return@runBlocking nft;
        }
    }

    fun getAccountAssets(address: String, chain: AlgorandChain): List<AlgorandToken> {
        return runBlocking {
            var API_ADDR = when (chain) {
                AlgorandChain.ALGORAND_MAINNET -> "https://mainnet-idx.algonode.cloud"
                AlgorandChain.ALGORAND_TESTNET -> "https://testnet-idx.algonode.cloud"
                AlgorandChain.ALGORAND_BETANET -> "https://betanet-idx.algonode.cloud"
            }
            val resp = client.get(API_ADDR + "/v2/accounts/" + address + "/assets") {
                contentType(ContentType.Application.Json)
            }.body<AssetHoldingsResponse>()

            val result = mutableListOf<AlgorandToken>()
            for (a in resp.assets){
                result.add(getToken(a.assetId.toString(), chain))
            }
            return@runBlocking result;
        }
    }


    fun verifyOwnership(address: String, assetId:String, chain: AlgorandChain): AccountAssetResponse{
        return runBlocking {
            val API_ADDR = when (chain) {
                AlgorandChain.ALGORAND_MAINNET -> "https://mainnet-api.algonode.cloud"
                AlgorandChain.ALGORAND_TESTNET -> "https://testnet-api.algonode.cloud"
                AlgorandChain.ALGORAND_BETANET -> "https://betanet-api.algonode.cloud"
            }
            val response =
                client.get(API_ADDR+"/v2/accounts/"+address+"/assets/"+assetId){
                    contentType(ContentType.Application.Json)
                }.body<AccountAssetResponse>()
            return@runBlocking response
        }
    }
}

