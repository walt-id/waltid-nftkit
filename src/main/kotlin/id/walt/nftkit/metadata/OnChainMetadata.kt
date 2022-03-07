package id.walt.nftkit.metadata

import id.walt.nftkit.services.NftMetadata
import id.walt.nftkit.services.NftService.encBase64Str
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

class OnChainMetadata : MetadataUri {
    override fun getTokenUri(nftMetadata: NftMetadata): String {
        val str : String = Json.encodeToString(serializer(),nftMetadata)
        val encodedStr: String = encBase64Str(str)

        return encodedStr;
    }
}