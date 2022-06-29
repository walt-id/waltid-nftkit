package id.walt.nftkit.metadata

import id.walt.nftkit.services.NftMetadata
import id.walt.nftkit.services.NftService
import id.walt.nftkit.services.WaltIdServices
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class NFTStorageResult(
    val ok: Boolean,
    val value: NFTStorageValue,
) {
    @Serializable
    data class NFTStorageValue(
        val ipnft: String,
        val url: String,
    )
}
@Serializable
data class NFTStorageAddFileResult(
    val ok: Boolean,
    val value: NFTStorageValue,
) {
    @Serializable
    data class NFTStorageValue(
        val cid: String,
    )
}


object IPFSMetadata: MetadataUri {

    val client = HttpClient(CIO.create{requestTimeout = 0}) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        install(Auth) {
            bearer {
                loadTokens {
                    val token = WaltIdServices.loadApiKeys().apiKeys.nftstorage
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
    override fun getTokenUri(nftMetadata: NftMetadata?): String {
        return runBlocking {
            val metadata= Json.encodeToString(nftMetadata)
            val result = client.submitForm(url = "https://api.nft.storage/store", formParameters = Parameters.build {
                append("meta", metadata)
            }).body<NFTStorageResult>()
            if(result.ok == true){
                return@runBlocking result.value.url
            }else{
                throw Exception("Something wrong with IPFS")
            }
        }
    }
}