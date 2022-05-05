package id.walt.nftkit.metadata

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import id.walt.nftkit.services.NFTsAlchemyResult
import id.walt.nftkit.services.NftMetadata
import id.walt.nftkit.services.NftService
import io.ktor.client.call.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream

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
    override fun getTokenUri(nftMetadata: NftMetadata?): String {
        return runBlocking {
            val metadata= Json.encodeToString(nftMetadata)
            val result = NftService.client.submitForm(url = "https://api.nft.storage/store", formParameters = Parameters.build {
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