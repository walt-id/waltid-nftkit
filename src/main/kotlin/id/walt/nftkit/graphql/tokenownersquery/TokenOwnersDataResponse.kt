package id.walt.nftkit.graphql.tokenownersquery

import com.expediagroup.graphql.client.Generated
import kotlinx.serialization.Serializable

@Generated
@Serializable
data class TokenOwnersDataResponse(
    val count: Int,
    val `data`: List<tokens_owners>? = null,
)
