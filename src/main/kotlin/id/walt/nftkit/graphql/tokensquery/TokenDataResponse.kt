package id.walt.nftkit.graphql.tokensquery

import com.expediagroup.graphql.client.Generated
import kotlin.collections.List
import kotlinx.serialization.Serializable

@Generated
@Serializable
public data class TokenDataResponse(
  public val `data`: List<TokenEntity>? = null,
)
