package id.walt.nftkit

import id.walt.nftkit.rest.NftKitApi
import id.walt.nftkit.services.NearMintingParameter
import id.walt.nftkit.services.NearNftService.getNFTforAccount
import id.walt.nftkit.services.NearNftService.getNftNearMetadata
import id.walt.nftkit.services.NearNftService.mintNftToken

fun main() {
    println("\n\n\n")
    
    /* /////////// */
    /* /////////// */
    /* /////////// */
    NftKitApi.start()


println("hey")
    val getNftNearMetadataa = getNftNearMetadata(
        "demo.khaled_lightency1.testnet",
        "testnet"
    )
    println(getNftNearMetadataa)

}



