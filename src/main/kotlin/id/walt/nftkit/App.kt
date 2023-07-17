package id.walt.nftkit

import com.algorand.algosdk.v2.client.model.Asset
import id.walt.nftkit.rest.NftKitApi
import id.walt.nftkit.services.AlgoNftMetadata
import id.walt.nftkit.services.AlgorandChain
import id.walt.nftkit.services.AlgorandNftService.getAccountAssets
import id.walt.nftkit.services.AlgorandNftService.getAssetMeatadata
import id.walt.nftkit.services.AlgorandNftService.getNftMetadata
import id.walt.nftkit.services.AlgorandNftService.getToken
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


fun main() {
    println("\n\n\n")
    
    /* /////////// */
    /* /////////// */
    /* /////////// */

    NftKitApi.start()


}



