package id.walt.nftkit.models

import kotlinx.serialization.Serializable

@Serializable
data class TokenArt(
    var url: String? = null,
    var imageData: String? = null,
)
