package id.walt.nftkit

import id.walt.nftkit.rest.NftKitApi
import id.walt.nftkit.services.Chain
import id.walt.nftkit.services.NftService
import io.javalin.core.util.RouteOverviewUtil.metaInfo
import java.lang.Long.parseLong


class App {
    val greeting: String
        get() {
            return "Hello World!"
        }
}



fun main(){


    NftKitApi.start()

    //val result = NftService.getAccountNFTsByAlchemy(Chain.MUMBAI, "0x2555e3a97c4ac9705D70b9e5B9b6cc6Fe2977A74");
    //0xf56345338cb4cddaf915ebef3bfde63e70fe3053 1000nfts
    //0xaf87c5Ce7a1fb6BD5aaDB6dd9C0b8EF51EF1BC31    0x2555e3a97c4ac9705D70b9e5B9b6cc6Fe2977A74
    /*println(result.size)
    result.forEach{println(it
            )}*/

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
