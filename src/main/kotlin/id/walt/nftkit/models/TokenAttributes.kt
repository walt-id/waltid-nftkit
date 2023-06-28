package id.walt.nftkit.models

import kotlinx.serialization.Serializable

@Serializable
data class TokenAttributes(
    val trait: String,
    var value: String,
)
