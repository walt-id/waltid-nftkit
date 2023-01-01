package id.walt.nftkit

import id.walt.nftkit.rest.NftKitApi
import id.walt.nftkit.services.NearContractMetadata
import id.walt.nftkit.services.NearMintingParameter
import id.walt.nftkit.services.NearNftService.mintNftToken
import id.walt.nftkit.services.NearTokenMetadata

fun main() {
    println("\n\n\n")
    
    /* /////////// */
    /* /////////// */
    /* /////////// */
    NftKitApi.start()
    print("NftKitApi started")

    val mint = mintNftToken(
        "demo.khaled_lightency1.testnet",
        NearMintingParameter(
            "khaled_lightency1.testnet",
            "2",
            "khaled_lightency1.testnet",
            NearTokenMetadata(
                "title",
                "description",
                "https://www.google.com",
                "media_hash",
                "copies",
                "issued_at",
                "expires_at",
                "starts_at",
                "updated_at",
                "extra",
                "reference",
                "reference_hash"
            )
        )

    )

    println(mint)
}



