package id.walt.nftkit.services

import id.walt.nftkit.Values
import id.walt.nftkit.chains.evm.erc721.Erc721TokenStandard
import id.walt.nftkit.metadata.MetadataUri
import id.walt.nftkit.metadata.MetadataUriFactory
import id.walt.nftkit.metadata.NFTStorageAddFileResult
import id.walt.nftkit.services.WaltIdServices.decBase64Str
import id.walt.nftkit.smart_contract_wrapper.Erc721OnchainCredentialWrapper
import id.walt.nftkit.utilis.Common
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.web3j.abi.EventEncoder
import org.web3j.abi.EventValues
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Event
import org.web3j.abi.datatypes.Type
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.protocol.core.methods.response.Log
import org.web3j.protocol.core.methods.response.TransactionReceipt
import java.math.BigInteger


@Serializable
data class NftMetadata(

    var description: String?= null,
    var name: String?=null,
    var image: String?=null,
    var image_data: String?=null,
    var external_url: String?=null,
    val attributes: List<Attributes>?=null

) {
    @Serializable
    data class Attributes(
        val trait_type: String,
        var value: String,
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
    //ERC1155
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

enum class DisplayType {
    NUMBER,
    BOOST_PERCENTAGE,
    BOOST_NUMBER,
    DATE
}

enum class AccessControl {
    OWNABLE,
    ROLE_BASED_ACCESS_CONTROL
}

data class DeploymentOptions(
    val accessControl: AccessControl,
    val tokenStandard: TokenStandard,
)

data class DeploymentParameter(
    val name: String,
    val symbol: String,
    val options: Options
) {
    @Serializable
    data class Options(
        val transferable: Boolean,
        val burnable: Boolean
    )
}

data class MintingParameter(
    val metadataUri: String?,
    val recipientAddress: String,
    val metadata: NftMetadata?,
)

data class MintingOptions(
    val metadataStorageType: MetadataStorageType,
    //val offChainMetadataStorageType: OffChainMetadataStorageType?,
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
    val result: List<Token>
)

@Serializable
data class Token(
    val from: String,
    val contractAddress: String,
    val to: String,
    val tokenID: String,
    val tokenName: String,
    val tokenSymbol: String,
    val timeStamp: Long
)


/*@Serializable
data class NFTsAlchemyResult(
    val ownedNfts : List<NftTokenByAlchemy>,
    val totalCount: Long,
    val blockHash: String?= null,
    val pageKey: String?= null
){
    @Serializable
    data class NftTokenByAlchemy(
        val contract: ContractAddressByAlchemy,
        val id: TokenIdByAlchemy,
    ){
        @Serializable
        data class ContractAddressByAlchemy(
            val address: String,
        )

        @Serializable
        data class TokenIdByAlchemy(
            var tokenId: String,
        )
    }
}*/


@Serializable
data class NFTsAlchemyResult(
    val ownedNfts: List<NftTokenByAlchemy>,
    val totalCount: Long,
    val blockHash: String? = null,
    val pageKey: String? = null
) {
    @Serializable
    data class NftTokenByAlchemy(
        val contract: ContractAddressByAlchemy,
        val id: TokenIdByAlchemy,
        val title: String,
        val description: String? = null,
        val tokenUri: TokenUriByAlchemy,
        //val media: MediaByAlchemy,
        val metadata: NftMetadata?,
       // val timeLastUpdated: String
    ) {
        @Serializable
        data class ContractAddressByAlchemy(
            val address: String,
        )

        @Serializable
        data class TokenIdByAlchemy(
            var tokenId: String,
            val tokenMetadata: TokenTypeByAlchemy
        ) {
            @Serializable
            data class TokenTypeByAlchemy(
                val tokenType: String
            )
        }

        @Serializable
        data class TokenUriByAlchemy(
            val raw: String,
            val gateway: String
        )

        @Serializable
        data class MediaByAlchemy(
            val raw: String,
            val gateway: String
        )
    }
}


object NftService {
    val client = HttpClient(CIO.create{requestTimeout = 0}) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        install(Auth) {
            bearer {
                loadTokens {
                    val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJkaWQ6ZXRocjoweDYwNDNEYThENjU2RTU3NTg2ZDk3MkM1ZDM5RUNENzI1NTNCM2Q1NjAiLCJpc3MiOiJuZnQtc3RvcmFnZSIsImlhdCI6MTY1MDUzNzUzNjk2OSwibmFtZSI6Ik5GVHMifQ.MBK_rahk6e6fCTheE7qLJeZ_OIyKGkbP63aVLPas1sw"
                    BearerTokens(token, token)
                }
            }
        }
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
            // LogLevel.BODY
        }
        expectSuccess = false
    }

    fun deploySmartContractToken(chain: Chain, parameter: DeploymentParameter, options: DeploymentOptions): DeploymentResponse {
        return Erc721TokenStandard.deployContract(chain, parameter, options)
    }

    fun mintToken(
        chain: Chain,
        contractAddress: String,
        parameter: MintingParameter,
        options: MintingOptions
    ): MintingResponse {
        var tokenUri: String?
        if (parameter.metadataUri != null && parameter.metadataUri != "") {
            tokenUri = parameter.metadataUri
        } else {
            val metadataUri: MetadataUri = MetadataUriFactory.getMetadataUri(options.metadataStorageType)
            tokenUri = metadataUri.getTokenUri(parameter.metadata)
        }

        return mintNewToken(parameter.recipientAddress, tokenUri, chain, contractAddress)
    }

     fun getNftMetadata(chain: Chain, contractAddress: String, tokenId: BigInteger): NftMetadata {
        var uri = getMetadatUri(chain, contractAddress, tokenId)
        if(Common.getMetadataType(uri).equals(MetadataStorageType.ON_CHAIN)){
            val decodedUri = decBase64Str(uri.substring(29))
            return Json{ ignoreUnknownKeys = true }.decodeFromString(decodedUri)
        }else{
            return getIPFSMetadataUsingNFTStorage(uri)
        }
    }

    fun getNftMetadataUri(chain: Chain, contractAddress: String, tokenId: BigInteger): String {
        return getMetadatUri(chain, contractAddress, tokenId)
    }

    fun balanceOf(chain: Chain, contractAddress: String, owner: String): BigInteger? {
        if (isErc721Standard(chain, contractAddress)) {
            return Erc721TokenStandard.balanceOf(chain, contractAddress, Address(owner))
        }
        return BigInteger.valueOf(0)
    }

    fun ownerOf(chain: Chain,contractAddress: String, tokenId: BigInteger): String?{
        //in the case of ERC721
        if(isErc721Standard(chain, contractAddress) == true){
            return Erc721TokenStandard.ownerOf(chain, contractAddress,Uint256(tokenId))

        }
        return String()
    }

    fun getTokenCollectionInfo(chain: Chain, contractAddress: String): TokenCollectionInfo {
        //in the case of ERC721
        if (isErc721Standard(chain, contractAddress)) {
            val name = Erc721TokenStandard.name(chain, contractAddress)
            val symbol = Erc721TokenStandard.symbol(chain, contractAddress)
            val tokenCollectionInfo = TokenCollectionInfo(name, symbol)
            return tokenCollectionInfo
        }
        return TokenCollectionInfo("", "")
    }

    fun getAccountNFTsByChainScan(chain: Chain, address: String): List<Token?> {
        return runBlocking {
            val url = Common.getNetworkBlockExplorerApiUrl(chain)
            val apiKey = Common.getNetworkBlockExplorerApiKey(chain)

            val events = fetchAccountNFTsTokens(address, 1, url, apiKey)
            val result = events.groupBy { Pair(it.contractAddress, it.tokenID) }
                .map { it.value.maxByOrNull { it.timeStamp } }
                .filter { address.equals(it?.to, ignoreCase = true) }

            return@runBlocking result
        }
    }

    fun getAccountNFTsByAlchemy(chain: Chain, account: String): List<NFTsAlchemyResult.NftTokenByAlchemy> {
        return runBlocking {
            val url = when (chain) {
                Chain.ETHEREUM -> Values.ETHEREUM_MAINNET_ALCHEMY_URL
                Chain.RINKEBY -> Values.ETHEREUM_TESTNET_RINKEBY_ALCHEMY_URL
                Chain.ROPSTEN -> Values.ETHEREUM_TESTNET_ROPSTEN_ALCHEMY_URL
                Chain.POLYGON -> Values.POLYGON_MAINNET_ALCHEMY_URL
                Chain.MUMBAI -> Values.POLYGON_TESTNET_MUMBAI_ALCHEMY_URL
            }

            val result = fetchAccountNFTsTokensByAlchemy(account = account, url = url)
            result.forEach {
                if ("0X" in it.id.tokenId || "0x" in it.id.tokenId) {
                    it.id.tokenId = BigInteger(it.id.tokenId.substring(2), 16).toString()
                }
            }
            return@runBlocking result
        }
    }

    fun updateMetadata(
        chain: Chain, contractAddress: String, tokenId: String, signedAccount: String?,
        key: String,
        value: String): TransactionResponse {
        val metadata= getNftMetadata(chain, contractAddress, BigInteger(tokenId))
        if("name".equals(key, true)){
            metadata.name= value
        }else if("description".equals(key, true)){
            metadata.description= value
        }else if("image".equals(key, true)){
            metadata.image= value
        }else{
            metadata.attributes?.filter {
                it.trait_type.equals(key, true)
            }?.map { it.value= value }
        }
        val oldUri= getMetadatUri(chain, contractAddress, BigInteger(tokenId))
        val metadataUri: MetadataUri = MetadataUriFactory.getMetadataUri(Common.getMetadataType(oldUri))
        val tokenUri = metadataUri.getTokenUri(metadata)
        val transactionReceipt = Erc721TokenStandard.updateTokenUri(chain, contractAddress, BigInteger(tokenId), Utf8String(tokenUri), signedAccount)
        return Common.getTransactionResponse(chain, transactionReceipt)
    }


    private suspend fun fetchAccountNFTsTokens(address: String, page: Int, url: String, apiKey: String): List<Token> {
        val txs =
            client.get("https://$url/api?module=account&action=tokennfttx&address=$address&page=$page&offset=5&startblock=0&sort=asc&apikey=$apiKey") {
                contentType(ContentType.Application.Json)
            }
                .body<NFTsEtherScanResult>()

        if (txs.status == "1") {
            return txs.result.plus(fetchAccountNFTsTokens(address, page + 1, url, apiKey))
        } else {
            return txs.result
        }
    }

    private suspend fun fetchAccountNFTsTokensByAlchemy(
        account: String,
        url: String,
        apiKey: String? = null
    ): List<NFTsAlchemyResult.NftTokenByAlchemy> {
        var apiKeyUrlParameter = ""
        if (apiKey != null) {
            apiKeyUrlParameter = "&pageKey=$apiKey"
        }
        val nfts =
            client.get("$url${WaltIdServices.loadApiKeys().apiKeys.alchemy}/getNFTs/?owner=$account&withMetadata = true$apiKeyUrlParameter") {
                contentType(ContentType.Application.Json)
            }
                .body<NFTsAlchemyResult>()

        if (nfts.pageKey != null) {
            return nfts.ownedNfts.plus(fetchAccountNFTsTokensByAlchemy(account, url, nfts.pageKey))
        } else {
            return nfts.ownedNfts
        }
    }

     fun getIPFSMetadataUsingNFTStorage(uri: String): NftMetadata {
        return runBlocking {
            var uriFormat= uri
            if(uri.contains("ipfs://", true)){}
            uriFormat= uri.replace("ipfs://","", true)
            val result = NftService.client.get("https://nftstorage.link/ipfs/$uriFormat") {
            }.body<NftMetadata>()
            return@runBlocking result
        }
    }

    fun addFileToIpfsUsingNFTStorage(file: ByteArray): NFTStorageAddFileResult {
        return runBlocking {
            val res = client.post("https://api.nft.storage/upload") {
                contentType(ContentType.Image.Any)
                setBody(file)
            }.body<NFTStorageAddFileResult>()
            return@runBlocking res
        }
    }


    private fun mintNewToken(
        recipientAddress: String,
        metadataUri: String,
        chain: Chain,
        contractAddress: String
    ): MintingResponse {
        if (isErc721Standard(chain, contractAddress) == true) {
            //val erc721TokenStandard = Erc721TokenStandard()
            val recipient = Address(recipientAddress)
            val tokenUri = Utf8String(metadataUri)
            val transactionReceipt: TransactionReceipt? =
                Erc721TokenStandard.mintToken(chain, contractAddress, recipient, tokenUri)
            val eventValues: EventValues? =
                staticExtractEventParameters(Erc721OnchainCredentialWrapper.TRANSFER_EVENT, transactionReceipt?.logs?.get(0))

            val url = WaltIdServices.getBlockExplorerUrl(chain)
            val ts =
                TransactionResponse(transactionReceipt!!.transactionHash, "$url/tx/${transactionReceipt.transactionHash}")
            val mr = MintingResponse(ts, eventValues?.indexedValues?.get(2)?.value as BigInteger)
            return mr
        } else {

        }
        return MintingResponse(null, null)
    }

    private fun getMetadatUri(chain: Chain, contractAddress: String, tokenId: BigInteger): String {
        if (isErc721Standard(chain, contractAddress) == true) {
            return Erc721TokenStandard.tokenURI(chain, contractAddress, Uint256(tokenId))
        }
        return ""
    }


    private fun isErc721Standard(chain: Chain, contractAddress: String): Boolean {
        return Erc721TokenStandard.supportsInterface(chain, contractAddress)
    }

    private fun staticExtractEventParameters(
        event: Event, log: Log?
    ): EventValues? {
        val topics: List<String> = log!!.topics
        val encodedEventSignature = EventEncoder.encode(event)
        if (topics == null || topics.size == 0 || topics[0] != encodedEventSignature) {
            return null
        }
        val indexedValues: MutableList<Type<*>?> = ArrayList<Type<*>?>()
        val nonIndexedValues: List<Type<*>?>? = FunctionReturnDecoder.decode(
            log.data, event.nonIndexedParameters
        )
        val indexedParameters: List<TypeReference<Type<*>?>> = event.indexedParameters
        for (i in indexedParameters.indices) {
            val value: Type<*>? = FunctionReturnDecoder.decodeIndexedValue<Type<*>>(
                topics[i + 1], indexedParameters[i]
            )
            indexedValues.add(value)
        }
        return EventValues(indexedValues, nonIndexedValues)
    }



}
