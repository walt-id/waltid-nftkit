package id.walt.nftkit.services

import id.walt.nftkit.Values
import id.walt.nftkit.chains.evm.erc721.Erc721TokenStandard
import id.walt.nftkit.metadata.MetadataUri
import id.walt.nftkit.metadata.MetadataUriFactory
import id.walt.nftkit.services.WaltIdServices.decBase64Str
import id.walt.nftkit.smart_contract_wrapper.Erc721OnchainCredentialWrapper
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.web3j.abi.EventEncoder
import org.web3j.abi.EventValues
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.*
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.protocol.core.methods.response.Log
import org.web3j.protocol.core.methods.response.TransactionReceipt
import java.math.BigInteger
import java.util.*






object VerificationService {

    val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
            })
        }
        expectSuccess = false
    }

    // Verify if NFT is part of a collection (contract address)
    fun  verifyCollection(chain: Chain, contractAddress: String, accountAddress: String): Boolean{
        return false
    }

    //  simply check if a certain trait type and trait value is in the metadata
    fun verifyTrait(chain: Chain, contractAddress: String, traitType: String, traitValue: String? = null, accountAddress: String): Boolean {
        return false
    }

}