package id.walt.nftkit.metadata

import id.walt.nftkit.services.NftMetadata
import id.walt.nftkit.services.WaltIdServices.encBase64Str
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

object OnChainMetadata : MetadataUri {
    override fun getTokenUri(nftMetadata: NftMetadata?): String {
        val str = Json.encodeToString(serializer(), nftMetadata)
        val encodedStr = encBase64Str(str)

        return "data:application/json;base64,$encodedStr"
    }
}
