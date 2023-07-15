package id.walt.nftkit.services

import com.algorand.algosdk.account.Account
import com.algorand.algosdk.crypto.Address
import com.algorand.algosdk.transaction.Transaction
import com.algorand.algosdk.util.Encoder
import com.algorand.algosdk.v2.client.common.AlgodClient
import com.algorand.algosdk.v2.client.common.IndexerClient
import com.algorand.algosdk.v2.client.model.Asset
import id.walt.nftkit.Values.ALGORAND_TESTNET_EXPLORER
import id.walt.nftkit.metadata.IPFSMetadata
import id.walt.nftkit.services.WaltIdServices.loadAlgorand
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement


enum class AlgorandChain {
    TESTNET,
    MAINNET,
    BETANET
}

@Serializable
data class AlgorandAccount(val address: String, val mnemonic: String)

@Serializable
data class AlgodResponse(val txId: String , val explorerUrl: String)

@Serializable
data class AlgoNftMetadata (
    var name: String? = null,
    var description: String? = null,
    var image: String? = null,
    var decimals: Int? = null,
    var unitName: String? = null,
    var properties : Map<String, JsonElement> ? = null,
)

object AlgorandNftService {

    val ALGOD_API_ADDR = "https://testnet-algorand.api.purestake.io/ps2"
    val IDX_API_ADDR = "https://testnet-algorand.api.purestake.io/idx2"

    val ALGOD_PORT = 443
    val ALGOD_API_TOKEN_KEY = "X-API-Key"
    val ALGOD_API_TOKEN = "B3SU4KcVKi94Jap2VXkK83xx38bsv95K5UZm2lab"
    val client = AlgodClient(ALGOD_API_ADDR, ALGOD_PORT, ALGOD_API_TOKEN, ALGOD_API_TOKEN_KEY)
    val idxClient = IndexerClient(IDX_API_ADDR, ALGOD_PORT, ALGOD_API_TOKEN, ALGOD_API_TOKEN_KEY)
    fun createAccount(): AlgorandAccount {
        val account = com.algorand.algosdk.account.Account()
        return AlgorandAccount(account.address.toString(), account.toMnemonic())
    }


    fun createAssetArc3(assetName : String , assetUnitName : String ,  url : String) : AlgodResponse{

        val SRC_ACCOUNT = loadAlgorand().algorandConfig.algorand_seed_Mnemonic
        val src = Account(SRC_ACCOUNT)
        println(src.getAddress())

        val params = client.TransactionParams().execute().body()

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

            val txResponse = client.RawTransaction().rawtxn(encodedTxBytes).execute(txHeaders, txValues).body()
                println("Successfully sent tx with ID " + txResponse.txId)
            return AlgodResponse(txResponse.txId , "$ALGORAND_TESTNET_EXPLORER"+"tx/${txResponse.txId}")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return AlgodResponse("Null" , "Null")
    }

    fun getAssetMeatadata(assetId: Long, chain: AlgorandChain) : Asset {

        val ALGOD_API_ADDR = when (chain) {
            AlgorandChain.MAINNET -> "https://mainnet-api.algonode.cloud"
            AlgorandChain.TESTNET -> "https://testnet-api.algonode.cloud"
            AlgorandChain.BETANET -> "https://betanet-api.algonode.cloud"
        }
        var client = AlgodClient(ALGOD_API_ADDR, ALGOD_PORT, ALGOD_API_TOKEN)

        return client.GetAssetByID(assetId).execute().body()

    }
    fun getNftMetadata(assetId: Long, chain: AlgorandChain):AlgoNftMetadata{

        return runBlocking {

            val asset: Asset = getAssetMeatadata(assetId, chain)
            var cid = (asset.params.url).substringAfter("ipfs://")
            val nft = IPFSMetadata.client.get("https://ipfs.algonode.xyz/ipfs/$cid") {}.body<AlgoNftMetadata>()

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