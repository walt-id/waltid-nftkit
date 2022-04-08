package id.walt.nftkit.services


import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import java.math.BigInteger


object VerificationService {

    val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
            })
        }
        expectSuccess = false
    }

    // Verify if NFT is part of a collection (contract address)
    fun  verifyCollection(chain: Chain, contractAddress: String, accountAddress: String): Boolean{
        val accountNfts = getAccountNftsPerContract(chain, contractAddress, accountAddress)
        if(accountNfts.size > 0){
            return true
        }
        return false
    }

    //  simply check if a certain trait type and trait value is in the metadata
    fun verifyTrait(chain: Chain, contractAddress: String, accountAddress: String, traitType: String, traitValue: String? = null): Boolean {
        val accountNfts = getAccountNftsPerContract(chain, contractAddress, accountAddress)

        if(accountNfts.size > 0){
            accountNfts.forEach{
                val nftMetadata = NftService.getNftMetadata(chain, it!!.contractAddress, BigInteger( it?.tokenID))
                if(nftMetadata.attributes?.filter { (it.trait_type.equals(traitType) && it.value.equals(traitValue)) || (traitValue == null && traitType.equals(it.trait_type) ) }!!.size > 0){
                    return true
                }
            }
        }
        return false
    }

    private fun getAccountNftsPerContract(chain: Chain, contractAddress: String, accountAddress: String): List<Token?> {
        val nfts = NftService.getAccountNFTsByChainScan(chain, accountAddress)
        return nfts.filter { it?.contractAddress.equals(contractAddress, ignoreCase = true) }
    }



}