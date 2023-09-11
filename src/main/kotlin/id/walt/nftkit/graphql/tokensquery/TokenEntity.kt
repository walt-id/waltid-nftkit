package id.walt.nftkit.graphql.tokensquery

import com.expediagroup.graphql.client.Generated
import id.walt.nftkit.graphql.JSONObject
import kotlinx.serialization.Serializable

@Generated
@Serializable
public data class TokenEntity(
    public val image: JSONObject? = null,
    public val attributes: JSONObject? = null,
)
