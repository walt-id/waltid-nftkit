package id.walt.nftkit.metadata

import com.beust.klaxon.Klaxon
import id.walt.nftkit.services.NftMetadataWrapper
import id.walt.nftkit.services.WaltIdServices
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.math.BigInteger
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.util.*


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
    override fun getTokenUri(nftMetadataWrapper: NftMetadataWrapper): String {

        val data: MutableMap<String, Any> = LinkedHashMap()
        val metadata= if (nftMetadataWrapper.evmNftMetadata != null) Json.encodeToString(nftMetadataWrapper.evmNftMetadata) else Json.encodeToString(nftMetadataWrapper.tezosNftMetadata)
        data["meta"] = metadata
        val boundary: String = BigInteger(35, Random()).toString()

        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.nft.storage/store"))
            .header("Authorization", "Bearer ${WaltIdServices.loadApiKeys().apiKeys.nftstorage}")
            .postMultipartFormData(boundary, data)
            .build()
         val httpClient: HttpClient = HttpClient.newHttpClient()
        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        val result = Klaxon()
            .parse<NFTStorageResult>(response.body())
        if(result!!.ok){
             return result.value.url
        }else{
            throw Exception("Something wrong with IPFS")
        }
    }

    private fun HttpRequest.Builder.postMultipartFormData(boundary: String, data: Map<String, Any>): HttpRequest.Builder {
        val byteArrays = ArrayList<ByteArray>()
        val separator = "--$boundary\r\nContent-Disposition: form-data; name=".toByteArray(StandardCharsets.UTF_8)

        for (entry in data.entries) {
            byteArrays.add(separator)
            byteArrays.add("\"${entry.key}\"\r\n\r\n${entry.value}\r\n".toByteArray(StandardCharsets.UTF_8))

        }
        byteArrays.add("--$boundary--".toByteArray(StandardCharsets.UTF_8))

        this.header("Content-Type", "multipart/form-data;boundary=$boundary")
            .POST(HttpRequest.BodyPublishers.ofByteArrays(byteArrays))
        return this


    }
}
