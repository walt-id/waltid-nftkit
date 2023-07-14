package id.walt.nftkit.services

import com.algorand.algosdk.account.Account
import com.algorand.algosdk.transaction.SignedTransaction
import com.algorand.algosdk.transaction.Transaction
import com.algorand.algosdk.util.Encoder
import com.algorand.algosdk.v2.client.Utils
import com.algorand.algosdk.v2.client.common.AlgodClient
import com.algorand.algosdk.v2.client.common.IndexerClient
import com.algorand.algosdk.v2.client.common.Response
import com.algorand.algosdk.v2.client.model.PostTransactionsResponse
import kotlinx.serialization.Serializable


@Serializable
data class AlgorandAccount(val address: String, val mnemonic: String)

object AlgorandNftService {

    val token = ""
    val algod = AlgodClient("https://testnet-api.algonode.cloud", 443, token)
    val indexer = IndexerClient("https://testnet-idx.algonode.cloud", 443)


    fun createAccount(): AlgorandAccount {
        val account = com.algorand.algosdk.account.Account()
        return AlgorandAccount(account.address.toString(), account.toMnemonic())
    }


    fun createAssetArc(){
        val ALGOD_API_ADDR = "https://testnet-algorand.api.purestake.io/ps2"
        val IDX_API_ADDR = "https://testnet-algorand.api.purestake.io/idx2"

        val ALGOD_PORT = 443
        val ALGOD_API_TOKEN_KEY = "X-API-Key"
        val ALGOD_API_TOKEN = "B3SU4KcVKi94Jap2VXkK83xx38bsv95K5UZm2lab"

        val client = AlgodClient(ALGOD_API_ADDR, ALGOD_PORT, ALGOD_API_TOKEN, ALGOD_API_TOKEN_KEY)
        val idxClient = IndexerClient(IDX_API_ADDR, ALGOD_PORT, ALGOD_API_TOKEN, ALGOD_API_TOKEN_KEY)

        val SRC_ACCOUNT = "famous hood lend donate orange globe spatial stamp opinion universe found gown river identify negative climb defy galaxy turkey height duty doctor hazard ability athlete"
        val src = Account(SRC_ACCOUNT)
        println(src.getAddress())

        val DEST_ADDR = "ZHGZZQ2PIWYRK6MIK44GKO3VGQUC7NS2V3UQ63M3DIMFUFGI4BRWK7WDBU"

        val params = client.TransactionParams().execute().body()
        val amount = 10L

        val tx = Transaction.AssetCreateTransactionBuilder()
            .suggestedParams(params)
            .sender(src.getAddress())
            .manager(src.getAddress())
            .reserve(src.getAddress())
            .freeze(src.getAddress())
            .clawback(src.getAddress())
            .assetTotal(1)
            .assetDecimals(0)
            .defaultFrozen(false)
            .assetUnitName("ABC")
            .assetName("ABC")
            .url("ipfs://bafkreid7pi6nfcv4km3yzsvdfvuldhkuzmlx44rr76uaa7lhir6qmdn47q#arc3")
            .build()
        val signedTx = src.signTransaction(tx)

        // send the transaction to the network
        try {
            val txHeaders = arrayOf("Content-Type")
            val txValues = arrayOf("application/x-binary")
            val encodedTxBytes = Encoder.encodeToMsgPack(signedTx)

            val txResponse = client.RawTransaction().rawtxn(encodedTxBytes).execute(txHeaders, txValues).body()
            println("Successfully sent tx with ID " + txResponse.txId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}