package id.walt.nftkit

import com.expediagroup.graphql.client.Generated
import com.expediagroup.graphql.client.types.GraphQLClientRequest
import id.walt.nftkit.tokensquery.TokenDataResponse
import kotlin.String
import kotlin.reflect.KClass
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

public const val TOKENS_QUERY: String =
    "{\n    tokens(where: {_and: {token_id:{_eq:tokenId}, collection_id:{_eq: collectionId}}} )\n        {\n            data{\n                image\n                attributes\n            }\n        }\n}"

@Generated
@Serializable
public class TokensQuery : GraphQLClientRequest<TokensQuery.Result> {
  @Required
  public override var query: String = TOKENS_QUERY

  public override fun responseType(): KClass<TokensQuery.Result> = TokensQuery.Result::class

  @Generated
  @Serializable
  public data class Result(
    public val tokens: TokenDataResponse,
  )
}
