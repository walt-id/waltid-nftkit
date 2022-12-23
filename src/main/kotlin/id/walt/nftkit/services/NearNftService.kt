package id.walt.nftkit.services

import kotlinx.serialization.Serializable


@Serializable
data class NearContractMetadata(
    val spec: String, // required, essentially a version like "nft-2.0.0", replacing "2.0.0" with the implemented version of NEP-177
    val name: String, // required, ex. "Mochi Rising — Digital Edition" or "Metaverse 3"
    val symbol: String, // required, ex. "MOCHI"
     val icon: String?= null, // Data URL
    val base_uri: String?= null, // Centralized gateway known to have reliable access to decentralized storage assets referenced by `reference` or `media` URLs
    val reference: String?= null, // URL to a JSON file with more info
    val reference_hash: String?= null, // Base64-encoded sha256 hash of JSON from reference field. Required if `reference` is included.

)
@Serializable
data class NearTokenMetadata(
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

object NearNftService {
}