package id.walt.nftkit.services

import id.walt.nftkit.Values
import id.walt.nftkit.chains.evm.erc721.Erc721TokenStandard
import id.walt.nftkit.metadata.MetadataUri
import id.walt.nftkit.metadata.MetadataUriFactory
import id.walt.nftkit.services.WaltIdServices.decBase64Str
import id.walt.nftkit.smart_contract_wrapper.Erc721OnchainCredentialWrapper
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
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
        //val display_type: DisplayType?
    )
}

data class TokenCollectionInfo(
    val name: String,
    val symbol: String
)

enum class Chain {
    ETHEREUM,
    POLYGON,
    RINKEBY,
    ROPSTEN,
    MUMBAI
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
    val metadata: NftMetadata?,
)

data class MintingOptions(
    val metadataStorageType: MetadataStorageType,
    val offChainMetadataStorageType: OffChainMetadataStorageType?,
)


data class TransactionResponse(
    val transactionId: String,
    val transactionExternalUrl: String
)

data class DeploymentResponse(
    val transactionResponse: TransactionResponse,
    val contractAddress: String,
    val contractExternalUrl: String
    )

data class MintingResponse(
    val transactionResponse: TransactionResponse?,
    val tokenId: BigInteger?
)

@Serializable
data class NFTsEtherScanResult(
    val status: String,
    val message: String,
    val result : List<Token>
)
@Serializable
data class Token(
    val from: String,
    val contractAddress: String,
    val to: String,
    val tokenID: String,
    val tokenName: String,
    val tokenSymbol: String,
)





object NftService {

    val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
            })
        }
        expectSuccess = false
    }

    fun deploySmartContractToken(chain: Chain, parameter: DeploymentParameter, options: DeploymentOptions?) : DeploymentResponse{
        //if(options?.tokenStandard == TokenStandard.ERC721){
            return Erc721TokenStandard.deployContract(chain, parameter.name, parameter.symbol)
        //}
    }

    fun mintToken(chain: Chain, contractAddress: String, parameter: MintingParameter, options: MintingOptions) :MintingResponse {
       var tokenUri: String?;
        if(parameter.metadataUri != null && parameter.metadataUri != ""){
            tokenUri = parameter.metadataUri
        }else{
            val metadataUri: MetadataUri = MetadataUriFactory.getMetadataUri()
            tokenUri = metadataUri.getTokenUri(parameter.metadata)
        }

        return mintNewToken(parameter.recipientAddress, tokenUri, chain, contractAddress)
    }

    fun getNftMetadata(chain: Chain, contractAddress: String, tokenId: BigInteger): NftMetadata {
        //Work just for on-chain metadata
        var uri = getMetadatUri(chain, contractAddress, tokenId)
        val decodedUri = decBase64Str(uri!!.substring(29))
        return Json.decodeFromString(decodedUri)
    }

    fun getNftMetadataUri(chain: Chain,contractAddress: String, tokenId: BigInteger): String?{
        return getMetadatUri(chain, contractAddress,tokenId)
    }

    fun balanceOf(chain: Chain,contractAddress: String, owner: String) : BigInteger? {
        if(isErc721Standard(chain, contractAddress)){
            return Erc721TokenStandard.balanceOf(chain, contractAddress, Address(owner))
        }
        return BigInteger.valueOf(0)
    }

    fun ownerOf(chain: Chain,contractAddress: String, tokenId: Long): String?{
        //in the case of ERC721
        if(isErc721Standard(chain, contractAddress) == true){
            return Erc721TokenStandard.ownerOf(chain, contractAddress,Uint256(BigInteger.valueOf(tokenId)))
        }
        return String()
    }

    fun getTokenCollectionInfo(chain: Chain,contractAddress: String): TokenCollectionInfo{
        //in the case of ERC721
        if(isErc721Standard(chain, contractAddress)) {
            val name = Erc721TokenStandard.name(chain,contractAddress)
            val symbol = Erc721TokenStandard.symbol(chain, contractAddress)
            val tokenCollectionInfo = TokenCollectionInfo(name, symbol)
            return tokenCollectionInfo
        }
        return TokenCollectionInfo("","")
    }

    fun getNFTsPerAddress(chain: Chain, address: String): List<Token> {
        return runBlocking {
            val url = when(chain){
                Chain.RINKEBY -> Values.ETHEREUM_TESTNET_RINKEBY_SCAN_API_URL
                Chain.POLYGON -> Values.POLYGON_MAINNET_SCAN_API_URL
                Chain.MUMBAI -> Values.POLYGON_TESTNET_MUMBAI_SCAN_API_URL
                else -> ""
            }

            val apiKey = when(chain){
                Chain.RINKEBY -> WaltIdServices.loadChainScanApiKeys().blockExplorerScanApiKeys.ethereum
                Chain.POLYGON -> WaltIdServices.loadChainScanApiKeys().blockExplorerScanApiKeys.polygon
                Chain.MUMBAI -> WaltIdServices.loadChainScanApiKeys().blockExplorerScanApiKeys.polygon
                else -> ""
            }

            val result = fetchTokens(address,1, url, apiKey)
            return@runBlocking result
        }
    }

    private suspend fun fetchTokens(address: String, page: Int, url: String, apiKey: String): List<Token> {
        val txs =
            client.get<NFTsEtherScanResult>("https://$url/api?module=account&action=tokennfttx&address=$address&page=$page&offset=5&startblock=0&sort=asc&apikey=$apiKey") {
                contentType(ContentType.Application.Json)
            }

        if(txs.status == "1"){
            return txs.result.plus(fetchTokens(address, page+1, url, apiKey))
        }else{
            return txs.result
        }
    }



    private fun mintNewToken(recipientAddress: String, metadataUri: String, chain: Chain ,contractAddress: String): MintingResponse{
        if(isErc721Standard(chain, contractAddress) == true){
            //val erc721TokenStandard = Erc721TokenStandard()
            val recipient = Address(recipientAddress)
            val tokenUri = Utf8String(metadataUri)
            val transactionReceipt: TransactionReceipt? = Erc721TokenStandard.mintToken(chain, contractAddress,recipient, tokenUri)
            val eventValues: EventValues? = staticExtractEventParameters(Erc721OnchainCredentialWrapper.TRANSFER_EVENT, transactionReceipt?.logs?.get(0))

            val url = WaltIdServices.getBlockExplorerUrl(chain)
            val ts = TransactionResponse(transactionReceipt!!.transactionHash, "$url/tx/${transactionReceipt!!.transactionHash}" )
            val mr: MintingResponse = MintingResponse(ts, eventValues?.indexedValues?.get(2)?.value as BigInteger)
            return mr
        }else{

        }
        return MintingResponse(null, null)
    }

    private fun getMetadatUri(chain: Chain ,contractAddress: String, tokenId: BigInteger): String?{
        if(isErc721Standard(chain, contractAddress) == true){
            return Erc721TokenStandard.tokenURI(chain, contractAddress, Uint256(tokenId))
        }
        return ""
    }



    private fun  isErc721Standard(chain: Chain,contractAddress: String): Boolean{
        return Erc721TokenStandard.supportsInterface(chain, contractAddress)
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