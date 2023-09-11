package id.walt.nftkit.graphql.tokensquery

import com.expediagroup.graphql.client.Generated
import id.walt.nftkit.graphql.JSONObject
import kotlinx.serialization.Serializable

@Generated
@Serializable
data class TokenEntity(
    val image: JSONObject? = null,
    val attributes: JSONObject? = null,
)
