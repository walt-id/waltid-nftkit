package id.walt.nftkit.services

import com.algorand.algosdk.v2.client.common.AlgodClient
import com.algorand.algosdk.v2.client.common.IndexerClient
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
    fun mintNftArc3() {
       // todo

    }


}