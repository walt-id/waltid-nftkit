package id.walt.nftkit.services




import id.walt.nftkit.opa.DynamicPolicy
import id.walt.nftkit.opa.PolicyRegistry
import id.walt.nftkit.utilis.Common
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import org.web3j.tx.exceptions.ContractCallException
import java.math.BigInteger

@Serializable
data class InternalTransactionsResponse(
    val status: String,
    val message: String,
    val result: List<InternalTransaction> ?=null
){
    @Serializable
    data class InternalTransaction(
        val from: String,
        val hash: String,
        val contractAddress: String
    )
}
object VerificationService {

    /*val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
            })
        }
        expectSuccess = false
    }*/

    // Verify if NFT is part of a collection (contract address)
    fun  verifyNftOwnership(chain: Chain, contractAddress: String, account: String, tokenId: String): Boolean{
        return when{
            Common.isEVMChain(chain) -> {
                return NFTsEvmOwnershipVerification(EVMChain.valueOf(chain.toString()), contractAddress, account, BigInteger(tokenId))
            }
            Common.isTezosChain(chain) -> {
                return NFTsTezosOwnershipVerification(chain, contractAddress, account, tokenId)
            }

            Common.isNearChain(chain) -> {
                return NFTsNearOwnershipVerification(NearChain.valueOf(chain.toString()), contractAddress, account, tokenId)
            }


            else -> {throw Exception("Chain  is not supported")}

        }
    }

     fun verifyNftOwnershipWithinCollection(chain: Chain, contractAddress: String, account: String): Boolean {
        return when{
            Common.isEVMChain(chain) -> {
                return verifyNftOwnershipWithinCollectionEvmChain(EVMChain.valueOf(chain.toString()), contractAddress, account)
            }
            Common.isTezosChain(chain) -> {
                return verifyNftOwnershipWithinCollectionTezosChain(chain, contractAddress, account)
            }

            Common.isNearChain(chain) -> {
                return verifyNftOwnershipWithinCollectionNearChain(NearChain.valueOf(chain.toString()), contractAddress, account)
            }

            else -> {throw Exception("Chain  is not supported")}
        }

    }


    //  simply check if a certain trait type and trait value is in the metadata
    fun verifyNftOwnershipWithTraits(chain: Chain, contractAddress: String, account: String, tokenId: String, traitType: String, traitValue: String? = null): Boolean {

        return when{
            Common.isEVMChain(chain) -> {
                val ownership= NFTsEvmOwnershipVerification(EVMChain.valueOf(chain.toString()), contractAddress, account, BigInteger(tokenId))
                if(ownership){
                    val metadata= NftService.getNftMetadata(EVMChain.valueOf(chain.toString()), contractAddress, BigInteger( tokenId))
                    if(metadata!!.attributes?.filter {
                            (it.trait_type.equals(traitType) && it.value.equals(
                                traitValue,
                                true
                            )) || (traitValue == null && traitType.equals(it.trait_type))
                        }!!.isNotEmpty()){
                        return true
                    }
                }
                return false;
            }
            Common.isTezosChain(chain) -> {
                val ownership= NFTsTezosOwnershipVerification(chain, contractAddress, account, tokenId)
                if(ownership){
                    val metadata= TezosNftService.getNftTezosMetadata(TezosChain.valueOf(chain.toString()), contractAddress, tokenId)
                    if(metadata!!.attributes?.filter {
                            (it.name.equals(traitType) && it.value.equals(
                                traitValue,
                                true
                            )) || (traitValue == null && traitType.equals(it.name))
                        }!!.isNotEmpty()){
                        return true
                    }
                }
                return false;
            }
            else -> {throw Exception("Chain  is not supported")}
        }


    }

    fun  dataNftVerification(chain: EVMChain, erc721FactorycontractAddress: String,erc721contractAddress: String,account: String, propertyKey: String?, propertyValue: String?): Boolean{
        val tx =
            runBlocking {
                val url = Common.getNetworkBlockExplorerApiUrl(chain)
                val apiKey = Common.getNetworkBlockExplorerApiKey(chain)
                val result= getOceanDaoContractCreationTransaction(erc721contractAddress, url, apiKey)
                return@runBlocking result
            }
        if(!tx.result?.get(0)?.from.equals(erc721FactorycontractAddress, ignoreCase = true)){
            return false
        }
        val ownership= NFTsEvmOwnershipVerification(chain, erc721contractAddress, account, BigInteger("1"))
        if(ownership == true ){
            if( propertyKey != null && propertyKey != "" && propertyValue != null) {
                return propertyVerification(chain, erc721contractAddress, "1", propertyKey, propertyValue)
            }
            return true
        }
        return false
    }

    fun verifyPolicy(chain: Chain, contractAddress: String, tokenId: String, policyName: String): Boolean {
        val policy = PolicyRegistry.listPolicies().get(policyName)
        if(policy == null) throw Exception("The policy doesn't exist")
        return when{
            Common.isEVMChain(chain) -> {
                val evmNftmetadata= NftService.getNftMetadata(EVMChain.valueOf(chain.toString()), contractAddress, BigInteger( tokenId))
                val nftMetadata = NftMetadataWrapper(evmNftmetadata,null)
                return DynamicPolicy.doVerify(policy!!.input, policy.policy, policy.policyQuery, nftMetadata)
            }
            Common.isTezosChain(chain) -> {
                val tezosNftmetadata= TezosNftService.getNftTezosMetadata(TezosChain.valueOf(chain.toString()), contractAddress, tokenId)
                val nftMetadata = NftMetadataWrapper(null,tezosNftmetadata)
                return DynamicPolicy.doVerify(policy!!.input, policy.policy, policy.policyQuery, nftMetadata)
            }
            Common.isNearChain(chain) -> {
                val nearNftmetadata= NearNftService.getTokenById( contractAddress,tokenId, NearChain.valueOf(chain.toString()))
                val nftMetadata = NftMetadataWrapper(null,null, nearNftmetadata)
                return DynamicPolicy.doVerify(policy!!.input, policy.policy, policy.policyQuery, nftMetadata)
            }
            else -> {throw Exception("Chain  is not supported")}
            }
    }


    private fun verifyNftOwnershipWithinCollectionTezosChain(chain: Chain, contractAddress: String, owner: String): Boolean {
        val result= TezosNftService.fetchAccountNFTsByTzkt(chain, owner, contractAddress).filter { Integer.parseInt(it.balance)>0 }
        return if (result.size >= 1) true else false
    }

    private fun verifyNftOwnershipWithinCollectionEvmChain(chain: EVMChain, contractAddress: String, owner: String): Boolean {
        val balance= NftService.balanceOf(chain, contractAddress, owner)
        return if (balance!!.compareTo(BigInteger("0")) == 1) true else false
    }


    private fun verifyNftOwnershipWithinCollectionNearChain(chain: NearChain, contractAddress: String, account: String): Boolean {
      try {
          val result = NearNftService.getNFTforAccount(account, contractAddress , NearChain.valueOf(chain.toString()))
          return true
      }
        catch (e: Exception){
            return false
        }
    }

    private suspend fun getOceanDaoContractCreationTransaction(erc721contractAddress: String,url: String, apiKey: String): InternalTransactionsResponse{
            return NftService.client.get("https://$url/api?module=account&action=txlistinternal&address=$erc721contractAddress&page=1&offset=1&startblock=0&sort=asc&apikey=$apiKey") {
                contentType(ContentType.Application.Json)
            }.body()

    }
    private fun NFTsEvmOwnershipVerification(chain: EVMChain, contractAddress: String, account: String, tokenId: BigInteger): Boolean{
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

    private fun NFTsTezosOwnershipVerification(chain: Chain, contractAddress: String, account: String, tokenId: String): Boolean{
        val result= TezosNftService.fetchAccountNFTsByTzkt(chain, account, contractAddress).filter { Integer.parseInt(it.balance)>0 && tokenId.equals(it.token.tokenId) }
        return if (result.size>= 1) true else false
    }


    private fun NFTsNearOwnershipVerification(chain:NearChain, contractAddress : String, account : String, tokenId : String) : Boolean{


        try {
            val result = NearNftService.getTokenById(contractAddress, tokenId , NearChain.valueOf(chain.toString()))
            return result.owner_id.equals(account, true)
        }
        catch (e: Exception){
            return false
        }
    }

    private fun propertyVerification(chain: EVMChain, contractAddress: String, tokenId: String, propertyKey: String, propertyValue: String): Boolean {
        val metadata= NftService.getNftMetadata(chain, contractAddress, BigInteger(tokenId))
        if(compareStrings(propertyKey,"name")){
            return compareStrings(propertyValue, metadata.name)
        }else if(compareStrings(propertyKey,"description")){
            return compareStrings(propertyValue, metadata.description)
        }else if(compareStrings(propertyKey,"image")){
            return compareStrings(propertyValue, metadata.image)
        }else if(compareStrings(propertyKey,"image_data")){
            return compareStrings(propertyValue, metadata.image_data)
        }else if(compareStrings(propertyKey,"external_url")){
            return compareStrings(propertyValue, metadata.external_url)
        }else {
            if (metadata.attributes != null && metadata.attributes.filter {
                    (it.trait_type.equals(propertyKey) && it.value.equals(
                        propertyValue,
                        true
                    )) || (propertyValue == null && propertyKey.equals(it.trait_type))
                }.size > 0) {
                return true
            }
        }
        return false
    }

    private fun compareStrings(s1: String, s2: String?): Boolean{
        return s1.equals(s2, ignoreCase = true)
    }

}
