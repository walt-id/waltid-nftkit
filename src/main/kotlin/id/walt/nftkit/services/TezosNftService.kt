package id.walt.nftkit.services

import id.walt.nftkit.Values
import id.walt.nftkit.metadata.MetadataUri
import id.walt.nftkit.metadata.MetadataUriFactory
import id.walt.nftkit.rest.TezosSCDeploymentResponse
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


enum class TezosChain {
    TEZOS,
    GHOSTNET
}

@Serializable
data class TezosNftMetadata(

    var name: String,
    val description: String? = null,
    var symbol: String? = null,
    var image: String? = null,
    var creators: List<String>? = null, //["tz1ZLRT3xiBgGRdNrTYXr8Stz4TppT3hukRi"],
    var decimals: String? = null,
    val displayUri: String? = null,
    val artifactUri: String? = null,
    val thumbnailUri: String? = null,
    val isTransferable: Boolean? = null,
    val isBooleanAmount: Boolean? = null,
    val shouldPreferSymbol: Boolean? = null,
    val attributes: List<Attribute>? = null,
    val tags: List<String>? = null,//["pxlshrd","Pâtisserie","fxhashturnsone"]
    val category: String? = null,
    val collectionName: String? = null,
    val creatorName: String? = null,
    val keywords: String? = null,//"gaming,collectible,rocket,monster,bear,battalion",
) {
    @Serializable
    data class Attribute(
        val name: String,
        var value: String,
        //val type: Sting?
        //val trait_type: String,
    )
}

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
        val metadata: TezosNftMetadata? = null,
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
    val metadata: TezosNftMetadata? = null,
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

    val client = HttpClient(CIO.create { requestTimeout = 0 }) {
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
            val contractExternalUrl = when (chain) {
                TezosChain.TEZOS -> Values.TEZOS_MAINNET_BETTER_CALL_DEV
                TezosChain.GHOSTNET -> Values.TEZOS_GHOSTNET_BETTER_CALL_DEV
            }
            return@runBlocking TezosSCDeploymentResponse(deployment.contractAddress, "$contractExternalUrl/${deployment.contractAddress}")
        }
    }

    fun mintNftToken(chain: TezosChain, contractAddress: String, parameter: TezosMintingParameter): OperationResponse {
        val tokenUri: String?
        if (parameter.metadataUri != null && parameter.metadataUri != "") {
            tokenUri = parameter.metadataUri
        } else {
            val metadataUri: MetadataUri = MetadataUriFactory.getMetadataUri(MetadataStorageType.OFF_CHAIN)
            val nftMetadataWrapper = NftMetadataWrapper(tezosNftMetadata = parameter.metadata)
            tokenUri = metadataUri.getTokenUri(nftMetadataWrapper)
        }
        return runBlocking {
            val amount = if (parameter.amount != null && parameter.amount != "") parameter.amount else null
            val values = mapOf(
                "chain" to chain.toString(),
                "owner" to parameter.recipientAddress,
                "fa2ContractAddress" to contractAddress,
                "metadata" to tokenUri,
                "tokenId" to parameter.tokenId,
                "amount" to amount
            )
            val tezosOperationResponse =
                NftService.client.post("${WaltIdServices.loadTezosConfig().tezosBackendServer}/tezos/contract/token/mint") {
                    contentType(ContentType.Application.Json)
                    setBody(values)
                }
                    .body<TezosOperationResponse>()
            val contractExternalUrl = when (Common.getTezosChain(chain.toString())) {
                TezosChain.TEZOS -> Values.TEZOS_MAINNET_BETTER_CALL_DEV
                TezosChain.GHOSTNET -> Values.TEZOS_GHOSTNET_BETTER_CALL_DEV
            }
            return@runBlocking OperationResponse(tezosOperationResponse.opHash, "$contractExternalUrl/opg/${tezosOperationResponse.opHash}")
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
            val contractExternalUrl = when (Common.getTezosChain(chain.toString())) {
                TezosChain.TEZOS -> Values.TEZOS_MAINNET_BETTER_CALL_DEV
                TezosChain.GHOSTNET -> Values.TEZOS_GHOSTNET_BETTER_CALL_DEV
            }
            return@runBlocking OperationResponse(tezosOperationResponse.opHash, "$contractExternalUrl/opg/${tezosOperationResponse.opHash}")
        }
    }

    fun getNftTezosMetadata(chain: TezosChain, contractAddress: String, tokenId: String): TezosNftMetadata? {
        return runBlocking {
            var contractAddressExp = ""
            var chainAPI = ""
            if (contractAddress != null) contractAddressExp = "token.contract=$contractAddress&"
            if (TezosChain.GHOSTNET == chain) chainAPI = ".ghostnet"
            val nfts =
                NftService.client.get("https://api$chainAPI.tzkt.io/v1/tokens?contract=$contractAddress&tokenId=$tokenId&type=fa2") {
                    contentType(ContentType.Application.Json)
                }
                    .body<String>()
            val jsonObject = Json.parseToJsonElement(nfts).jsonArray
            val result = parseNftTezosMetadataResult(jsonObject)
            return@runBlocking result[0].metadata
        }
    }

    fun fetchAccountNFTsByTzkt(
        chain: Chain,
        account: String,
        contractAddress: String? = null,
    ): List<TezosNFTsTzktResult> {
        return runBlocking {
            var contractAddressExp = ""
            var chainAPI = ""
            if (contractAddress != null) contractAddressExp = "token.contract=$contractAddress&"
            if (Chain.GHOSTNET == chain) chainAPI = ".ghostnet"

            val nfts =
                NftService.client.get("https://api$chainAPI.tzkt.io/v1/tokens/balances?account=$account&$contractAddressExp select=id,account,token,balance,transfersCount&type=fa2&token.totalSupply=1") {
                    contentType(ContentType.Application.Json)
                }
                    .body<String>()
            val jsonObject = Json.parseToJsonElement(nfts).jsonArray
            return@runBlocking parseAccountNFTsByTzktResult(jsonObject)
        }
    }

    fun getContractMetadata(chain: TezosChain, contractAddress: String): JsonObject {
        return runBlocking {
            var chainAPI = ""
            if (Chain.GHOSTNET.name == chain.name)
                chainAPI = ".ghostnet"

            val contractMetadata =
                NftService.client.get("https://api$chainAPI.tzkt.io/v1/contracts/$contractAddress/bigmaps/metadata/keys/") {
                    contentType(ContentType.Application.Json)
                }
                    .body<List<TezosContractMetadataResponse>>()
            if (contractMetadata.isEmpty()) {
                val value = mapOf("error" to JsonPrimitive("The contract doesn't have metadata"))
                return@runBlocking JsonObject(value)
            }
            val metadataURI = decodeHex(contractMetadata[0].value)
            val metadata = NftService.fetchIPFSData(metadataURI)
            return@runBlocking Json.parseToJsonElement(metadata).jsonObject
        }
    }

    fun decodeHex(value: String): String {
        require(value.length % 2 == 0) { "Must have an even length" }
        return value.chunked(2)
            .map { it.toInt(16).toByte() }
            .toByteArray()
            .toString(Charsets.ISO_8859_1)  // Or whichever encoding your input uses
    }

    private fun parseAccountNFTsByTzktResult(nfts: JsonArray): List<TezosNFTsTzktResult> {
        return nfts.jsonArray.map {
            var attributes: List<TezosNftMetadata.Attribute>? = null
            if (it.jsonObject["token"]?.jsonObject?.get("metadata")?.jsonObject?.get("attributes")?.metaInfo.equals("kotlinx.serialization.json.JsonArray.class")) {
                attributes = it.jsonObject["token"]?.jsonObject?.get("metadata")?.jsonObject?.get("attributes")?.jsonArray?.map {
                    TezosNftMetadata.Attribute(
                        it.jsonObject["name"]?.jsonPrimitive?.content ?: "",
                        it.jsonObject["value"]?.jsonPrimitive?.content ?: ""
                    )
                }
            }
            TezosNFTsTzktResult(
                it.jsonObject["id"]?.jsonPrimitive?.long ?: 0,
                TezosNFTsTzktResult.Account(
                    it.jsonObject["account"]?.jsonObject?.get("address")?.jsonPrimitive?.content ?: ""
                ),
                TezosNFTsTzktResult.Token(
                    it.jsonObject["token"]?.jsonObject?.get("id")?.jsonPrimitive?.long ?: 0,
                    TezosNFTsTzktResult.Token.Contract(
                        it.jsonObject["token"]?.jsonObject?.get("contract")?.jsonObject?.get("address")?.jsonPrimitive?.content ?: ""
                    ),
                    it.jsonObject["token"]?.jsonObject?.get("tokenId")?.jsonPrimitive?.content ?: "",
                    it.jsonObject["token"]?.jsonObject?.get("standard")?.jsonPrimitive?.content ?: "",
                    it.jsonObject["token"]?.jsonObject?.get("totalSupply")?.jsonPrimitive?.content ?: "",
                    TezosNftMetadata(
                        it.jsonObject["token"]?.jsonObject?.get("metadata")?.jsonObject?.get("name")?.jsonPrimitive?.content ?: "",
                        it.jsonObject["token"]?.jsonObject?.get("metadata")?.jsonObject?.get("description")?.jsonPrimitive?.content,
                        it.jsonObject["token"]?.jsonObject?.get("metadata")?.jsonObject?.get("symbol")?.jsonPrimitive?.content,
                        it.jsonObject["token"]?.jsonObject?.get("metadata")?.jsonObject?.get("image")?.jsonPrimitive?.content,
                        it.jsonObject["token"]?.jsonObject?.get("metadata")?.jsonObject?.get("creators")?.jsonArray?.map { it.jsonPrimitive.content },
                        it.jsonObject["token"]?.jsonObject?.get("metadata")?.jsonObject?.get("decimals")?.jsonPrimitive?.content,
                        it.jsonObject["token"]?.jsonObject?.get("metadata")?.jsonObject?.get("displayUri")?.jsonPrimitive?.content,
                        it.jsonObject["token"]?.jsonObject?.get("metadata")?.jsonObject?.get("artifactUri")?.jsonPrimitive?.content,
                        it.jsonObject["token"]?.jsonObject?.get("metadata")?.jsonObject?.get("thumbnailUri")?.jsonPrimitive?.content,
                        it.jsonObject["token"]?.jsonObject?.get("metadata")?.jsonObject?.get("isTransferable")?.jsonPrimitive?.booleanOrNull,
                        it.jsonObject["token"]?.jsonObject?.get("metadata")?.jsonObject?.get("isBooleanAmount")?.jsonPrimitive?.booleanOrNull,
                        it.jsonObject["token"]?.jsonObject?.get("metadata")?.jsonObject?.get("shouldPreferSymbol")?.jsonPrimitive?.booleanOrNull,
                        attributes,
                        it.jsonObject["token"]?.jsonObject?.get("metadata")?.jsonObject?.get("tags")?.jsonArray?.map { it.jsonPrimitive.content },
                        it.jsonObject["token"]?.jsonObject?.get("metadata")?.jsonObject?.get("category")?.jsonPrimitive?.content,
                        it.jsonObject["token"]?.jsonObject?.get("metadata")?.jsonObject?.get("collectionName")?.jsonPrimitive?.content,
                        it.jsonObject["token"]?.jsonObject?.get("metadata")?.jsonObject?.get("creatorName")?.jsonPrimitive?.content,
                        it.jsonObject["token"]?.jsonObject?.get("metadata")?.jsonObject?.get("keywords")?.jsonPrimitive?.content
                    )
                ),
                it.jsonObject["balance"]?.jsonPrimitive?.content,
                it.jsonObject["transfersCount"]?.jsonPrimitive?.long ?: 0,
                it.jsonObject["firstLevel"]?.jsonPrimitive?.long,
                it.jsonObject["firstTime"]?.jsonPrimitive?.toString(),
                it.jsonObject["lastLevel"]?.jsonPrimitive?.long,
                it.jsonObject["lastTime"]?.jsonPrimitive?.toString(),

                )
        }
    }

    private fun parseNftTezosMetadataResult(nfts: JsonArray): List<TezosNFTMetadataTzktResult> {
        return nfts.jsonArray.map {
            var attributes: List<TezosNftMetadata.Attribute>? = null
            if (it.jsonObject["metadata"]?.jsonObject?.get("attributes")?.metaInfo.equals("kotlinx.serialization.json.JsonArray.class")) {
                attributes = it.jsonObject["metadata"]?.jsonObject?.get("attributes")?.jsonArray?.map {
                    TezosNftMetadata.Attribute(
                        it.jsonObject["name"]?.jsonPrimitive?.content ?: "",
                        it.jsonObject["value"]?.jsonPrimitive?.content ?: ""
                    )
                }
            }
            TezosNFTMetadataTzktResult(
                it.jsonObject["id"]?.jsonPrimitive?.long ?: 0,
                TezosNFTsTzktResult.Token.Contract(it.jsonObject["contract"]?.jsonObject?.get("address")?.jsonPrimitive?.content ?: ""),
                it.jsonObject["tokenId"]?.jsonPrimitive?.content ?: "",
                it.jsonObject["standard"]?.jsonPrimitive?.content ?: "",
                TezosNftMetadata(
                    it.jsonObject["metadata"]?.jsonObject?.get("name")?.jsonPrimitive?.content ?: "",
                    it.jsonObject["metadata"]?.jsonObject?.get("description")?.jsonPrimitive?.content,
                    it.jsonObject["metadata"]?.jsonObject?.get("symbol")?.jsonPrimitive?.content,
                    it.jsonObject["metadata"]?.jsonObject?.get("image")?.jsonPrimitive?.content,
                    it.jsonObject["metadata"]?.jsonObject?.get("creators")?.jsonArray?.map { it.jsonPrimitive.content },
                    it.jsonObject["metadata"]?.jsonObject?.get("decimals")?.jsonPrimitive?.content,
                    it.jsonObject["metadata"]?.jsonObject?.get("displayUri")?.jsonPrimitive?.content,
                    it.jsonObject["metadata"]?.jsonObject?.get("artifactUri")?.jsonPrimitive?.content,
                    it.jsonObject["metadata"]?.jsonObject?.get("thumbnailUri")?.jsonPrimitive?.content,
                    it.jsonObject["metadata"]?.jsonObject?.get("isTransferable")?.jsonPrimitive?.booleanOrNull,
                    it.jsonObject["metadata"]?.jsonObject?.get("isBooleanAmount")?.jsonPrimitive?.booleanOrNull,
                    it.jsonObject["metadata"]?.jsonObject?.get("shouldPreferSymbol")?.jsonPrimitive?.booleanOrNull,
                    attributes,
                    it.jsonObject["metadata"]?.jsonObject?.get("tags")?.jsonArray?.map { it.jsonPrimitive.content },
                    it.jsonObject["metadata"]?.jsonObject?.get("category")?.jsonPrimitive?.content,
                    it.jsonObject["metadata"]?.jsonObject?.get("collectionName")?.jsonPrimitive?.content,
                    it.jsonObject["metadata"]?.jsonObject?.get("creatorName")?.jsonPrimitive?.content,
                    it.jsonObject["metadata"]?.jsonObject?.get("keywords")?.jsonPrimitive?.content
                )
            )
        }
    }
}
