package id.walt.nftkit

import id.walt.nftkit.rest.NftKitApi
import id.walt.nftkit.services.AccessControlService
import id.walt.nftkit.services.Chain
import io.ktor.utils.io.core.*
import org.bouncycastle.util.encoders.Hex
import org.web3j.crypto.Hash
import kotlin.text.toByteArray


class App {
    val greeting: String
        get() {
            return "Hello World!"
        }
}


//fun ByteArray.toHex(): String = joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }

fun main(){


    NftKitApi.start()


    //println(AccessControlService.hasRole(Chain.RINKEBY, "0x24ce09fe8d26662f606bf9741e3667a0b6f1f896", "MINTER_ROLE", "0x8448Ff4b2733b52f62d81ca46d64bD16786299Cd"))
    //println(AccessControlService.renounceRole(Chain.RINKEBY, "0x24ce09fe8d26662f606bf9741e3667a0b6f1f896", "MINTER_ROLE", "0x8448Ff4b2733b52f62d81ca46d64bD16786299Cd"))

    //println(AccessControlService.revokeRole(Chain.RINKEBY, "0x24ce09fe8d26662f606bf9741e3667a0b6f1f896", "MINTER_ROLE", "0x6E7448a6335d5C947953994d071D4Dc1F6e5BE96"))

    //AccessControlService.hasRole(Chain.RINKEBY, "0x24ce09fe8d26662f606bf9741e3667a0b6f1f896", "DEFAULT_ADMIN_ROLE", "0xaf87c5Ce7a1fb6BD5aaDB6dd9C0b8EF51EF1BC31")
    //AccessControlService.getRoleAdmin(Chain.RINKEBY, "0x24ce09fe8d26662f606bf9741e3667a0b6f1f896", "MINTER_ROLE")
    //println(AccessControlService.grantRole(Chain.RINKEBY, "0x24ce09fe8d26662f606bf9741e3667a0b6f1f896", "MINTER_ROLE", "0x6E7448a6335d5C947953994d071D4Dc1F6e5BE96"))




//0x9f2df0fed2c77648de5860a4cc508cd0818c85b8b8a1ab4ceeef8d981c8956a6
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
