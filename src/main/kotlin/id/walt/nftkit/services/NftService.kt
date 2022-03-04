package id.walt.nftkit.services

import id.walt.nftkit.chains.evm.erc721.Erc721TokenStandard
import id.walt.nftkit.metadata.MetadataUri
import id.walt.nftkit.metadata.MetadataUriFactory
import id.walt.nftkit.smart_contract_wrapper.Erc721OnchainCredentialWrapper
import kotlinx.serialization.Serializable
import org.web3j.abi.EventEncoder
import org.web3j.abi.EventValues
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.*
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.protocol.core.methods.response.Log
import org.web3j.protocol.core.methods.response.TransactionReceipt
import java.math.BigInteger
import java.util.*


@Serializable
data class NftMetadata(
    val description: String,
    val name: String,
    val image: String?,
    val attributes: List<Attributes>
) {
    @Serializable
    data class Attributes(
        val trait_type: String,
        val value: String,
        val display_type: DisplayType?
    )
}

data class TokenInfo(
    val name: String,
    val symbol: String
)

enum class Chain {
    ETHEREUM,
    PALYGON,
    BSC,
    RINKEBY,
    CELO
}
enum class TokenStandard {
    ERC721,
    ERC1155
}

enum class MetadataStorageType {
    ON_CHAIN,
    OFF_CHAIN
}

enum class OffChainMetadataStorageType {
    IPFS,
    ARWAVE,
    FILECOIN,
    CENTRALIZED
}

enum class DisplayType{
    NUMBER,
    BOOST_PERCENTAGE,
    BOOST_NUMBER,
    DATE
}

data class DeploymentOptions(
    val onchain: Boolean = true,
    val tokenStandard: TokenStandard,
)

data class DeploymentParameter(
    val name: String,
    val symbol: String,
    val uri: String,
    val owner: String
)

data class MintingParameter(
    val metadataUri : String?,
    val recipientAddress: String,
    val metadata: NftMetadata,
)

data class MintingOptions(
    val tokenStandard: TokenStandard,
    val metadataStorageType: MetadataStorageType,
    val offChainMetadataStorageType: OffChainMetadataStorageType?,
)


data class TransactionResponse(
    val transactionId: String,
    val transactionExternalUrl: String
)

data class DeploymentResponse(
    val transactionResponse: TransactionResponse,
    val contractAddress: String
)

data class MintingResponse(
    val transactionResponse: TransactionResponse?,
    val tokenId: BigInteger?
)





object NftService {

    fun DeployNewSmartContractToken(chain: Chain, parameter: DeploymentParameter, options: DeploymentOptions?) /*: TransactionResponse*/{

    }

    fun mintToken(chain: Chain, contractAddress: String, parameter: MintingParameter, options: MintingOptions) :MintingResponse {
       var tokenUri: String;
        if(parameter.metadataUri != null){
            tokenUri = parameter.metadataUri
        }else{
            val metadataUri: MetadataUri = MetadataUriFactory.getMetadataUri()
            tokenUri = metadataUri.getTokenUri(parameter.metadata)
        }

        return mintNewToken(parameter.recipientAddress, tokenUri, chain, contractAddress, TokenStandard.ERC721)
    }

    fun getNftMetadata(chain: Chain,contractAddress: String, tokenId: Int)/*: NftMetadata*/{
    }

    fun getNftMetadataUri(chain: Chain,contractAddress: String, tokenId: Int): String{
        return String()
    }

    fun balanceOf(chain: Chain,contractAddress: String, tokenId: Int) : BigInteger {
        return BigInteger.valueOf(0)
    }

    fun ownerOf(tokenId: Int): String{
        return String()
    }

    fun getTokenInfo(chain: Chain,contractAddress: String)/*: TokenInfo*/{
    }

    fun encBase64Str(data: String): String = String(Base64.getEncoder().encode(data.toByteArray()))

    private fun mintNewToken(recipientAddress: String, metadataUri: String, chain: Chain ,contractAddress: String,tokenStandard: TokenStandard): MintingResponse{
        if(tokenStandard == TokenStandard.ERC721){
            val erc721TokenStandard = Erc721TokenStandard()
            val recipient: Address = Address(recipientAddress)
            val tokenUri: Utf8String = Utf8String(metadataUri)
            val transactionReceipt: TransactionReceipt? = erc721TokenStandard.mintToken(contractAddress,recipient, tokenUri)
            val eventValues: EventValues? = staticExtractEventParameters(Erc721OnchainCredentialWrapper.TRANSFER_EVENT, transactionReceipt?.logs?.get(0))
            val ts: TransactionResponse = TransactionResponse(transactionReceipt!!.transactionHash,"https://ropsten.etherscan.io/tx/"+ transactionReceipt!!.transactionHash)
            val mr: MintingResponse = MintingResponse(ts, eventValues?.indexedValues?.get(2)?.value as BigInteger)
            return mr
        }else{

        }
        return MintingResponse(null, null)
    }


    private fun staticExtractEventParameters(
        event: Event, log: Log?
    ): EventValues? {
        val topics: List<String> = log!!.getTopics()
        val encodedEventSignature = EventEncoder.encode(event)
        if (topics == null || topics.size == 0 || topics[0] != encodedEventSignature) {
            return null
        }
        val indexedValues: MutableList<Type<*>?> = ArrayList<Type<*>?>()
        val nonIndexedValues: List<Type<*>?>? = FunctionReturnDecoder.decode(
            log.getData(), event.getNonIndexedParameters()
        )
        val indexedParameters: List<TypeReference<Type<*>?>> = event.getIndexedParameters()
        for (i in indexedParameters.indices) {
            val value: Type<*>? = FunctionReturnDecoder.decodeIndexedValue<Type<*>>(
                topics[i + 1], indexedParameters[i]
            )
            indexedValues.add(value)
        }
        return EventValues(indexedValues, nonIndexedValues)
    }


}