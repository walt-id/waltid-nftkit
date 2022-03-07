package id.walt.nftkit

import id.walt.nftkit.services.*
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Type
import java.util.*


class App {
    val greeting: String
        get() {
            return "Hello World!"
        }
}

/*fun main() {
    println(App().greeting)

    val erc721OnchainCredential = Erc721OnchainCredential()

    val name = erc721OnchainCredential.name("0xc831de165bD2356230e60DF549324034dB5A3BD5")
    println(name)

    val tokenuri = Utf8String("ewogICJkZXNjcmlwdGlvbiI6ICJGcmllbmRseSBPcGVuU2VhIENyZWF0dXJlIHRoYXQgZW5qb3lzIGxvbmcgc3dpbXMgaW4gdGhlIG9jZWFuLiIsIAogICJleHRlcm5hbF91cmwiOiAiaHR0cHM6Ly9vcGVuc2VhY3JlYXR1cmVzLmlvLzMiLCAKICAiaW1hZ2UiOiAiaHR0cHM6Ly9zdG9yYWdlLmdvb2dsZWFwaXMuY29tL29wZW5zZWEtcHJvZC5hcHBzcG90LmNvbS9wdWZmcy8zLnBuZyIsIAogICJuYW1lIjogIkRhdmUgU3RhcmJlbGx5Igp9")
    val address : Address = Address("0x2555e3a97c4ac9705D70b9e5B9b6cc6Fe2977A74")
    val receipt = erc721OnchainCredential.mintToken("0xc831de165bD2356230e60DF549324034dB5A3BD5", address,tokenuri)
    println(receipt)



    val tokenUri =  erc721OnchainCredential.tokenURI("0xc831de165bD2356230e60DF549324034dB5A3BD5", Uint256(2))
    println(tokenUri)


}*/



fun main(){
    val attribute1 : NftMetadata.Attributes = NftMetadata.Attributes(trait_type = "a1", value = "v1", display_type = null)
    val attribute2 : NftMetadata.Attributes = NftMetadata.Attributes(trait_type = "a2", value = "v2", display_type = null)

    val attributes = mutableListOf(attribute1, attribute2)
    val nftMetadata : NftMetadata = NftMetadata(name = "m1", description = "m2", image = "", attributes = attributes)

    /*val str : String = Json.encodeToString(serializer(),nftMetadata)
    println(str)

    val encodedStr: String = encBase64Str(str)
    println(encodedStr)
*/
    /*val erc721TokenStandard = Erc721TokenStandard()//next id: 16
    val tokenUri =  erc721TokenStandard.tokenURI("0xc831de165bD2356230e60DF549324034dB5A3BD5", Uint256(11))
    println(tokenUri)*/
    val mintingOptions : MintingOptions = MintingOptions(TokenStandard.ERC721, MetadataStorageType.ON_CHAIN, null)
    val mintingParameter: MintingParameter = MintingParameter(null, "0x2555e3a97c4ac9705D70b9e5B9b6cc6Fe2977A74",nftMetadata)
    val ms: MintingResponse = NftService.mintToken(Chain.RINKEBY, "0xc831de165bD2356230e60DF549324034dB5A3BD5", mintingParameter, mintingOptions)

    println(ms)
}
