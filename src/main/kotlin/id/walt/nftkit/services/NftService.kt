package id.walt.nftkit.services

import id.walt.nftkit.Values
import id.walt.nftkit.chains.evm.erc721.Erc721TokenStandard
import id.walt.nftkit.chains.evm.erc721.SoulBoundTokenStandard
import id.walt.nftkit.metadata.IPFSMetadata
import id.walt.nftkit.metadata.MetadataUri
import id.walt.nftkit.metadata.MetadataUriFactory
import id.walt.nftkit.metadata.NFTStorageAddFileResult
import id.walt.nftkit.models.NFTsInfos
import id.walt.nftkit.services.WaltIdServices.decBase64Str
import id.walt.nftkit.smart_contract_wrapper.Erc721OnchainCredentialWrapper
import id.walt.nftkit.utilis.Common
import io.javalin.core.util.RouteOverviewUtil.metaInfo
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import org.web3j.abi.EventEncoder
import org.web3j.abi.EventValues
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.*
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.protocol.core.methods.response.Log
import org.web3j.protocol.core.methods.response.TransactionReceipt
import java.math.BigInteger

@Serializable
sealed class Value {

    data class StringValue(val value: String) : Value()

    data class NumberValue(val value: Int) : Value()
}

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

        var value: JsonPrimitive? = null

    )
}

data class NftMetadataWrapper(
    val evmNftMetadata: NftMetadata?= null,
    val tezosNftMetadata: TezosNftMetadata?= null,
    val nearNftMetadata: NearNftMetadata?= null,
    val flowNftMetadata: FlowNFTMetadata?= null,
    val uniqueNftMetadata: UniqueNftMetadata?= null,
    val algorandNftMetadata : AlgoNftMetadata?= null,

)

data class TokenCollectionInfo(
    val name: String,
    val symbol: String
)

enum class Chain {
    ETHEREUM,
    POLYGON,
    GOERLI,
    SEPOLIA,
    MUMBAI,
    TEZOS,
    GHOSTNET,
    MAINNET,
    TESTNET,
    BETANET,
    ASTAR,
    MOONBEAM,
    UNIQUE,
    OPAL,
    ALGORAND_MAINNET,
    ALGORAND_TESTNET,
    ALGORAND_BETANET,
    SHIMMEREVM
}

enum class EVMChain {
    ETHEREUM,
    POLYGON,
    GOERLI,
    SEPOLIA,
    MUMBAI,
    ASTAR,
    MOONBEAM,
    SHIMMEREVM,
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

@Serializable
data class Trait_shimmer(
    val trait_type: String,
    val value: String
)
@Serializable

data class Metadata(
    val attributes: JsonArray?=null,
    val description: String?=null,
    val name: String?=null,
)
@Serializable
data class Owner(
    val hash: String,
    val implementation_name: String?,
    val is_contract: Boolean,
    val is_verified: Boolean?,
    val name: String?,
    val private_tags: List<String>?,
    val public_tags: List<String>?,
    val watchlist_names: List<String>?
)
@Serializable

data class Token_info(
    val address: String,
    val circulating_market_cap: String?,
    val decimals: Int?,
    val exchange_rate: String?,
    val holders: String,
    val icon_url: String?,
    val name: String,
    val symbol: String,
    val total_supply: String?,
    val type: String
)
@Serializable
data class Item(
    val animation_url: String?,
    val external_app_url: String?,
    val id: String,
    val image_url: String?,
    val is_unique: Boolean,
    val metadata: Metadata,
    val owner: Owner,
    val token: Token_info
)
@Serializable

data class shimmerNFT(
    val items: List<Item>,
    val next_page_params: String?,
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
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
            // LogLevel.BODY
        }
        expectSuccess = false
    }

    fun deploySmartContractToken(chain: EVMChain, parameter: DeploymentParameter, options: DeploymentOptions): DeploymentResponse {
        return if (parameter.options.transferable){
            Erc721TokenStandard.deployContract(chain, parameter, options)
        }else{
            SoulBoundTokenStandard.deployContract(chain)
        }
    }


    fun mintToken(
        chain: EVMChain,
        contractAddress: String,
        parameter: MintingParameter,
        options: MintingOptions,
        isSoulBound: Boolean
    ): MintingResponse {
        var tokenUri: String?
        if (parameter.metadataUri != null && parameter.metadataUri != "") {
            tokenUri = parameter.metadataUri
        } else {
            val metadataUri: MetadataUri = MetadataUriFactory.getMetadataUri(options.metadataStorageType)
            val nftMetadataWrapper= NftMetadataWrapper(evmNftMetadata = parameter.metadata)
            tokenUri = metadataUri.getTokenUri(nftMetadataWrapper)
        }

        return mintNewToken(parameter.recipientAddress, tokenUri, chain, contractAddress , isSoulBound )
    }


     fun getNftMetadata(chain: EVMChain, contractAddress: String, tokenId: BigInteger): NftMetadata {
        var uri = getMetadatUri(chain, contractAddress, tokenId)
        if(Common.getMetadataType(uri).equals(MetadataStorageType.ON_CHAIN)){
            val decodedUri = decBase64Str(uri.substring(29))
            return Json{ ignoreUnknownKeys = true }.decodeFromString(decodedUri)
        }else{
            if(uri.contains("https://", true)){
                return getWebDocumentMetadata(uri)
            }else{
                return getIPFSMetadataUsingNFTStorage(uri)
            }
        }
    }

    fun getNftMetadataUri(chain: EVMChain, contractAddress: String, tokenId: BigInteger): String {
        return getMetadatUri(chain, contractAddress, tokenId)
    }

    fun balanceOf(chain: EVMChain, contractAddress: String, owner: String): BigInteger? {
        if (isErc721Standard(chain, contractAddress)) {
            return Erc721TokenStandard.balanceOf(chain, contractAddress, Address(owner))
        }
        return BigInteger.valueOf(0)
    }

    fun ownerOf(chain: EVMChain,contractAddress: String, tokenId: BigInteger): String{
        //in the case of ERC721
        if(isErc721Standard(chain, contractAddress) == true){
            return Erc721TokenStandard.ownerOf(chain, contractAddress,Uint256(tokenId))

        }
        return String()
    }

    fun ownerOfSoulbound( chain: EVMChain , contractAddress: String , tokenId: Uint256) : String{
        return SoulBoundTokenStandard.ownerOf(chain, contractAddress, tokenId).toString()
    }

    fun transferFrom(chain: EVMChain, contractAddress: String, from: String, to: String, tokenId: BigInteger, signedAccount: String?): TransactionResponse {
        val transactionReceipt = Erc721TokenStandard.transferFrom(chain, contractAddress, Address(from), Address(to), Uint256(tokenId), signedAccount)
        return Common.getTransactionResponse(chain, transactionReceipt)
    }

    fun safeTransferFrom(chain: EVMChain, contractAddress: String, from: String, to: String, tokenId: BigInteger, signedAccount: String?): TransactionResponse {
        val transactionReceipt = Erc721TokenStandard.safeTransferFrom(chain, contractAddress, Address(from), Address(to), Uint256(tokenId), signedAccount)
        return Common.getTransactionResponse(chain, transactionReceipt)
    }

    fun safeTransferFrom(chain: EVMChain, contractAddress: String, from: String, to: String, tokenId: BigInteger, data: String?, signedAccount: String?): TransactionResponse {
        val transactionReceipt = Erc721TokenStandard.safeTransferFrom(chain, contractAddress, Address(from), Address(to), Uint256(tokenId), DynamicBytes(
            data?.toByteArray() ?: null
        ), signedAccount)
        return Common.getTransactionResponse(chain, transactionReceipt)
    }

    fun setApprovalForAll(chain: EVMChain, contractAddress: String, operator: String, approved: Boolean, signedAccount: String?): TransactionResponse {
        val transactionReceipt = Erc721TokenStandard.setApprovalForAll(chain, contractAddress, Address(operator), Bool(approved), signedAccount)
        return Common.getTransactionResponse(chain, transactionReceipt)
    }

    fun isApprovedForAll(chain: EVMChain, contractAddress: String, owner: String, operator: String): Boolean {
        return Erc721TokenStandard.isApprovedForAll(chain, contractAddress, Address(owner), Address(operator)).value
    }

    fun approve(chain: EVMChain, contractAddress: String, to: String, tokenId: BigInteger, signedAccount: String?): TransactionResponse {
        val transactionReceipt = Erc721TokenStandard.approve(chain, contractAddress, Address(to), Uint256(tokenId), signedAccount)
        return Common.getTransactionResponse(chain, transactionReceipt)
    }

    fun getApproved(chain: EVMChain, contractAddress: String, tokenId: BigInteger): String {
        return Erc721TokenStandard.getApproved(chain, contractAddress, Uint256(tokenId)).value
    }
    fun getTokenCollectionInfo(chain: EVMChain, contractAddress: String): TokenCollectionInfo {
        //in the case of ERC721
        if (isErc721Standard(chain, contractAddress)) {
            val name = Erc721TokenStandard.name(chain, contractAddress)
            val symbol = Erc721TokenStandard.symbol(chain, contractAddress)
            val tokenCollectionInfo = TokenCollectionInfo(name, symbol)
            return tokenCollectionInfo
        }
        return TokenCollectionInfo("", "")
    }

    fun getAccountNFTs(chain: Chain, account: String): NFTsInfos {
        return when{
            Common.isEVMChain(chain) -> {

                val result = getAccountNFTsByAlchemy(chain, account)
                return (NFTsInfos(evmNfts = result))
            }
            Common.isTezosChain(chain) -> {
                val result= TezosNftService.fetchAccountNFTsByTzkt(chain, account)
                return (NFTsInfos(tezosNfts = result))
            }
            Common.isPolkadotParachain(chain) -> {
                val evmErc721CollectiblesResult= PolkadotNftService.fetchEvmErc721CollectiblesBySubscan(
                    PolkadotParachain.valueOf(chain.toString()), account)
                if(evmErc721CollectiblesResult.data?.list == null) return NFTsInfos()
                val result= evmErc721CollectiblesResult.data.list.map {
                    var nftMetadata: NftMetadata? = null
                    try {
                        nftMetadata= getNftMetadata(EVMChain.valueOf(chain.toString()), it.contract, BigInteger( it.token_id))
                    }catch (e: Exception){}
                    PolkadotEvmNft(it.contract, it.token_id, nftMetadata)
                }
                return (NFTsInfos(polkadotEvmNft = result))
            }
            Common.isUniqueParachain(chain) -> {
                val uniqueNftsResult = PolkadotNftService.fetchUniqueNFTs(UniqueNetwork.valueOf(chain.toString()), account)
                if(uniqueNftsResult.data == null || uniqueNftsResult.data.size == 0)  return NFTsInfos()

                val result= uniqueNftsResult.data.map {
                    val metadata= PolkadotNftService.fetchUniqueNFTMetadata(UniqueNetwork.valueOf(chain.toString()), it.collection_id.toString(), it.token_id.toString())
                    val uniqueNftMetadata= PolkadotNftService.parseNftMetadataUniqueResponse(metadata!!)
                    PolkadotUniqueNft(it.collection_id.toString(), it.token_id.toString(), uniqueNftMetadata)
                }
                return (NFTsInfos(polkadotUniqueNft = result))
            }
            else -> {throw Exception("Chain  is not supported")}
        }
    }

    fun getAccountNFTsByChainScan(chain: EVMChain, address: String): List<Token?> {
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


    fun getShimmerNFTinstances(smartContractAddress: String) : shimmerNFT{

        return runBlocking {
            val url = "https://explorer.evm.testnet.shimmer.network/api/v2/tokens/${smartContractAddress}/instances"
            val nfts = client.get(url)
            {
                contentType(ContentType.Application.Json)
            }.body<shimmerNFT>()

            println("nfts: $nfts")
            return@runBlocking nfts
        }

    }



    fun getAccountNFTsByAlchemy(chain: Chain, account: String): List<NFTsAlchemyResult.NftTokenByAlchemy> {
        return runBlocking {
            val url = when (chain) {
                Chain.ETHEREUM -> Values.ETHEREUM_MAINNET_ALCHEMY_URL
                Chain.GOERLI -> Values.ETHEREUM_TESTNET_GOERLI_ALCHEMY_URL
                Chain.SEPOLIA -> Values.ETHEREUM_TESTNET_SEPOLIA_ALCHEMY_URL
                Chain.POLYGON -> Values.POLYGON_MAINNET_ALCHEMY_URL
                Chain.MUMBAI -> Values.POLYGON_TESTNET_MUMBAI_ALCHEMY_URL
                Chain.SHIMMEREVM -> Values.SHIMMEREVM_TESTNET_BLOCK_EXPLORER_URL
                Chain.TEZOS -> throw Exception("Tezos is not supported")
                Chain.GHOSTNET -> throw Exception("Ghostnet is not supported")
                Chain.TESTNET  -> throw Exception("Near testnet is not supported")
                Chain.MAINNET  -> throw Exception("Near mainnet is not supported")
                Chain.ASTAR  -> throw Exception("ASTAR is not supported")
                Chain.MOONBEAM  -> throw Exception("MOONBEAM is not supported")
                Chain.UNIQUE -> throw Exception("UNIQUE is not supported")
                Chain.OPAL -> throw Exception("OPAL is not supported")
                else -> throw Exception ("Not supported")
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
        chain: EVMChain, contractAddress: String, tokenId: String, signedAccount: String?,
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
            }?.map {
                it.value= JsonPrimitive(value)
            }
        }
        val oldUri= getMetadatUri(chain, contractAddress, BigInteger(tokenId))
        val metadataUri: MetadataUri = MetadataUriFactory.getMetadataUri(Common.getMetadataType(oldUri))
        val nftMetadataWrapper= NftMetadataWrapper(evmNftMetadata = metadata)
        val tokenUri = metadataUri.getTokenUri(nftMetadataWrapper)
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
            client.get("$url${WaltIdServices.loadApiKeys().apiKeys.alchemy}/getNFTs/?owner=$account&withMetadata=true$apiKeyUrlParameter") {
                contentType(ContentType.Application.Json)
            }
                .body<NFTsAlchemyResult>()

        if (nfts.pageKey != null) {
            return nfts.ownedNfts.plus(fetchAccountNFTsTokensByAlchemy(account, url, nfts.pageKey))
        } else {
            return nfts.ownedNfts
        }
    }


    fun fetchIPFSData(uri: String): String {
        return runBlocking {
            var uriFormat= uri
            uriFormat= uri.replace("ipfs://","", true)
            val result = IPFSMetadata.client.get("https://nftstorage.link/ipfs/$uriFormat") {
            }.body<String>()
            return@runBlocking result
        }
    }

     fun getIPFSMetadataUsingNFTStorage(uri: String): NftMetadata {
        return runBlocking {
            var uriFormat= uri
            if(uri.contains("ipfs://", true)){}
            uriFormat= uri.replace("ipfs://","", true)
            val result = IPFSMetadata.client.get("https://nftstorage.link/ipfs/$uriFormat") {
            }.body<NftMetadata>()
            return@runBlocking result
        }
    }

    fun getWebDocumentMetadata(uri: String): NftMetadata {
        return runBlocking {
            val nft = client.get(uri) {
            }.body<String>()
            val jsonObject = Json.parseToJsonElement(nft).jsonObject
            val result= parseNftEvmMetadataResult(jsonObject)
            return@runBlocking result
        }
    }
    fun addFileToIpfsUsingNFTStorage(file: ByteArray): NFTStorageAddFileResult {
        return runBlocking {
            val res = IPFSMetadata.client.post("https://api.nft.storage/upload") {
                contentType(ContentType.Image.Any)
                setBody(file)
            }.body<NFTStorageAddFileResult>()
            return@runBlocking res
        }
    }


    private fun mintNewToken(
        recipientAddress: String,
        metadataUri: String,
        chain: EVMChain,
        contractAddress: String,
        isSoulBound : Boolean
    ): MintingResponse {
        if (!isSoulBound) {

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
        } else if (isSoulBound){
            val transactionReceipt: TransactionReceipt? =
                SoulBoundTokenStandard.safeMint(chain, contractAddress, recipientAddress, metadataUri)
            val eventValues: EventValues? =
                staticExtractEventParameters(Erc721OnchainCredentialWrapper.TRANSFER_EVENT, transactionReceipt?.logs?.get(0))

            val url = WaltIdServices.getBlockExplorerUrl(chain)
            val ts =
                TransactionResponse(transactionReceipt!!.transactionHash, "$url/tx/${transactionReceipt.transactionHash}")
            val mr = MintingResponse(ts, eventValues?.indexedValues?.get(2)?.value as BigInteger)
            return mr
        }
        return MintingResponse(null, null)
    }


    fun revokeToken(chain: EVMChain, contractAddress: String, tokenId: BigInteger, signedAccount: String?): TransactionResponse {
        val transactionReceipt = SoulBoundTokenStandard.revoke(chain, contractAddress, Uint256(tokenId), signedAccount)
        return Common.getTransactionResponse(chain, transactionReceipt)

    }

    fun unequipToken(chain: EVMChain , contractAddress: String, tokenId: BigInteger, signedAccount: String?): TransactionResponse {
        val transactionReceipt = SoulBoundTokenStandard.unequip(chain, contractAddress, Uint256(tokenId), signedAccount)
        return Common.getTransactionResponse(chain, transactionReceipt)
    }

    private fun getMetadatUri(chain: EVMChain, contractAddress: String, tokenId: BigInteger): String {
        if (isErc721Standard(chain, contractAddress) == true) {
            return Erc721TokenStandard.tokenURI(chain, contractAddress, Uint256(tokenId))
        }
        return ""
    }


     fun isErc721Standard(chain: EVMChain, contractAddress: String): Boolean {
        return Erc721TokenStandard.supportsInterface(chain, contractAddress)
    }
     fun isSoulBoundStandard(chain: EVMChain, contractAddress: String): Boolean {
        return SoulBoundTokenStandard.supportsInterface(chain, contractAddress)
    }

    private fun parseNftEvmMetadataResult(nft: JsonObject): NftMetadata{
        var attributes: List<NftMetadata.Attributes>?=null
        if(nft.get("attributes")?.metaInfo.equals("kotlinx.serialization.json.JsonArray.class")){
            attributes= nft.get("attributes")?.jsonArray?.map {
                val trait_type= it.jsonObject.get("trait_type")?.jsonPrimitive?.content

                val value= it.jsonObject.get("value")?.jsonPrimitive?.content

                // verify is value is number
                if(value?.toIntOrNull() != null){
                    NftMetadata.Attributes(trait_type!!, JsonPrimitive(value.toInt()))
                }else{
                    NftMetadata.Attributes(trait_type!!, JsonPrimitive(value))
                }

            }
        }
        return NftMetadata(
            name= nft.get("name")?.jsonPrimitive?.content,
            description = nft.get("description")?.jsonPrimitive?.content,
            image = nft.get("image")?.jsonPrimitive?.content,
            image_data = nft.get("image_data")?.jsonPrimitive?.content,
            external_url = nft.get("external_url")?.jsonPrimitive?.content,
            attributes = attributes
        )
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
