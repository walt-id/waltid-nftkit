package id.walt.nftkit.graphql

import com.expediagroup.graphql.client.Generated
import com.expediagroup.graphql.client.types.GraphQLClientRequest
import id.walt.nftkit.graphql.tokenownersquery.TokenOwnersDataResponse
import kotlin.String
import kotlin.reflect.KClass
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

public val TOKEN_OWNERS_QUERY: String =
    "{\n  token_owners(where:{owner:{_eq: \"address\"}})\n  {count\n    data{token_id\n    collection_id\n    }\n  }\n}"

@Generated
@Serializable
public class TokenOwnersQuery : GraphQLClientRequest<TokenOwnersQuery.Result> {
  @Required
  public override var query: String = TOKEN_OWNERS_QUERY

  public override fun responseType(): KClass<Result> =
      Result::class

  @Generated
  @Serializable
  public data class Result(
      public val token_owners: TokenOwnersDataResponse,
  )
}
