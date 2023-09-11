package id.walt.nftkit.graphql.tokensquery

import com.expediagroup.graphql.client.Generated
import id.walt.nftkit.graphql.JSONObject
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

@Generated
@Serializable
data class TokenEntity(
    val image: JSONObject? = null,
    val attributes: Map<String, TokenEntityAttributes>? = null,
) {
    @Serializable
    data class TokenEntityAttributes(
        val name: String,
        val value: JsonPrimitive
    )
}
