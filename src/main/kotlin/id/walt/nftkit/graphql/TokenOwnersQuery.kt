package id.walt.nftkit.graphql

import com.expediagroup.graphql.client.Generated
import com.expediagroup.graphql.client.types.GraphQLClientRequest
import id.walt.nftkit.graphql.tokenownersquery.TokenOwnersDataResponse
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

val TOKEN_OWNERS_QUERY: String =
    "{\n  token_owners(where:{owner:{_eq: \"address\"}})\n  {count\n    data{token_id\n    collection_id\n    }\n  }\n}"

@Generated
@Serializable
class TokenOwnersQuery : GraphQLClientRequest<TokenOwnersQuery.Result> {
    @Required
    override var query: String = TOKEN_OWNERS_QUERY

    override fun responseType(): KClass<Result> =
        Result::class

    @Generated
    @Serializable
    data class Result(
        val token_owners: TokenOwnersDataResponse,
    )
}
