package id.walt.nftkit.graphql

import com.expediagroup.graphql.client.Generated
import com.expediagroup.graphql.client.types.GraphQLClientRequest
import id.walt.nftkit.graphql.tokensquery.TokenDataResponse
import kotlin.String
import kotlin.reflect.KClass
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

const val TOKENS_QUERY: String =
    "{\n    tokens(where: {_and: {token_id:{_eq:tokenId}, collection_id:{_eq: collectionId}}} )\n        {\n            data{\n                image\n                attributes\n            }\n        }\n}"

@Generated
@Serializable
class TokensQuery : GraphQLClientRequest<TokensQuery.Result> {
  @Required
  override var query: String = TOKENS_QUERY

  override fun responseType(): KClass<Result> = Result::class

  @Generated
  @Serializable
  data class Result(
      val tokens: TokenDataResponse,
  )
}
