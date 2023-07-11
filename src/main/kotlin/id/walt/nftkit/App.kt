package id.walt.nftkit

import com.algorand.algosdk.v2.client.model.Asset
import id.walt.nftkit.rest.NftKitApi
import id.walt.nftkit.services.AlgoNftMetadata
import id.walt.nftkit.services.AlgorandChain
import id.walt.nftkit.services.AlgorandNftService.getAssetMeatadata
import id.walt.nftkit.services.AlgorandNftService.getNftMetadata
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


fun main() {
    println("\n\n\n")
    val test = getNftMetadata(254630815, AlgorandChain.TESTNET)
    println(test)
    //            com.algorand.algosdk.v2.client.model.Account acc = client.AccountInformation(src.getAddress()).execute().body();
//            System.out.println(acc);

//            PostTransactionsResponse txResponse = client.RawTransaction().rawtxn(encodedTxBytes).execute(txHeaders, txValues).body();
//            System.out.println("Successfully sent tx with ID " + txResponse.txId);
//
    /* /////////// */
    /* /////////// */
    /* /////////// */
    NftKitApi.start()


}



