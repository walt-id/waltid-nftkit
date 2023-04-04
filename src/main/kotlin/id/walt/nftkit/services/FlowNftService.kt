package id.walt.nftkit.services

import kotlinx.serialization.Serializable


enum class FlowChain {
    TESTNET, MAINNET
}

@Serializable
data class FlowNFTTraits(
    val name: String,
    val value: String,
    val displayType: String,
    val rarity: String,
)
@Serializable
data class FlowNFTMetadata(
    val id : String,
    val name: String,
    val description: String,
    val thumbnail: String,
    val owner : String,
    val type : String,
    val externalURL: String?= null,
    val collectionName: String,
    val collectionDescription: String,
    val traits: List<FlowNFTTraits>,
)
object FlowNftService {

}