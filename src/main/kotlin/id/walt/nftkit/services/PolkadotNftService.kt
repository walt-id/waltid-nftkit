package id.walt.nftkit.services

import com.expediagroup.graphql.client.serialization.GraphQLClientKotlinxSerializer
import com.expediagroup.graphql.client.spring.GraphQLWebClient
import id.walt.nftkit.TokenOwnersQuery
import id.walt.nftkit.TokensQuery
import id.walt.nftkit.tokenownersquery.TokenOwnersDataResponse
import id.walt.nftkit.tokensquery.TokenDataResponse
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
import kotlinx.serialization.json.Json.Default.decodeFromJsonElement

enum class PolkadotParachain {
    ASTAR,
    MOONBEAM
}

enum class UniqueNetwork{
    UNIQUE,
    OPAL
}

@Serializable
data class PolkadotNFTsSubscanResult(
    val data: Data?=null,
){
    @Serializable
    data class Data(
        val ERC721: List<Erc721>?=null,
    ){
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
    val data: Data?=null,
){
    @Serializable
    data class Data(
        val count: Int,
        val list: List<TokenHolder>?=null,
    ){
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
    val ipfsCid: String?=null,
    val attributes: List<Attribute>?=null
){
    @Serializable
    data class Attribute(
        val name: String,
        var value: JsonElement?,
    )
}

@Serializable
data class PolkadotEvmNft(
    val constractAddress: String,
    val tokenId: String,
    val nftMetadata:NftMetadata ?=null
)

@Serializable
data class PolkadotUniqueNft(
    val collectionId: String,
    val tokenId: String,
    val metadata:UniqueNftMetadata ?=null
)
object PolkadotNftService {

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

    // val seed: String = ""
    // val seedPassword: String = ""

    // val signer = Sr25519SignerWrapper(seed, seedPassword)
    // // create SDK god object. First parameter - signer, second - base url of backend of blockchain
    // val sdk = UniqueSdk(signer, "https://rest.opal.uniquenetwork.dev")

    fun fetchAccountTokensBySubscan(parachain: PolkadotParachain, account: String): PolkadotNFTsSubscanResult{
        return runBlocking {
            val values = mapOf("address" to account)
            val parachainAPI= when(parachain){
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


    fun fetchEvmErc721CollectiblesBySubscan(parachain: PolkadotParachain, account: String):SubscanEvmErc721CollectiblesResult{
        return runBlocking {
            val v = SubscanEvmErc721CollectiblesBody(account, 100)
            val parachainAPI= when(parachain){
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

    fun fetchUniqueNFTs(network: UniqueNetwork, account: String): TokenOwnersDataResponse {
        return runBlocking {
            val uniqueGraphqlClient = GraphQLWebClient(url = getUniqueNetworkIndexerUrl(network),serializer = GraphQLClientKotlinxSerializer())
            val tokenOwnersQuery = TokenOwnersQuery()
            tokenOwnersQuery.query = tokenOwnersQuery.query.replace("address", account)
            uniqueGraphqlClient.execute(tokenOwnersQuery).data!!.token_owners
        }
    }

    fun fetchUniqueNFTsMetadata(network: UniqueNetwork, collectionId: String, tokenId: String): TokenDataResponse? {
        return runBlocking {
            val uniqueGraphqlClient = GraphQLWebClient(url = getUniqueNetworkIndexerUrl(network),serializer = GraphQLClientKotlinxSerializer())
            val tokensQuery = TokensQuery()
            tokensQuery.query= tokensQuery.query.replace("tokenId",tokenId)
            tokensQuery.query= tokensQuery.query.replace("collectionId",collectionId)
            uniqueGraphqlClient.execute(tokensQuery).data?.tokens
        }
    }

    fun parseNftMetadataUniqueResponse(tokens: TokenDataResponse): UniqueNftMetadata {
        val metadata = (tokens.data ?: throw IllegalArgumentException("Tokens have no data?"))[0]
        val attributes = metadata.attributes?.values?.map {
            Json.decodeFromJsonElement<UniqueNftMetadata.Attribute>(it)
        }
        val tokenImage = metadata.image!!
        return UniqueNftMetadata(
            fullUrl = tokenImage["fullUrl"]!!.jsonPrimitive.content,
            ipfsCid = tokenImage["ipfsCid"]!!.jsonPrimitive.content,
            attributes
        )
    }


    private fun getUniqueNetworkIndexerUrl(uniqueNetwork: UniqueNetwork): String {
        return when(uniqueNetwork){
            UniqueNetwork.UNIQUE -> WaltIdServices.loadIndexers().indexersUrl.uniqueUrl
            UniqueNetwork.OPAL -> WaltIdServices.loadIndexers().indexersUrl.opalUrl
        }
    }

}