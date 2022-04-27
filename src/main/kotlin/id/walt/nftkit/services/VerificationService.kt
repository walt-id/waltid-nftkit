package id.walt.nftkit.services


import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import org.web3j.tx.exceptions.ContractCallException
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
    fun  verifyCollection(chain: Chain, contractAddress: String, account: String, tokenId: String): Boolean{
        return ownershipVerification(chain, contractAddress, account, BigInteger(tokenId))
    }

    //  simply check if a certain trait type and trait value is in the metadata
    fun verifyTrait(chain: Chain, contractAddress: String, account: String, tokenId: String, traitType: String, traitValue: String? = null): Boolean {
        val ownership= ownershipVerification(chain, contractAddress, account, BigInteger(tokenId))
        if(ownership == true){
            val metadata= NftService.getNftMetadata(chain, contractAddress, BigInteger( tokenId))
            if(metadata.attributes?.filter { (it.trait_type.equals(traitType) && it.value.equals(traitValue, true)) || (traitValue == null && traitType.equals(it.trait_type) ) }!!.size > 0){
                return true
            }
        }
        return false
    }

    private fun ownershipVerification(chain: Chain, contractAddress: String, account: String, tokenId: BigInteger): Boolean{
        try {
            val owner= NftService.ownerOf(chain, contractAddress, tokenId)
            if(account.equals(owner, true)){
                return true
            }
            return false
        }catch (e: ContractCallException){
            return false
        }
        return false
    }

}