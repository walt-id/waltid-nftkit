package id.walt.nftkit.services

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*


enum class FlowChain {
    TESTNET, MAINNET
}

@Serializable
data class Rarity(
    val score : String,
    val max : String,
    val description: String,
)
@Serializable
data class CollectionSocial(
    val twitter: String,
)

@Serializable
data class CollectionPath(
    val domain: String,
    val identifier: String
)
@Serializable
data class Edition(
    val name: String,
    val number: String,
    @Contextual
    val max: Any?
)
@Serializable
data class Traits(
    val traits: List<Trait>
)
@Serializable
data class Trait(
    val name: String?=null,
    val value: String?=null,
    val displayType: String?=null,
    val rarity: Rarity?=null,
)


@Serializable
data class FlowNFTMetadata(
    val id: String,
    val name: String,
    val description: String,
    val thumbnail: String,
    val owner: String?=null,
    val type: String?=null,
    val royalties: List<JsonElement>,
    val externalURL: String?=null,
    val serialNumber: String?=null,
    val collectionPublicPath: CollectionPath?=null,
    val collectionStoragePath: CollectionPath?=null,
    val collectionProviderPath: CollectionPath?=null,
    val collectionPublic: String?=null,
    val collectionPublicLinkedType: String?=null,
    val collectionProviderLinkedType: String?=null,
    val collectionName: String?=null,
    val collectionDescription: String?=null,
    val collectionExternalURL: String?=null,
    val collectionSquareImage: String?=null,
    val collectionBannerImage: String?=null,
    val collectionSocials: CollectionSocial?=null,
    val edition: Edition?=null,
    val traits: Traits?=null,
    val medias: String?=null,
    val license:String?=null,
)

@Serializable
data class FlowTokenMetadata(
    val id: String,
    val name: String,
    val description: String,
    val thumbnail: String,
    val externalURL: String?=null,
    val identifier: String?=null,
    val traits: Traits?=null,

)


@Serializable
data class FlowNFTsMetadata(
    val id: String,
    val name: String,
    val description: String,
    val thumbnail: String,
    val owner: String?=null,
    val type: String?=null,
    val royalties: List<JsonElement>,
    val externalURL: String?=null,
    val collectionPublicPath: CollectionPath?=null,
    val collectionStoragePath: CollectionPath?=null,
    val collectionProviderPath: CollectionPath?=null,
    val publicLinkedType: JsonElement?=null,
    val privateLinkedType: JsonElement?=null,
    val collectionName: String?=null,
    val collectionDescription: String?=null,
    val collectionExternalURL: String?=null,
    val collectionSquareImage: String?=null,
    val collectionBannerImage: String?=null,
    val collectionSocials: CollectionSocial?=null,
)

object FlowNftService {

    fun getAllNFTs(account_id: String, chain: FlowChain): List<FlowNFTsMetadata> {
        return runBlocking {
            val values = mapOf(
                "account_id" to account_id,
                "chain" to chain.toString()
            )
            val operationResult =
                NftService.client.post("${WaltIdServices.loadTezosConfig().tezosBackendServer}/flow/getAllNFTs") {
                    contentType(ContentType.Application.Json)

                    setBody(
                        values
                    )
                }
                    //  .body<JsonObject>().entries.first().value.jsonArray.first().jsonObject["id"].jsonPrimitive.content

                    .body<JsonObject>().entries.flatMap {

                        it.value.jsonArray.map {
                            println(it)

                            Json {
                                ignoreUnknownKeys = true
                            }.decodeFromJsonElement<FlowNFTsMetadata>(it)
                        }


                    }
            return@runBlocking operationResult
        }
    }


    fun getNFTbyId(
        account_id: String,
        contractAddress: String,
        collectionPublicPath: String,
        id: String,
        chain: FlowChain
    ): FlowNFTMetadata {
        return runBlocking {
            val values = mapOf(
                "account_id" to account_id,
                "chain" to chain.toString(),
                "contractAddress" to contractAddress,
                "collectionPublicPath" to collectionPublicPath,
                "id" to id


            )
            val operationResult =
                NftService.client.post("${WaltIdServices.loadTezosConfig().tezosBackendServer}/flow/getNFTById") {
                    contentType(ContentType.Application.Json)

                    setBody(
                        values
                    )
                }
                    .body<FlowNFTMetadata>()
            return@runBlocking operationResult
        }
    }

    fun getNFTinCollectionPath(account_id: String, collectionPath: String, chain: FlowChain): List<FlowTokenMetadata> {
        return runBlocking {
            val values = mapOf(
                "account_id" to account_id,
                "chain" to chain.toString().lowercase(),
                "collectionPath" to collectionPath
            )

            val operationResult =
                NftService.client.post("${WaltIdServices.loadTezosConfig().tezosBackendServer}/flow/getNFTsInCollection") {
                    contentType(ContentType.Application.Json)

                    setBody(
                        values
                    )
                }

                    .body<List<FlowTokenMetadata>>()
            return@runBlocking operationResult

        }
    }

}