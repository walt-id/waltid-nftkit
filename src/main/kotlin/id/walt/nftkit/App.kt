package id.walt.nftkit

import id.walt.nftkit.rest.NftKitApi

import id.walt.nftkit.services.*

fun main() {
    println("\n\n\n")


    /* /////////// */
    /* /////////// */
    /* /////////// */
    NftKitApi.start()

    /*val result= VerificationService.OceanDaoVerification(Chain.MUMBAI, "0x02828CD6AD674B9831b30e900488999899a39BA0", "0x3cCD6F1ADcf0e77FEC9Ae72a33fB69dc3a77835a", "0x2555e3a97c4ac9705d70b9e5b9b6cc6fe2977a74")
    println(result)*/
  //  ownershipVerification(Chain.MUMBAI, "0x6A2E25BebFdab76cdD8F61B4385672ba7C7F834A", "1", "0x2555e3a97c4ac9705d70b9e5b9b6cc6fe2977a74")

    /*val attribute1 : NftMetadata.Attributes = NftMetadata.Attributes(trait_type = "valid", value = "true")
    val attributes = mutableListOf(attribute1)
    val nftMetadata = NftMetadata(name = "Bored apes", description = "Bored apes#01", image = "https://thumbor.forbes.com/thumbor/fit-in/900x510/https://www.forbes.com/advisor/in/wp-content/uploads/2022/03/monkey-g412399084_1280.jpg", attributes = attributes)
    val metadata= Json.encodeToString(nftMetadata)
    println(metadata)
    println("Store metadata result is: " + nftStorageAddMetadataTest(metadata))*/

/*    val attribute1 : NftMetadata.Attributes = NftMetadata.Attributes(trait_type = "a6", value = "v6"*//*, display_type = null*//*)
    val attribute2 : NftMetadata.Attributes = NftMetadata.Attributes(trait_type = "a6", value = "v6"*//*, display_type = null*//*)

    val attributes = mutableListOf(attribute1, attribute2)
    val nftMetadata : NftMetadata = NftMetadata(name = "m6", description = "m6", image = "", attributes = attributes)*/

    /*println(erc721TokenStandard.supportsInterface(Chain.RINKEBY,"0xc831de165bD2356230e60DF549324034dB5A3BD5"))*/

    /* val mintingOptions : MintingOptions = MintingOptions(TokenStandard.ERC721, MetadataStorageType.ON_CHAIN, null)
    val mintingParameter: MintingParameter = MintingParameter(null, "0x2555e3a97c4ac9705D70b9e5B9b6cc6Fe2977A74",nftMetadata)
    val ms: MintingResponse = NftService.mintToken(Chain.RINKEBY, "0xc831de165bD2356230e60DF549324034dB5A3BD5", mintingParameter, mintingOptions)
*/
}

fun ownershipVerification(chain: Chain, contractAddress: String, tokenID: String, account: String){
    val result = VerificationService.verifyCollection(chain, contractAddress, account, tokenID)
    println(result)
}


