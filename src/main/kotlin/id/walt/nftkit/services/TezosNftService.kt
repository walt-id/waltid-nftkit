package id.walt.nftkit.services

import com.fasterxml.jackson.databind.ObjectMapper
import id.walt.nftkit.Values
import id.walt.nftkit.metadata.MetadataUri
import id.walt.nftkit.metadata.MetadataUriFactory
import id.walt.nftkit.rest.TezosSCDeploymentResponse
import id.walt.nftkit.utilis.Common
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


enum class TezosChain {
    TEZOS,
    GHOSTNET
}

@Serializable
data class TezosNftMetadata(

    var name: String,
    val description: String?= null,
    var symbol: String,
    var image: String?= null,
    var creators: List<String>?= null, //["tz1ZLRT3xiBgGRdNrTYXr8Stz4TppT3hukRi"],
    var decimals: String?= null,
    val displayUri: String?= null,
    val artifactUri: String?= null,
    val thumbnailUri: String?= null,
    val isTransferable:Boolean?= null,
    val isBooleanAmount:Boolean?= null,
    val shouldPreferSymbol:Boolean?= null,
    val attributes: List<NftMetadata.Attributes>?=null,
    val tags: List<String>?= null,//["pxlshrd","Pâtisserie","fxhashturnsone"]
    val category: String?= null,
    val collectionName: String?= null,
    val creatorName: String?= null,
    val keywords: String?= null,//"gaming,collectible,rocket,monster,bear,battalion",
    )

data class TezosMintingParameter(
    val metadataUri: String?,
    val recipientAddress: String,
    val tokenId: String,
    val amount: String?,
    val metadata: TezosNftMetadata?,
)

@Serializable
data class TezosNFTsTzktResult(
    val id: Long,
    val account: Account,
    val token: Token,
    val balance: String? = null,
    val transfersCount: Long,
    val firstLevel: Long? = null,
    val firstTime: String? = null,
    val lastLevel: Long? = null,
    val lastTime: String? = null,

) {

    @Serializable
    data class Account(
        val address: String,
    )
    @Serializable
    data class Token(
        val id: Long,
        val contract: Contract,
        val tokenId: String,
        val standard: String,
        val totalSupply: String,
        val metadata: TezosNftMetadata?= null,
        // val timeLastUpdated: String
    ) {
        @Serializable
        data class Contract(
            val address: String,
        )
    }
}

@Serializable
data class TezosNFTMetadataTzktResult(
    val id: Long,
    val contract: TezosNFTsTzktResult.Token.Contract,
    val tokenId: String,
    val standard: String,
    val metadata: TezosNftMetadata?= null,
    )
@Serializable
data class TezosNftsSCDeploymentResponse(
    val contractAddress: String
)

@Serializable
data class TezosOperationResponse(
    val opHash: String
)

@Serializable
data class OperationResponse(
    val operationHash: String,
    val operationExternalUrl: String
)

enum class Fa2SmartContractType {
    SINGLE,
    MULTIPLE
}

object TezosNftService {

    val client = HttpClient(CIO.create{requestTimeout = 0}) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
            // LogLevel.BODY
        }
        expectSuccess = false
    }

    fun deploySmartContract(chain: TezosChain, owner: String, type: Fa2SmartContractType): TezosSCDeploymentResponse {
        return runBlocking {
            val values = mapOf("chain" to chain.toString(), "owner" to owner, "type" to type.toString())
            val deployment =
                NftService.client.post("${WaltIdServices.loadTezosConfig().tezosBackendServer}/tezos/contract/deployment") {
                    contentType(ContentType.Application.Json)
                    setBody(values)
                }
                    .body<TezosNftsSCDeploymentResponse>()
            val contractExternalUrl= when(chain){
                TezosChain.TEZOS -> Values.TEZOS_MAINNET_BETTER_CALL_DEV
                TezosChain.GHOSTNET -> Values.TEZOS_GHOSTNET_BETTER_CALL_DEV
            }
            val tezosSCDeploymentResponse= TezosSCDeploymentResponse(deployment.contractAddress, "$contractExternalUrl/${deployment.contractAddress}")
            return@runBlocking tezosSCDeploymentResponse
        }
    }

    fun mintNftToken(chain: TezosChain, contractAddress: String, parameter: TezosMintingParameter): OperationResponse {
        var tokenUri: String?
        if (parameter.metadataUri != null && parameter.metadataUri != "") {
            tokenUri = parameter.metadataUri
        } else {
            val metadataUri: MetadataUri = MetadataUriFactory.getMetadataUri(MetadataStorageType.OFF_CHAIN)
            val nftMetadataWrapper= NftMetadataWrapper(tezosNftMetadata = parameter.metadata)
            tokenUri = metadataUri.getTokenUri(nftMetadataWrapper)
        }
        return runBlocking {
            val amount= if(parameter.amount != null && parameter.amount != "") parameter.amount else null
            val values = mapOf("chain" to chain.toString(), "owner" to parameter.recipientAddress, "fa2ContractAddress" to contractAddress, "metadata" to tokenUri , "tokenId" to parameter.tokenId, "amount" to amount )
            val tezosOperationResponse =
                NftService.client.post("${WaltIdServices.loadTezosConfig().tezosBackendServer}/tezos/contract/token/mint") {
                    contentType(ContentType.Application.Json)
                    setBody(values)
                }
                    .body<TezosOperationResponse>()
            val contractExternalUrl= when(Common.getTezosChain(chain.toString())){
                TezosChain.TEZOS -> Values.TEZOS_MAINNET_BETTER_CALL_DEV
                TezosChain.GHOSTNET -> Values.TEZOS_GHOSTNET_BETTER_CALL_DEV
            }
            val operationResponse= OperationResponse(tezosOperationResponse.opHash, "$contractExternalUrl/opg/${tezosOperationResponse.opHash}")
            return@runBlocking operationResponse
        }
    }

    fun addMinter(chain: TezosChain, contractAddress: String, minter: String): OperationResponse {
        return runBlocking {
            val values = mapOf("chain" to chain.toString(), "minter" to minter, "fa2ContractAddress" to contractAddress)
            val tezosOperationResponse =
                NftService.client.post("${WaltIdServices.loadTezosConfig().tezosBackendServer}/tezos/contract/minter") {
                    contentType(ContentType.Application.Json)
                    setBody(values)
                }
                    .body<TezosOperationResponse>()
            val contractExternalUrl= when(Common.getTezosChain(chain.toString())){
                TezosChain.TEZOS -> Values.TEZOS_MAINNET_BETTER_CALL_DEV
                TezosChain.GHOSTNET -> Values.TEZOS_GHOSTNET_BETTER_CALL_DEV
            }
            val operationResponse= OperationResponse(tezosOperationResponse.opHash, "$contractExternalUrl/opg/${tezosOperationResponse.opHash}")
            return@runBlocking operationResponse
        }
    }

    fun getNftTezosMetadata(chain: Chain, contractAddress: String, tokenId: String): TezosNftMetadata? {
        return runBlocking {
            var contractAddressExp= ""
            var chainAPI= ""
            if(contractAddress != null)  contractAddressExp= "token.contract=$contractAddress&"
            if(Chain.GHOSTNET.equals(chain)) chainAPI= ".ghostnet"
            val nfts =
                NftService.client.get("https://api$chainAPI.tzkt.io/v1/tokens?contract=$contractAddress&tokenId=$tokenId&type=fa2&token.totalSupply=1") {
                    contentType(ContentType.Application.Json)
                }
                    .body<List<TezosNFTMetadataTzktResult>>()

            return@runBlocking nfts.get(0).metadata
        }
    }
     fun fetchAccountNFTsByTzkt(
        chain: Chain,
        account: String,
        contractAddress: String?= null,
    ): List<TezosNFTsTzktResult> {
        return runBlocking {
            var contractAddressExp= ""
            var chainAPI= ""
            if(contractAddress != null)  contractAddressExp= "token.contract=$contractAddress&"
            if(Chain.GHOSTNET.equals(chain)) chainAPI= ".ghostnet"
            val nfts =
                NftService.client.get("https://api$chainAPI.tzkt.io/v1/tokens/balances?account=$account&$contractAddressExp select=id,account,token,balance,transfersCount&type=fa2&token.totalSupply=1") {
                    contentType(ContentType.Application.Json)
                }
                    .body<List<TezosNFTsTzktResult>>()

            return@runBlocking nfts
        }
    }
}