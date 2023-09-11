package id.walt.nftkit.services

import com.beust.klaxon.Klaxon
import com.expediagroup.graphql.client.serialization.types.KotlinxGraphQLResponse
import com.expediagroup.graphql.client.serializer.GraphQLClientSerializer
import com.expediagroup.graphql.client.types.GraphQLClientRequest
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonBuilder
import kotlinx.serialization.serializer
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.full.createType

/**
 * GraphQL client serializer that uses kotlinx.serialization for serializing requests and deserializing responses.
 */
class PolkadotGraphQLClientKotlinxSerializer(private val jsonBuilder: JsonBuilder.() -> Unit = {}) : GraphQLClientSerializer {

    private val responseSerializerCache = ConcurrentHashMap<KClass<*>, KSerializer<KotlinxGraphQLResponse<Any?>>>()
    private val requestSerializerCache = ConcurrentHashMap<KClass<*>, KSerializer<Any?>>()

    private val json = Json {
        ignoreUnknownKeys = true
        apply(jsonBuilder)
        classDiscriminator = "__typename"
        coerceInputValues = true
        encodeDefaults = false
    }

    override fun serialize(request: GraphQLClientRequest<*>): String {
        println("GRAPHQL SERIALIZE 1: $request (JSON: ${Klaxon().toJsonString(request)})")
        return json.encodeToString(requestSerializer(request), request)
    }

    override fun serialize(requests: List<GraphQLClientRequest<*>>): String {
        println("GRAPHQL SERIALIZE 2: $requests (JSON: ${Klaxon().toJsonString(requests)})")
        val serializedRequests = requests.map { request ->
            json.encodeToString(requestSerializer(request), request)
        }
        return "[${serializedRequests.joinToString(",")}]"
    }

    override fun <T : Any> deserialize(rawResponse: String, responseType: KClass<T>): KotlinxGraphQLResponse<T> {
        println("GRAPHQL DESERIALIZE 1: (type: $responseType) $rawResponse")
        return json.decodeFromString(
            responseSerializer(responseType) as KSerializer<KotlinxGraphQLResponse<T>>,
            rawResponse
        )
    }

    override fun deserialize(rawResponses: String, responseTypes: List<KClass<*>>): List<KotlinxGraphQLResponse<*>> {
        println("GRAPHQL DESERIALIZE 2: $rawResponses (is: $responseTypes)")
        val jsonElement = json.parseToJsonElement(rawResponses)
        return if (jsonElement is JsonArray) {
            jsonElement.withIndex().map { (index, element) ->
                json.decodeFromJsonElement(responseSerializer(responseTypes[index]), element)
            }
        } else {
            // should never be the case
            listOf(
                json.decodeFromJsonElement(responseSerializer(responseTypes.first()), jsonElement)
            )
        }
    }

    private fun requestSerializer(request: GraphQLClientRequest<*>): KSerializer<Any?> =
        requestSerializerCache.computeIfAbsent(request::class) {
            json.serializersModule.serializer(request::class.createType())
        }

    private fun <T : Any> responseSerializer(resultType: KClass<T>): KSerializer<KotlinxGraphQLResponse<Any?>> =
        responseSerializerCache.computeIfAbsent(resultType) {
            val resultTypeSerializer = json.serializersModule.serializer(resultType.createType())
            KotlinxGraphQLResponse.serializer(
                resultTypeSerializer
            )
        }
}
