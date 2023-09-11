package id.walt.nftkit.services

import com.expediagroup.graphql.client.serialization.GraphQLClientKotlinxSerializer
import com.expediagroup.graphql.client.spring.GraphQLWebClient
import id.walt.nftkit.graphql.TokenOwnersQuery
import id.walt.nftkit.graphql.TokensQuery
import id.walt.nftkit.graphql.tokenownersquery.TokenOwnersDataResponse
import id.walt.nftkit.graphql.tokensquery.TokenDataResponse
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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

enum class PolkadotParachain {
    ASTAR,
    MOONBEAM
}

enum class UniqueNetwork {
    UNIQUE,
    OPAL
}

@Serializable
data class PolkadotNFTsSubscanResult(
    val data: Data? = null,
) {
    @Serializable
    data class Data(
        val ERC721: List<Erc721>? = null,
    ) {
        @Serializable
        data class Erc721(
            val symbol: String,
            val balance: String,
            val contract: String,
        )
    }
}

@Serializable
data class SubscanEvmErc721CollectiblesBody(
    val address: String,
    val row: Int
)

@Serializable
data class SubscanEvmErc721CollectiblesResult(
    val data: Data? = null,
) {
    @Serializable
    data class Data(
        val count: Int,
        val list: List<TokenHolder>? = null,
    ) {
        @Serializable
        data class TokenHolder(
            val contract: String,
            val holder: String,
            val token_id: String,
            val storage_url: String
        )
    }
}

@Serializable
data class UniqueNftMetadata(
    val fullUrl: String,
    val ipfsCid: String,
    val attributes: List<Attribute>? = null
) {
    @Serializable
    data class Attribute(
        val name: String,
        var value: String,
    )
}

@Serializable
data class PolkadotEvmNft(
    val constractAddress: String,
    val tokenId: String,
    val nftMetadata: NftMetadata? = null
)

@Serializable
data class PolkadotUniqueNft(
    val collectionId: String,
    val tokenId: String,
    val metadata: UniqueNftMetadata? = null
)

object PolkadotNftService {

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

    // val seed: String = ""
    // val seedPassword: String = ""

    // val signer = Sr25519SignerWrapper(seed, seedPassword)
    // // create SDK god object. First parameter - signer, second - base url of backend of blockchain
    // val sdk = UniqueSdk(signer, "https://rest.opal.uniquenetwork.dev")

    fun fetchAccountTokensBySubscan(parachain: PolkadotParachain, account: String): PolkadotNFTsSubscanResult {
        return runBlocking {
            val values = mapOf("address" to account)
            val parachainAPI = when (parachain) {
                PolkadotParachain.ASTAR -> "astar"
                PolkadotParachain.MOONBEAM -> "moonbeam"
            }
            client.post("https://$parachainAPI.api.subscan.io/api/scan/account/tokens") {
                contentType(ContentType.Application.Json)
                header("X-API-Key", WaltIdServices.loadApiKeys().apiKeys.subscan)
                setBody(values)
            }.body<PolkadotNFTsSubscanResult>()
        }
    }


    fun fetchEvmErc721CollectiblesBySubscan(parachain: PolkadotParachain, account: String): SubscanEvmErc721CollectiblesResult {
        return runBlocking {
            val v = SubscanEvmErc721CollectiblesBody(account, 100)
            val parachainAPI = when (parachain) {
                PolkadotParachain.ASTAR -> "astar"
                PolkadotParachain.MOONBEAM -> "moonbeam"
            }
            client.post("https://$parachainAPI.api.subscan.io/api/scan/evm/erc721/collectibles") {
                contentType(ContentType.Application.Json)
                header("X-API-Key", WaltIdServices.loadApiKeys().apiKeys.subscan)
                setBody(v)
            }.body<SubscanEvmErc721CollectiblesResult>()
        }
    }

    fun fetchUniqueNFTs(network: UniqueNetwork, account: String): TokenOwnersDataResponse = runBlocking {
        println("--- FETCH UNIQUE NFTs")
        val uniqueGraphqlClient = GraphQLWebClient(url = getUniqueNetworkIndexerUrl(network), serializer = PolkadotGraphQLClientKotlinxSerializer())
        val tokenOwnersQuery = TokenOwnersQuery()
        tokenOwnersQuery.query = tokenOwnersQuery.query.replace("address", account)
        uniqueGraphqlClient.execute(tokenOwnersQuery).data!!.token_owners
    }

    fun fetchUniqueNFTMetadata(network: UniqueNetwork, collectionId: String, tokenId: String): TokenDataResponse? = runBlocking {
        println("--- FETCH UNIQUE NFT METADATA")
        val uniqueGraphqlClient = GraphQLWebClient(url = getUniqueNetworkIndexerUrl(network), serializer = PolkadotGraphQLClientKotlinxSerializer())
        val tokensQuery = TokensQuery()
        tokensQuery.query = tokensQuery.query.replace("tokenId", tokenId)
        tokensQuery.query = tokensQuery.query.replace("collectionId", collectionId)
        uniqueGraphqlClient.execute(tokensQuery).also { println("DATA FROM QUERY IS: ${it.data}") }.data?.tokens
    }

    fun fetchUniqueNFTsMetadata(network: UniqueNetwork, account: String): List<UniqueNftMetadata> = runBlocking {
        val tokens = mutableListOf<UniqueNftMetadata>()

        fetchUniqueNFTs(network, account).data?.forEach {
            println("Current NFT: ${it.token_id} of ${it.collection_id} on $network of $account")
            val fetchResult = fetchUniqueNFTMetadata(network, it.collection_id.toString(), it.token_id.toString())
            val parseResult = parseNftMetadataUniqueResponse(fetchResult!!)
            tokens.add(parseResult)
        }

        tokens
    }

    fun parseNftMetadataUniqueResponse(tokens: TokenDataResponse): UniqueNftMetadata {
        val metadata = (tokens.data ?: throw IllegalArgumentException("Tokens have no data?"))[0]

        println("Tokens: ${Json.encodeToString(tokens)}")
        println("Metadata: ${Json.encodeToString(metadata)}")

        /*
        val attributes = metadata.attributes?.values?.map {
            Json.decodeFromJsonElement<UniqueNftMetadata.Attribute>(it)
        }
         */

        val attributes = metadata.attributes?.values?.map {
            //Json.decodeFromJsonElement<UniqueNftMetadata.Attribute>()
            UniqueNftMetadata.Attribute(it.name, it.value.content)
        }

        val tokenImage = metadata.image!!

        return UniqueNftMetadata(
            fullUrl = tokenImage["fullUrl"]!!.jsonPrimitive.content,
            ipfsCid = tokenImage["ipfsCid"]!!.jsonPrimitive.content,
            attributes
        )
    }


    private fun getUniqueNetworkIndexerUrl(uniqueNetwork: UniqueNetwork): String {
        return when (uniqueNetwork) {
            UniqueNetwork.UNIQUE -> WaltIdServices.loadIndexers().indexersUrl.uniqueUrl
            UniqueNetwork.OPAL -> WaltIdServices.loadIndexers().indexersUrl.opalUrl
        }
    }

}
