package id.walt.nftkit


/*
{
    "description": "Friendly OpenSea Creature that enjoys long swims in the ocean.",
    "external_url": "https://openseacreatures.io/3",
    "image": "https://storage.googleapis.com/opensea-prod.appspot.com/puffs/3.png",
    "name": "Dave Starbelly",
    "attributes": [ ... ],
}
*/

data class NftMetaData(
    val description: String,
    val name: String,
    val attributes: List<Attributes>
    ) {
    data class Attributes(
        val name: String,
        val value: String
    )
}

data class MintingOptions(
    val onchain: Boolean = true,
    val storageType: Boolean = false
)



object NftService {

    fun mintToken(contractAddress: String, address: String, tokenUri: String) {}

    fun mintToken(contractAddress: String, address: String, metaData: NftMetaData, options: MintingOptions?) {}
}