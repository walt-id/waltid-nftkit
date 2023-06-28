package id.walt.nftkit.models

import kotlinx.serialization.Serializable

@Serializable
data class NftListDataTransferObject(
    val items: List<NftDetailDataTransferObject>
)

@Serializable
data class NftDetailDataTransferObject(
    val id: String,
    var name: String? = null,
    var description: String? = null,
    val contract: String? = null,
    val type: String? = null,
    var externalUrl: String? = null,
    val attributes: List<TokenAttributes>? = null,
    val art: TokenArt? = null,
)