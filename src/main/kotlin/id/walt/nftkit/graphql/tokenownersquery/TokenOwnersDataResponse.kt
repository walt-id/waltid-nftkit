package id.walt.nftkit.graphql.tokenownersquery

import com.expediagroup.graphql.client.Generated
import kotlin.Int
import kotlin.collections.List
import kotlinx.serialization.Serializable

@Generated
@Serializable
public data class TokenOwnersDataResponse(
    public val count: Int,
    public val `data`: List<tokens_owners>? = null,
)
