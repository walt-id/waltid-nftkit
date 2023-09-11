package id.walt.nftkit.services


import id.walt.nftkit.opa.DynamicPolicy
import id.walt.nftkit.opa.PolicyRegistry
import id.walt.nftkit.utilis.Common
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import org.web3j.tx.exceptions.ContractCallException
import java.math.BigInteger

@Serializable
data class InternalTransactionsResponse(
    val status: String, val message: String, val result: List<InternalTransaction>? = null
) {
    @Serializable
    data class InternalTransaction(
        val from: String, val hash: String, val contractAddress: String
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
    fun verifyNftOwnership(chain: Chain, contractAddress: String, account: String, tokenId: String): Boolean {
        return when {
            Common.isEVMChain(chain) -> NFTsEvmOwnershipVerification(
                EVMChain.valueOf(chain.toString()), contractAddress, account, BigInteger(tokenId)
            )

            Common.isTezosChain(chain) -> NFTsTezosOwnershipVerification(chain, contractAddress, account, tokenId)
            Common.isNearChain(chain) -> NFTsNearOwnershipVerification(
                NearChain.valueOf(chain.toString()), contractAddress, account, tokenId
            )

            Common.isPolkadotParachain(chain) -> NFTsPolkadotOwnershipVerification(
                PolkadotParachain.valueOf(chain.toString()), contractAddress, account, tokenId
            )

            Common.isUniqueParachain(chain) -> NFTsUniqueOwnershipVerification(
                UniqueNetwork.valueOf(chain.toString()), contractAddress, account, tokenId
            )

            Common.isAlgorand(chain) -> NFTsAlgorandOwnershipVerification(AlgorandChain.valueOf(chain.toString()), account, tokenId)

            else -> throw Exception("Chain  is not supported")
        }
    }

    fun verifyNftOwnershipOnFlow(chain: Chain, contractAddress: String, account: String, tokenId: String, collectionPath: String): Boolean {
        return NFTsFlowOwnershipVerification(FlowChain.valueOf(chain.toString()), contractAddress, account, tokenId, collectionPath)
    }

    fun verifyNftOwnershipInCollectionFlow(chain: Chain, contractAddress: String, account: String, collectionPath: String): Boolean {
        return NFTsFlowOwnershipVerificationInCollection(
            FlowChain.valueOf(chain.toString()), contractAddress, account, collectionPath
        )
    }

    fun verifyNftOwnershipWithCollectionId(chain: UniqueNetwork, collectionId: String, account: String, tokenId: String): Boolean {
        return NFTsUniqueOwnershipVerification(chain, collectionId, account, tokenId)
    }


    fun verifyNftOwnershipWithinCollection(chain: Chain, contractAddress: String, account: String): Boolean {
        return when {
            Common.isEVMChain(chain) -> {
                return verifyNftOwnershipWithinCollectionEvmChain(EVMChain.valueOf(chain.toString()), contractAddress, account)
            }

            Common.isTezosChain(chain) -> {
                return verifyNftOwnershipWithinCollectionTezosChain(chain, contractAddress, account)
            }

            Common.isNearChain(chain) -> {
                return verifyNftOwnershipWithinCollectionNearChain(NearChain.valueOf(chain.toString()), contractAddress, account)
            }

            Common.isPolkadotParachain(chain) -> {
                return verifyNftOwnershipWithinCollectionPolkadotChain(
                    PolkadotParachain.valueOf(chain.toString()), contractAddress, account
                )
            }

            Common.isUniqueParachain(chain) -> {
                return verifyNftOwnershipWithinCollectionUniqueParachain(UniqueNetwork.valueOf(chain.toString()), contractAddress, account)
            }

            else -> {
                throw Exception("Chain  is not supported")
            }
        }

    }

    fun verifyNftOwnershipWithinCollectionWithCollectionId(chain: UniqueNetwork, collectionId: String, account: String): Boolean {
        return verifyNftOwnershipWithinCollectionUniqueParachain(chain, collectionId, account)
    }


    //  simply check if a certain trait type and trait value is in the metadata
    fun verifyNftOwnershipWithTraits(
        chain: Chain, contractAddress: String, account: String, tokenId: String, traitType: String, traitValue: String? = null
    ): Boolean {
        when {
            Common.isEVMChain(chain) -> {
                val ownership =
                    NFTsEvmOwnershipVerification(EVMChain.valueOf(chain.toString()), contractAddress, account, BigInteger(tokenId))
                if (ownership) {
                    val metadata = NftService.getNftMetadata(EVMChain.valueOf(chain.toString()), contractAddress, BigInteger(tokenId))

                    metadata.attributes?.map {
                        if (it.trait_type == traitType && it.value?.content.equals(traitValue)) {
                            return true
                        }
                    }
                }
                return false
            }

            Common.isTezosChain(chain) -> {
                val ownership = NFTsTezosOwnershipVerification(chain, contractAddress, account, tokenId)
                if (ownership) {
                    val metadata = TezosNftService.getNftTezosMetadata(TezosChain.valueOf(chain.toString()), contractAddress, tokenId)
                    println("ATTRIBUTES: ${metadata?.attributes}")
                    if (metadata!!.attributes?.filter {
                            (it.name.equals(traitType) && it.value.equals(
                                traitValue,
                                true
                            )) || (traitValue == null && traitType.equals(it.name))
                        }!!.isNotEmpty()) {
                        return true
                    }
                }

                return false
            }

            Common.isPolkadotParachain(chain) -> {
                val ownership =
                    NFTsPolkadotOwnershipVerification(PolkadotParachain.valueOf(chain.toString()), contractAddress, account, tokenId)
                if (ownership) {
                    val metadata = NftService.getNftMetadata(EVMChain.valueOf(chain.toString()), contractAddress, BigInteger(tokenId))
                    if (metadata.attributes?.filter {
                            (it.trait_type.equals(traitType) && it.value?.equals(
                                traitValue
                            ) != false) || ((traitValue == null) && traitType.equals(it.trait_type))
                        }!!.isNotEmpty()) {
                        return true
                    }
                }
                return false
            }


            else -> {
                throw Exception("Chain  is not supported")
            }
        }


    }

    fun verifyNftOwnershipWithTraitsWithCollectionId(
        chain: UniqueNetwork, collectionId: String, account: String, tokenId: String, name: String, value: String? = null
    ): Boolean {
        val ownership = NFTsUniqueOwnershipVerification(chain, collectionId, account, tokenId)
        if (ownership) {
            val result = PolkadotNftService.fetchUniqueNFTMetadata(chain, collectionId, tokenId)
            if (result != null && result.data != null) {
                val uniqueNftMetadata = PolkadotNftService.parseNftMetadataUniqueResponse(result)
                if (uniqueNftMetadata.attributes?.filter {
                        (it.name.equals(name) && it.value.toString().equals(
                            value, true
                        )) || (value == null && name.equals(it.name))
                    }!!.isNotEmpty()) {
                    return true
                }
            }
        }
        return false
    }

    fun dataNftVerification(
        chain: EVMChain,
        erc721FactorycontractAddress: String,
        erc721contractAddress: String,
        account: String,
        propertyKey: String?,
        propertyValue: String?
    ): Boolean {
        val tx = runBlocking {
            val url = Common.getNetworkBlockExplorerApiUrl(chain)
            val apiKey = Common.getNetworkBlockExplorerApiKey(chain)
            val result = getOceanDaoContractCreationTransaction(erc721contractAddress, url, apiKey)
            return@runBlocking result
        }
        if (!tx.result?.get(0)?.from.equals(erc721FactorycontractAddress, ignoreCase = true)) {
            return false
        }
        val ownership = NFTsEvmOwnershipVerification(chain, erc721contractAddress, account, BigInteger("1"))
        if (ownership == true) {
            if (propertyKey != null && propertyKey != "" && propertyValue != null) {
                return propertyVerification(chain, erc721contractAddress, "1", propertyKey, propertyValue)
            }
            return true
        }
        return false
    }

    fun verifyPolicy(chain: Chain, contractAddress: String, tokenId: String, policyName: String): Boolean {
        val policy = PolicyRegistry.listPolicies().get(policyName)
        if (policy == null) throw Exception("The policy doesn't exist")
        return when {
            Common.isEVMChain(chain) -> {
                val evmNftmetadata = NftService.getNftMetadata(EVMChain.valueOf(chain.toString()), contractAddress, BigInteger(tokenId))
                val nftMetadata = NftMetadataWrapper(evmNftMetadata = evmNftmetadata)
                return DynamicPolicy.doVerify(policy.input, policy.policy, policy.policyQuery, nftMetadata)
            }

            Common.isTezosChain(chain) -> {
                val tezosNftmetadata = TezosNftService.getNftTezosMetadata(TezosChain.valueOf(chain.toString()), contractAddress, tokenId)
                val nftMetadata = NftMetadataWrapper(tezosNftMetadata = tezosNftmetadata)
                return DynamicPolicy.doVerify(policy.input, policy.policy, policy.policyQuery, nftMetadata)
            }

            Common.isNearChain(chain) -> {
                val nearNftmetadata = NearNftService.getTokenById(contractAddress, tokenId, NearChain.valueOf(chain.toString()))
                val nftMetadata = NftMetadataWrapper(nearNftMetadata = nearNftmetadata)
                return DynamicPolicy.doVerify(policy.input, policy.policy, policy.policyQuery, nftMetadata)
            }


            Common.isPolkadotParachain(chain) -> {
                val evmNftmetadata = NftService.getNftMetadata(EVMChain.valueOf(chain.toString()), contractAddress, BigInteger(tokenId))
                val nftMetadata = NftMetadataWrapper(evmNftMetadata = evmNftmetadata)
                return DynamicPolicy.doVerify(policy.input, policy.policy, policy.policyQuery, nftMetadata)
            }

            Common.isAlgorand(chain) -> {
                val algorandNftmetadata = AlgorandNftService.getNftMetadata(tokenId, AlgorandChain.valueOf(chain.toString()))
                val nftMetadata = NftMetadataWrapper(algorandNftMetadata = algorandNftmetadata)
                return DynamicPolicy.doVerify(policy.input, policy.policy, policy.policyQuery, nftMetadata)
            }

            else -> {
                throw Exception("Chain  is not supported")
            }
        }
    }

    fun verifyPolicyAlgorand(chain: Chain, tokenId: String, policyName: String): Boolean {
        val policy = PolicyRegistry.listPolicies().get(policyName)
        if (policy == null) throw Exception("The policy doesn't exist")
        val algorandNftmetadata = AlgorandNftService.getNftMetadata(tokenId, AlgorandChain.valueOf(chain.toString()))
        val nftMetadata = NftMetadataWrapper(algorandNftMetadata = algorandNftmetadata)
        return DynamicPolicy.doVerify(policy.input, policy.policy, policy.policyQuery, nftMetadata)
    }

    fun verifyPolicyWithCollectionId(chain: UniqueNetwork, collectionId: String, tokenId: String, policyName: String): Boolean {
        val policy = PolicyRegistry.listPolicies().get(policyName)
        if (policy == null) throw Exception("The policy doesn't exist")
        val result = PolkadotNftService.fetchUniqueNFTMetadata(chain, collectionId, tokenId)
        val uniqueNftMetadata = PolkadotNftService.parseNftMetadataUniqueResponse(result!!)
        val nftMetadata = NftMetadataWrapper(uniqueNftMetadata = uniqueNftMetadata)
        return DynamicPolicy.doVerify(policy.input, policy.policy, policy.policyQuery, nftMetadata)
    }


    private fun verifyNftOwnershipWithinCollectionTezosChain(chain: Chain, contractAddress: String, owner: String): Boolean {
        val result = TezosNftService.fetchAccountNFTsByTzkt(chain, owner, contractAddress).filter { Integer.parseInt(it.balance) > 0 }
        return result.isNotEmpty()
    }

    private fun verifyNftOwnershipWithinCollectionEvmChain(chain: EVMChain, contractAddress: String, owner: String): Boolean {
        val balance = NftService.balanceOf(chain, contractAddress, owner)
        return balance!!.compareTo(BigInteger("0")) == 1
    }


    private fun verifyNftOwnershipWithinCollectionNearChain(chain: NearChain, contractAddress: String, account: String): Boolean {
        try {
            val result = NearNftService.getNFTforAccount(account, contractAddress, NearChain.valueOf(chain.toString()))
            return true
        } catch (e: Exception) {
            return false
        }
    }

    private fun verifyNftOwnershipWithinCollectionPolkadotChain(
        parachain: PolkadotParachain, contractAddress: String, owner: String
    ): Boolean {
        val polkadotNFTsSubscanResult = PolkadotNftService.fetchAccountTokensBySubscan(parachain, owner)
        if (polkadotNFTsSubscanResult.data == null) return false
        val result = polkadotNFTsSubscanResult.data.ERC721?.filter {
            Integer.parseInt(it.balance) > 0 && contractAddress.uppercase().equals(it.contract.uppercase())
        }
        return result!!.isNotEmpty()
    }

    private fun verifyNftOwnershipWithinCollectionUniqueParachain(
        parachain: UniqueNetwork, collectionId: String, account: String
    ): Boolean {
        val uniqueNftsResult = PolkadotNftService.fetchUniqueNFTs(parachain, account)
        return !(uniqueNftsResult.data.isNullOrEmpty())
    }

    private suspend fun getOceanDaoContractCreationTransaction(
        erc721contractAddress: String, url: String, apiKey: String
    ): InternalTransactionsResponse {
        return NftService.client.get("https://$url/api?module=account&action=txlistinternal&address=$erc721contractAddress&page=1&offset=1&startblock=0&sort=asc&apikey=$apiKey") {
            contentType(ContentType.Application.Json)
        }.body()

    }

    private fun NFTsEvmOwnershipVerification(chain: EVMChain, contractAddress: String, account: String, tokenId: BigInteger): Boolean {
        try {
            val owner = NftService.ownerOf(chain, contractAddress, tokenId)
            return account.equals(owner, true)
        } catch (e: ContractCallException) {
            return false
        }
        return false
    }

    private fun NFTsTezosOwnershipVerification(chain: Chain, contractAddress: String, account: String, tokenId: String): Boolean {
        val result = TezosNftService.fetchAccountNFTsByTzkt(chain, account, contractAddress)
            .filter { Integer.parseInt(it.balance) > 0 && tokenId.equals(it.token.tokenId) }
        return result.isNotEmpty()
    }

    fun NFTsAlgorandOwnershipVerification(chain: AlgorandChain, account: String, assetId: String): Boolean {
        val result = AlgorandNftService.verifyOwnership(account, assetId, chain)
        return result.assetHolding?.assetId.toString().equals(assetId)
    }

    fun NFTAlgorandOwnershipVerificationWithTraits(
        chain: AlgorandChain, account: String, assetId: String, traitType: String, traitValue: String
    ): Boolean {
        val result = AlgorandNftService.verifyOwnerShipWithTraits(account, assetId, chain, traitType, traitValue)
        return result
    }

    private fun NFTsNearOwnershipVerification(chain: NearChain, contractAddress: String, account: String, tokenId: String): Boolean {


        try {
            val result = NearNftService.getTokenById(contractAddress, tokenId, NearChain.valueOf(chain.toString()))
            return result.owner_id.equals(account, true)
        } catch (e: Exception) {
            return false
        }
    }

    private fun NFTsFlowOwnershipVerification(
        chain: FlowChain, contractAddress: String, account: String, tokenId: String, collectionPath: String
    ): Boolean {
        try {
            val result = FlowNftService.getNFTbyId(account, contractAddress, collectionPath, tokenId, FlowChain.valueOf(chain.toString()))
            // if the result is not null return true
            return result.id.equals(tokenId, true)

        } catch (e: Exception) {
            return false
        }
    }

    private fun NFTsFlowOwnershipVerificationInCollection(
        chain: FlowChain, contractAddress: String, account: String, collectionPath: String
    ): Boolean {

        try {
            val result = FlowNftService.getNFTinCollectionPath(account, collectionPath, FlowChain.valueOf(chain.toString()))
            for (i in result.indices) {

                // if identifier has contract address
                if (result[i].identifier?.contains(contractAddress.removePrefix("0x"), true) == true) {
                    return true
                }

            }
            return false
        } catch (e: Exception) {
            return false
        }
    }


    fun verifyPolicyFlow(
        chain: Chain, contractAddress: String, collectionPath: String, tokenId: String, policyName: String, account: String
    ): Boolean {
        val policy = PolicyRegistry.listPolicies().get(policyName)

        if (policy == null) throw Exception("The policy doesn't exist")
        if (policy.input == null) throw Exception("The policy doesn't have input")
        if (policy.policy == null) throw Exception("The policy doesn't have policy")
        if (policy.policyQuery == null) throw Exception("The policy doesn't have policyQuery")

        val flowNftmetadata =
            FlowNftService.getNFTbyId(account, contractAddress, collectionPath, tokenId, FlowChain.valueOf(chain.toString()))
        val nftMetadata = NftMetadataWrapper(null, null, null, flowNftmetadata)
        return DynamicPolicy.doVerify(policy.input, policy.policy, policy.policyQuery, nftMetadata)

    }


    private fun NFTsPolkadotOwnershipVerification(
        parachain: PolkadotParachain, contractAddress: String, account: String, tokenId: String
    ): Boolean {
        val evmErc721CollectiblesResult = PolkadotNftService.fetchEvmErc721CollectiblesBySubscan(parachain, account)
        if (evmErc721CollectiblesResult.data?.list == null) return false
        val result = evmErc721CollectiblesResult.data.list.filter {
            contractAddress.uppercase().equals(it.contract.uppercase()) && tokenId.equals(it.token_id)
        }
        return result!!.isNotEmpty()
    }

    private fun NFTsUniqueOwnershipVerification(parachain: UniqueNetwork, collectionId: String, account: String, tokenId: String): Boolean {
        val uniqueNftsResult = PolkadotNftService.fetchUniqueNFTs(parachain, account)
        if (uniqueNftsResult.data.isNullOrEmpty()) return false
        val result =
            uniqueNftsResult.data.filter { collectionId.equals(it.collection_id.toString()) && tokenId.equals(it.token_id.toString()) }
        return result.isNotEmpty()
    }


    private fun propertyVerification(
        chain: EVMChain, contractAddress: String, tokenId: String, propertyKey: String, propertyValue: String
    ): Boolean {
        val metadata = NftService.getNftMetadata(chain, contractAddress, BigInteger(tokenId))
        if (compareStrings(propertyKey, "name")) {
            return compareStrings(propertyValue, metadata.name)
        } else if (compareStrings(propertyKey, "description")) {
            return compareStrings(propertyValue, metadata.description)
        } else if (compareStrings(propertyKey, "image")) {
            return compareStrings(propertyValue, metadata.image)
        } else if (compareStrings(propertyKey, "image_data")) {
            return compareStrings(propertyValue, metadata.image_data)
        } else if (compareStrings(propertyKey, "external_url")) {
            return compareStrings(propertyValue, metadata.external_url)
        } else {
            if ((metadata.attributes != null) && metadata.attributes.filter {
                    (it.trait_type.equals(propertyKey) && it.value?.equals(
                        propertyValue
                    ) != false) || ((propertyValue == null) && propertyKey.equals(it.trait_type))
                }.isNotEmpty()) {
                return true
            }
        }
        return false
    }

    private fun compareStrings(s1: String, s2: String?): Boolean {
        return s1.equals(s2, ignoreCase = true)
    }

}
