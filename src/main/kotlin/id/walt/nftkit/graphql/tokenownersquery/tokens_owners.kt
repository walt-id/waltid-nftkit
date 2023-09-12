package id.walt.nftkit.graphql.tokenownersquery

import com.expediagroup.graphql.client.Generated
import kotlin.Int
import kotlinx.serialization.Serializable

@Generated
@Serializable
public data class tokens_owners(
  public val token_id: Int,
  public val collection_id: Int,
)
