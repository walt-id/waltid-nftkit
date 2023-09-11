package id.walt.nftkit.graphql.tokensquery

import com.expediagroup.graphql.client.Generated
import kotlinx.serialization.Serializable

@Generated
@Serializable
data class TokenDataResponse(
    val `data`: List<TokenEntity>? = null,
)
