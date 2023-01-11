package id.walt.nftkit.services

import com.fasterxml.jackson.databind.ObjectMapper
import id.walt.nftkit.Values
import id.walt.nftkit.metadata.MetadataUri
import id.walt.nftkit.metadata.MetadataUriFactory
import id.walt.nftkit.rest.TezosSCDeploymentResponse
import id.walt.nftkit.utilis.Common
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.Identity.decode
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.Serializable
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.*
import java.util.*


enum class TezosChain {
    TEZOS,
    GHOSTNET
}

@Serializable
data class TezosNftMetadata(

    var name: String,
    val description: String?= null,
    var symbol: String?= null,
    var image: String?= null,
    var creators: List<String>?= null, //["tz1ZLRT3xiBgGRdNrTYXr8Stz4TppT3hukRi"],
    var decimals: String?= null,
    val displayUri: String?= null,
    val artifactUri: String?= null,
    val thumbnailUri: String?= null,
    val isTransferable:Boolean?= null,
    val isBooleanAmount:Boolean?= null,
    val shouldPreferSymbol:Boolean?= null,
    val attributes: List<NftMetadata.Attributes>?=null,
    val tags: List<String>?= null,//["pxlshrd","Pâtisserie","fxhashturnsone"]
    val category: String?= null,
    val collectionName: String?= null,
    val creatorName: String?= null,
    val keywords: String?= null,//"gaming,collectible,rocket,monster,bear,battalion",
    )

data class TezosMintingParameter(
    val metadataUri: String?,
    val recipientAddress: String,
    val tokenId: String,
    val amount: String?,
    val metadata: TezosNftMetadata?,
)

@Serializable
data class TezosNFTsTzktResult(
    val id: Long,
    val account: Account,
    val token: Token,
    val balance: String? = null,
    val transfersCount: Long,
    val firstLevel: Long? = null,
    val firstTime: String? = null,
    val lastLevel: Long? = null,
    val lastTime: String? = null,

) {

    @Serializable
    data class Account(
        val address: String,
    )
    @Serializable
    data class Token(
        val id: Long,
        val contract: Contract,
        val tokenId: String,
        val standard: String,
        val totalSupply: String,
        val metadata: TezosNftMetadata?= null,
        // val timeLastUpdated: String
    ) {
        @Serializable
        data class Contract(
            val address: String,
        )
    }
}

@Serializable
data class TezosNFTMetadataTzktResult(
    val id: Long,
    val contract: TezosNFTsTzktResult.Token.Contract,
    val tokenId: String,
    val standard: String,
    val metadata: TezosNftMetadata?= null,
    )
@Serializable
data class TezosNftsSCDeploymentResponse(
    val contractAddress: String
)

@Serializable
data class TezosOperationResponse(
    val opHash: String
)

@Serializable
data class OperationResponse(
    val operationHash: String,
    val operationExternalUrl: String
)

@Serializable
data class TezosContractMetadataResponse(
    val key: String,
    val value: String
)

enum class Fa2SmartContractType {
    SINGLE,
    MULTIPLE
}

object TezosNftService {

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

    fun deploySmartContract(chain: TezosChain, owner: String, type: Fa2SmartContractType): TezosSCDeploymentResponse {
        return runBlocking {
            val values = mapOf("chain" to chain.toString(), "owner" to owner, "type" to type.toString())
            val deployment =
                NftService.client.post("${WaltIdServices.loadTezosConfig().tezosBackendServer}/tezos/contract/deployment") {
                    contentType(ContentType.Application.Json)
                    setBody(values)
                }
                    .body<TezosNftsSCDeploymentResponse>()
            val contractExternalUrl= when(chain){
                TezosChain.TEZOS -> Values.TEZOS_MAINNET_BETTER_CALL_DEV
                TezosChain.GHOSTNET -> Values.TEZOS_GHOSTNET_BETTER_CALL_DEV
            }
            val tezosSCDeploymentResponse= TezosSCDeploymentResponse(deployment.contractAddress, "$contractExternalUrl/${deployment.contractAddress}")
            return@runBlocking tezosSCDeploymentResponse
        }
    }

    fun mintNftToken(chain: TezosChain, contractAddress: String, parameter: TezosMintingParameter): OperationResponse {
        var tokenUri: String?
        if (parameter.metadataUri != null && parameter.metadataUri != "") {
            tokenUri = parameter.metadataUri
        } else {
            val metadataUri: MetadataUri = MetadataUriFactory.getMetadataUri(MetadataStorageType.OFF_CHAIN)
            val nftMetadataWrapper= NftMetadataWrapper(tezosNftMetadata = parameter.metadata)
            tokenUri = metadataUri.getTokenUri(nftMetadataWrapper)
        }
        return runBlocking {
            val amount= if(parameter.amount != null && parameter.amount != "") parameter.amount else null
            val values = mapOf("chain" to chain.toString(), "owner" to parameter.recipientAddress, "fa2ContractAddress" to contractAddress, "metadata" to tokenUri , "tokenId" to parameter.tokenId, "amount" to amount )
            val tezosOperationResponse =
                NftService.client.post("${WaltIdServices.loadTezosConfig().tezosBackendServer}/tezos/contract/token/mint") {
                    contentType(ContentType.Application.Json)
                    setBody(values)
                }
                    .body<TezosOperationResponse>()
            val contractExternalUrl= when(Common.getTezosChain(chain.toString())){
                TezosChain.TEZOS -> Values.TEZOS_MAINNET_BETTER_CALL_DEV
                TezosChain.GHOSTNET -> Values.TEZOS_GHOSTNET_BETTER_CALL_DEV
            }
            val operationResponse= OperationResponse(tezosOperationResponse.opHash, "$contractExternalUrl/opg/${tezosOperationResponse.opHash}")
            return@runBlocking operationResponse
        }
    }

    fun addMinter(chain: TezosChain, contractAddress: String, minter: String): OperationResponse {
        return runBlocking {
            val values = mapOf("chain" to chain.toString(), "minter" to minter, "fa2ContractAddress" to contractAddress)
            val tezosOperationResponse =
                NftService.client.post("${WaltIdServices.loadTezosConfig().tezosBackendServer}/tezos/contract/minter") {
                    contentType(ContentType.Application.Json)
                    setBody(values)
                }
                    .body<TezosOperationResponse>()
            val contractExternalUrl= when(Common.getTezosChain(chain.toString())){
                TezosChain.TEZOS -> Values.TEZOS_MAINNET_BETTER_CALL_DEV
                TezosChain.GHOSTNET -> Values.TEZOS_GHOSTNET_BETTER_CALL_DEV
            }
            val operationResponse= OperationResponse(tezosOperationResponse.opHash, "$contractExternalUrl/opg/${tezosOperationResponse.opHash}")
            return@runBlocking operationResponse
        }
    }

    fun getNftTezosMetadata(chain: TezosChain, contractAddress: String, tokenId: String): TezosNftMetadata? {
        return runBlocking {
            var contractAddressExp= ""
            var chainAPI= ""
            if(contractAddress != null)  contractAddressExp= "token.contract=$contractAddress&"
            if(TezosChain.GHOSTNET.equals(chain)) chainAPI= ".ghostnet"
            val nfts =
                NftService.client.get("https://api$chainAPI.tzkt.io/v1/tokens?contract=$contractAddress&tokenId=$tokenId&type=fa2&token.totalSupply=1") {
                    contentType(ContentType.Application.Json)
                }
                    .body<List<TezosNFTMetadataTzktResult>>()

            return@runBlocking nfts.get(0).metadata
        }
    }
     fun fetchAccountNFTsByTzkt(
        chain: Chain,
        account: String,
        contractAddress: String?= null,
    ): List<TezosNFTsTzktResult> {
        return runBlocking {
            var contractAddressExp= ""
            var chainAPI= ""
            if(contractAddress != null)  contractAddressExp= "token.contract=$contractAddress&"
            if(Chain.GHOSTNET.equals(chain)) chainAPI= ".ghostnet"

            val nfts =
                NftService.client.get("https://api$chainAPI.tzkt.io/v1/tokens/balances?account=$account&$contractAddressExp select=id,account,token,balance,transfersCount&type=fa2&token.totalSupply=1") {
                    contentType(ContentType.Application.Json)
                }
                    .body<String>()
            val jsonObject = Json.parseToJsonElement(nfts).jsonArray
            val result= parseNftsResult(jsonObject)
            return@runBlocking result
        }
    }

    fun getContractMetadata(chain: TezosChain, contractAddress: String): JsonObject {
        return runBlocking {
            var chainAPI= ""
            if(Chain.GHOSTNET.equals(chain)) chainAPI= ".ghostnet"

            val contractMetadata =
                NftService.client.get("https://api$chainAPI.tzkt.io/v1/contracts/$contractAddress/bigmaps/metadata/keys/") {
                    contentType(ContentType.Application.Json)
                }
                    .body<List<TezosContractMetadataResponse>>()
            if(contractMetadata.size.equals(0)) {
                val value = mapOf("error" to JsonPrimitive("The contract doesn't have metadata"))
                return@runBlocking JsonObject(value)
            }
            val metadataURI=   decodeHex(contractMetadata.get(0).value)
            val metadata= NftService.fetchIPFSData(metadataURI)
            val jsonObject = Json.parseToJsonElement(metadata).jsonObject
            return@runBlocking jsonObject
        }
    }

    fun decodeHex( value: String): String {
        require(value.length % 2 == 0) {"Must have an even length"}
        return value.chunked(2)
            .map { it.toInt(16).toByte() }
            .toByteArray()
            .toString(Charsets.ISO_8859_1)  // Or whichever encoding your input uses
    }

    fun parseNftsResult(nfts: JsonArray): List<TezosNFTsTzktResult>{
        return nfts.jsonArray.map {
            TezosNFTsTzktResult(
                it.jsonObject.get("id")?.jsonPrimitive?.long ?: 0,
                TezosNFTsTzktResult.Account(
                    it.jsonObject.get("account")?.jsonObject?.get("address")?.jsonPrimitive?.content ?: ""
                ),
                TezosNFTsTzktResult.Token(
                    it.jsonObject.get("token")?.jsonObject?.get("id")?.jsonPrimitive?.long ?: 0,
                    TezosNFTsTzktResult.Token.Contract(it.jsonObject.get("token")?.jsonObject?.get("contract")?.jsonObject?.get("address")?.jsonPrimitive?.content ?: ""),
                    it.jsonObject.get("token")?.jsonObject?.get("tokenId")?.jsonPrimitive?.content ?: "",
                    it.jsonObject.get("token")?.jsonObject?.get("standard")?.jsonPrimitive?.content ?: "",
                    it.jsonObject.get("token")?.jsonObject?.get("totalSupply")?.jsonPrimitive?.content ?: "",
                    TezosNftMetadata(
                        it.jsonObject.get("token")?.jsonObject?.get("metadata")?.jsonObject?.get("name")?.jsonPrimitive?.content ?: "",
                        it.jsonObject.get("token")?.jsonObject?.get("metadata")?.jsonObject?.get("description")?.jsonPrimitive?.content,
                        it.jsonObject.get("token")?.jsonObject?.get("metadata")?.jsonObject?.get("symbol")?.jsonPrimitive?.content,
                        it.jsonObject.get("token")?.jsonObject?.get("metadata")?.jsonObject?.get("image")?.jsonPrimitive?.content,
                        it.jsonObject.get("token")?.jsonObject?.get("metadata")?.jsonObject?.get("creators")?.jsonArray?.map { it.jsonPrimitive?.content },
                        it.jsonObject.get("token")?.jsonObject?.get("metadata")?.jsonObject?.get("decimals")?.jsonPrimitive?.content,
                        it.jsonObject.get("token")?.jsonObject?.get("metadata")?.jsonObject?.get("displayUri")?.jsonPrimitive?.content,
                        it.jsonObject.get("token")?.jsonObject?.get("metadata")?.jsonObject?.get("artifactUri")?.jsonPrimitive?.content,
                        it.jsonObject.get("token")?.jsonObject?.get("metadata")?.jsonObject?.get("thumbnailUri")?.jsonPrimitive?.content,
                        it.jsonObject.get("token")?.jsonObject?.get("metadata")?.jsonObject?.get("isTransferable")?.jsonPrimitive?.booleanOrNull,
                        it.jsonObject.get("token")?.jsonObject?.get("metadata")?.jsonObject?.get("isBooleanAmount")?.jsonPrimitive?.booleanOrNull,
                        it.jsonObject.get("token")?.jsonObject?.get("metadata")?.jsonObject?.get("shouldPreferSymbol")?.jsonPrimitive?.booleanOrNull,
                        it.jsonObject.get("token")?.jsonObject?.get("metadata")?.jsonObject?.get("attributes")?.jsonArray?.map {
                            NftMetadata.Attributes(it.jsonObject.get("trait_type")?.jsonPrimitive?.content ?: "", it.jsonObject.get("value")?.jsonPrimitive?.content ?: "")
                        },
                        it.jsonObject.get("token")?.jsonObject?.get("metadata")?.jsonObject?.get("tags")?.jsonArray?.map { it.jsonPrimitive?.content },
                        it.jsonObject.get("token")?.jsonObject?.get("metadata")?.jsonObject?.get("category")?.jsonPrimitive?.content,
                        it.jsonObject.get("token")?.jsonObject?.get("metadata")?.jsonObject?.get("collectionName")?.jsonPrimitive?.content,
                        it.jsonObject.get("token")?.jsonObject?.get("metadata")?.jsonObject?.get("creatorName")?.jsonPrimitive?.content,
                        it.jsonObject.get("token")?.jsonObject?.get("metadata")?.jsonObject?.get("keywords")?.jsonPrimitive?.content
                    )
                ),
                it.jsonObject.get("balance")?.jsonPrimitive?.content,
                it.jsonObject.get("transfersCount")?.jsonPrimitive?.long ?: 0,
                it.jsonObject.get("firstLevel")?.jsonPrimitive?.long,
                it.jsonObject.get("firstTime")?.jsonPrimitive?.toString(),
                it.jsonObject.get("lastLevel")?.jsonPrimitive?.long,
                it.jsonObject.get("lastTime")?.jsonPrimitive?.toString(),

                )
        }
    }
}