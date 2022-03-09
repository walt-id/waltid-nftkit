package id.walt.nftkit

import id.walt.nftkit.chains.evm.erc721.Erc721TokenStandard
import id.walt.nftkit.services.*
import id.walt.nftkit.smart_contract_wrapper.Erc721OnchainCredentialWrapper
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Type
import org.web3j.abi.datatypes.generated.Uint256
import java.math.BigInteger
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
    val attribute1 : NftMetadata.Attributes = NftMetadata.Attributes(trait_type = "a6", value = "v6", display_type = null)
    val attribute2 : NftMetadata.Attributes = NftMetadata.Attributes(trait_type = "a6", value = "v6", display_type = null)

    val attributes = mutableListOf(attribute1, attribute2)
    val nftMetadata : NftMetadata = NftMetadata(name = "m6", description = "m6", image = "", attributes = attributes)

    /*val str : String = Json.encodeToString(serializer(),nftMetadata)
    println(str)

    val encodedStr: String = encBase64Str(str)
    println(encodedStr)
*/
    /*val erc721TokenStandard = Erc721TokenStandard()//next id: 16
    val tokenUri =  erc721TokenStandard.tokenURI(Chain.RINKEBY,"0xc831de165bD2356230e60DF549324034dB5A3BD5", Uint256(23))
    println(tokenUri)

    println(erc721TokenStandard.supportsInterface(Chain.RINKEBY,"0xc831de165bD2356230e60DF549324034dB5A3BD5"))*/

   /* val mintingOptions : MintingOptions = MintingOptions(TokenStandard.ERC721, MetadataStorageType.ON_CHAIN, null)
    val mintingParameter: MintingParameter = MintingParameter(null, "0x2555e3a97c4ac9705D70b9e5B9b6cc6Fe2977A74",nftMetadata)
    val ms: MintingResponse = NftService.mintToken(Chain.RINKEBY, "0xc831de165bD2356230e60DF549324034dB5A3BD5", mintingParameter, mintingOptions)

    println(ms)*/

    val tokenUri = NftService.getNftMetadataUri(Chain.RINKEBY, "0xc831de165bD2356230e60DF549324034dB5A3BD5", BigInteger.valueOf(26) )
    println(tokenUri)

    val metadata = NftService.getNftMetadata(Chain.RINKEBY, "0xc831de165bD2356230e60DF549324034dB5A3BD5", BigInteger.valueOf(26) )
    println(metadata)


    /*val balance = NftService.balanceOf(Chain.RINKEBY, "0xc831de165bD2356230e60DF549324034dB5A3BD5", "0xc831de165bd2356230e60df549324034db5a3bd5", 0)
    println(balance)*/

/**val owner = NftService.ownerOf(Chain.RINKEBY, "0xc831de165bD2356230e60DF549324034dB5A3BD5", 4)
println(owner)*/
    /*val tokenCollectionInfo = NftService.getTokenCollectionInfo(Chain.RINKEBY, "0xc831de165bD2356230e60DF549324034dB5A3BD5")
    println(tokenCollectionInfo)*/

}

/*fun main(){
    val dr = Erc721TokenStandard.deployContract(Chain.RINKEBY,"name", "symbol")
    println(dr)
}*/
