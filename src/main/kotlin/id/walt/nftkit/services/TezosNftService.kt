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
            val tezosSCDeploymentResponse =
                TezosSCDeploymentResponse(deployment.contractAddress, "$contractExternalUrl/${deployment.contractAddress}")
            return@runBlocking tezosSCDeploymentResponse
        }
    }

    fun mintNftToken(chain: TezosChain, contractAddress: String, parameter: TezosMintingParameter): OperationResponse {
        var tokenUri: String?
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
            val operationResponse =
                OperationResponse(tezosOperationResponse.opHash, "$contractExternalUrl/opg/${tezosOperationResponse.opHash}")
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
            val contractExternalUrl = when (Common.getTezosChain(chain.toString())) {
                TezosChain.TEZOS -> Values.TEZOS_MAINNET_BETTER_CALL_DEV
                TezosChain.GHOSTNET -> Values.TEZOS_GHOSTNET_BETTER_CALL_DEV
            }
            val operationResponse =
                OperationResponse(tezosOperationResponse.opHash, "$contractExternalUrl/opg/${tezosOperationResponse.opHash}")
            return@runBlocking operationResponse
        }
    }

    fun getNftTezosMetadata(chain: TezosChain, contractAddress: String, tokenId: String): TezosNftMetadata? {
        return runBlocking {
            var contractAddressExp = ""
            var chainAPI = ""
            if (contractAddress != null) contractAddressExp = "token.contract=$contractAddress&"
            if (TezosChain.GHOSTNET.equals(chain)) chainAPI = ".ghostnet"
            val nfts =
                NftService.client.get("https://api$chainAPI.tzkt.io/v1/tokens?contract=$contractAddress&tokenId=$tokenId&type=fa2") {
                    contentType(ContentType.Application.Json)
                }
                    .body<String>()
            val jsonObject = Json.parseToJsonElement(nfts).jsonArray
            val result = parseNftTezosMetadataResult(jsonObject)
            return@runBlocking result.get(0).metadata
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
            if (Chain.GHOSTNET.equals(chain)) chainAPI = ".ghostnet"

            val nfts =
                NftService.client.get("https://api$chainAPI.tzkt.io/v1/tokens/balances?account=$account&$contractAddressExp select=id,account,token,balance,transfersCount&type=fa2&token.totalSupply=1") {
                    contentType(ContentType.Application.Json)
                }
                    .body<String>()
            val jsonObject = Json.parseToJsonElement(nfts).jsonArray
            val result = parseAccountNFTsByTzktResult(jsonObject)
            return@runBlocking result
        }
    }

    fun getContractMetadata(chain: TezosChain, contractAddress: String): JsonObject {
        return runBlocking {
            var chainAPI = ""
            if (Chain.GHOSTNET.equals(chain)) chainAPI = ".ghostnet"

            val contractMetadata =
                NftService.client.get("https://api$chainAPI.tzkt.io/v1/contracts/$contractAddress/bigmaps/metadata/keys/") {
                    contentType(ContentType.Application.Json)
                }
                    .body<List<TezosContractMetadataResponse>>()
            if (contractMetadata.size.equals(0)) {
                val value = mapOf("error" to JsonPrimitive("The contract doesn't have metadata"))
                return@runBlocking JsonObject(value)
            }
            val metadataURI = decodeHex(contractMetadata.get(0).value)
            val metadata = NftService.fetchIPFSData(metadataURI)
            val jsonObject = Json.parseToJsonElement(metadata).jsonObject
            return@runBlocking jsonObject
        }
    }

    fun decodeHex(value: String): String {
        require(value.length % 2 == 0) { "Must have an even length" }
        return value.chunked(2)
            .map { it.toInt(16).toByte() }
            .toByteArray()
            .toString(Charsets.ISO_8859_1)  // Or whichever encoding your input uses
    }

    private val JsonElement?.asString: String
        get() = if (this != null) (this as? JsonPrimitive ?: error("JsonPrimitive")).content else ""

    private infix fun JsonObject?.obj(key: String): JsonObject? = this?.get(key)?.jsonObject

    private infix fun JsonObject?.str(key: String): String = this?.get(key)?.jsonPrimitive.asString
    private infix fun JsonObject?.long(key: String): Long? = this?.get(key)?.jsonPrimitive?.longOrNull
    private infix fun JsonObject?.bool(key: String): Boolean? = this?.get(key)?.jsonPrimitive?.booleanOrNull
    private infix fun JsonObject?.arr(key: String): JsonArray? = this?.get(key)?.jsonArray
    private infix fun JsonObject?.arrMapPrimitives(key: String): List<String>? = (this arr key)?.map { it.jsonPrimitive.content }

    private operator fun JsonObject?.get(key: String): JsonElement? = this?.get(key)


    fun makeTezosNftMetadata(metadata: JsonObject?, attributes: List<TezosNftMetadata.Attribute>?) = TezosNftMetadata(
        name = metadata str "name",
        description = metadata str "description",
        symbol = metadata str "symbol",
        image = metadata str "image",
        creators = metadata arrMapPrimitives "creators",
        decimals = metadata str "decimals",
        displayUri = metadata str "displayUri",
        artifactUri = metadata str "artifactUri",
        thumbnailUri = metadata str "thumbnailUri",
        isTransferable = metadata bool "isTransferable",
        isBooleanAmount = metadata bool "isBooleanAmount",
        shouldPreferSymbol = metadata bool "shouldPreferSymbol",
        attributes = attributes,
        tags = metadata arrMapPrimitives "tags",
        category = metadata str "category",
        collectionName = metadata str "collectionName",
        creatorName = metadata str "creatorName",
        keywords = metadata str "keywords"
    )


    private fun parseAccountNFTsByTzktResult(nfts: JsonArray): List<TezosNFTsTzktResult> =
        nfts.jsonArray.map {
            var attributes: List<TezosNftMetadata.Attribute>? = null

            val nft = it.jsonObject

            val token = nft obj "token"
            val metadata = token obj "metadata"

            if ((metadata obj "attributes")?.metaInfo.equals("kotlinx.serialization.json.JsonArray.class")) {
                attributes = metadata["attributes"]?.jsonArray?.map {
                    TezosNftMetadata.Attribute(
                        nft str "name",
                        nft str "value"
                    )
                }
            }
            TezosNFTsTzktResult(
                (nft long "id") ?: 0,
                TezosNFTsTzktResult.Account(
                    nft obj "account" str "address"
                ),
                token = TezosNFTsTzktResult.Token(
                    id = (nft long "id") ?: 0,
                    contract = TezosNFTsTzktResult.Token.Contract(token obj "contract" str "address"),
                    tokenId = token str "tokenId",
                    standard = token str "standard",
                    totalSupply = token str "totalSupply",
                    metadata = makeTezosNftMetadata(metadata, attributes)
                ),
                balance = nft str "balance",
                transfersCount = (nft long "transfersCount") ?: 0,
                firstLevel = nft long "firstLevel",
                firstTime = nft str "firstTime",
                lastLevel = nft long "lastLevel",
                lastTime = nft str "lastTime"
            )
        }

    private fun parseNftTezosMetadataResult(nfts: JsonArray): List<TezosNFTMetadataTzktResult> {
        return nfts.jsonArray.map {

            val nft = it.jsonObject

            val metadata = nft obj "metadata"

            var attributes: List<TezosNftMetadata.Attribute>? = null
            if ((metadata obj "attributes")?.metaInfo.equals("kotlinx.serialization.json.JsonArray.class")) {
                attributes = (metadata obj "attributes")?.jsonArray?.map {
                    TezosNftMetadata.Attribute(
                        name = nft str "name",
                        value = nft str "value"
                    )
                }
            }
            TezosNFTMetadataTzktResult(
                id = (nft long "id") ?: 0,
                contract = TezosNFTsTzktResult.Token.Contract(nft obj "contract" str "address"),
                tokenId = nft str "tokenId",
                standard = nft str "standard",
                metadata = makeTezosNftMetadata(metadata, attributes)
            )
        }
    }
}
