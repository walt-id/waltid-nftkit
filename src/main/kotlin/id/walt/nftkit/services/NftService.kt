package id.walt.nftkit.services

import kotlinx.serialization.SerializationStrategy
import java.math.BigInteger
import kotlinx.serialization.Serializable


@Serializable
data class NftMetadata(
    val description: String,
    val name: String,
    val image: String?,
    val attributes: List<Attributes>
) {
    data class Attributes(
        val trait_type: String,
        val value: String,
        val display_type: DisplayType?
    )
}

data class TokenInfo(
    val name: String,
    val symbol: String
)

enum class Chain {
    ETHEREUM,
    PALYGON,
    BSC,
    RINKEBY,
    CELO
}
enum class TokenStandard {
    ERC721,
    ERC1155
}

enum class MetadataStorageType {
    ON_CHAIN,
    OFF_CHAIN
}

enum class OffChainMetadataStorageType {
    IPFS,
    ARWAVE,
    FILECOIN,
    CENTRALIZED
}

enum class DisplayType{
    NUMBER,
    BOOST_PERCENTAGE,
    BOOST_NUMBER,
    DATE
}

data class DeploymentOptions(
    val onchain: Boolean = true,
    val tokenStandard: TokenStandard,
)

data class DeploymentParameter(
    val name: String,
    val symbol: String,
    val uri: String,
    val owner: String
)

data class MintingParameter(
    val metadataUri : String,
    val recipientAddress: String,
    val metadata: NftMetadata,
    val tokenStandard: TokenStandard
)

data class MintingOptions(
    val metadataStorageType: MetadataStorageType,
    val offChainMetadataStorageType: OffChainMetadataStorageType?
)


data class TransactionResponse(
    val chain: String,
    val transactionId: String,
    val transactionExternalUrl: String
)

data class DeploymentResponse(
    val transactionResponse: TransactionResponse,
    val contractAddress: String
)

data class MintingResponse(
    val transactionResponse: TransactionResponse,
    val tokenId: BigInteger
)





object NftService {

    fun DeployNewSmartContractToken(chain: Chain, deploymentParameter: DeploymentParameter, options: DeploymentOptions?) /*: TransactionResponse*/{

    }

    fun mintToken(chain: Chain, contractAddress: String, MintingParameter: MintingParameter, options: MintingOptions?) /*: TransactionResponse*/{

    }

    fun getNftMetadata(chain: Chain,contractAddress: String, tokenId: Int)/*: NftMetadata*/{
    }

    fun getNftMetadataUri(chain: Chain,contractAddress: String, tokenId: Int): String{
        return String()
    }

    fun balanceOf(chain: Chain,contractAddress: String, tokenId: Int) : BigInteger {
        return BigInteger.valueOf(0)
    }

    fun ownerOf(tokenId: Int): String{
        return String()
    }

    fun getTokenInfo(chain: Chain,contractAddress: String)/*: TokenInfo*/{
    }
}