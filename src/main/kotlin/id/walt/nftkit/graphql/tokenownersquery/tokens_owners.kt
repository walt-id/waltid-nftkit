package id.walt.nftkit.graphql.tokenownersquery

import com.expediagroup.graphql.client.Generated
import kotlinx.serialization.Serializable

@Generated
@Serializable
data class tokens_owners(
    val token_id: Int,
    val collection_id: Int,
)
